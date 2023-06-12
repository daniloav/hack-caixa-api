package br.com.hack.services;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import br.com.hack.dao.model.ProdutoDAO;
import br.com.hack.model.Produto;
import br.com.hack.model.SimulacaoEmprestimoRequest;
import br.com.hack.model.SimulacaoEmprestimoResponse;

@Service
public class SimulacaoEmprestimoService {

	ProdutoDAO bd = new ProdutoDAO();

	public SimulacaoEmprestimoResponse simularEmprestimo(SimulacaoEmprestimoRequest solicitacao) {

		double valorSolicitado = solicitacao.getValorSolicitado();
		int quantidadeParcelas = solicitacao.getQuantidadeParcelas();

		List<Produto> produto = bd.getInstance().leTodos();

		for (int i = 0; i < produto.size(); i++) {

			if (quantidadeParcelas >= produto.get(i).getNumeroMinimoParcelas()
					&& quantidadeParcelas <= produto.get(i).getNumeroMaximoParcelas()
					|| produto.get(i).getNumeroMaximoParcelas() == 0
							&& valorSolicitado >= produto.get(i).getValorMinimo()
							&& valorSolicitado <= produto.get(i).getValorMaximo()
					|| produto.get(i).getValorMaximo() == 0) {

				SimulacaoEmprestimoResponse response = new SimulacaoEmprestimoResponse();

				response.setCodigoProduto(produto.get(i).getCodigoProduto());
				response.setDescricaoProduto(produto.get(i).getNomeProduto());
				response.setTaxaJuros(produto.get(i).getTaxaJuros());
				response.setResultadoSimulacao(null);

				List<SimulacaoEmprestimoResponse.ResultadoSimulacao> resultadoSimulacao = new ArrayList<>();

				resultadoSimulacao
						.add(calcularAmortizacaoSAC(valorSolicitado, quantidadeParcelas, response.getTaxaJuros()));

				resultadoSimulacao
						.add(calcularAmortizacaoPRICE(valorSolicitado, quantidadeParcelas, response.getTaxaJuros()));

				response.setResultadoSimulacao(resultadoSimulacao);

				gravarEventoSimulacao(response);

				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(response);

				SimulacaoEmprestimoResponse responsef = gson.fromJson(json, SimulacaoEmprestimoResponse.class);

				return responsef;

				// return response;
			}

		}

		return null;
	}

	private SimulacaoEmprestimoResponse.ResultadoSimulacao calcularAmortizacaoSAC(double valorSolicitado,
			int quantidadeParcelas, double taxaJuros) {

		SimulacaoEmprestimoResponse.ResultadoSimulacao sac = new SimulacaoEmprestimoResponse.ResultadoSimulacao();
		sac.setTipo("SAC");

		double valorAmortizacao = valorSolicitado / quantidadeParcelas;
		double saldoDevedor = valorSolicitado;

		List<SimulacaoEmprestimoResponse.Parcela> parcelas = new ArrayList<>();

		for (int i = 1; i <= quantidadeParcelas; i++) {
			double valorJuros = saldoDevedor * taxaJuros;
			double valorPrestacao = valorAmortizacao + valorJuros;

			SimulacaoEmprestimoResponse.Parcela parcela = new SimulacaoEmprestimoResponse.Parcela(i, valorAmortizacao,
					valorJuros, valorPrestacao);
			;

			parcelas.add(parcela);

			saldoDevedor -= valorAmortizacao;

		}
		sac.setParcelas(parcelas);

		return sac;
	}

	private SimulacaoEmprestimoResponse.ResultadoSimulacao calcularAmortizacaoPRICE(double valorSolicitado,
			int quantidadeParcelas, double taxaJuros) {
		
		

		SimulacaoEmprestimoResponse.ResultadoSimulacao price = new SimulacaoEmprestimoResponse.ResultadoSimulacao();
		price.setTipo("PRICE");

		double valorParcela = calcularValorParcelaPrice(valorSolicitado, taxaJuros, quantidadeParcelas);
		double saldoDevedor = valorSolicitado;

		List<SimulacaoEmprestimoResponse.Parcela> parcelas = new ArrayList<>();

		for (int j = 1; j <= quantidadeParcelas; j++) {
			double juros = saldoDevedor * taxaJuros;
			double amortizacao = valorParcela - juros;
			
			

			SimulacaoEmprestimoResponse.Parcela parcela = new SimulacaoEmprestimoResponse.Parcela(j, amortizacao, juros,
					valorParcela);

			parcelas.add(parcela);
			
			saldoDevedor -= amortizacao;

			

		}
		price.setParcelas(parcelas);

		return price;
	}

	private double calcularValorParcelaPrice(double valorSolicitado, double taxaJuros, int quantidadeParcelas) {
		double taxa = taxaJuros	;
		//double denominador = (taxa * Math.pow(1 + taxa, quantidadeParcelas)) / (Math.pow(1 + taxa, quantidadeParcelas) - 1);
		double valorParcela = valorSolicitado * (taxa / (1 - Math.pow(1 + taxa, -quantidadeParcelas)));
		return valorParcela;
	}

	private void gravarEventoSimulacao(SimulacaoEmprestimoResponse response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(response);

			// Configuração do cliente Event Hubs
			String connectionString = "Endpoint=sb://eventhack.servicebus.windows.net/;SharedAccessKeyName=hack;SharedAccessKey=HeHeVaVqyVkntO2FnjQcs2Ilh/4MUDo4y+AEhKp8z+g=;EntityPath=simulacoes";
			String eventHubName = "simulacoes";

			// Criação do cliente Event Hubs
			EventHubProducerClient producerClient = new EventHubClientBuilder()
					.connectionString(connectionString, eventHubName).buildProducerClient();

			// Criação do evento
			EventData eventData = new EventData(json.getBytes());
			EventDataBatch eventDataBatch = producerClient.createBatch();
			eventDataBatch.tryAdd(eventData);

			// Envio do evento para o Event Hub
			producerClient.send(eventDataBatch);

			// Fechamento do cliente Event Hubs
			producerClient.close();

			System.out.println("Evento de simulação gravado com sucesso: " + json);
		} catch (JsonProcessingException e) {
			System.out.println("Erro ao converter objeto para JSON: " + e.getMessage());
		}
	}

}

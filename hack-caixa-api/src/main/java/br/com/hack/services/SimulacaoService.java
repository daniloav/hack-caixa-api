package br.com.hack.services;

//Danilo Sousa de Oliveira - C137050 / GIT: daniloav/hack-caixa-api

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
import br.com.hack.model.Request;
import br.com.hack.model.Response;

@Service
public class SimulacaoService {

	ProdutoDAO bd = new ProdutoDAO();

	public Response simularEmprestimo(Request solicitacao) {

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

				Response response = new Response();

				response.setCodigoProduto(produto.get(i).getCodigoProduto());
				response.setDescricaoProduto(produto.get(i).getNomeProduto());
				response.setTaxaJuros(produto.get(i).getTaxaJuros());
				response.setResultadoSimulacao(null);

				List<Response.ResultadoSimulacao> resultadoSimulacao = new ArrayList<>();

				resultadoSimulacao
						.add(calcularAmortizacaoSAC(valorSolicitado, quantidadeParcelas, response.getTaxaJuros()));

				resultadoSimulacao
						.add(calcularAmortizacaoPRICE(valorSolicitado, quantidadeParcelas, response.getTaxaJuros()));

				response.setResultadoSimulacao(resultadoSimulacao);

				gravarEventoSimulacao(response);

				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(response);

				Response responsef = gson.fromJson(json, Response.class);

				return responsef;

				// return response;
			}

		}

		return null;
	}

	private Response.ResultadoSimulacao calcularAmortizacaoSAC(double valorSolicitado, int quantidadeParcelas,
			double taxaJuros) {

		Response.ResultadoSimulacao sac = new Response.ResultadoSimulacao();
		sac.setTipo("SAC");

		double valorAmortizacao = valorSolicitado / quantidadeParcelas;
		double saldoDevedor = valorSolicitado;

		List<Response.Parcela> parcelas = new ArrayList<>();

		for (int i = 1; i <= quantidadeParcelas; i++) {
			double valorJuros = saldoDevedor * taxaJuros;
			double valorPrestacao = valorAmortizacao + valorJuros;

			Response.Parcela parcela = new Response.Parcela(i, valorAmortizacao, valorJuros, valorPrestacao);
			;

			parcelas.add(parcela);

			saldoDevedor -= valorAmortizacao;

		}
		sac.setParcelas(parcelas);

		return sac;
	}

	private Response.ResultadoSimulacao calcularAmortizacaoPRICE(double valorSolicitado, int quantidadeParcelas,
			double taxaJuros) {

		Response.ResultadoSimulacao price = new Response.ResultadoSimulacao();
		price.setTipo("PRICE");

		double valorParcela = calcularValorParcelaPrice(valorSolicitado, taxaJuros, quantidadeParcelas);
		double saldoDevedor = valorSolicitado;

		List<Response.Parcela> parcelas = new ArrayList<>();

		for (int j = 1; j <= quantidadeParcelas; j++) {
			double juros = saldoDevedor * taxaJuros;
			double amortizacao = valorParcela - juros;

			Response.Parcela parcela = new Response.Parcela(j, amortizacao, juros, valorParcela);

			parcelas.add(parcela);

			saldoDevedor -= amortizacao;

		}
		price.setParcelas(parcelas);

		return price;
	}

	private double calcularValorParcelaPrice(double valorSolicitado, double taxaJuros, int quantidadeParcelas) {
		double taxa = taxaJuros;
		double valorParcela = valorSolicitado * (taxa / (1 - Math.pow(1 + taxa, -quantidadeParcelas)));
		return valorParcela;
	}

	private void gravarEventoSimulacao(Response response) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(response);

			
			String connectionString = "Endpoint=sb://eventhack.servicebus.windows.net/;SharedAccessKeyName=hack;SharedAccessKey=HeHeVaVqyVkntO2FnjQcs2Ilh/4MUDo4y+AEhKp8z+g=;EntityPath=simulacoes";
			String eventHubName = "simulacoes";

	
			EventHubProducerClient producerClient = new EventHubClientBuilder()
					.connectionString(connectionString, eventHubName).buildProducerClient();

			
			EventData eventData = new EventData(json.getBytes());
			EventDataBatch eventDataBatch = producerClient.createBatch();
			eventDataBatch.tryAdd(eventData);

			
			producerClient.send(eventDataBatch);

	
			producerClient.close();

			System.out.println("Evento de simulação gravado com sucesso: " + json);
		} catch (JsonProcessingException e) {
			System.out.println("Erro ao converter objeto para JSON: " + e.getMessage());
		}
	}

}

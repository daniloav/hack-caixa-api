package br.com.hack.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventDataBatch;
import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubProducerClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.hack.dao.ProdutoDAO;
import br.com.hack.model.Parcela;
import br.com.hack.model.Produto;
import br.com.hack.model.SimulacaoEmprestimoResponse;

@Service
public class SimulacaoEmprestimoService {

	ProdutoDAO bd = new ProdutoDAO();

	public SimulacaoEmprestimoResponse simularEmprestimo(SolicitacaoSimulacaoEmprestimo solicitacao) {

		double valorSolicitado = solicitacao.getValorSolicitado();
		int quantidadeParcelas = solicitacao.getQuantidadeParcelas();

		List<Produto> produto = bd.leTodos();

		for (int i = 0; i < produto.size(); i++) {

			if (produto.get(i).isParametrosValidos(valorSolicitado, quantidadeParcelas)) {

				SimulacaoEmprestimoResponse response = new SimulacaoEmprestimoResponse();

				response.setCodigoProduto(produto.get(i).getCodigoProduto());
				response.setDescricaoProduto(produto.get(i).getNomeProduto());
				response.setTaxaJuros(produto.get(i).getTaxaJuros());
				response.setResultadoSimulacaoSAC(
						calcularAmortizacaoSAC(valorSolicitado, quantidadeParcelas, response.getTaxaJuros()));
				response.setResultadoSimulacaoPRICE(
						calcularAmortizacaoPrice(valorSolicitado, quantidadeParcelas, response.getTaxaJuros()));

				gravarEventoSimulacao(response); // Grava o evento no Eventhub

				return response;
			}

		}

		return null; // Retorna null se a simulação falhou ou os dados são inválidos
	}

	private List<Parcela> calcularAmortizacaoSAC(double valorSolicitado, int quantidadeParcelas, double taxaJuros) {
		List<Parcela> parcelas = new ArrayList<>();

		double valorAmortizacao = valorSolicitado / quantidadeParcelas;
		double saldoDevedor = valorSolicitado;

		for (int i = 1; i <= quantidadeParcelas; i++) {
			double valorJuros = saldoDevedor * taxaJuros;
			double valorPrestacao = valorAmortizacao + valorJuros;

			Parcela parcela = new Parcela();
			parcela.setTipo("SAC");
			parcela.setNumero(i);
			parcela.setValorAmortizacao(valorAmortizacao);
			parcela.setValorJuros(valorJuros);
			parcela.setValorPrestacao(valorPrestacao);

			parcelas.add(parcela);

			saldoDevedor -= valorAmortizacao;
		}

		return parcelas;
	}

	private List<Parcela> calcularAmortizacaoPrice(double valorSolicitado, int quantidadeParcelas, double taxaJuros) {

		List<Parcela> parcelas = new ArrayList<>();

		double valorParcela = calcularValorParcelaPrice(valorSolicitado, taxaJuros, quantidadeParcelas);
		double saldoDevedor = valorSolicitado;

		for (int j = 1; j <= quantidadeParcelas; j++) {
			double juros = saldoDevedor * taxaJuros;
			double amortizacao = valorParcela - juros;
			saldoDevedor -= amortizacao;
			Parcela parcela = new Parcela();
			parcela.setTipo("PRICE");
			parcela.setNumero(j);
			parcela.setValorAmortizacao(amortizacao);
			parcela.setValorJuros(juros);
			parcela.setValorPrestacao(valorParcela);
			parcelas.add(parcela);
		}

		return parcelas;
	}

	private double calcularValorParcelaPrice(double valorSolicitado, double taxaJuros, int quantidadeParcelas) {
		double taxa = taxaJuros / 100;
		double denominador = Math.pow(1 + taxa, quantidadeParcelas) - 1;
		double valorParcela = (valorSolicitado * taxa * Math.pow(1 + taxa, quantidadeParcelas)) / denominador;
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

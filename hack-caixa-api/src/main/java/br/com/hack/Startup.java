package br.com.hack;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);

		// Configurações de conexão
		String connectionString = "Endpoint=sb://eventhack.servicebus.windows.net/;SharedAccessKeyName=hack;SharedAccessKey=HeHeVaVqyVkntO2FnjQcs2Ilh/4MUDo4y+AEhKp8z+g=;EntityPath=simulacoes";
		String eventHubName = "simulacoes";
		String consumerGroup = EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME;

		// Criação do cliente do Event Hub
		EventHubClientBuilder eventHubClientBuilder = new EventHubClientBuilder()
				.connectionString(connectionString, eventHubName).consumerGroup(consumerGroup);

		// Configuração do leitor de eventos
		EventHubConsumerAsyncClient consumerClient = eventHubClientBuilder.buildAsyncConsumerClient();

		// Recebendo eventos
		consumerClient.receive().subscribe(event -> {
			System.out.println("Evento recebido: " + event.getData().getBodyAsString());
		}, error -> {
			System.err.println("Erro ao receber eventos: " + error.toString());
		});

		// Aguardando a recepção dos eventos por um período de tempo
		try {
			TimeUnit.SECONDS.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Encerrando o cliente
		consumerClient.close();
	}

}

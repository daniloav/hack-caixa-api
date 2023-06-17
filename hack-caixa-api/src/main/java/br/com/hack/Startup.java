package br.com.hack;

//Danilo Sousa de Oliveira - C137050 / GIT: daniloav/hack-caixa-api

import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.azure.messaging.eventhubs.EventHubClientBuilder;
import com.azure.messaging.eventhubs.EventHubConsumerAsyncClient;

@SpringBootApplication
public class Startup {

	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
		
		
		//Validar envio ao eventHub - Tah de 30 em 30 segundos
		String connectionString = "Endpoint=sb://eventhack.servicebus.windows.net/;SharedAccessKeyName=hack;SharedAccessKey=HeHeVaVqyVkntO2FnjQcs2Ilh/4MUDo4y+AEhKp8z+g=;EntityPath=simulacoes";
		String eventHubName = "simulacoes";
		String consumerGroup = EventHubClientBuilder.DEFAULT_CONSUMER_GROUP_NAME;
		EventHubClientBuilder eventHubClientBuilder = new EventHubClientBuilder()
				.connectionString(connectionString, eventHubName).consumerGroup(consumerGroup);
		EventHubConsumerAsyncClient consumerClient = eventHubClientBuilder.buildAsyncConsumerClient();
		consumerClient.receive().subscribe(event -> {
			System.out.println("Evento recebido: " + event.getData().getBodyAsString());
		}, error -> {
			System.err.println("Erro ao receber eventos: " + error.toString());
		});
		try {
			TimeUnit.SECONDS.sleep(30);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		consumerClient.close();
	}

}

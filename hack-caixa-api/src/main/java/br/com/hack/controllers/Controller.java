package br.com.hack.controllers;

//Danilo Sousa de Oliveira - C137050 / GIT: daniloav/hack-caixa-api

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hack.model.Request;
import br.com.hack.model.Response;
import br.com.hack.services.SimulacaoService;

@RestController
@RequestMapping("/emprestimo")
public class Controller {

	private final SimulacaoService simulacaoEmprestimoService;

	@Autowired
	public Controller(SimulacaoService simulacaoEmprestimoService) {
		this.simulacaoEmprestimoService = simulacaoEmprestimoService;
	}

	@PostMapping("/simular")
	public ResponseEntity<Response> simularEmprestimo(
			@RequestBody Request solicitacao) {
		Response response = simulacaoEmprestimoService.simularEmprestimo(solicitacao);
		if (response != null) {
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
}

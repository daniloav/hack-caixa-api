package br.com.hack.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.hack.model.SimulacaoEmprestimoResponse;

@RestController
@RequestMapping("/emprestimo")
public class SimulacaoEmprestimoController {

    private final SimulacaoEmprestimoService simulacaoEmprestimoService;

    @Autowired
    public SimulacaoEmprestimoController(SimulacaoEmprestimoService simulacaoEmprestimoService) {
        this.simulacaoEmprestimoService = simulacaoEmprestimoService;
    }

    @PostMapping("/simular")
    public ResponseEntity<SimulacaoEmprestimoResponse> simularEmprestimo(@RequestBody SolicitacaoSimulacaoEmprestimo solicitacao) {
        SimulacaoEmprestimoResponse response = simulacaoEmprestimoService.simularEmprestimo(solicitacao);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}


package br.com.hack.model;

import java.util.List;

public class SimulacaoEmprestimoResponse {
	
	private int codigoProduto;
    private String descricaoProduto;
    private double taxaJuros;
    private Parcelas resultadoSimulacaoSAC;
    private Parcelas resultadoSimulacaoPRICE;

    
	public int getCodigoProduto() {
		return codigoProduto;
	}
	public void setCodigoProduto(int codigoProduto) {
		this.codigoProduto = codigoProduto;
	}
	public String getDescricaoProduto() {
		return descricaoProduto;
	}
	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}
	public double getTaxaJuros() {
		return taxaJuros;
	}
	public void setTaxaJuros(double taxaJuros) {
		this.taxaJuros = taxaJuros;
	}
	

	public Parcelas getResultadoSimulacaoSAC() {
		return resultadoSimulacaoSAC;
	}
	public void setResultadoSimulacaoSAC(Parcelas sac, Parcelas price) {
		this.resultadoSimulacaoSAC = sac;
		this.resultadoSimulacaoPRICE = price;
	}
	public Parcelas getResultadoSimulacaoPRICE() {
		return resultadoSimulacaoPRICE;
	}
	public void setResultadoSimulacaoPRICE(Parcelas resultadoSimulacaoPRICE) {
		this.resultadoSimulacaoPRICE = resultadoSimulacaoPRICE;
	}

	
	
	    
}

package br.com.hack.model;

import java.util.List;

public class SimulacaoEmprestimoResponse {
	
	private int codigoProduto;
    private String descricaoProduto;
    private double taxaJuros;
    private List<Parcela> resultadoSimulacaoSAC;
    private List<Parcela> resultadoSimulacaoPRICE;
    private String tipo;
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
	public List<Parcela> getResultadoSimulacaoSAC() {
		return resultadoSimulacaoSAC;
	}
	public void setResultadoSimulacaoSAC(List<Parcela> resultadoSimulacaoSAC) {
		this.resultadoSimulacaoSAC = resultadoSimulacaoSAC;
	}
	public List<Parcela> getResultadoSimulacaoPRICE() {
		return resultadoSimulacaoPRICE;
	}
	public void setResultadoSimulacaoPRICE(List<Parcela> resultadoSimulacaoPRICE) {
		this.resultadoSimulacaoPRICE = resultadoSimulacaoPRICE;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
    
	
	    
}

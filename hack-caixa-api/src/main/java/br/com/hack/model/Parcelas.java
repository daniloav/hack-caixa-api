package br.com.hack.model;

public class Parcelas {
	
	private Double valorAmortizacao;
	private Double valorJuros;
	private Double valorPrestacao;
	
	
	
	public Parcelas(Double valorAmortizacao, Double valorJuros, Double valorPrestacao, int numero) {
		super();
		this.valorAmortizacao = valorAmortizacao;
		this.valorJuros = valorJuros;
		this.valorPrestacao = valorPrestacao;
		this.numero = numero;
	}
	
	private int numero;
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public Double getValorAmortizacao() {
		return valorAmortizacao;
	}
	public void setValorAmortizacao(Double valorAmortizacao) {
		this.valorAmortizacao = valorAmortizacao;
	}
	public Double getValorJuros() {
		return valorJuros;
	}
	public void setValorJuros(Double valorJuros) {
		this.valorJuros = valorJuros;
	}
	public Double getValorPrestacao() {
		return valorPrestacao;
	}
	public void setValorPrestacao(Double valorPrestacao) {
		this.valorPrestacao = valorPrestacao;
	}
	

}

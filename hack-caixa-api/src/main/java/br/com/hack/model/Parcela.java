package br.com.hack.model;

public class Parcela {

	private int numero;
	private double valorAmortizacao;
	private double valorJuros;
	private double valorPrestacao;

	
	public int getNumero() {
		return numero;
	}
	public void setNumero(int numero) {
		this.numero = numero;
	}
	public double getValorAmortizacao() {
		return valorAmortizacao;
	}
	public void setValorAmortizacao(double valorAmortizacao) {
		this.valorAmortizacao = valorAmortizacao;
	}
	public double getValorJuros() {
		return valorJuros;
	}
	public void setValorJuros(double valorJuros) {
		this.valorJuros = valorJuros;
	}
	public double getValorPrestacao() {
		return valorPrestacao;
	}
	public void setValorPrestacao(double valorPrestacao) {
		this.valorPrestacao = valorPrestacao;
	}

}

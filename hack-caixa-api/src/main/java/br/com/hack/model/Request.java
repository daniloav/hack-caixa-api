package br.com.hack.model;

//Danilo Sousa de Oliveira - C137050 / GIT: daniloav/hack-caixa-api

public class Request {

	private double valorSolicitado;
	private int quantidadeParcelas;

	public double getValorSolicitado() {
		return valorSolicitado;
	}

	public void setValorSolicitado(double valorSolicitado) {

		this.valorSolicitado = valorSolicitado;
	}

	public int getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(int quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}
}

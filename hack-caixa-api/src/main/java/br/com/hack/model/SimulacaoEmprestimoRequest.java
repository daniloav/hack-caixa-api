package br.com.hack.model;

public class SimulacaoEmprestimoRequest {

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

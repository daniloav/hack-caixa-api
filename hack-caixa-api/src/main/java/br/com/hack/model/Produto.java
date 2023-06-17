package br.com.hack.model;

//Danilo Sousa de Oliveira - C137050 / GIT: daniloav/hack-caixa-api

public class Produto {

	private int codigoProduto;

	private String nomeProduto;

	private double taxaJuros;

	private int numeroMinimoParcelas;

	private int numeroMaximoParcelas;

	private double valorMinimo;

	private double valorMaximo;

	public int getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(int codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public double getTaxaJuros() {
		return taxaJuros;
	}

	public void setTaxaJuros(double taxaJuros) {
		this.taxaJuros = taxaJuros;
	}

	public int getNumeroMinimoParcelas() {
		return numeroMinimoParcelas;
	}

	public void setNumeroMinimoParcelas(int numeroMinimoParcelas) {
		this.numeroMinimoParcelas = numeroMinimoParcelas;
	}

	public int getNumeroMaximoParcelas() {
		return numeroMaximoParcelas;
	}

	public void setNumeroMaximoParcelas(int numeroMaximoParcelas) {
		this.numeroMaximoParcelas = numeroMaximoParcelas;
	}

	public double getValorMinimo() {
		return valorMinimo;
	}

	public void setValorMinimo(double valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public double getValorMaximo() {
		return valorMaximo;
	}

	public void setValorMaximo(double valorMaximo) {
		this.valorMaximo = valorMaximo;
	}



}

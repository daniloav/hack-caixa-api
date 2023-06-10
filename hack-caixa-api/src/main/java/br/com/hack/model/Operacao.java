package br.com.hack.model;

public class Operacao extends Parcelas {
	
	public Operacao(Double valorAmortizacao, Double valorJuros, Double valorPrestacao, int numero) {
		super(valorAmortizacao, valorJuros, valorPrestacao, numero);
		// TODO Auto-generated constructor stub
	}

	private String tipo;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
	
	

}

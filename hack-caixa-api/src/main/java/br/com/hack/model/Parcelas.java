package br.com.hack.model;

import java.util.List;

public class Parcelas {
	
	private String tipo;
	private List<Parcela> parcelas;
	
	
	
	
	public Parcelas(String tipo, List<Parcela> parcelas) {

		this.parcelas = parcelas;
		setTipo(tipo);
	}
	
	public List<Parcela> getParcelas() {
		return parcelas;
	}
	public void setParcelas(List<Parcela> parcelas) {
		this.parcelas = parcelas;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


}

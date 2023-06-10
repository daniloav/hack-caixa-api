package br.com.hack.model;

import java.util.List;

public class SimulacaoEmprestimoResponse {
    private int codigoProduto;
    private String descricaoProduto;
    private double taxaJuros;
    private List<ResultadoSimulacao> resultadoSimulacao;

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

    public List<ResultadoSimulacao> getResultadoSimulacao() {
        return resultadoSimulacao;
    }

    public void setResultadoSimulacao(List<ResultadoSimulacao> resultadoSimulacao) {
        this.resultadoSimulacao = resultadoSimulacao;
    }

    public static class ResultadoSimulacao {
        private String tipo;
        private List<Parcela> parcelas;

        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public List<Parcela> getParcelas() {
            return parcelas;
        }

        public void setParcelas(List<Parcela> parcelas) {
            this.parcelas = parcelas;
        }
    }

    public static class Parcela {
        private int numero;
        private double valorAmortizacao;
        private double valorJuros;
        private double valorPrestacao;
        
        public Parcela(int numero, double valorAmortizacao, double valorJuros, double valorPrestacao) {
            this.numero = numero;
            this.valorAmortizacao = valorAmortizacao;
            this.valorJuros = valorJuros;
            this.valorPrestacao = valorPrestacao;
        }

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
}

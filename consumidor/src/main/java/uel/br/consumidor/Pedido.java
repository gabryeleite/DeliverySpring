package uel.br.consumidor;

public class Pedido extends ItemCardapio {
    private int quantidade;
    private Double valorTotal;
    private String nomeRestaurante;

    public Pedido(int id, String nome, String descricao, double preco, String nomeRestaurante) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.nomeRestaurante = nomeRestaurante;
        this.quantidade = 1;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getNomeRestaurante() {
        return nomeRestaurante;
    }

    public void setNomeRestaurante(String nomeRestaurante) {
        this.nomeRestaurante = nomeRestaurante;
    }
}

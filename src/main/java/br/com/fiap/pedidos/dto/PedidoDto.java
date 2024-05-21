package br.com.fiap.pedidos.dto;


import java.io.Serializable;
import java.util.List;


public class PedidoDto implements Serializable {

    private String cpfCliente;
    private List<Long> produtosIds;
    private Long enderecoId;

    // Getters e Setters

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public List<Long> getProdutosIds() {
        return produtosIds;
    }

    public void setProdutosIds(List<Long> produtosIds) {
        this.produtosIds = produtosIds;
    }

    public Long getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Long enderecoId) {
        this.enderecoId = enderecoId;
    }
}
package br.com.fiap.pedidos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponseDto implements Serializable {

    private String cpfCliente;
    private String codigoDoPedido;
    private String nomeCliente;
    private String emailCliente;
    private Double ValorTotal;

}

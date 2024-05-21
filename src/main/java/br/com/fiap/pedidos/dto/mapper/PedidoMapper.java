package br.com.fiap.pedidos.dto.mapper;

import br.com.fiap.pedidos.dto.PedidoResponseDto;
import br.com.fiap.pedidos.entity.Pedido;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PedidoMapper {

    public static PedidoResponseDto toDto(Pedido pedido) {
        PedidoResponseDto dto = new PedidoResponseDto();

        dto.setNomeCliente(pedido.getCliente().getNome());
        dto.setCpfCliente(pedido.getCliente().getCpf());
        dto.setCodigoDoPedido(pedido.getCodigoPedido());
        dto.setEmailCliente(pedido.getCliente().getEmail());
        dto.setValorTotal(pedido.getValorTotal());

        return dto;
    }
}

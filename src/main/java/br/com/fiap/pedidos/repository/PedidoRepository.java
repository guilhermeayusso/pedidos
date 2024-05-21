package br.com.fiap.pedidos.repository;


import br.com.fiap.pedidos.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<Pedido> findByCodigoPedido(String codigoPedido);
}

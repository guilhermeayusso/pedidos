package br.com.fiap.pedidos.repository;


import br.com.fiap.pedidos.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ClienteRepository extends JpaRepository<Cliente, Long> {


}

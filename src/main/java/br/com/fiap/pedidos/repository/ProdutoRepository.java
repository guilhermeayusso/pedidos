package br.com.fiap.pedidos.repository;

import br.com.fiap.pedidos.entity.Produto;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ProdutoRepository extends JpaRepository<Produto, Long> {


}

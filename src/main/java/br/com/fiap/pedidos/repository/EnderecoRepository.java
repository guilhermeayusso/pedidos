package br.com.fiap.pedidos.repository;

import br.com.fiap.pedidos.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
}
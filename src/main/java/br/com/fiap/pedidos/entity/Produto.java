package br.com.fiap.pedidos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "produto")
public class Produto implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idProdutoPedido;

  @Column(nullable = false)
  private Long id;

  @Column(nullable = false, length = 100)
  private String nome;

  @Column(nullable = false)
  private double preco;

  @Column(length = 500)
  private String descricao;

  @Transient
  private int quantidadeEmEstoque;


  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pedido_id", nullable = false)
  private Pedido pedido;

  @Override
  public String toString() {
    return "Produto{" +
            "id=" + id +
            ", nome='" + nome + '\'' +
            ", preco=" + preco +
            ", descricao='" + descricao + '\'' +
            ", quantidadeEmEstoque=" + quantidadeEmEstoque +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Produto produto = (Produto) o;
    return Double.compare(preco, produto.preco) == 0 && quantidadeEmEstoque == produto.quantidadeEmEstoque && Objects.equals(id, produto.id) && Objects.equals(nome, produto.nome) && Objects.equals(descricao, produto.descricao);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nome, preco, descricao, quantidadeEmEstoque);
  }
}
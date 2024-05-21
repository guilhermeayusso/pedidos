package br.com.fiap.pedidos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private double valorTotal;

  @Column(nullable = false)
  private String codigoPedido;

  @Column(nullable = false)
  private String cep;

  @JsonIgnore
  @ManyToOne
  @JoinColumn(name = "cliente_id")
  private Cliente cliente;

  @Enumerated(EnumType.STRING)
  @Column(name = "pagamento",nullable = false,length = 25)
  private Pagamento pagamento = Pagamento.NAO_REALIZADO;

  @Enumerated(EnumType.STRING)
  @Column(name = "status",nullable = false,length = 25)
  private StatusPedido statusPedido = StatusPedido.PENDENTE;

  @JsonIgnore
  @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Produto> produtos;


  public enum Pagamento {
    REALIZADO, NAO_REALIZADO
  }

  public enum StatusPedido {
    PENDENTE,
    CONFIRMADO,
    CANCELADO,
    ENTREGUE
  }

  @Override
  public String toString() {
    return "Pedido{" +
            "id=" + id +
            ", valorTotal=" + valorTotal +
            ", codigoPedido='" + codigoPedido + '\'' +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Pedido pedido = (Pedido) o;
    return Double.compare(valorTotal, pedido.valorTotal) == 0 && Objects.equals(id, pedido.id) && Objects.equals(codigoPedido, pedido.codigoPedido);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, valorTotal, codigoPedido);
  }
}
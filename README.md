# Microsserviço de Pedidos

Este microsserviço gerencia pedidos, permitindo operações de criação, confirmação, cancelamento e agrupamento por CEP.

## Endpoints

### Agrupar por CEP

**Descrição:** Agrupa pedidos por proximidade de CEP.

- **URL:** `/pedidos/agrupar-por-cep`
- **Método:** `GET`
- **Resposta de Sucesso:**
    - **Código:** `200 OK`
    - **Exemplo de Corpo da Resposta:**
      ```json
      [
        ["pedidoId1", "pedidoId2"],
        ["pedidoId3"]
      ]
      ```

### Cadastrar Pedido

**Descrição:** Cria um novo pedido.

- **URL:** `/api/v1/pedidos`
- **Método:** `POST`
- **Corpo da Requisição:**
  ```json
  {
    "cpfCliente": "76252398454",
    "produtosIds": [1, 2, 4],
    "enderecoId": 1
  }
  ```
### Confirmar Pedido
- **Descrição:** Confirma um pedido existente.
- **URL:** /api/v1/pedidos/confirmar/{codigoPedido}
- **Método:** PUT

### Cancelar Pedido
- **Descrição:** Confirma um pedido existente.
- **URL:** /api/v1/pedidos/cancelar/{codigoPedido}
- **Método:** PUT

### Finalizar Pedido
- **Descrição:** Confirma um pedido existente.
- **URL:** /api/v1/pedidos/finalizar/{codigoPedido}
- **Método:** PUT
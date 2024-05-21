package br.com.fiap.pedidos.service;

import br.com.fiap.pedidos.dto.PedidoDto;
import br.com.fiap.pedidos.entity.Cliente;
import br.com.fiap.pedidos.entity.Endereco;
import br.com.fiap.pedidos.entity.Pedido;
import br.com.fiap.pedidos.entity.Produto;
import br.com.fiap.pedidos.exception.*;
import br.com.fiap.pedidos.repository.ClienteRepository;
import br.com.fiap.pedidos.repository.PedidoRepository;
import br.com.fiap.pedidos.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@RequiredArgsConstructor
@Service
public class PedidoService {


    @Autowired
    private WebClient.Builder webClientBuilder;

    private final PedidoRepository pedidoRepository;

    private final EnderecoService enderecoService;

    private final ClienteRepository clienteRepository;

    private final ProdutoRepository produtoRepository;

    @Autowired
    private final GeolocationService geolocationService;


    @Autowired
    private Environment env;

    public Cliente getCliente(String cpf) {

        String url = env.getProperty("url.clientes") + cpf;
        WebClient webClient = webClientBuilder.build();

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        clientResponse -> Mono.error(new ClienteNotFoundException("Cliente não encontrado.")))
                .onStatus(status -> status.is5xxServerError(),
                        clientResponse -> Mono.error(new ClienteServerException("Erro de servidor ao atualizar o cliente")))
                .bodyToMono(Cliente.class)
                .block();

    }

    public Produto getProduto(Long id) {
        String url = env.getProperty("url.produtos") + id;
        WebClient webClient = webClientBuilder.build();

        return webClient.get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        clientResponse -> Mono.error(new ProdutoNotFoundException("Produto não encontrado.")))
                .onStatus(status -> status.is5xxServerError(),
                        clientResponse -> Mono.error(new ProdutoServerException("Erro de servidor ao atualizar o produto")))
                .bodyToMono(Produto.class)
                .block();

    }

    public List<List<Pedido>> agruparPedidosPorCepProximo() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<List<Pedido>> gruposDePedidos = new ArrayList<>();

        // Mapear CEPs para coordenadas
        Map<String, double[]> cepCoordenadas = new HashMap<>();
        for (Pedido pedido : pedidos) {
            String cep = pedido.getCep();
            double[] coordenadas = geolocationService.getLatLong(cep);
            if (coordenadas != null) {
                cepCoordenadas.put(cep, coordenadas);
            }
        }

        // Algoritmo de agrupamento baseado na distância
        for (Pedido pedido : pedidos) {
            String cep = pedido.getCep();
            double[] coordenadas = cepCoordenadas.get(cep);
            boolean addedToGroup = false;

            for (List<Pedido> grupo : gruposDePedidos) {
                Pedido grupoPedido = grupo.get(0);
                double[] grupoCoordenadas = cepCoordenadas.get(grupoPedido.getCep());
                double distancia = geolocationService.calculateDistance(coordenadas, grupoCoordenadas);

                if (distancia < 5) { // Considerar pedidos próximos se a distância for menor que 5 km
                    grupo.add(pedido);
                    addedToGroup = true;
                    break;
                }
            }

            if (!addedToGroup) {
                List<Pedido> novoGrupo = new ArrayList<>();
                novoGrupo.add(pedido);
                gruposDePedidos.add(novoGrupo);
            }
        }

        return gruposDePedidos;
    }


    private void atualizarEstoqueProduto(Long produtoId, Integer quantidade) {
        String url = env.getProperty("url.atualizaestoque") + produtoId;

        Produto produto = new Produto();
        produto.setQuantidadeEmEstoque(quantidade);

        HttpEntity<Produto> requestEntity = new HttpEntity<>(produto);
        WebClient webClient = webClientBuilder.build();

        webClient.patch()
                .uri(url)
                .body(Mono.just(produto), Produto.class)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        clientResponse -> Mono.error(new ProdutoNotFoundException("Produto não encontrado.")))
                .onStatus(status -> status.is5xxServerError(),
                        clientResponse -> Mono.error(new ProdutoServerException("Erro de servidor ao atualizar o produto")))
                .bodyToMono(Void.class)
                .block();

    }


    @Transactional
    public Pedido save(PedidoDto pedidoDto) {

        double vlrTotal = 0;
        List<Produto> produtosList = new ArrayList<>();
        Pedido pedidoSave = new Pedido();
        Cliente cliente = new Cliente();
        Produto produto = new Produto();

        cliente = getCliente(pedidoDto.getCpfCliente());

        List enderecosSave = new ArrayList();

        if (cliente.getEnderecos() != null && !cliente.getEnderecos().isEmpty()) {
            for (Endereco endereco : cliente.getEnderecos()) {
                if (endereco.getId() == pedidoDto.getEnderecoId()) {
                    enderecosSave.add(endereco);
                    pedidoSave.setCep(endereco.getCep());
                }
            }
        }
        if (cliente.getEnderecos() != null && !cliente.getEnderecos().isEmpty()) {
            cliente.getEnderecos().clear();
        }

        for (Long id : pedidoDto.getProdutosIds()) {
            produto = getProduto(id);
            if (produto != null) {
                if (produto.getQuantidadeEmEstoque() >= 1) {
                    produto.setPedido(pedidoSave);
                    vlrTotal += produto.getPreco();
                    produtosList.add(produto);
                } else {
                    throw new OutOfStockException(produto.getNome());
                }
            }
        }

        cliente.setId(null);


        cliente = clienteRepository.saveAndFlush(cliente);

        enderecoService.salvarEnderecos(enderecosSave, cliente);

        pedidoSave.setCodigoPedido(generateUniqueCode());
        pedidoSave.setCliente(cliente);
        pedidoSave.setValorTotal(vlrTotal);

        pedidoSave = pedidoRepository.save(pedidoSave);
        produtoRepository.saveAllAndFlush(produtosList);

        for (Produto prd : produtosList) {
            atualizarEstoqueProduto(prd.getId(), prd.getQuantidadeEmEstoque() - 1);
        }


        return pedidoSave;
    }

    @Transactional
    public Pedido confirmarPedido(String codigoPedido) {
        Pedido pedido = pedidoRepository.findByCodigoPedido(codigoPedido).orElseThrow(
                () -> new EntityNotFoundException("Pedido Não Existe")
        );
        if (pedido.getStatusPedido() == Pedido.StatusPedido.PENDENTE) {
            pedido.setPagamento(Pedido.Pagamento.REALIZADO);
            pedido.setStatusPedido(Pedido.StatusPedido.CONFIRMADO);
            return pedidoRepository.save(pedido);
        } else {
            throw new OrderCannotBeCancelledException("Pedido não está pendente. ");
        }
    }

    @Transactional
    public Pedido cancelarPedido(String codigoPedido) {
        Pedido pedido = pedidoRepository.findByCodigoPedido(codigoPedido).orElseThrow(
                () -> new EntityNotFoundException("Pedido Não Existe")
        );
        if (pedido.getStatusPedido() == Pedido.StatusPedido.PENDENTE) {
            pedido.setStatusPedido(Pedido.StatusPedido.CANCELADO);
            return pedidoRepository.save(pedido);
        } else {
            throw new OrderCannotBeCancelledException("Pedido não pode ser cancelado. ");
        }
    }

    @Transactional
    public Pedido finalizarPedido(String codigoPedido) {
        Pedido pedido = pedidoRepository.findByCodigoPedido(codigoPedido).orElseThrow(
                () -> new EntityNotFoundException("Pedido Não Existe")
        );
        if (pedido.getStatusPedido() == Pedido.StatusPedido.CONFIRMADO) {
            pedido.setStatusPedido(Pedido.StatusPedido.ENTREGUE);
            return pedidoRepository.save(pedido);
        } else {
            throw new OrderCannotBeCancelledException("Pedido não está confirmado. ");
        }
    }

    public String generateUniqueCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }


}

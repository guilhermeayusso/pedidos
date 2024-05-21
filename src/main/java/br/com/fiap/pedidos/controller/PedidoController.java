package br.com.fiap.pedidos.controller;


import br.com.fiap.pedidos.dto.PedidoDto;
import br.com.fiap.pedidos.dto.PedidoResponseDto;
import br.com.fiap.pedidos.dto.mapper.PedidoMapper;
import br.com.fiap.pedidos.entity.Pedido;
import br.com.fiap.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDto> getCliente (@RequestBody PedidoDto pedidoDto){
        Pedido pedido = pedidoService.save(pedidoDto);
        PedidoResponseDto responseDto = PedidoMapper.toDto(pedido);
        return ResponseEntity.status(201).body(responseDto);
    }

    @PutMapping("/confirmar/{codigoPedido}")
    public ResponseEntity<PedidoResponseDto> confirmarPedido(@PathVariable String codigoPedido){
        Pedido pedido = pedidoService.confirmarPedido(codigoPedido);
        PedidoResponseDto dto = PedidoMapper.toDto(pedido);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/cancelar/{codigoPedido}")
    public ResponseEntity<PedidoResponseDto> cancelarPedido(@PathVariable String codigoPedido){
        Pedido pedido = pedidoService.cancelarPedido(codigoPedido);
        PedidoResponseDto dto = PedidoMapper.toDto(pedido);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/finalizar/{codigoPedido}")
    public ResponseEntity<PedidoResponseDto> finalizarPedido(@PathVariable String codigoPedido){
        Pedido pedido = pedidoService.finalizarPedido(codigoPedido);
        PedidoResponseDto dto = PedidoMapper.toDto(pedido);
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/agrupar-por-cep")
    public ResponseEntity<List<List<Pedido>>> agruparPedidosPorCep() {
        List<List<Pedido>> gruposDePedidos = pedidoService.agruparPedidosPorCepProximo();
        return ResponseEntity.ok(gruposDePedidos);
    }
}


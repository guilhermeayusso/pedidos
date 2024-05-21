package br.com.fiap.pedidos.service;


import br.com.fiap.pedidos.entity.Cliente;
import br.com.fiap.pedidos.entity.Endereco;
import br.com.fiap.pedidos.repository.EnderecoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    @Transactional
    public void salvarEnderecos(List<Endereco> enderecoList, Cliente cliente) {

        if (enderecoList != null && !enderecoList.isEmpty()) {
            for (Endereco endereco : enderecoList) {
                endereco.setId(null);
                endereco.setCliente(cliente);
                enderecoRepository.save(endereco);
            }
        }
    }



}

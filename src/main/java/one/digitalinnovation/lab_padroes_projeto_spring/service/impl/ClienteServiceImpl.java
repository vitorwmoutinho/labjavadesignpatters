package one.digitalinnovation.lab_padroes_projeto_spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import one.digitalinnovation.lab_padroes_projeto_spring.model.Cliente;
import one.digitalinnovation.lab_padroes_projeto_spring.model.ClienteRepository;
import one.digitalinnovation.lab_padroes_projeto_spring.model.Endereco;
import one.digitalinnovation.lab_padroes_projeto_spring.model.EnderecoRepository;
import one.digitalinnovation.lab_padroes_projeto_spring.service.ClienteService;
import one.digitalinnovation.lab_padroes_projeto_spring.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {
    // Singleton: Injetar os componentes do Spring com @Autowired.
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    // Strategy: Implementar os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<Cliente> buscarTodos() {
        // Buscar todos os Clientes.
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        // Buscar Cliente por ID de forma segura.
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public void inserir(Cliente cliente) {
        // Inserir Cliente com o tratamento do CEP.
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        // Buscar Cliente por ID, caso exista, atualizar.
        Optional<Cliente> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            cliente.setId(id); // Atualiza o ID do cliente com o existente.
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        // Deletar Cliente por ID.
        clienteRepository.deleteById(id);
    }

    private <S> void salvarClienteComCep(Cliente cliente) {
        // Verificar se o Endereco do Cliente já existe (pelo CEP).
        String cep = cliente.getEndereco();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCEP e persistir o retorno.
            ViaCepService novoEndereco = viaCepService;
            enderecoRepository.save((S) novoEndereco);
            return novoEndereco;
        });
        cliente.getEndereco();
        // Inserir ou atualizar Cliente, vinculando o Endereco (novo ou existente).
        clienteRepository.save(cliente);
    }

}

 

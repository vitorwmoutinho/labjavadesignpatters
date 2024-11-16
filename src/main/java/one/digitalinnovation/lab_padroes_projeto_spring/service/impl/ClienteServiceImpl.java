package one.digitalinnovation.lab_padroes_projeto_spring.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.lab_padroes_projeto_spring.model.Cliente;
import one.digitalinnovation.lab_padroes_projeto_spring.model.ClienteRepository;
import one.digitalinnovation.lab_padroes_projeto_spring.model.Endereco;
import one.digitalinnovation.lab_padroes_projeto_spring.model.EnderecoRepository;
import one.digitalinnovation.lab_padroes_projeto_spring.service.ClienteService;
import one.digitalinnovation.lab_padroes_projeto_spring.service.ViaCepService;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private EnderecoRepository enderecoRepository;
    
    @Autowired
    private ViaCepService viaCepService;

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
        // Inserir um novo cliente com o endereço.
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        // Atualizar Cliente existente (caso exista).
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            cliente.setId(id);
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        // Deletar Cliente por ID.
        clienteRepository.deleteById(id);
    }

    /**
     * Método privado para salvar o Cliente com base no CEP.
     * 
     * @param cliente Objeto Cliente a ser salvo.
     */
    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}

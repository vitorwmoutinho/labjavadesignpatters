package one.digitalinnovation.lab_padroes_projeto_spring.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import one.digitalinnovation.lab_padroes_projeto_spring.model.Endereco;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
public interface ViaCepService {

	Object enderecoRepository = null;

    @GetMapping("/{cep}/json/")
	Endereco consultarCep(@PathVariable("cep") String cep);

    public Endereco consultarCep(String cep);
}

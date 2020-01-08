package br.com.dividas.controle.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.dividas.controle.dto.PlanilhaDividas;
import br.com.dividas.controle.service.DividasService;

@RestController
@RequestMapping("/")
public class DividasController {
	
	@Autowired
	private DividasService service;
	
	@RequestMapping(value = "/teste", method = RequestMethod.POST)
    public PlanilhaDividas buscarDividaPorAmigo(@RequestParam String nome) throws IOException, GeneralSecurityException {
		
		service.buscarDividaPorAmigo(nome);
		
		return null;
    }

}

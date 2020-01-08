package br.com.dividas.controle.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.dividas.controle.dto.PlanilhaDividas;
import br.com.dividas.controle.dto.Emprestimo;
import br.com.dividas.controle.dto.Pessoa;

@Service
public class PlanilhaDividasService {
	
	@Autowired
	private GoogleSheetsService googleSheetsService;
	
	@Value("${google.spreadsheet.id}")
	private String spreadsheetId;
	
	private static final String RANGE = "dividas";
	
	public PlanilhaDividas buscar() throws GeneralSecurityException, IOException {
		List<List<Object>> planilha = googleSheetsService.buscar(spreadsheetId, RANGE);
		
		return montarDivida(planilha);
	}
	
	private PlanilhaDividas montarDivida(List<List<Object>> planilha) {
		if(planilha == null)
			return new PlanilhaDividas();
		
		return PlanilhaDividas.builder().dividas(planilha.stream().map(this::pessoaMapper).collect(Collectors.toList())).build();
	}
	
	private Pessoa pessoaMapper(List<Object> linha) {
		String nome = (String) linha.get(0);
		linha.remove(nome);
		List<Emprestimo> emprestimo = linha.stream().map(valor -> new Emprestimo(valor)).collect(Collectors.toList());
		
		return new Pessoa(nome, emprestimo);
	}

}

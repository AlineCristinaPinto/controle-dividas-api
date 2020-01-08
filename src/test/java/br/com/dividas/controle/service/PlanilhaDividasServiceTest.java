package br.com.dividas.controle.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.dividas.controle.dto.PlanilhaDividas;
import br.com.dividas.controle.dto.Emprestimo;
import br.com.dividas.controle.dto.Pessoa;

public class PlanilhaDividasServiceTest {
	
	@InjectMocks
	private PlanilhaDividasService service;
	
	@Mock
	private GoogleSheetsService googleSheetsService;
	
	@Before
    public void setup(){
		MockitoAnnotations.initMocks(this);
    }
	
	@Test(expected=IOException.class)
	public void buscar_erroExcecao_IOException() throws GeneralSecurityException, IOException{
		Mockito.when(googleSheetsService.buscar(Mockito.any(), Mockito.any())).thenThrow(IOException.class);
		
		service.buscar();
	}
	
	@Test(expected=GeneralSecurityException.class)
	public void buscar_erroExcecao_GeneralSecurityException() throws GeneralSecurityException, IOException{
		Mockito.when(googleSheetsService.buscar(Mockito.any(), Mockito.any())).thenThrow(GeneralSecurityException.class);
		
		service.buscar();
	}
	
	@Test
	public void buscar_planilhaVazia() throws GeneralSecurityException, IOException {
		Mockito.when(googleSheetsService.buscar(Mockito.any(), Mockito.any())).thenReturn(null);
		
		PlanilhaDividas planilhaDividas = service.buscar();
		PlanilhaDividas dividaEsperada = new PlanilhaDividas();
		
		Assert.assertEquals(dividaEsperada, planilhaDividas);
	}
	
	@Test
	public void buscar_sucesso() throws GeneralSecurityException, IOException {
		List<List<Object>> retornoPlanilha = montarRetornoPlanilha();
		Mockito.when(googleSheetsService.buscar(Mockito.any(), Mockito.any())).thenReturn(retornoPlanilha);
		
		PlanilhaDividas planilhaDividas = service.buscar();
		PlanilhaDividas dividaEsperada = montarDivida();
		
		Assert.assertEquals(dividaEsperada, planilhaDividas);
	}	
	
	private PlanilhaDividas montarDivida() {
		List<Pessoa> pessoas = new ArrayList<>();
		pessoas.add(montarPessoa("João", "5.0"));
		pessoas.add(montarPessoa("Ana", "10.85"));
		
		return PlanilhaDividas.builder().dividas(pessoas).build();
	}
	
	private Pessoa montarPessoa(String nome, String valor){
		List<Emprestimo> emprestimo = new ArrayList<>();		
		emprestimo.add(new Emprestimo(valor));
		
		return new Pessoa(nome, emprestimo);
	}
	
	private List<List<Object>> montarRetornoPlanilha(){
		List<List<Object>> planilha = new ArrayList<>();
				
		planilha.add(montarLinha("João", "5"));
		planilha.add(montarLinha("Ana", "10.85"));
		
		return planilha;
	}
	
	private List<Object> montarLinha(String nome, String valor){
		List<Object> linha = new ArrayList<>();
		
		linha.add(nome);
		linha.add(valor);
		
		return linha;
	}
	

}

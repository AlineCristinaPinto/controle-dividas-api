package br.com.dividas.controle.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.dividas.controle.dto.Emprestimo;
import br.com.dividas.controle.dto.Pessoa;
import br.com.dividas.controle.dto.PlanilhaDividas;
import br.com.dividas.controle.exception.BusinessException;
import br.com.dividas.controle.response.DividaResponse;


public class DividasServiceTest {
	
	@InjectMocks
	private DividasService service;

	@Mock
	private PlanilhaDividasService planilhaDividasService;
	
	private static final String NOME_ANA = "Ana";
	private static final String NOME_JOAO = "João";
	
	@Before
    public void setup(){
		MockitoAnnotations.initMocks(this);
    }
	
	@Test(expected=BusinessException.class)
	public void buscarDividaPorAmigo_erroNomeNulo() {
		service.buscarDividaPorAmigo(null);
	}
	
	@Test(expected=BusinessException.class)
	public void buscarDividaPorAmigo_erroNomeVazio() {
		service.buscarDividaPorAmigo(StringUtils.EMPTY);
	}
	
	@Test(expected=BusinessException.class)
	public void buscarDividaPorAmigo_erroBuscarPlanilha_IOException() throws GeneralSecurityException, IOException {
		Mockito.when(planilhaDividasService.buscar()).thenThrow(IOException.class);
		service.buscarDividaPorAmigo(NOME_ANA);
	}
	
	@Test(expected=BusinessException.class)
	public void buscarDividaPorAmigo_erroBuscarPlanilha_GeneralSecurityException() throws GeneralSecurityException, IOException {
		Mockito.when(planilhaDividasService.buscar()).thenThrow(GeneralSecurityException.class);
		service.buscarDividaPorAmigo(NOME_ANA);
	}
	
	@Test(expected=BusinessException.class)
	public void buscarDividaPorAmigo_erroBuscarPlanilha_PlanilhaVazia() throws GeneralSecurityException, IOException {
		Mockito.when(planilhaDividasService.buscar()).thenReturn(new PlanilhaDividas());
		service.buscarDividaPorAmigo(NOME_ANA);
	}
	
	@Test(expected=BusinessException.class)
	public void buscarDividaPorAmigo_nomeNaoEncontrado() throws GeneralSecurityException, IOException {
		PlanilhaDividas planilha = montarPlanilha();
		Mockito.when(planilhaDividasService.buscar()).thenReturn(planilha);
		service.buscarDividaPorAmigo(NOME_ANA);
	}
	
	@Test
	public void buscarDividaPorAmigo_sucesso() throws GeneralSecurityException, IOException {
		PlanilhaDividas planilha = montarPlanilha();
		Mockito.when(planilhaDividasService.buscar()).thenReturn(planilha);
		List<DividaResponse> divida = service.buscarDividaPorAmigo(NOME_JOAO);
		
		Assert.assertEquals(Double.valueOf(7.90), divida.get(0).getValorDevido(), 0.1);
	}
	
	private PlanilhaDividas montarPlanilha() {
		List<Pessoa> pessoas = new ArrayList<>();
		pessoas.add(montarPessoa("João", "5.0", "2.90"));
		pessoas.add(montarPessoa("Amanda", "10.85", "5.10"));
		
		return PlanilhaDividas.builder().dividas(pessoas).build();
	}
	
	private Pessoa montarPessoa(String nome, String valor1, String valor2){
		List<Emprestimo> emprestimos = new ArrayList<>();	
		
		emprestimos.add(new Emprestimo(valor1));
		emprestimos.add(new Emprestimo(valor2));
		
		return new Pessoa(nome, emprestimos);
	}
	
}

package br.com.dividas.controle.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.dividas.controle.dto.Pessoa;
import br.com.dividas.controle.dto.PlanilhaDividas;
import br.com.dividas.controle.exception.BusinessException;
import br.com.dividas.controle.response.DividaResponse;
import br.com.dividas.controle.utils.CalculadoraUtil;

@Service
public class DividasService {

	@Autowired
	private PlanilhaDividasService planilhaDividasService;

	private static final String ERRO_CAMPO_INVALIDO = "O nome recebido está nulo ou vazio.";
	private static final String ERRO_BUSCAR_PLANILHA = "Ocorreu um erro ao obter planilha. Tente novamente mais tarde.";
	private static final String ERRO_PLANILHA_VAZIA = "Não existem dividas na planilha";
	private static final String ERRO_SEM_DIVIDAS = "Não há dividas com o nome informado.";

	public List<DividaResponse> buscarDividaPorAmigo(String nome) {
		verificarCampoInvalido(nome);
		
		PlanilhaDividas planilha = buscarPlanilha();
		List<Pessoa> amigos = buscarAmigo(planilha.getDividas(), nome);

		return calcularDivida(amigos);
	}

	private List<DividaResponse> calcularDivida(List<Pessoa> amigos) {
		List<DividaResponse> dividas = new ArrayList<>();

		for (Pessoa amigo : amigos) {
			dividas.add(new DividaResponse(amigo.getNome(), CalculadoraUtil.somarElementosLista(amigo.getEmprestimo()
					.stream().map(emprestimo -> emprestimo.getValor()).collect(Collectors.toList()))));
		}

		return dividas;
	}

	public List<Pessoa> buscarAmigo(List<Pessoa> amigos, String nome) {
		List<Pessoa> resultado;

		resultado = amigos.stream().filter(amigo -> amigo.getNome().contains(nome)).collect(Collectors.toList());

		if (resultado.isEmpty())
			throw new BusinessException(ERRO_SEM_DIVIDAS);

		return resultado;
	}

	private void verificarCampoInvalido(String nome) {
		if (StringUtils.isBlank(nome)) {
			throw new BusinessException(ERRO_CAMPO_INVALIDO);
		}
	}

	private PlanilhaDividas buscarPlanilha() {
		PlanilhaDividas planilha = new PlanilhaDividas();

		try {
			planilha = planilhaDividasService.buscar();
		} catch (GeneralSecurityException | IOException e) {
			throw new BusinessException(ERRO_BUSCAR_PLANILHA);
		}

		return verificarPlanilha(planilha);
	}

	private PlanilhaDividas verificarPlanilha(PlanilhaDividas planilha) {

		if (planilha.getDividas() == null)
			throw new BusinessException(ERRO_PLANILHA_VAZIA);

		return planilha;
	}

}

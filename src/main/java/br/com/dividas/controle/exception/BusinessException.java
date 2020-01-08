package br.com.dividas.controle.exception;

public class BusinessException extends RuntimeException{
	
	private static final long serialVersionUID = 2948382789043683999L;

	public BusinessException(String mensagem) {
		super(mensagem);
	}

}

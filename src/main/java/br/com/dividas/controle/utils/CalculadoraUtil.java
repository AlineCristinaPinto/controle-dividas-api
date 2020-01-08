package br.com.dividas.controle.utils;

import java.util.List;
import java.util.stream.Collectors;

public class CalculadoraUtil {
	
	public static Double somarElementosLista(List<Double> lista) {
		return lista.stream().collect(Collectors.summingDouble(d -> d));
	}

}

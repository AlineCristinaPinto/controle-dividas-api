package br.com.dividas.controle.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import br.com.dividas.controle.utils.GoogleSheetsUtil;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GoogleSheetsService {

	public List<List<Object>> buscar(String spreadsheetId, String range) throws GeneralSecurityException, IOException {
		Sheets sheets  = GoogleSheetsUtil.getSheetsService();
		ValueRange resultado = sheets.spreadsheets().values().get(spreadsheetId, range).execute();

		return resultado.getValues();
	}
}

package com.santana.sc.sptrans;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Crawler {

	

	public String getToken() throws Exception {
		
		String cookie = "";

		String url = "http://api.olhovivo.sptrans.com.br/v0/Login/Autenticar?token=00bdda67079e3d5538df13c3f72531a5888c04649e4bcf39209a8b79f397b31a";
		URL c = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) c.openConnection();

		connection = (HttpURLConnection) c.openConnection();
		connection.setRequestMethod("GET");


		// Send request
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.flush();
		wr.close();

		Map<String, List<String>> headerFields = connection.getHeaderFields();

		Set<String> headerFieldsSet = headerFields.keySet();
		Iterator<String> hearerFieldsIter = headerFieldsSet.iterator();

		while (hearerFieldsIter.hasNext()) {

			String headerFieldKey = hearerFieldsIter.next();

			if ("Set-Cookie".equalsIgnoreCase(headerFieldKey)) {
				List<String> headerFieldValue = headerFields.get(headerFieldKey);
				for (String headerValue : headerFieldValue) {
					String[] fields = headerValue.split(";\"s*");
					cookie = fields[0];					
				}
			}

		}

		return cookie;
	}

	public List<Bus> getBusesByLine(String cookie, String line) throws Exception {
		
		List<Bus> buses = new ArrayList<Bus>();			
		StringBuffer buffer = new StringBuffer();
		buffer.append("http://api.olhovivo.sptrans.com.br/v0/Linha/Buscar?termosBusca=");
		buffer.append(line);
		URL url = new URL(buffer.toString());

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("Cookie", cookie);

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		String total = "";

		while ((inputLine = in.readLine()) != null) {
			total = total + inputLine;
		}
		System.out.println(total);
			
		JSONParser parser = new JSONParser();

		JSONArray linhas = (JSONArray) parser.parse(total);

		Iterator<JSONObject> linhasIterator = linhas.iterator();
		while (linhasIterator.hasNext()) {
			JSONObject linha = (JSONObject) linhasIterator.next();

			Long codigo = (Long) linha.get("CodigoLinha");
			String letreiro = (String) linha.get("Letreiro");
			String denominacaoTPTS = (String) linha.get("DenominacaoTPTS");
			String denominacaoTSTP = (String) linha.get("DenominacaoTSTP");
			Long sentido = (Long) linha.get("Sentido");

			url = new URL("http://api.olhovivo.sptrans.com.br/v0/Posicao?codigoLinha=" + codigo);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Cookie", cookie);
			connection.toString();

			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			inputLine = "";
			total = "";

			while ((inputLine = in.readLine()) != null) {
				total = total + inputLine;
			}

			JSONObject units2 = (JSONObject) parser.parse(total);

			String hr = (String) units2.get("hr");
			JSONArray onibusLista = (JSONArray) units2.get("vs");

			Iterator onibusIterator = onibusLista.iterator();
			while (onibusIterator.hasNext()) {
				JSONObject onibus = (JSONObject) it.next();
				Double py = (Double) onibus.get("py");
				Double px = (Double) onibus.get("px");

				String denomicao = sentido == 1 ? denominacaoTPTS : denominacaoTSTP;
				
				Bus bus = new Bus();
				bus.setCode((Integer.parseInt((String) onibus.get("p"))));
				bus.setLat(py);
				bus.setLon(px);
				bus.setName(denomicao);
				bus.setLetters(letreiro);
					
				buses.add(bus);

				System.out.println("\"Codigo: " + bus.getCode()
						+ "\" Letreiro: \"" + letreiro + "\" Denominacao: "
						+ denomicao + " Hora: " + hr + " lat: " + py
						+ "long: " + px);
			}
		}

		in.close();
		return buses;
	}

}

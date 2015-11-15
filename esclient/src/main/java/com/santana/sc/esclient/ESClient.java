package com.santana.sc.esclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.support.format.ValueFormatter.GeoHash;

import com.santana.sc.sptrans.Bus;

public class ESClient {

	public void putDataBus(int busCode, int lineCode, String name,
			String letters, String lat, String lon, Date date) {

		Client client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(
						"localhost", 9300));

		double vetor[] = new double[2];
		vetor[0] = Double.parseDouble(lon);
		vetor[1] = Double.parseDouble(lat);
		// GeoPoint point = new GeoPoint(Double.parseDouble(lat),
		// Double.parseDouble(lon));
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("busCode", "" + busCode);
		data.put("lineCode", "" + lineCode);
		data.put("location", vetor);
		// data.put("lon", lon);
		data.put("name", name);
		data.put("letters", letters);
		data.put("dateBus", date);

		IndexResponse response = client.prepareIndex("sptrans", "onibus")
				.setSource(data).execute().actionGet();

		client.close();
	}

	public List<Bus> getBus(String letters) {
		Client client = new TransportClient()
				.addTransportAddress(new InetSocketTransportAddress(
						"localhost", 9300));

		SearchResponse response = client.prepareSearch("sptrans")
				.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.setQuery(QueryBuilders.termQuery("letters", letters))
				.setFrom(0).setSize(60).setExplain(true).execute().actionGet();

		SearchHit[] results = response.getHits().getHits();
		List<Bus> buses = new ArrayList<Bus>();
		for (SearchHit hit : results) {
			Bus bus = new Bus();
			Map<String, Object> result = hit.getSource();
			bus.setLetters((String) result.get("letters"));
			ArrayList lista = (ArrayList) result.get("location");
			bus.setLat((Double) lista.get(0));
			bus.setLon((Double) lista.get(1));
			buses.add(bus);
		}

		client.close();
		return buses;
	}

}

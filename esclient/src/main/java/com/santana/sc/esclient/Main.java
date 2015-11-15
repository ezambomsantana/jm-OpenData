package com.santana.sc.esclient;

import java.util.Date;
import java.util.List;

import com.santana.sc.sptrans.Bus;
import com.santana.sc.sptrans.Crawler;

public class Main {

	public static void main(String[] args) throws Exception {

		while (true) {

			Crawler c = new Crawler();
			String cookie = c.getToken();
			List<Bus> buses = c.getBusesByLine(cookie, "715M-10");

			ESClient client = new ESClient();
			for (Bus b : buses) {
				client.putDataBus(b.getCode(), 0, b.getName(), b.getLetters(),
						"" + b.getLat(), "" + b.getLon(), new Date());
			}

			Thread.sleep(30000);

		}

	}

}

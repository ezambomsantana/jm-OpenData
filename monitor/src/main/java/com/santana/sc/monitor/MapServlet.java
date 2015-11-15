package com.santana.sc.monitor;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.santana.sc.esclient.ESClient;
import com.santana.sc.sptrans.Bus;

public class MapServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ESClient client = new ESClient();
		List<Bus> lista = client.getBus("715M");
		request.setAttribute("lista", lista);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/listaBus.jsp");
        rd.forward(request, response);
		
	}

}

package org.yukung;

import java.io.PrintWriter;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yukung.service.HelloEjbLocal;
import org.yukung.service.HelloEjbRemote;

@WebServlet(urlPatterns={"/servlet/SampleServlet"})
public class SampleServlet extends HttpServlet {

	@EJB
	private HelloEjbRemote helloRemote;

	@EJB
	private HelloEjbLocal helloLocal;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter pw = response.getWriter();
		pw.println("This is Servlet3.0");
		pw.println(helloLocal.greetLocal("EJB"));
		pw.println(helloRemote.greetRemote("EJB"));
	}
}

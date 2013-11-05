package org.yukung;

import java.io.PrintWriter;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yukung.service.HelloEjb;

@WebServlet(urlPatterns={"/servlet/SampleServlet"})
public class SampleServlet extends HttpServlet {

	@EJB
	private HelloEjb hello;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter pw = response.getWriter();
		pw.println("This is Servlet3.0");
		pw.println(hello.greet("EJB"));
	}
}

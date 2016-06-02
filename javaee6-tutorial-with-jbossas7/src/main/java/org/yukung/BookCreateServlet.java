package org.yukung;

import java.io.PrintWriter;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yukung.domain.Book;
import org.yukung.service.BookService;

@WebServlet(urlPatterns={"/book/create"})
public class BookCreateServlet extends HttpServlet {
	@EJB
	private BookService bookService;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Book book = new Book();
		book.setTitle("Learning JavaEE6.");
		book.setDescription("3rd Edition");
		book = bookService.create(book);

		PrintWriter pw = response.getWriter();
		pw.println(book.toString());
	}
}

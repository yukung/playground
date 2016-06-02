package org.yukung;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yukung.domain.Book;
import org.yukung.service.BookService;

@WebServlet(urlPatterns={"/book/find"})
public class BookFindServlet extends HttpServlet {
	@EJB
	private BookService bookService;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter pw = response.getWriter();
		pw.println("<h1>Book List</h1><br>");
		List<Book> books = bookService.findAll();
		for (Book b : books) {
			pw.println(b.toString() + "<br>");
		}
	}
}

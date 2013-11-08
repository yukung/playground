package org.yukung.resource;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.yukung.domain.Book;
import org.yukung.service.BookService;

@Path("/books")
public class BookResource {

	@EJB
	private BookService bookService;

	@GET
	@Produces("application/json")
	public List<Book> listBooks() {
		return bookService.findAll();
	}

	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Book registerBook(Book book) {
		return bookService.create(book);
	}
}

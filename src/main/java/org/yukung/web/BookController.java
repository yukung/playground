package org.yukung.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;

import org.yukung.domain.Book;
import org.yukung.service.BookService;

@Model
public class BookController {
	@EJB
	private BookService bookService;
	private Book book = new Book();
	private List<Book> books = new ArrayList<Book>();

	@PostConstruct
	public void doPostConstruct() {
		books = bookService.findAll();
	}
	public String listBooksAction() {
		books = bookService.findAll();
		return "listBooks.xhtml";
	}
	public String registerBookAction() {
		book = bookService.create(book);
		return "registeredBook.xhtml";
	}
	public Book getBook() { return book; }
	public void setBook(Book book) { this.book = book; }
	public List<Book> getBooks() { return books; }
	public void setBooks(List<Book> books) { this.books = books; }
}
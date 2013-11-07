package org.yukung.service;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.yukung.domain.Book;
import org.yukung.repository.BookRepository;

@Stateless
@Local(BookService.class)
public class BookServiceImpl implements BookService {
	@Inject
	private BookRepository bookRepository;

	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}
	@Override
	public Book create(Book book) {
		bookRepository.persist(book);
		return book;
	}
}

package org.yukung.service;

import java.util.List;

import org.yukung.domain.Book;

public interface BookService {
	List<Book> findAll();
	Book create(Book book);
}

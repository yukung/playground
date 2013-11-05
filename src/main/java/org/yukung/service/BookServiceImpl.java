package org.yukung.service;

import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.yukung.domain.Book;

@Stateless
@Local(BookService.class)
public class BookServiceImpl implements BookService {
	@PersistenceContext(unitName = "javaee6PU")
	private EntityManager em;

	@Override
	public List<Book> findAll() {
		TypedQuery<Book> query = em.createQuery("SELECT b from Book b", Book.class);
		return query.getResultList();
	}
	@Override
	public Book create(Book book) {
		em.persist(book);
		return book;
	}
}

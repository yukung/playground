package org.yukung.repository;

import java.util.List;

import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.yukung.domain.Book;

@Named
public class BookRepository extends JpaRepository<Book, Long> {
	public List<Book> findAll() {
		return createQuery("SELECT b FROM Book b").getResultList();
	}
	public List<Book> findByLikeTitle(String title) {
		TypedQuery<Book> query = createQuery("SELECT b FROM Book b WHERE b.title LIKE %:title%");
		query.setParameter("title", title);
		return query.getResultList();
	}
}
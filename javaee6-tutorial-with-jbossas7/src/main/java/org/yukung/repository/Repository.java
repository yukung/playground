package org.yukung.repository;

import java.io.Serializable;

public interface Repository<T, PK extends Serializable> {
	T findById(PK id);
	T persist(T entity);
	void remove(T entity);
	void remove(PK id);
}
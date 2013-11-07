package org.yukung.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public abstract class JpaRepository<T, PK extends Serializable> implements Repository<T, PK> {
	@PersistenceContext(unitName = "javaee6PU")
	EntityManager entityManager;

	private final Class<T> type = initType();

	private Class<T> initType() {
		ParameterizedType pt = (ParameterizedType)getClass().getGenericSuperclass();
		Type t = pt.getActualTypeArguments()[0];
		@SuppressWarnings("unchecked")
		Class<T> classOfT = (Class<T>) t;
		return classOfT;
	}
	private Class<T> getType() {
		return type;
	}
	protected TypedQuery<T> createQuery(String jpql) {
		return entityManager.createQuery(jpql, getType());
	}
	@Override
	public T findById(PK id) {
		return entityManager.find(getType(), id);
	}
	@Override
	public T persist(T entity) {
		entityManager.persist(entity);
		return entity;
	}
	@Override
	public void remove(T entity) {
		entityManager.remove(entity);
	}
	@Override
	public void remove(PK id) {
		T entity = findById(id);
		remove(entity);
	}
}
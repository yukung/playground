/*
 * Copyright (C) 2014 Yusuke Ikeda <yukung.i@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package intec.sli.iwstudy.teamcalendar.infrastructure.mybatis;

import intec.sli.iwstudy.teamcalendar.domain.repository.Repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * {@link Repository} の MyBatis による実装です。
 * 
 * <p>エンティティの永続化機構として Relational Database を利用するため、
 * {@link Repository} で提供される操作に加えて、RDB固有の操作を提供します。
 * 
 * @author yukung
 * @param <K> Key の型
 * @param <E> Entity の型
 *
 */
public abstract class RepositoryMyBatisImpl<K, E> implements Repository<K, E> {
	
	private final Class<E> entityClass = initializeEntityClass();
	
	@SuppressWarnings("unchecked")
	private Class<E> initializeEntityClass() {
		ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
		Type t = pt.getActualTypeArguments()[0];
		return (Class<E>) t;
	}
	
	@SuppressWarnings("unused")
	private Class<E> getEntityClass() {
		return entityClass;
	}
	
}

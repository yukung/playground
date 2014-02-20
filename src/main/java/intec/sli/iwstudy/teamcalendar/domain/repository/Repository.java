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
package intec.sli.iwstudy.teamcalendar.domain.repository;

/**
 * Entity の永続化機構を抽象化する Repository の基底インタフェースです。
 * Entity に対する汎用的な CRUD 操作を提供します。
 * 
 * <p>永続化機構が Relational Database または Key-Value Store かを問わずにデータを永続化します。
 * 
 * <p>Entity 固有の Repository を作成する場合は、このインターフェースを継承したサブインターフェースを作成してください。
 * 
 * @author yukung
 * 
 * @param <K> Key の型
 * @param <E> Entity の型
 *
 */
public interface Repository<K, E> {
	
	/**
	 * 指定した Key でデータを取得します。データは指定した Entity の型に変換して取得します。
	 * 
	 * @param entityClass Entity の型
	 * @param key Key
	 * @return Entity 
	 */
	E find(Class<E> entityClass, K key);
	
	/**
	 * Entity を永続化します。
	 * 
	 * <p>Entity に Key が指定されていない場合、 Entity は新規追加され、
	 * Repository から払い出された Key が Entity に設定されて返却されます。
	 * 
	 * <p>Entity に Key が指定されている場合、 Entity は更新され、
	 * 更新後の Entity の状態で返却されます。
	 * 
	 * <p>指定した Key が存在しなかった場合は、{@code null} が返却されます。
	 * 
	 * @param entity Entity オブジェクト 
	 * @return 永続化後の Entity オブジェクト
	 */
	E persist(E entity);
	
	/**
	 * Entity を削除します。
	 * 
	 * @param entity Entity オブジェクト
	 */
	void remove(E entity);
	
}

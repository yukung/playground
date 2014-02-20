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
/**
 * 業務の概念を表すドメインのモデルが収められたパッケージです。エンティティとも呼ばれます。
 * 
 * <p>エンティティとは業務上で扱うデータそのものを表すもので、Relational-Database を用いる場合は
 * 1レコードのデータに相当します。
 * 
 * <p>ドメイン中の処理でエンティティ同士を比較することや、文字列表現することも考慮し、
 * <ul>
 * <li>{@code java.lang.Object#hashCode()}
 * <li>{@code java.lang.Object#equals(Object)}
 * <li>{@code java.lang.Object#toString()}
 * </ul>
 * は極力実装すること。
 */
package intec.sli.iwstudy.teamcalendar.domain.model;
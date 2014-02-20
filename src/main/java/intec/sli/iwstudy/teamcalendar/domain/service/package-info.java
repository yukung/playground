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
 * 業務処理を表すサービスが収められたパッケージです。
 * 
 * <p>1つのユースケースに付き1つのサービスクラスが対応します。
 * 
 * <p>また、このパッケージ内に存在するサービスのインターフェースで定義されたメソッドは、
 * メソッド内がトランザクション境界となります。
 */
package intec.sli.iwstudy.teamcalendar.domain.service;
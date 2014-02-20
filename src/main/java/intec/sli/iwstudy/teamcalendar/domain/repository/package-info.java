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
 * オブジェクトの永続化及び、永続化されたオブジェクトを検索する手段を提供する
 * Repository インターフェースが収められたパッケージです。
 * 
 * <p>Repository は、オブジェクト保管先のストレージの種類を問わず、統一的なインターフェースで
 * オブジェクトを永続化、検索する API を提供します。
 * 
 * <p>Repository は Domain 層の Service クラス群から利用され、Service クラス群から
 * Infrastructure 層を隠蔽することで、オブジェクト保管先のストレージが変更されても
 * 影響が出ないようにする役割を持ちます。
 */
package intec.sli.iwstudy.teamcalendar.domain.repository;
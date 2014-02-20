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
package intec.sli.iwstudy.teamcalendar.domain.service.event;

import intec.sli.iwstudy.teamcalendar.domain.model.Event;

import java.util.List;

/**
 * Event にまつわる業務処理を司るサービスインターフェースです。
 * 
 * @author yukung
 *
 */
public interface EventService {
	
	/**
	 * 全ての Event を取得します。
	 * 
	 * @return 全ての Event のリスト
	 */
	List<Event> list();
	
	/**
	 * Event を新規作成します。
	 * 
	 * @param event Event オブジェクト
	 * @return 作成された Event の ID
	 */
	int create(Event event);
	
	/**
	 * Event の内容を更新します。
	 * 
	 * @param event Event オブジェクト
	 * @return 更新が成功した場合は true, 失敗した場合は false
	 */
	boolean update(Event event);
	
	/**
	 * Event を削除します。
	 * @param event Event オブジェクト
	 */
	void delete(Event event);
	
}
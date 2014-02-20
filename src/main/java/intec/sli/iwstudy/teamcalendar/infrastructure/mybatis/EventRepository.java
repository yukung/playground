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

import intec.sli.iwstudy.teamcalendar.domain.model.Event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Event エンティティへの操作を行う Repository の MyBatis 実装です。
 * 
 * @author yukung
 *
 */
@Repository
public class EventRepository extends RepositoryMyBatisImpl<Integer, Event> {
	
	@Autowired
	private EventMapper eventMapper;
	
	/**
	 * 全ての Event エンティティを取得します。
	 * 
	 * @return Event Entity の List
	 */
	public List<Event> findAll() {
		return eventMapper.findAll();
	}
	
	@Override
	public Event find(Class<Event> entityClass, Integer id) {
		return eventMapper.findById(id);
	}
	
	/**
	 * 全ての Event エンティティの数を取得します。
	 * 
	 * @return 全ての Event Entity の数
	 */
	public int countAll() {
		return eventMapper.countAll();
	}
	
	@Override
	public Event persist(Event event) {
		if (event == null) {
			throw new IllegalArgumentException();
		} else if (event.getText() == null && event.getFrom() == null && event.getTo() == null) {
			throw new IllegalArgumentException();
		}
		int count = 0;
		if (event.getId() == null) {
			count = eventMapper.insert(event);
		} else {
			count = eventMapper.update(event);
		}
		if (count == 0) {
			return null;
		}
		return event;
	}
	
	@Override
	public void remove(Event event) {
		if (event == null || event.getId() == null) {
			throw new IllegalArgumentException();
		}
		eventMapper.delete(event);
	}
	
}

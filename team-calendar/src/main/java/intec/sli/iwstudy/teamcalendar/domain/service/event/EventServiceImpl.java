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
import intec.sli.iwstudy.teamcalendar.infrastructure.mybatis.EventRepository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Event サービスの実装クラス。
 * 
 * @author yukung
 *
 */
@Service
public class EventServiceImpl implements EventService {
	
	private static final Logger LOG = LoggerFactory.getLogger(EventServiceImpl.class);
	
	@Autowired
	private EventRepository eventRepository;
	
	@Override
	public List<Event> list() {
		return eventRepository.findAll();
	}
	
	@Transactional
	@Override
	public int create(Event event) {
		if (event == null || event.getFrom() == null || event.getTo() == null || event.getText() == null) {
			throw new IllegalArgumentException();
		}
		eventRepository.persist(event);
		LOG.info("Create event. ID is {}.", event.getId());
		return event.getId();
	}
	
	@Transactional
	@Override
	public boolean update(Event event) {
		if (event == null || event.getId() == null) {
			throw new IllegalArgumentException();
		}
		if (event.getFrom() == null && event.getTo() == null && event.getText() == null) {
			throw new IllegalArgumentException();
		}
		Event persisted = eventRepository.persist(event);
		LOG.info("Update event. Parameter value is {}.", event);
		return persisted != null ? true : false;
	}
	
	@Transactional
	@Override
	public void delete(Event event) {
		if (event == null || event.getId() == null) {
			throw new IllegalArgumentException();
		}
		eventRepository.remove(event);
		LOG.info("Delete event. ID is {}.", event.getId());
	}
	
}
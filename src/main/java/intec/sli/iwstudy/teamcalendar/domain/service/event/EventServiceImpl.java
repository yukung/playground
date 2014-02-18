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
import intec.sli.iwstudy.teamcalendar.domain.repository.event.EventMapper;
import intec.sli.iwstudy.teamcalendar.domain.service.DataNotFoundException;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl implements EventService {
	
	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
	
	@Autowired
	private EventMapper eventMapper;
	
	@Override
	public List<Event> list() {
		List<Event> events = eventMapper.findAll();
		if (events == null) {
			throw new DataNotFoundException();
		}
		return events;
	}
	
	@Transactional
	@Override
	public int create(Event event) {
		if (event == null || event.getFrom() == null || event.getTo() == null || event.getText() == null) {
			throw new IllegalArgumentException();
		}
		eventMapper.insert(event);
		logger.info("Create event. ID is {}.", event.getId());
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
		int count = eventMapper.update(event);
		logger.info("Update event. Parameter value is {}.", event);
		return count == 1;
	}
	
	@Transactional
	@Override
	public boolean delete(Event event) {
		if (event == null || event.getId() == null) {
			throw new IllegalArgumentException();
		}
		int count = eventMapper.delete(event);
		logger.info("Delete event. ID is {}.", event.getId());
		return count == 1;
	}
	
}
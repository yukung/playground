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
package intec.sli.iwstudy.teamcalendar.app.web.service;

import intec.sli.iwstudy.teamcalendar.domain.model.Event;
import intec.sli.iwstudy.teamcalendar.domain.service.event.EventService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.dozer.Mapper;

import com.dhtmlx.planner.DHXEv;
import com.dhtmlx.planner.DHXEvent;
import com.dhtmlx.planner.DHXEventsManager;
import com.dhtmlx.planner.DHXStatus;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * JavaPlanner ライブラリを用いたイベントの操作を行うクラスです。
 * 
 * @author yukung
 *
 */
public class CustomEventsManager extends DHXEventsManager {
	
	private EventService eventService;
	
	private Mapper mapper;
	
	/**
	 * HTTPリクエスト情報を元に EventsManager を初期化するコンストラクタ。
	 * 
	 * @param request HTTPリクエスト
	 */
	public CustomEventsManager(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public Iterable<DHXEv> getEvents() {
		List<Event> events = eventService.list();
		List<DHXEv> dhxEvents = Lists.transform(events, new Function<Event, DHXEv>() {
			@Override
			public DHXEv apply(Event input) {
				return new DHXEvent(input.getId(), input.getFrom(), input.getTo(), input.getText());
			}
		});
		return dhxEvents;
	}
	
	@Override
	public DHXStatus saveEvent(DHXEv dhxEvent, DHXStatus status) {
		Event event = mapper.map(dhxEvent, Event.class);
		switch (status) {
			case UPDATE:
				eventService.update(event);
				break;
			case DELETE:
				eventService.delete(event);
				break;
			case INSERT:
				eventService.create(event);
				break;
			default:
				throw new UnsupportedOperationException("Invalid Status: ".concat(status.toString()));
		}
		return status;
	}
	
	@Override
	public DHXEv createEvent(String id, DHXStatus status) {
		return new DHXEvent();
	}
	
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
	
	public void setBeanMapper(Mapper mapper) {
		this.mapper = mapper;
	}
	
}

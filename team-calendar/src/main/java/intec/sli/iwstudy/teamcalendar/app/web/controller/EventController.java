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
package intec.sli.iwstudy.teamcalendar.app.web.controller;

import intec.sli.iwstudy.teamcalendar.app.web.service.CustomEventsManager;
import intec.sli.iwstudy.teamcalendar.domain.service.event.EventService;

import javax.servlet.http.HttpServletRequest;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.dhtmlx.planner.DHXPlanner;
import com.dhtmlx.planner.DHXSkin;
import com.dhtmlx.planner.data.DHXDataFormat;

/**
 * Event の情報を表示する Web UI のコントローラクラスです。
 * 
 * @author yukung
 *
 */
@Controller
@RequestMapping
public class EventController {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private Mapper mapper;
	
	@RequestMapping("/render")
	public ModelAndView render(HttpServletRequest request) throws Exception {
		DHXPlanner planner = new DHXPlanner("./assets/dhtmlxscheduler/", DHXSkin.TERRACE);
		planner.setInitialDate(2013, 1, 7);
		planner.config.setScrollHour(8);
		planner.setWidth(900);
		planner.load("list", DHXDataFormat.JSON);
		planner.data.dataprocessor.setURL("list");
		
		ModelAndView mav = new ModelAndView("article");
		mav.addObject("body", planner.render());
		return mav;
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public String list(HttpServletRequest request) {
		CustomEventsManager eventsManager = new CustomEventsManager(request);
		eventsManager.setEventService(eventService);
		eventsManager.setBeanMapper(mapper);
		return eventsManager.run();
	}
}

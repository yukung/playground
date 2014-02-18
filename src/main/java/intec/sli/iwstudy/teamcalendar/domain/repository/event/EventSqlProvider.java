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
package intec.sli.iwstudy.teamcalendar.domain.repository.event;

import intec.sli.iwstudy.teamcalendar.domain.model.Event;

import org.apache.ibatis.jdbc.SQL;

public class EventSqlProvider {
	
	public String updateEvent(final Event event) {
		return new SQL() {
			
			{
				UPDATE("events");
				if (event.getFrom() != null) {
					SET("start_date = #{from}");
				}
				if (event.getTo() != null) {
					SET("end_date = #{to}");
				}
				if (event.getText() != null) {
					SET("event_name = #{text}");
				}
				WHERE("event_id = #{id}");
			}
		}.toString();
	}
}
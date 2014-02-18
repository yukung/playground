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

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

public interface EventMapper {
	
	@Select("SELECT * FROM events")
	@Results({
		@Result(column = "event_id", property = "id"),
		@Result(column = "start_date", property = "from"),
		@Result(column = "end_date", property = "to"),
		@Result(column = "event_name", property = "text")
	})
	List<Event> findAll();
	
	@Select("SELECT * FROM events WHERE event_id = #{id}")
	@Results({
		@Result(column = "event_id", property = "id"),
		@Result(column = "start_date", property = "from"),
		@Result(column = "end_date", property = "to"),
		@Result(column = "event_name", property = "text")
	})
	Event findById(@Param("id") Integer id);
	
	@Insert("INSERT INTO events (start_date, end_date, event_name) VALUES (#{from}, #{to}, #{text})")
//	@SelectKey(statement = "CALL IDENTITY()", keyProperty = "id", before = false, resultType = int.class)
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(Event event);
	
//	@Update("UPDATE events SET start_date = #{from}, end_date = #{to}, event_name = #{text} WHERE event_id = #{id}")
	@UpdateProvider(type = EventSqlProvider.class, method = "updateEvent")
	int update(Event event);
	
	@Delete("DELETE FROM events WHERE event_id = #{id}")
	int delete(Event event);
}
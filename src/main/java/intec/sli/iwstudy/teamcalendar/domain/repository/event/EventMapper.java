package intec.sli.iwstudy.teamcalendar.domain.repository.event;

import intec.sli.iwstudy.teamcalendar.domain.model.Event;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

public interface EventMapper {
	
	@Select("SELECT * FROM events WHERE event_id = #{eventId}")
	@Results({
		@Result(column = "event_id", property = "id"),
		@Result(column = "event_name", property = "text")
	})
	Event findById(@Param("eventId") String eventId);
	
	@Select("SELECT * FROM events")
	@Results({
		@Result(column = "event_id", property = "id"),
		@Result(column = "event_name", property = "text")
	})
	List<Event> findAll();
}

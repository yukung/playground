package intec.sli.iwstudy.teamcalendar.domain.repository.event;

import intec.sli.iwstudy.teamcalendar.domain.model.Event;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

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
	
	@Insert("INSERT INTO events (start_date, end_date, event_name) VALUES (#{from}, #{to}, #{text})")
	@SelectKey(statement = "CALL IDENTITY()", keyProperty = "id", before = false, resultType = int.class)
//	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(Event event);
}

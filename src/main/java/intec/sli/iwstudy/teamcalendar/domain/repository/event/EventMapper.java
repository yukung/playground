package intec.sli.iwstudy.teamcalendar.domain.repository.event;

import intec.sli.iwstudy.teamcalendar.domain.model.Event;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

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
	@SelectKey(statement = "CALL IDENTITY()", keyProperty = "id", before = false, resultType = int.class)
//	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insert(Event event);
	
	@Update("UPDATE events SET start_date = #{from}, end_date = #{to}, event_name = #{text} WHERE event_id = #{id}")
	int update(Event event);
	
	@Delete("DELETE FROM events WHERE event_id = #{id}")
	int delete(Event event);
}

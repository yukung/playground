package intec.sli.iwstudy.teamcalendar.domain;

import intec.sli.iwstudy.teamcalendar.domain.model.Event;
import intec.sli.iwstudy.teamcalendar.domain.repository.event.EventMapper;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SampleServiceImpl implements SampleService {
	
	@Autowired
	private EventMapper mapper;
	
	
	@Override
	public void print(String id) {
		Event event = mapper.findById(Integer.valueOf(id));
		if (event != null) {
			System.out.println(event.getId());
			System.out.println(event.getText());
			System.out.println(new DateTime(event.getFrom()));
			System.out.println(new DateTime(event.getTo()));
		}
	}
	
	@Transactional
	@Override
	public int insert() {
		Event event = new Event();
		event.setFrom(new Date());
		event.setTo(new Date(new DateTime().plusDays(7).toDate().getTime()));
		event.setText("なんかの予定");
		int result = mapper.insert(event);
		return event.getId();
	}
	
	@Override
	public void printAll() {
		List<Event> events = mapper.findAll();
		System.out.println(events.size());
		for (Event event : events) {
			System.out.println(event.getId());
			System.out.println(event.getText());
			System.out.println(new DateTime(event.getFrom()));
			System.out.println(new DateTime(event.getTo()));
		}
	}
	
	@Transactional
	@Override
	public int update(String id) {
		Event event = mapper.findById(Integer.valueOf(id));
		event.setText("更新したよ");
		int result = mapper.update(event);
		return result;
	}
	
	@Transactional
	@Override
	public int delete(String id) {
		Event event = mapper.findById(Integer.valueOf(id));
		int result = mapper.delete(event);
		return result;
	}
}

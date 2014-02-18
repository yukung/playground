package intec.sli.iwstudy.teamcalendar.domain;

import intec.sli.iwstudy.teamcalendar.domain.model.Event;
import intec.sli.iwstudy.teamcalendar.domain.repository.event.EventMapper;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleServiceImpl implements SampleService {
	
	@Autowired
	private EventMapper mapper;
	
	
	@Override
	public void print(String id) {
		List<Event> events = mapper.findAll();
		System.out.println(events.size());
		for (Event event : events) {
			System.out.println(event.getId());
			System.out.println(event.getText());
			System.out.println(new DateTime(event.getFrom()));
			System.out.println(new DateTime(event.getTo()));
		}
	}
	
}

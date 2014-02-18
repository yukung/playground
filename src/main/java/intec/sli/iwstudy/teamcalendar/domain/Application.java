package intec.sli.iwstudy.teamcalendar.domain;

import intec.sli.iwstudy.teamcalendar.domain.model.Event;
import intec.sli.iwstudy.teamcalendar.domain.service.event.EventService;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
//		SampleService service = context.getBean(SampleService.class);
		EventService service = context.getBean(EventService.class);
//		service.printAll();
		Event event = new Event();
		event.setFrom(new Date());
		event.setTo(new Date());
		event.setText("hogehoge");
		int generatedId = service.create(event);
		System.out.println("--- 自動採番されたIDでイベントを１つ取得するよ ---");
		System.out.println(String.valueOf(generatedId));
		Event event2 = new Event();
		event2.setId(generatedId);
		event2.setText("更新したよ！！！！！！！！！");
		System.out.println("--- 更新するよ ---");
		service.update(event2);
		System.out.println("消すよん");
		service.delete(event2);
//		service.print(String.valueOf(generatedId));
//		service.delete(String.valueOf(generatedId));
//		service.print(String.valueOf(generatedId));
	}
}

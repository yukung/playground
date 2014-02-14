package intec.sli.iwstudy.teamcalendar.domain;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		SampleService service = context.getBean(SampleService.class);
		service.print("45");
	}
	
}

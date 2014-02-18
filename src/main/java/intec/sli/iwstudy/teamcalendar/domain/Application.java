package intec.sli.iwstudy.teamcalendar.domain;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
		SampleService service = context.getBean(SampleService.class);
		service.printAll();
		int generatedId = service.insert();
		System.out.println("--- 自動採番されたIDでイベントを１つ取得するよ ---");
		service.print(String.valueOf(generatedId));
		System.out.println("--- 更新するよ ---");
		service.update(String.valueOf(generatedId));
		service.print(String.valueOf(generatedId));
		service.delete(String.valueOf(generatedId));
		service.print(String.valueOf(generatedId));
	}
}

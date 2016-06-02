package org.yukung.sample.guice;

import com.google.inject.Inject;

public class InjectSample extends AbstractInjectSample {

	@Inject
	private Service service;

	@Inject
	public InjectSample(Service service) {
		super(service, "dummy");
		System.out.println("コンストラクタ <" + service + ">");
	}

	@Inject
	public void injectedMethod(Service service) {
		System.out.println("メソッド <" + service + ">");
	}

	public void execute() {
		superExecute();
		System.out.println("フィールド <" + service + ">");
	}

	@Inject
	public void setService(Service service) {
		this.service = service;
		System.out.println("セッター <" + service + ">");
	}

}

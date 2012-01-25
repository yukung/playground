package org.yukung.sample.guice;

import com.google.inject.Inject;

public class Client {

	private final Service service;

	@Inject
	public Client(Service service) {
		this.service = service;
	}

	public void service() {
		service.service();
	}
}

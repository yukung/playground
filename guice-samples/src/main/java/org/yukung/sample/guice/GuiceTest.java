package org.yukung.sample.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceTest {

	/**
	 * Guice のテスト
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new Module());
		Client client = injector.getInstance(Client.class);
		client.service();
	}

}

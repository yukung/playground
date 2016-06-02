package org.yukung.sample.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * 単純なProviderの例
 * 
 * @author ikeda_yusuke
 * 
 */
public class SimpleProviderMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(Integer.class).toProvider(SimpleProvider.class);
			}
		});
		for (int i = 0; i < 5; i++) {
			System.out.println(injector.getInstance(Injectee.class).value);
		}
	}

	static class Injectee {
		@Inject
		Integer value;
	}

}

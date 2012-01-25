package org.yukung.sample.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class InjectSampleMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AbstractModule() {

			@Override
			protected void configure() {
				bind(Service.class).to(ServiceImpl.class);
			}
		});
		InjectSample injectSample = injector.getInstance(InjectSample.class);
		// ↑この時点でコンストラクタインジェクションとメソッドインジェクションは行われている
		injectSample.execute(); // ここでフィールドにインジェクションされたオブジェクトを参照
	}

}

package org.yukung.sample.guice;

import com.google.inject.Provider;

class SimpleProvider implements Provider<Integer> {

	static int count = 0;

	public Integer get() {
		return Integer.valueOf(count++);
	}
}
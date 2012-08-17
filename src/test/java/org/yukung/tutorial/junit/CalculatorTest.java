package org.yukung.tutorial.junit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void multiplyで3と4の乗算結果が取得できる() {
		Calculator calc = new Calculator();
		int expected = 12;
		int actual = calc.multiply(3, 4);
		assertThat(actual, is(expected));
	}

	@Test
	public void multiplyで5と7の乗算結果が取得できる() throws Exception {
		Calculator calc = new Calculator();
		int expected = 35;
		int actual = calc.multiply(5, 7);
		assertThat(actual, is(expected));
	}
}

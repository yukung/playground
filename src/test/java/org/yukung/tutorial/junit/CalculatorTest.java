package org.yukung.tutorial.junit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class CalculatorTest {

	@RunWith(Theories.class)
	public static class 乗算メソッドのパラメータ化テスト {

		@Theory
		public void multiplyで3と4の乗算結果が取得できる() {
			Calculator calc = new Calculator();
			int expected = 12;
			int actual = calc.multiply(3, 4);
			assertThat(actual, is(expected));
		}

		@Theory
		public void multiplyで5と7の乗算結果が取得できる() {
			Calculator calc = new Calculator();
			int expected = 35;
			int actual = calc.multiply(5, 7);
			assertThat(actual, is(expected));
		}

	}

	@RunWith(Theories.class)
	public static class 除算メソッドのパラメータ化テスト {

		@Theory
		public void devideで3と2の除算結果が取得できる() {
			Calculator calc = new Calculator();
			float expected = 1.5f;
			float actual = calc.divide(3, 2);
			assertThat(actual, is(expected));
		}

		@Test(expected = IllegalArgumentException.class)
		public void divideの第2引数に0を指定した場合にはIllegalArgumentExceptionを送出する() {
			Calculator calc = new Calculator();
			calc.divide(5, 0);
		}
	}

}

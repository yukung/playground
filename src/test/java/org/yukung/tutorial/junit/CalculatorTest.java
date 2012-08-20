package org.yukung.tutorial.junit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.experimental.runners.Enclosed;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class CalculatorTest {

	@RunWith(Theories.class)
	public static class 乗算メソッドのパラメータ化テスト {
		@DataPoints
		public static Fixture[] DATAS = { new Fixture(3, 4, 12), new Fixture(5, 7, 35), };

		@Theory
		public void multiplyで乗算結果が取得できること(Fixture fx) {
			String msg = fx.x + "*" + fx.y + "=" + fx.expected;
			Calculator calc = new Calculator();
			int expected = fx.expected;
			int actual = calc.multiply(fx.x, fx.y);
			assertThat(msg, actual, is(expected));
		}

		static class Fixture {
			int x, y, expected;

			Fixture(int x, int y, int expected) {
				this.x = x;
				this.y = y;
				this.expected = expected;
			}
		}

	}

	@RunWith(Theories.class)
	public static class 除算メソッドのパラメータ化テスト {
		@DataPoints
		public static Fixture[] DATAS = { new Fixture(3, 2, 1.5f), new Fixture(5, 0, 0.0f), };

		@Theory
		public void devideで除算結果が取得できること(Fixture fx) {
			String msg = fx.x + "/" + fx.y + "=" + fx.expected;
			Calculator calc = new Calculator();
			float expected = fx.expected;
			try {
				float actual = calc.divide(fx.x, fx.y);
				assertThat(msg, actual, is(expected));
			} catch (IllegalArgumentException e) {
				assertThat(msg, e, is(IllegalArgumentException.class));
			}
		}

		static class Fixture {
			int x, y;
			float expected;

			Fixture(int x, int y, float expected) {
				this.x = x;
				this.y = y;
				this.expected = expected;
			}
		}
	}

}

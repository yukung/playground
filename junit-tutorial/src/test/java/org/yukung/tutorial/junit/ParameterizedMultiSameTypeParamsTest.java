package org.yukung.tutorial.junit;

import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/*
 * 組み合わせによるパラメータ化テスト
 */
@RunWith(Theories.class)
public class ParameterizedMultiSameTypeParamsTest {

	@DataPoint
	public static int INT_PARAM_1 = 3;
	@DataPoint
	public static int INT_PARAM_2 = 4;

	@Theory
	public void test(int x, int y) throws Exception {
		System.out.println("test(" + x + ", " + y + ")");
	}

}

package org.yukung.tutorial.junit;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Dateの年月日だけを検証するMatcher.
 * 
 * @author yukung
 * 
 */
public class IsDate extends BaseMatcher<Date> {

	private final int yyyy;
	private final int mm;
	private final int dd;
	Object actual; // describeTo を通じて実測値や報告内容を保持する

	IsDate(int yyyy, int mm, int dd) {
		this.yyyy = yyyy;
		this.mm = mm;
		this.dd = dd;
	}

	@Override
	public boolean matches(Object actual) {
		this.actual = actual;
		if (!(actual instanceof Date))
			return false;
		Calendar cal = Calendar.getInstance();
		cal.setTime((Date) actual);
		if (yyyy != cal.get(Calendar.YEAR))
			return false;
		if (mm != cal.get(Calendar.MONTH) + 1)
			return false;
		if (dd != cal.get(Calendar.DATE))
			return false;
		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue(yyyy + "/" + mm + "/" + dd);
		if (actual != null) {
			description.appendText(" but actual is ");
			description.appendValue(new SimpleDateFormat("yyyy/MM/dd").format((Date) actual));
		}
	}

	public static Matcher<Date> dateOf(int yyyy, int mm, int dd) {
		return new IsDate(yyyy, mm, dd);
	}
}

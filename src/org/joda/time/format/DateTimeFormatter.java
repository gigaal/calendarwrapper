package org.joda.time.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.internal.utils.ISO8601Utils;


public class DateTimeFormatter {

	private final String mPattern;
	
	public DateTimeFormatter(String pattern) {
		mPattern = pattern;
	}

	public DateTimeFormatter() {
		mPattern = "";
	}

	public String getPattern() {
		return mPattern;
	}

	public DateTime parseDateTime(String str) throws IllegalArgumentException {
		try {
			if ("".equals(mPattern)) {
				Calendar c = ISO8601Utils.parse(str);
				return new DateTime(c);
			} else {
				Date d = new SimpleDateFormat(mPattern).parse(str);
				return new DateTime(d);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
}

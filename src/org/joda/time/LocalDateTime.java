package org.joda.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.joda.time.format.DateTimeFormatter;

public class LocalDateTime {

	public static LocalDateTime parse(String rawDate, DateTimeFormatter formatter) throws IllegalArgumentException {
		SimpleDateFormat df = new SimpleDateFormat(formatter.getPattern(), Locale.getDefault());
		try {
			return new DateTime(df.parse(rawDate)).toLocalDateTime();
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	private final int mYear, mMonthOfYear, mDayOfMonth, mHourOfDay, mMinuteOfHour, mSecondOfMinute, mMillisOfSecond;
	
	public LocalDateTime(Calendar calendar) {
		this(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 
				calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND), calendar.get(Calendar.MILLISECOND));
	}
	
	public LocalDateTime(int year,
            int monthOfYear,
            int dayOfMonth, 
            int hourOfDay,
			int minuteOfHour) {
		this(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 0, 0);
	}
	
	public LocalDateTime(int year,
            int monthOfYear,
            int dayOfMonth, 
            int hourOfDay,
			int minuteOfHour,
			int secondOfMinute,
			int millisOfSecond) {
		mYear = year;
		mMonthOfYear = monthOfYear;
		mDayOfMonth = dayOfMonth;
		mHourOfDay = hourOfDay;
		mMinuteOfHour = minuteOfHour;
		mSecondOfMinute = secondOfMinute;
		mMillisOfSecond = millisOfSecond;
	}

	public LocalTime toLocalTime() {
		return new LocalTime(mHourOfDay, mMinuteOfHour, mSecondOfMinute, mMillisOfSecond);
	}

	public DateTime toDateTime(DateTimeZone dateTimeZone) {
		return new DateTime(mYear, mMonthOfYear, mDayOfMonth, mHourOfDay, mMinuteOfHour, mSecondOfMinute, mMillisOfSecond, dateTimeZone);
	}
}

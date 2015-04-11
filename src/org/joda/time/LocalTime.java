package org.joda.time;

import java.util.Calendar;


public final class LocalTime {

	/** Constant for midnight. */
    public static final LocalTime MIDNIGHT = new LocalTime(0, 0, 0, 0);
	
	public static LocalTime now() {
		return new LocalTime();
	}
	
	private final int mHourOfDay, mMinuteOfHour, mSecondOfMinute, mMillisOfSecond;
	
	public LocalTime(LocalTime localTime) {
		this(localTime.getHourOfDay(), localTime.getMinuteOfHour(), localTime.getSecondOfMinute(), localTime.getMillisOfSecond());
	}
	
	public LocalTime(
			int hourOfDay,
			int minuteOfHour,
			int secondOfMinute,
			int millisOfSecond) {
		mHourOfDay = hourOfDay;
		mMinuteOfHour = minuteOfHour;
		mSecondOfMinute = secondOfMinute;
		mMillisOfSecond = millisOfSecond;
	}

	public LocalTime(Calendar calendar) {
		this(calendar.get(Calendar.HOUR_OF_DAY),
				calendar.get(Calendar.MINUTE),
				calendar.get(Calendar.SECOND),
				calendar.get(Calendar.MILLISECOND));
	}
	
	public LocalTime() {
		this(Calendar.getInstance());
	}
	
	public LocalTime(int hourOfDay,
			int minuteOfHour) {
		this(hourOfDay, minuteOfHour, 0, 0);
	}
	
	public LocalTime(int hourOfDay,
			int minuteOfHour, int secondOfMinute) {
		this(hourOfDay, minuteOfHour, secondOfMinute, 0);
	}
	
	public LocalTime(long instant) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(instant);
		
		mHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		mMinuteOfHour = calendar.get(Calendar.MINUTE);
		mSecondOfMinute = calendar.get(Calendar.SECOND);
		mMillisOfSecond = calendar.get(Calendar.MILLISECOND);
	}
	
	public LocalTime(DateTime dateTime) {
		this(dateTime.toCalendar());
	}
	
	public int getHourOfDay() {
		return mHourOfDay;
	}
	
	public int getMinuteOfHour() {
		return mMinuteOfHour;
	}
	
	public int getSecondOfMinute() {
		return mSecondOfMinute;
	}
	
	public int getMillisOfSecond() {
		return mMillisOfSecond;
	}
	
	public boolean isBefore(LocalTime localTime) {
		return toDateTimeToday(DateTimeZone.UTC).isBefore(localTime.toDateTimeToday(DateTimeZone.UTC));
	}
	
	public boolean isAfter(LocalTime localTime) {
		return toDateTimeToday(DateTimeZone.UTC).isAfter(localTime.toDateTimeToday(DateTimeZone.UTC));
	}
	
	public LocalTime plusHours(int hours) {
		if (hours == 0) {
			return this;
		}
		
		Calendar cal = Calendar.getInstance(DateTimeZone.UTC.getTimeZone());
		DateTime dateTime = toDateTime(cal);
		return new LocalTime(dateTime.plusHours(hours));
	}
	
	public LocalTime plusMinutes(int minutes) {
		if (minutes == 0) {
			return this;
		}
		
		Calendar cal = Calendar.getInstance(DateTimeZone.UTC.getTimeZone());
		DateTime dateTime = toDateTime(cal);
		return new LocalTime(dateTime.plusMinutes(minutes));
	}
	
	public DateTime toDateTimeToday() {
        return toDateTime(Calendar.getInstance());
	}
	
	public DateTime toDateTimeToday(DateTimeZone zone) {
        return toDateTime(Calendar.getInstance(zone.getTimeZone()));
	}
	
	public DateTime toDateTime(DateTime dateTime) {
        return toDateTime(dateTime.toCalendar());
	}
	
	public DateTime toDateTime(Instant instant) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(instant.getMillis());
        return toDateTime(c);
	}
	
	public DateTime toDateTime(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, mHourOfDay);
        calendar.set(Calendar.MINUTE, mMinuteOfHour);
        calendar.set(Calendar.SECOND, mSecondOfMinute);
        calendar.set(Calendar.MILLISECOND, mMillisOfSecond);
        
        return new DateTime(calendar);
	}
	
	public boolean isEqual(LocalTime localTime) {
		return equals(localTime);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mHourOfDay;
		result = prime * result + mMillisOfSecond;
		result = prime * result + mMinuteOfHour;
		result = prime * result + mSecondOfMinute;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocalTime other = (LocalTime) obj;
		if (mHourOfDay != other.mHourOfDay)
			return false;
		if (mMillisOfSecond != other.mMillisOfSecond)
			return false;
		if (mMinuteOfHour != other.mMinuteOfHour)
			return false;
		if (mSecondOfMinute != other.mSecondOfMinute)
			return false;
		return true;
	}
	
	
	
}

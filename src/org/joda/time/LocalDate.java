package org.joda.time;

import java.util.Calendar;

public class LocalDate implements Comparable<LocalDate> {

	private final int mYear, mMonthOfYear, mDayOfMonth;
	
	public LocalDate(
            int year,
            int monthOfYear,
            int dayOfMonth) {
		mYear = year;
		mMonthOfYear = monthOfYear;
		mDayOfMonth = dayOfMonth;
	}
	
	public LocalDate(long millis) {
		Calendar c = Calendar.getInstance(DateTimeZone.UTC.getTimeZone());
		c.setTimeInMillis(millis);
		mYear = c.get(Calendar.YEAR);
		mMonthOfYear = c.get(Calendar.MONTH);
		mDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
	}
	
	public LocalDate plusDays(int days) {
		return toDateTimeAtCurrentTime(DateTimeZone.UTC).plusDays(days).toLocalDate();
	}
	
	public DateTime toDateTimeAtCurrentTime(DateTimeZone zone) {
		Calendar calendar = Calendar.getInstance(zone.getTimeZone());
		calendar.set(mYear, mMonthOfYear, mDayOfMonth);
		return new DateTime(calendar);
	}
	
	public DateTime toDateTimeAtStartOfDay() {
		return toDateTimeAtStartOfDay(DateTimeZone.getDefault());
	}
	
	public DateTime toDateTimeAtStartOfDay(DateTimeZone zone) {
		Calendar calendar = Calendar.getInstance();
	    calendar.set(mYear, mMonthOfYear, mDayOfMonth);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return new DateTime(calendar);
	}
	
	public boolean isBefore(LocalDate localDate) {
		return toDateTimeAtCurrentTime(DateTimeZone.UTC).isBefore(localDate.toDateTimeAtCurrentTime(DateTimeZone.UTC));
	}
	
	public boolean isAfter(LocalDate localDate) {
		return toDateTimeAtCurrentTime(DateTimeZone.UTC).isAfter(localDate.toDateTimeAtCurrentTime(DateTimeZone.UTC));
	}
	
	public boolean isEqual(LocalDate localDate) {
		return equals(localDate);
	}

	@Override
	public int compareTo(LocalDate another) {
		return toDateTimeAtCurrentTime(DateTimeZone.UTC).compareTo(another.toDateTimeAtCurrentTime(DateTimeZone.UTC));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + mDayOfMonth;
		result = prime * result + mMonthOfYear;
		result = prime * result + mYear;
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
		LocalDate other = (LocalDate) obj;
		if (mDayOfMonth != other.mDayOfMonth)
			return false;
		if (mMonthOfYear != other.mMonthOfYear)
			return false;
		if (mYear != other.mYear)
			return false;
		return true;
	}
	
	
}

package org.joda.time;

import java.util.Calendar;

import org.joda.time.base.AbstractInstant;
import org.joda.time.internal.utils.ISO8601Utils;


public class Instant extends AbstractInstant {

	
	private long mMillis;
	
	public Instant(long millis) {
		mMillis = millis;
	}
	
	@Override
	public long getMillis() {
		return mMillis;
	}

	@Override
	public DateTimeZone getZone() {
		return DateTimeZone.UTC;
	}

	@Override
	public String toString() {
		Calendar c = Calendar.getInstance(DateTimeZone.UTC.getTimeZone());
		c.setTimeInMillis(mMillis);
		return ISO8601Utils.fromCalendar(c);
	}
}

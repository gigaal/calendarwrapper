package org.joda.time;

public final class Interval {

	private final DateTime mStart, mEnd;
	
	public Interval(DateTime start, DateTime end) {
		mStart = start;
		mEnd = end;
	}
	
	public long getStartMillis() {
		return mStart.getMillis();
	}
	
	public long getEndMillis() {
		return mEnd.getMillis();
	}
	
	public boolean overlaps(Interval interval) {
		long thisStart = getStartMillis();
        long thisEnd = getEndMillis();
        if (interval == null) {
            long now = DateTimeUtils.currentTimeMillis();
            return (thisStart < now && now < thisEnd);
        }  else {
            long otherStart = interval.getStartMillis();
            long otherEnd = interval.getEndMillis();
            return (thisStart < otherEnd && otherStart < thisEnd);
        }
	}
}

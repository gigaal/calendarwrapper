package org.joda.time;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

import org.joda.time.field.FieldUtils;
import org.joda.time.format.FormatUtils;

public final class DateTimeZone {

	/** Maximum offset. */
    private static final int MAX_MILLIS = (86400 * 1000) - 1;
    
	public static final DateTimeZone UTC = forTimeZone(TimeZone.getTimeZone("UTC"));
	
	private final String mId;
	private final TimeZone mTimeZone; 
	
	public static Set<String> getAvailableIDs() {
		return new HashSet<String>(Arrays.asList(TimeZone.getAvailableIDs()));
	}
	
	public static DateTimeZone getDefault() {
		return DateTimeZone.forTimeZone(TimeZone.getDefault());
	}
	
	public static void setDefault(DateTimeZone timezone) {
		if (timezone == null) {
			throw new IllegalArgumentException("Invalid time zone");
		}
		
		if (!TimeZone.getDefault().equals(timezone.getTimeZone())) {
			TimeZone.setDefault(timezone.getTimeZone());
		}
	}
	
	public static DateTimeZone forID(String id) {
		return forTimeZone(TimeZone.getTimeZone(id));
	}
	
	public static DateTimeZone forOffsetHours(int hoursOffset) {
		return forOffsetHoursMinutes(hoursOffset, 0);
	}
	
	public static DateTimeZone forOffsetHoursMinutes(int hoursOffset, int minutesOffset) {
		if (hoursOffset == 0 && minutesOffset == 0) {
            return DateTimeZone.UTC;
        }
        if (hoursOffset < -23 || hoursOffset > 23) {
            throw new IllegalArgumentException("Hours out of range: " + hoursOffset);
        }
        if (minutesOffset < -59 || minutesOffset > 59) {
            throw new IllegalArgumentException("Minutes out of range: " + minutesOffset);
        }
        if (hoursOffset > 0 && minutesOffset < 0) {
            throw new IllegalArgumentException("Positive hours must not have negative minutes: " + minutesOffset);
        }
        if (hoursOffset == 0 && minutesOffset == 0) {
			return forTimeZone(TimeZone.getTimeZone("UTC"));
        } else if ((hoursOffset == 0 && minutesOffset > 0) || hoursOffset > 0) {
        	return forID("GMT+" + formatTime(hoursOffset) + ":" + formatTime(minutesOffset));
        } else if (hoursOffset == 0 && minutesOffset < 0) {
        	return forID("GMT-" + formatTime(hoursOffset) + ":" + formatTime(Math.abs(minutesOffset)));
        } else if (hoursOffset < 0) {
        	return forID("GMT" + hoursOffset + ":" + formatTime(Math.abs(minutesOffset)));
        }
        throw new IllegalArgumentException("Illegal offset values");
	}
	
	private static String formatTime(int value) {
		return ("00" + value).substring(Integer.toString(value).length());
	}
	
	private static synchronized DateTimeZone fixedOffsetZone(String id, int offset) {
        if (offset == 0) {
            return DateTimeZone.UTC;
        }
        DateTimeZone zone = new DateTimeZone(id, offset);
        return zone;
    }
	
	/**
	 * Dummy function just to preserve the mapping with the original 
	 * DateTimeZone class from joda. Here we just return the same timezone
	 * passed as parameter
	 * @param timezone
	 * @return
	 */
	public static DateTimeZone forTimeZone(TimeZone timezone) {
		return new DateTimeZone(timezone);
	}
	
	public DateTimeZone(TimeZone timezone) {
		if (timezone.getID() == null || "".equals(timezone.getID())) {
			throw new IllegalArgumentException("Invalid time zone");
		}
		mTimeZone = timezone;
		mId = timezone.getID();
	}
	
	public DateTimeZone() {
		this(TimeZone.getDefault());
	}
	
	public DateTimeZone(String customId, int offset) {
		mTimeZone = TimeZone.getTimeZone("UTC");
		mTimeZone.setRawOffset(offset);
		mId = customId;
	}
	
	public DateTimeZone(String forID) {
		this(TimeZone.getTimeZone(forID));
	}
	
	public TimeZone getTimeZone() {
		return mTimeZone;
	}
	
	public TimeZone toTimeZone() {
		if (!mId.equals(mTimeZone.getID())) {
			return new java.util.SimpleTimeZone(mTimeZone.getRawOffset(), getID());
		}
		return mTimeZone;
	}
	
	public String getID() {
		return mId;
	}

	 /**
     * Gets the millisecond offset to add to UTC to get local time.
     * 
     * @param instant  milliseconds from 1970-01-01T00:00:00Z to get the offset for
     * @return the millisecond offset to add to UTC to get local time
     */
    public int getOffset(long instant) {
    	return mTimeZone.getOffset(instant);
    }
    
    public String getName(long instant) {
    	return getName(instant, Locale.getDefault());
    }
    
    public String getName(long instant, Locale locale) {
    	return mTimeZone.getDisplayName(mTimeZone.inDaylightTime(new Date(instant)), TimeZone.LONG, locale);
    }
    
    public String getShortName(long instant) {
    	return getShortName(instant, Locale.getDefault());
    }
    
    public String getShortName(long instant, Locale locale) {
    	return mTimeZone.getDisplayName(mTimeZone.inDaylightTime(new Date(instant)), TimeZone.SHORT, locale);
    }
    
    public boolean isLocalDateTimeGap(LocalDateTime localDateTime) {
    	try {
            localDateTime.toDateTime(this);
            return false;
        } catch (IllegalArgumentException ex) {
            return true;
        }
	}
    
    public String toString() {
    	return mTimeZone.toString();
    }
    
    /**
     * Formats a timezone offset string.
     * <p>
     * This method is kept separate from the formatting classes to speed and
     * simplify startup and classloading.
     * 
     * @param offset  the offset in milliseconds
     * @return the time zone string
     */
    private static String printOffset(int offset) {
        StringBuffer buf = new StringBuffer();
        if (offset >= 0) {
            buf.append('+');
        } else {
            buf.append('-');
            offset = -offset;
        }

        int hours = offset / DateTimeConstants.MILLIS_PER_HOUR;
        FormatUtils.appendPaddedInteger(buf, hours, 2);
        offset -= hours * (int) DateTimeConstants.MILLIS_PER_HOUR;

        int minutes = offset / DateTimeConstants.MILLIS_PER_MINUTE;
        buf.append(':');
        FormatUtils.appendPaddedInteger(buf, minutes, 2);
        offset -= minutes * DateTimeConstants.MILLIS_PER_MINUTE;
        if (offset == 0) {
            return buf.toString();
        }

        int seconds = offset / DateTimeConstants.MILLIS_PER_SECOND;
        buf.append(':');
        FormatUtils.appendPaddedInteger(buf, seconds, 2);
        offset -= seconds * DateTimeConstants.MILLIS_PER_SECOND;
        if (offset == 0) {
            return buf.toString();
        }

        buf.append('.');
        FormatUtils.appendPaddedInteger(buf, offset, 3);
        return buf.toString();
    }

	@Override
	public int hashCode() {
		return 31 + getID().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
            return true;
        }
        if (obj instanceof DateTimeZone) {
        	DateTimeZone other = (DateTimeZone) obj;
            return
                getID().equals(other.getID()) &&
                mTimeZone.getRawOffset() == other.getTimeZone().getRawOffset();
        }
        return false;
	}

}

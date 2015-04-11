package org.joda.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.base.AbstractInstant;
import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.internal.utils.ISO8601Utils;


public final class DateTime extends AbstractInstant {

	private volatile Calendar mCalendar;
	
	public static DateTime now() {
		return new DateTime();
	}
	
	public static DateTime now(DateTimeZone zone) {
		return new DateTime(zone);
	}
	
	public static DateTime parse(String str) {
		DateTimeFormatter dtf = new DateTimeFormatter();
		return dtf.parseDateTime(str);
	}
	
	public DateTime() {
		this(Calendar.getInstance());
		mCalendar.setTimeInMillis(DateTimeUtils.currentTimeMillis());
	}
	
	public DateTime(DateTimeZone zone) {
		this(Calendar.getInstance(), zone);
		mCalendar.setTimeInMillis(DateTimeUtils.currentTimeMillis());
	}
	
	public DateTime(String str) {
		try {
			mCalendar = ISO8601Utils.parse(str);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public DateTime(DateTime dateTime) {
		this(dateTime.toCalendar());	
	}
	
	public DateTime(long instant) {
		this(instant, (DateTimeZone)null);
	}
	
	public DateTime(long instant, DateTimeZone zone) {
		mCalendar = Calendar.getInstance();
		mCalendar.setTimeInMillis(instant);
		if (zone != null) {
			mCalendar.setTimeZone(zone.getTimeZone());
		}
	}
	
	public DateTime(Calendar calendar) {
		this(calendar, null);
	}
	
	public DateTime(Calendar calendar, DateTimeZone zone) {
		mCalendar = calendar;
		if (zone != null) {
			mCalendar.setTimeZone(zone.getTimeZone());
		}
	}
	
	public DateTime(Date date) {
		mCalendar = Calendar.getInstance();
		mCalendar.setTime(date);
	}
	
	public DateTime(int year, 
			int monthOfYear, int dayOfMonth,
			int hourOfDay, int minuteOfHour) {
		this(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 0, 0);
	}
	
	public DateTime(int year, 
			int monthOfYear, int dayOfMonth,
			int hourOfDay, int minuteOfHour, 
			int secondOfMinute, int millisOfSecond) {
		this(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond, null);
	}
	
	public DateTime(int year, 
			int monthOfYear, int dayOfMonth,
			int hourOfDay, int minuteOfHour, 
			int secondOfMinute, int millisOfSecond,
			DateTimeZone zone) {
		if (monthOfYear <= 0 || monthOfYear > 12) {
			throw new IllegalArgumentException("Month value must be between 1 and 12");
		} else if (dayOfMonth < 1 || dayOfMonth > 31) {
			throw new IllegalArgumentException("dayOfMonth value must be between 1 and 31");
		}
		if (zone == null) {
			mCalendar = Calendar.getInstance();
		} else {
			mCalendar = Calendar.getInstance(zone.getTimeZone());
		}
		mCalendar.set(Calendar.YEAR, year);
		mCalendar.set(Calendar.MONTH, monthOfYear - 1); //Java months start on 0 for January
		mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		mCalendar.set(Calendar.MINUTE, minuteOfHour);
		mCalendar.set(Calendar.SECOND, secondOfMinute);
		mCalendar.set(Calendar.MILLISECOND, millisOfSecond);
        
		//checks if calendar is valid
		mCalendar.getTime();
		if (!checkCalendar(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond)) {
			throw new IllegalArgumentException("Invalid calendar values");
		}
	}
	
	private boolean checkCalendar(int year, int monthOfYear, int dayOfMonth,
			int hourOfDay, int minuteOfHour, int secondOfMinute,
			int millisOfSecond) {
		return getYear() == year
					&& getMonthOfYear() == monthOfYear
					&& getDayOfMonth() == dayOfMonth
					&& getHourOfDay() == hourOfDay
					&& getMinuteOfHour() == minuteOfHour
					&& getSecondOfMinute() == secondOfMinute
					&& getMillisOfSecond() == millisOfSecond;
				
	}

	public DateTime withTimeAtStartOfDay() {
		return withTime(0, 0, 0, 0);
	}
	
	public DateTime withDate(int year, 
			int monthOfYear, int dayOfMonth) {
		if (monthOfYear < 1 || monthOfYear > 12 || dayOfMonth < 1 || dayOfMonth > 31) {
			throw new IllegalArgumentException("Invalid calendar values");
		}
		
		Calendar calendar = getNewCalendar();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear - 1); //Java months start on 0 for January
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		return new DateTime(calendar);
	}
	
	public DateTime withTime(int hourOfDay,
			int minuteOfHour, int secondOfMinute, int millisOfSecond) {
		Calendar calendar = getNewCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minuteOfHour);
        calendar.set(Calendar.SECOND, secondOfMinute);
        calendar.set(Calendar.MILLISECOND, millisOfSecond);
        
        return new DateTime(calendar);
	}

	public long getMillis() {
		return mCalendar.getTimeInMillis();
	}
	
	private Calendar getNewCalendar() {
		Calendar c = Calendar.getInstance(mCalendar.getTimeZone());
		c.setTimeInMillis(mCalendar.getTimeInMillis());
		return c;
	}
	
	public DateTime plusMillis(int millis) {
		if (millis == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.MILLISECOND, millis);
		return new DateTime(c);
	}
	
	public DateTime plusSeconds(int seconds) {
		if (seconds == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.SECOND, seconds);
		return new DateTime(c);
	}
	
	public DateTime plusMinutes(int minutes) {
		if (minutes == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.MINUTE, minutes);
		return new DateTime(c);
	}
	
	public DateTime plusHours(int hours) {
		if (hours == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.HOUR_OF_DAY, hours);
		return new DateTime(c);
	}
	
	public DateTime plusDays(int days) {
		if (days == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.DAY_OF_MONTH, days);
		return new DateTime(c);
	}
	
	public DateTime plusWeeks(int weeks) {
		if (weeks == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.WEEK_OF_YEAR, weeks);
		return new DateTime(c);
	}
	
	public DateTime plusMonths(int months) {
		if (months == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.MONTH, months);
		return new DateTime(c);
	}
	
	public DateTime plusYears(int years) {
		if (years == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.YEAR, years);
		return new DateTime(c);
	}
	
	public DateTime minusSeconds(int seconds) {
		if (seconds == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.SECOND, -seconds);
		return new DateTime(c);
	}
	
	public DateTime minusMinutes(int minutes) {
		if (minutes == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.MINUTE, -minutes);
		return new DateTime(c);
	}
	
	public DateTime minusHours(int hours) {
		if (hours == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.HOUR_OF_DAY, -hours);
		return new DateTime(c);
	}
	
	public DateTime minusDays(int days) {
		if (days == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.DAY_OF_YEAR, -days);
		return new DateTime(c);
	}
	
	public DateTime minusMonths(int months) {
		if (months == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.MONTH, -months);
		return new DateTime(c);
	}
	
	public DateTime minusYears(int years) {
		if (years == 0) return this;
		
		Calendar c = getNewCalendar();
		c.add(Calendar.YEAR, -years);
		return new DateTime(c);
	}
	
	public DateTime plus(BaseSingleFieldPeriod period) {
		Calendar c = getNewCalendar();
		c.add(period.getFieldType(), period.getValue());
		return new DateTime(c);
	}
	
	public int getMillisOfSecond() {
		return mCalendar.get(Calendar.MILLISECOND);
	}
	
	public int getSecondOfMinute() {
		return mCalendar.get(Calendar.SECOND);
	}
	
	public int getMinuteOfHour() {
		return mCalendar.get(Calendar.MINUTE);
	}
	
	public int getHourOfDay() {
		return mCalendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public int getDayOfMonth() {
		return mCalendar.get(Calendar.DAY_OF_MONTH);
	}
	
	public int getWeekOfWeekyear() {
		return mCalendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	public int getMonthOfYear() {
		//Calendar has a 0-based index for months
		return mCalendar.get(Calendar.MONTH) + 1;
	}
	
	public int getYear() {
		return mCalendar.get(Calendar.YEAR);
	}
	
    public boolean isBefore(long instant) {
        return (getMillis() < instant);
    }
    
    public boolean isBefore(ReadableInstant dateTime) {
    	return isBefore(DateTimeUtils.getInstantMillis(dateTime));
    }
    
    public boolean isBeforeNow() {
    	return isBefore(DateTimeUtils.currentTimeMillis());
    }
    
    public boolean isAfter(long instant) {
        return (getMillis() > instant);
    }
    
    public boolean isAfter(ReadableInstant dateTime) {
    	return isAfter(DateTimeUtils.getInstantMillis(dateTime));
    }
    
    public boolean isAfterNow() {
    	return isAfter(DateTimeUtils.currentTimeMillis());
    }
    
    public LocalTime toLocalTime() {
    	return new LocalTime(mCalendar);
    }
    
    public LocalDate toLocalDate() {
    	return new LocalDate(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
    }
    
    public Calendar toCalendar() {
    	return mCalendar;
    }
    
    public Calendar toCalendar(Locale locale) {
    	if (locale == null) {
            locale = Locale.getDefault();
        }
        Calendar cal = Calendar.getInstance(mCalendar.getTimeZone(), locale);
        cal.setTime(mCalendar.getTime());
        return cal;
    }
    
    public DateTime toDateTime(DateTimeZone zone) {
    	Calendar c = Calendar.getInstance();
    	if (zone != null) {
    		c.setTimeZone(zone.getTimeZone());
    	}
    	c.setTimeInMillis(mCalendar.getTimeInMillis());
    	return new DateTime(c);
    }
    
    public Date toDate() {
    	return mCalendar.getTime();
    }
    
    public LocalDateTime toLocalDateTime() {
		return new LocalDateTime(mCalendar);
	}
    
    public Property minuteOfHour() {
    	return new Property(this, Calendar.MINUTE);
    }
    
    public Property hourOfDay() {
    	return new Property(this, Calendar.HOUR_OF_DAY);
    }
    
    public static final class Property {
    	
    	private DateTime mDateTime;
    	private int mField;
    	
    	public Property(DateTime dateTime, int field) {
    		mDateTime = dateTime;
    		mField = field;
    	}
    	
    	public DateTime roundFloorCopy() {
    		Calendar c = mDateTime.toCalendar();
    		switch (mField) {
    		case Calendar.MILLISECOND:
    			//do nothing
    			break;
    		case Calendar.SECOND:
    			c.set(Calendar.MILLISECOND, 0);
    			break;
    		case Calendar.MINUTE:
    			c.set(Calendar.MILLISECOND, 0);
    			c.set(Calendar.SECOND, 0);
    			break;
    		case Calendar.HOUR_OF_DAY:
    			c.set(Calendar.MILLISECOND, 0);
    			c.set(Calendar.SECOND, 0);
    			c.set(Calendar.MINUTE, 0);
    			break;
    		}
    		return new DateTime(c);
    	}
    	
    	public DateTime roundCeilingCopy() {
    		Calendar c = mDateTime.toCalendar();
    		switch (mField) {
    		case Calendar.MILLISECOND:
    			//do nothing
    			break;
    		case Calendar.SECOND:
    			c.set(Calendar.MILLISECOND, 0);
    			c.add(Calendar.SECOND, 1);
    			break;
    		case Calendar.MINUTE:
    			c.set(Calendar.MILLISECOND, 0);
    			c.set(Calendar.SECOND, 0);
    			c.add(Calendar.MINUTE, 1);
    			break;
    		case Calendar.HOUR_OF_DAY:
    			c.set(Calendar.MILLISECOND, 0);
    			c.set(Calendar.SECOND, 0);
    			c.set(Calendar.MINUTE, 0);
    			c.add(Calendar.HOUR_OF_DAY, 1);
    			break;
    		}
    		return new DateTime(c);
    	}
    }

	@Override
	public int compareTo(ReadableInstant another) {
		if (another instanceof DateTime) {
			return mCalendar.compareTo(((DateTime) another).toCalendar());
		} else {
			long lhs = getMillis();
			long rhs = another.getMillis();
			return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
		}
	}

	@Override
	public DateTimeZone getZone() {
		return new DateTimeZone(mCalendar.getTimeZone());
	}
	
	@Override
	public String toString() {
		return ISO8601Utils.fromCalendar(mCalendar);
	}

}

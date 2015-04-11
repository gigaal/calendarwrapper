package org.joda.time;

import java.util.Calendar;

import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;



public class Years extends BaseSingleFieldPeriod {
	
	public static Years yearsBetween(ReadableInstant start, ReadableInstant end) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(start.getMillis());
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(end.getMillis());
		
		int y1 = c1.get(Calendar.YEAR);
		int y2 = c2.get(Calendar.YEAR);
		return new Years(y2 - y1);
	}
	
	private Years(int years) {
        super(years, Calendar.YEAR);
    }
	
	public Years multipliedBy(int scalar) {
        return new Years(FieldUtils.safeMultiply(getValue(), scalar));
    }

    public Years dividedBy(int divisor) {
        if (divisor == 1) {
            return this;
        }
        return new Years(getValue() / divisor);
    }
	
    public int getYears() {
    	return getValue();
    }
}

package org.joda.time;

import java.util.Calendar;

import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;



public class Days extends BaseSingleFieldPeriod {
	
	public static Days daysBetween(ReadableInstant start, ReadableInstant end) {
		return new Days(Hours.hoursBetween(start, end).getValue() / 24);
	}
	
	private Days(int days) {
        super(days, Calendar.DAY_OF_MONTH);
    }
	
	public Days multipliedBy(int scalar) {
        return new Days(FieldUtils.safeMultiply(getValue(), scalar));
    }

    public Days dividedBy(int divisor) {
        if (divisor == 1) {
            return this;
        }
        return new Days(getValue() / divisor);
    }
	
    public int getDays() {
    	return getValue();
    }
}

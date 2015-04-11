package org.joda.time;

import java.util.Calendar;

import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;



public class Months extends BaseSingleFieldPeriod {
	
	public static Months monthsBetween(ReadableInstant start, ReadableInstant end) {
		Calendar c1 = Calendar.getInstance();
		c1.setTimeInMillis(start.getMillis());
		Calendar c2 = Calendar.getInstance();
		c2.setTimeInMillis(end.getMillis());
		
		int m1 = c1.get(Calendar.YEAR) * 12 + c1.get(Calendar.MONTH);
		int m2 = c2.get(Calendar.YEAR) * 12 + c2.get(Calendar.MONTH);
		return new Months(m2 - m1);
	}
	
	private Months(int months) {
        super(months, Calendar.MONTH);
    }
	
	public Months multipliedBy(int scalar) {
        return new Months(FieldUtils.safeMultiply(getValue(), scalar));
    }

    public Months dividedBy(int divisor) {
        if (divisor == 1) {
            return this;
        }
        return new Months(getValue() / divisor);
    }
	
    public int getMonths() {
    	return getValue();
    }
}

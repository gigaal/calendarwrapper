package org.joda.time;

import java.util.Calendar;

import org.joda.time.base.BaseSingleFieldPeriod;
import org.joda.time.field.FieldUtils;



public class Seconds extends BaseSingleFieldPeriod {
	
	public static Seconds secondsBetween(ReadableInstant start, ReadableInstant end) {
		long amount = BaseSingleFieldPeriod.millisBetween(start.getMillis(), end.getMillis());
		return new Seconds(FieldUtils.safeToInt(Math.round(amount / (1000f))));
	}
	
	private Seconds(int seconds) {
        super(seconds, Calendar.SECOND);
    }
	
	public int getSeconds() {
		return getValue();
	}
	
	public Seconds multipliedBy(int scalar) {
        return new Seconds(FieldUtils.safeMultiply(getValue(), scalar));
    }

    /**
     * Returns a new instance with the minutes divided by the specified divisor.
     * The calculation uses integer division, thus 3 divided by 2 is 1.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param divisor  the amount to divide by, may be negative
     * @return the new period divided by the specified divisor
     * @throws ArithmeticException if the divisor is zero
     */
    public Seconds dividedBy(int divisor) {
        if (divisor == 1) {
            return this;
        }
        return new Seconds(getValue() / divisor);
    }
	
}

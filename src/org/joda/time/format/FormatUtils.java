package org.joda.time.format;

public class FormatUtils {

	private static final double LOG_10 = Math.log(10);
	
	/**
     * Restricted constructor.
     */
    private FormatUtils() {
    }

    /**
     * Converts an integer to a string, prepended with a variable amount of '0'
     * pad characters, and appends it to the given buffer.
     *
     * <p>This method is optimized for converting small values to strings.
     *
     * @param buf receives integer converted to a string
     * @param value value to convert to a string
     * @param size minumum amount of digits to append
     */
    public static void appendPaddedInteger(StringBuffer buf, int value, int size) {
        if (value < 0) {
            buf.append('-');
            if (value != Integer.MIN_VALUE) {
                value = -value;
            } else {
                for (; size > 10; size--) {
                    buf.append('0');
                }
                buf.append("" + -(long)Integer.MIN_VALUE);
                return;
            }
        }
        if (value < 10) {
            for (; size > 1; size--) {
                buf.append('0');
            }
            buf.append((char)(value + '0'));
        } else if (value < 100) {
            for (; size > 2; size--) {
                buf.append('0');
            }
            // Calculate value div/mod by 10 without using two expensive
            // division operations. (2 ^ 27) / 10 = 13421772. Add one to
            // value to correct rounding error.
            int d = ((value + 1) * 13421772) >> 27;
            buf.append((char) (d + '0'));
            // Append remainder by calculating (value - d * 10).
            buf.append((char) (value - (d << 3) - (d << 1) + '0'));
        } else {
            int digits;
            if (value < 1000) {
                digits = 3;
            } else if (value < 10000) {
                digits = 4;
            } else {
                digits = (int)(Math.log(value) / LOG_10) + 1;
            }
            for (; size > digits; size--) {
                buf.append('0');
            }
            buf.append(Integer.toString(value));
        }
    }
}

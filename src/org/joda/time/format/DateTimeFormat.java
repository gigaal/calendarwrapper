package org.joda.time.format;


public class DateTimeFormat {
	
    public static DateTimeFormatter forPattern(String pattern) {
		return createFormatterForPattern(pattern);
	}
	
	/**
     * Select a format from a custom pattern.
     *
     * @param pattern  pattern specification
     * @throws IllegalArgumentException if the pattern is invalid
     * @see #appendPatternTo
     */
    private static DateTimeFormatter createFormatterForPattern(String pattern) {
        return new DateTimeFormatter(pattern);
    }
}

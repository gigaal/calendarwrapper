package org.joda.time;

public interface ReadableInstant extends Comparable<ReadableInstant> {

	/**
	 * Get the value as the number of milliseconds since
	 * the epoch, 1970-01-01T00:00:00Z.
	 *
	 * @return the value as milliseconds
	 */
	long getMillis();
	
    /**
     * Gets the time zone of the instant from the chronology.
     * 
     * @return the DateTimeZone that the instant is using, never null
     */
    DateTimeZone getZone();
	
	/**
     * Compares this object with the specified object for equality based
     * on the millisecond instant and the Chronology. All ReadableInstant
     * instances are accepted.
     * <p>
     * To compare two instants for absolute time (ie. UTC milliseconds 
     * ignoring the chronology), use {@link #isEqual(ReadableInstant)} or
     * {@link #compareTo(Object)}.
     *
     * @param readableInstant  a readable instant to check against
     * @return true if millisecond and chronology are equal, false if
     *  not or the instant is null or of an incorrect type
     */
    boolean equals(Object readableInstant);

    /**
     * Gets a hash code for the instant that is compatible with the 
     * equals method.
     * <p>
     * The formula used must be as follows:
     * <pre>
     * ((int) (getMillis() ^ (getMillis() >>> 32))) +
     * (getChronology().hashCode())
     * </pre>
     *
     * @return a hash code as defined above
     */
    int hashCode();
}

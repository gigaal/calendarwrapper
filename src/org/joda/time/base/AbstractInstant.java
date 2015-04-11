package org.joda.time.base;

import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;



public abstract class AbstractInstant implements ReadableInstant {

	public abstract long getMillis();
  
    
	/**
     * Compares this object with the specified object for ascending
     * millisecond instant order. This ordering is inconsistent with
     * equals, as it ignores the Chronology.
     * <p>
     * All ReadableInstant instances are accepted.
     *
     * @param other  a readable instant to check against
     * @return negative value if this is less, 0 if equal, or positive value if greater
     * @throws NullPointerException if the object is null
     * @throws ClassCastException if the object type is not supported
     */
    public int compareTo(ReadableInstant other) {
        if (this == other) {
            return 0;
        }
        
        long otherMillis = other.getMillis();
        long thisMillis = getMillis();
        
        // cannot do (thisMillis - otherMillis) as can overflow
        if (thisMillis == otherMillis) {
            return 0;
        }
        if (thisMillis < otherMillis) {
            return -1;
        } else {
            return 1;
        }
    }
    
    /**
     * Is this instant equal to the millisecond instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  a millisecond instant to check against
     * @return true if this instant is before the instant passed in
     */
    public boolean isEqual(long instant) {
        return (getMillis() == instant);
    }

    /**
     * Is this instant equal to the current instant
     * comparing solely by millisecond.
     * 
     * @return true if this instant is before the current instant
     */
    public boolean isEqualNow() {
        return isEqual(DateTimeUtils.currentTimeMillis());
    }

    /**
     * Is this instant equal to the instant passed in
     * comparing solely by millisecond.
     *
     * @param instant  an instant to check against, null means now
     * @return true if the instant is equal to the instant passed in
     */
    public boolean isEqual(ReadableInstant instant) {
        long instantMillis = DateTimeUtils.getInstantMillis(instant);
        return isEqual(instantMillis);
    }
    
    /**
     * Compares this object with the specified object for equality based
     * on the millisecond instant, chronology and time zone.
     * <p>
     * Two objects which represent the same instant in time, but are in
     * different time zones (based on time zone id), will be considered to
     * be different. Only two objects with the same {@link DateTimeZone},
     * {@link Chronology} and instant are equal.
     * <p>
     * See {@link #isEqual(ReadableInstant)} for an equals method that
     * ignores the Chronology and time zone.
     * <p>
     * All ReadableInstant instances are accepted.
     *
     * @param readableInstant  a readable instant to check against
     * @return true if millisecond and chronology are equal, false if
     *  not or the instant is null or of an incorrect type
     */
    public boolean equals(Object readableInstant) {
        // must be to fulfil ReadableInstant contract
        if (this == readableInstant) {
            return true;
        }
        if (readableInstant instanceof ReadableInstant == false) {
            return false;
        }
        ReadableInstant otherInstant = (ReadableInstant) readableInstant;
        return
            getMillis() == otherInstant.getMillis()
             && getZone().equals(otherInstant.getZone());
    }

    /**
     * Gets a hash code for the instant as defined in <code>ReadableInstant</code>.
     *
     * @return a suitable hash code
     */
    public int hashCode() {
        // must be to fulfil ReadableInstant contract
        return
            ((int) (getMillis() ^ (getMillis() >>> 32)));
    }
}

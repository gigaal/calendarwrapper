package org.joda.time;

public class IllegalInstantException extends IllegalArgumentException {
    
    /** Serialization lock. */
    private static final long serialVersionUID = 2858712538216L;


    /**
     * Constructor.
     * 
     * @param message  the message
     */
    public IllegalInstantException(String message) {
        super(message);
    }
    
}

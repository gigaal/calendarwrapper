package org.joda.time.base;

import org.joda.time.field.FieldUtils;


public abstract class BaseSingleFieldPeriod {
	
	private volatile int mFieldType;
	private volatile int mPeriod;
	
	protected BaseSingleFieldPeriod(int period, int fieldType) {
		mPeriod = period;
		mFieldType = fieldType;
    }
	
	protected static long millisBetween(long instant1, long instant2) {
		return FieldUtils.safeSubtract(instant1, instant2);
	}
	
	public int getValue() {
		return mPeriod;
	}

	public int getFieldType() {
		return mFieldType;
	}
}


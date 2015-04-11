package com.datetimewrapper.test;

import junit.framework.TestCase;

import org.joda.time.DateTime;

public class BasicTest extends TestCase {

	private long mNowMillis;
	private DateTime mNowDateTime;
	
	protected void setUp() throws Exception {
		super.setUp();
		mNowMillis = System.currentTimeMillis();
		mNowDateTime = new DateTime(mNowMillis);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDateTimeMethods() {
		assertNotNull(mNowDateTime);
		assertEquals(mNowDateTime.getMillis(), mNowMillis); 
		
		for (int i = 0; i < 100000; i++) {}
		assertTrue(mNowDateTime.isBeforeNow());
		assertTrue(mNowDateTime.isBefore(System.currentTimeMillis()));
		assertFalse(mNowDateTime.isAfterNow());
		assertFalse(mNowDateTime.isAfter(System.currentTimeMillis()));
		
		for (int increment = 2; increment <= 1024; increment *= 2) {
			DateTime d = mNowDateTime.plusMillis(increment);
			assertTrue(d.getMillis() == mNowDateTime.getMillis() + increment);
			
			d = mNowDateTime.plusSeconds(increment);
			assertNotNull(d);
			assertFalse(d.isEqual(mNowDateTime));
			assertTrue(d.isAfter(mNowMillis));
			
			d = mNowDateTime.plusMinutes(increment);
			assertNotNull(d);
			assertFalse(d.isEqual(mNowDateTime));
			assertTrue(d.isAfter(mNowMillis));
			
			d = mNowDateTime.plusHours(increment);
			assertNotNull(d);
			assertFalse(d.isEqual(mNowDateTime));
			assertTrue(d.isAfter(mNowMillis));
			
			d = mNowDateTime.plusDays(increment);
			assertNotNull(d);
			assertFalse(d.isEqual(mNowDateTime));
			assertTrue(d.isAfter(mNowMillis));
			
			d = mNowDateTime.plusMonths(increment);
			assertNotNull(d);
			assertFalse(d.isEqual(mNowDateTime));
			assertTrue(d.isAfter(mNowMillis));
			
			d = mNowDateTime.plusYears(increment);
			assertTrue(d.getYear() == mNowDateTime.getYear() + increment);
			assertFalse(d.isEqual(mNowDateTime));
			assertTrue(d.isAfter(mNowMillis));
		}
	}
	
	
}

package com.datetimewrapper.test;

/*
 *  Copyright 2001-2013 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.Duration;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;
import org.joda.time.base.AbstractInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * This class is a Junit unit test for DateTime.
 *
 * @author Stephen Colebourne
 */
public class DateTimeBasicsTest extends TestCase {
    // Test in 2002/03 as time zones are more well known
    // (before the late 90's they were all over the place)

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");

    long y2002days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365;
    long y2003days = 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 
                     366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 
                     365 + 365 + 366 + 365 + 365 + 365 + 366 + 365 + 365 + 365 +
                     366 + 365 + 365;
    
    // 2002-06-09
    private long TEST_TIME_NOW =
            (y2002days + 31L + 28L + 31L + 30L + 31L + 9L -1L) * DateTimeConstants.MILLIS_PER_DAY;
            
    // 2002-04-05
    private long TEST_TIME1 =
            (y2002days + 31L + 28L + 31L + 5L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 12L * DateTimeConstants.MILLIS_PER_HOUR
            + 24L * DateTimeConstants.MILLIS_PER_MINUTE;
        
    // 2003-05-06
    private long TEST_TIME2 =
            (y2003days + 31L + 28L + 31L + 30L + 6L -1L) * DateTimeConstants.MILLIS_PER_DAY
            + 14L * DateTimeConstants.MILLIS_PER_HOUR
            + 28L * DateTimeConstants.MILLIS_PER_MINUTE;
    
    private DateTimeZone originalDateTimeZone = null;
    private TimeZone originalTimeZone = null;
    private Locale originalLocale = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static TestSuite suite() {
        return new TestSuite(DateTimeBasicsTest.class);
    }

    public DateTimeBasicsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    protected void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
        originalDateTimeZone = null;
        originalTimeZone = null;
        originalLocale = null;
    }

    //-----------------------------------------------------------------------
    public void testTest() {
        assertEquals("2002-06-09T00:00:00.000Z", new Instant(TEST_TIME_NOW).toString());
        assertEquals("2002-04-05T12:24:00.000Z", new Instant(TEST_TIME1).toString());
        assertEquals("2003-05-06T14:28:00.000Z", new Instant(TEST_TIME2).toString());
    }

    //-----------------------------------------------------------------------
    public void testGetters() {
        DateTime test = new DateTime();
        
        assertEquals(LONDON, test.getZone());
        assertEquals(TEST_TIME_NOW, test.getMillis());
        
        assertEquals(2002, test.getYear());
        assertEquals(6, test.getMonthOfYear());
        assertEquals(9, test.getDayOfMonth());
        assertEquals(23, test.getWeekOfWeekyear());
        assertEquals(1, test.getHourOfDay());
        assertEquals(0, test.getMinuteOfHour());
        assertEquals(0, test.getSecondOfMinute());
        assertEquals(0, test.getMillisOfSecond());
    }

    //-----------------------------------------------------------------------
    public void testEqualsHashCode() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test2 = new DateTime(TEST_TIME1);
        assertEquals(true, test1.equals(test2));
        assertEquals(true, test2.equals(test1));
        assertEquals(true, test1.equals(test1));
        assertEquals(true, test2.equals(test2));
        assertEquals(true, test1.hashCode() == test2.hashCode());
        assertEquals(true, test1.hashCode() == test1.hashCode());
        assertEquals(true, test2.hashCode() == test2.hashCode());
        
        DateTime test3 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.equals(test3));
        assertEquals(false, test2.equals(test3));
        assertEquals(false, test3.equals(test1));
        assertEquals(false, test3.equals(test2));
        assertEquals(false, test1.hashCode() == test3.hashCode());
        assertEquals(false, test2.hashCode() == test3.hashCode());
        
        assertEquals(false, test1.equals("Hello"));
        assertEquals(true, test1.equals(new MockInstant()));
        assertEquals(false, test1.equals(new DateTime(TEST_TIME1, PARIS)));
    }
    
    class MockInstant extends AbstractInstant {
        public String toString() {
            return null;
        }
        public long getMillis() {
            return TEST_TIME1;
        }
        
		@Override
		public DateTimeZone getZone() {
			return DateTimeZone.getDefault();
		}
    }

    public void testCompareTo() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(0, test1.compareTo(test1a));
        assertEquals(0, test1a.compareTo(test1));
        assertEquals(0, test1.compareTo(test1));
        assertEquals(0, test1a.compareTo(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(-1, test1.compareTo(test2));
        assertEquals(+1, test2.compareTo(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2);
        assertEquals(-1, test1.compareTo(test3));
        assertEquals(+1, test3.compareTo(test1));
        assertEquals(0, test3.compareTo(test2));
        
        assertEquals(+1, test2.compareTo(new MockInstant()));
        assertEquals(0, test1.compareTo(new MockInstant()));
        
        try {
            test1.compareTo(null);
            fail();
        } catch (NullPointerException ex) {}
//        try {
//            test1.compareTo(new Date());
//            fail();
//        } catch (ClassCastException ex) {}
    }
    
    public void testIsEqual_RI() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(true, test1.isEqual(test1a));
        assertEquals(true, test1a.isEqual(test1));
        assertEquals(true, test1.isEqual(test1));
        assertEquals(true, test1a.isEqual(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.isEqual(test2));
        assertEquals(false, test2.isEqual(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.isEqual(test3));
        assertEquals(false, test3.isEqual(test1));
        assertEquals(true, test3.isEqual(test2));
        
        assertEquals(false, new DateTime(TEST_TIME_NOW + 1).isEqual(null));
        assertEquals(true, new DateTime(TEST_TIME_NOW).isEqual(null));
        assertEquals(false, new DateTime(TEST_TIME_NOW - 1).isEqual(null));
    }
    
    //-----------------------------------------------------------------------
    public void testIsBefore_long() {
        assertEquals(true, new DateTime(TEST_TIME1).isBefore(TEST_TIME2));
        assertEquals(false, new DateTime(TEST_TIME1).isBefore(TEST_TIME1));
        assertEquals(false, new DateTime(TEST_TIME2).isBefore(TEST_TIME1));
    }
    
    public void testIsBeforeNow() {
        assertEquals(true, new DateTime(TEST_TIME_NOW - 1).isBeforeNow());
        assertEquals(false, new DateTime(TEST_TIME_NOW).isBeforeNow());
        assertEquals(false, new DateTime(TEST_TIME_NOW + 1).isBeforeNow());
    }
    
    public void testIsBefore_RI() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(false, test1.isBefore(test1a));
        assertEquals(false, test1a.isBefore(test1));
        assertEquals(false, test1.isBefore(test1));
        assertEquals(false, test1a.isBefore(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.isBefore(test2));
        assertEquals(false, test2.isBefore(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2);
        assertEquals(true, test1.isBefore(test3));
        assertEquals(false, test3.isBefore(test1));
        assertEquals(false, test3.isBefore(test2));
        
        assertEquals(false, test2.isBefore(new MockInstant()));
        assertEquals(false, test1.isBefore(new MockInstant()));
        
        assertEquals(false, new DateTime(TEST_TIME_NOW + 1).isBefore(null));
        assertEquals(false, new DateTime(TEST_TIME_NOW).isBefore(null));
        assertEquals(true, new DateTime(TEST_TIME_NOW - 1).isBefore(null));
    }
    
    //-----------------------------------------------------------------------
    public void testIsAfter_long() {
        assertEquals(false, new DateTime(TEST_TIME1).isAfter(TEST_TIME2));
        assertEquals(false, new DateTime(TEST_TIME1).isAfter(TEST_TIME1));
        assertEquals(true, new DateTime(TEST_TIME2).isAfter(TEST_TIME1));
    }
    
    public void testIsAfterNow() {
        assertEquals(false, new DateTime(TEST_TIME_NOW - 1).isAfterNow());
        assertEquals(false, new DateTime(TEST_TIME_NOW).isAfterNow());
        assertEquals(true, new DateTime(TEST_TIME_NOW + 1).isAfterNow());
    }
    
    public void testIsAfter_RI() {
        DateTime test1 = new DateTime(TEST_TIME1);
        DateTime test1a = new DateTime(TEST_TIME1);
        assertEquals(false, test1.isAfter(test1a));
        assertEquals(false, test1a.isAfter(test1));
        assertEquals(false, test1.isAfter(test1));
        assertEquals(false, test1a.isAfter(test1a));
        
        DateTime test2 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.isAfter(test2));
        assertEquals(true, test2.isAfter(test1));
        
        DateTime test3 = new DateTime(TEST_TIME2);
        assertEquals(false, test1.isAfter(test3));
        assertEquals(true, test3.isAfter(test1));
        assertEquals(false, test3.isAfter(test2));
        
        assertEquals(true, test2.isAfter(new MockInstant()));
        assertEquals(false, test1.isAfter(new MockInstant()));
        
        assertEquals(true, new DateTime(TEST_TIME_NOW + 1).isAfter(null));
        assertEquals(false, new DateTime(TEST_TIME_NOW).isAfter(null));
        assertEquals(false, new DateTime(TEST_TIME_NOW - 1).isAfter(null));
    }
    
    //-----------------------------------------------------------------------
    public void testSerialization() throws Exception {
        DateTime test = new DateTime(TEST_TIME_NOW);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(test.toCalendar());
        byte[] bytes = baos.toByteArray();
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        DateTime result = new DateTime((Calendar)ois.readObject());
        ois.close();
        
        assertEquals(test, result);
    }

    //-----------------------------------------------------------------------
    public void testToString() {
        DateTime test = new DateTime(TEST_TIME_NOW);
        assertEquals("2002-06-09T01:00:00.000+01:00", test.toString());
        
        test = new DateTime(TEST_TIME_NOW, PARIS);
        assertEquals("2002-06-09T02:00:00.000+02:00", test.toString());
    }

    public void testToDateTime_DateTimeZone() {
        DateTime test = new DateTime(TEST_TIME1);
        DateTime result = test.toDateTime(LONDON);
        assertEquals(test, result);

        test = new DateTime(TEST_TIME1);
        result = test.toDateTime(PARIS);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(PARIS, result.getZone());

        test = new DateTime(TEST_TIME1, PARIS);
        result = test.toDateTime((DateTimeZone) null);
        assertEquals(test.getMillis(), result.getMillis());
        assertEquals(LONDON, result.getZone());

        test = new DateTime(TEST_TIME1);
        result = test.toDateTime((DateTimeZone) null);
        assertEquals(test, result);
    }

    public void testToDate() {
        DateTime test = new DateTime(TEST_TIME1);
        Date result = test.toDate();
        assertEquals(test.getMillis(), result.getTime());
    }

    public void testToCalendar_Locale() {
        DateTime test = new DateTime(TEST_TIME1);
        Calendar result = test.toCalendar(null);
        assertEquals(test.getMillis(), result.getTime().getTime());
        assertEquals(TimeZone.getTimeZone("Europe/London"), result.getTimeZone());

        test = new DateTime(TEST_TIME1, PARIS);
        result = test.toCalendar(null);
        assertEquals(test.getMillis(), result.getTime().getTime());
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), result.getTimeZone());

        test = new DateTime(TEST_TIME1, PARIS);
        result = test.toCalendar(Locale.UK);
        assertEquals(test.getMillis(), result.getTime().getTime());
        assertEquals(TimeZone.getTimeZone("Europe/Paris"), result.getTimeZone());
    }

    //-----------------------------------------------------------------------
    public void testWithDate_int_int_int() {
        DateTime test = new DateTime(2002, 4, 5, 1, 2, 3, 4);
        DateTime result = test.withDate(2003, 5, 6);
        DateTime expected = new DateTime(2003, 5, 6, 1, 2, 3, 4);
        assertEquals(expected, result);
        
        test = new DateTime(TEST_TIME1);
        try {
            test.withDate(2003, 13, 1);
            fail();
        } catch (IllegalArgumentException ex) {}
    }
    
    public void testWithDate_int_int_int_toDST1() {
        // 2010-03-28T02:55 is DST time, need to change to 03:55
        DateTime test = new DateTime(2015, 1, 10, 2, 55, 0, 0, PARIS);
        DateTime result = test.withDate(2010, 3, 28);
        DateTime expected = new DateTime(2010, 3, 28, 3, 55, 0, 0, PARIS);
        assertEquals(expected, result);
    }
    
    public void testWithDate_int_int_int_toDST2() {
        // 2010-03-28T02:55 is DST time, need to change to 03:55
        DateTime test = new DateTime(2015, 1, 28, 2, 55, 0, 0, PARIS);
        DateTime result = test.withDate(2010, 3, 28);
        DateTime expected = new DateTime(2010, 3, 28, 3, 55, 0, 0, PARIS);
        assertEquals(expected, result);
    }
    
    public void testWithDate_int_int_int_affectedByDST() {
        // 2010-03-28T02:55 is DST time, need to avoid time being changed to 03:55
        DateTime test = new DateTime(2015, 1, 28, 2, 55, 0, 0, PARIS);
        DateTime result = test.withDate(2010, 3, 10);
        DateTime expected = new DateTime(2010, 3, 10, 2, 55, 0, 0, PARIS);
        assertEquals(expected, result);
    }
    
    public void testWithTime_int_int_int_int_toDST() {
        // 2010-03-28T02:55 is DST time, need to change to 03:55
        DateTime test = new DateTime(2010, 3, 28, 0, 0, 0, 0, PARIS);
        DateTime result = test.withTime(2, 55, 0, 0);
        DateTime expected = new DateTime(2010, 3, 28, 3, 55, 0, 0, PARIS);
        assertEquals(expected, result);
    }
    
    public void testPlusYears_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.plusYears(1);
        DateTime expected = new DateTime(2003, 5, 3, 1, 2, 3, 4);
        assertEquals(expected, result);
        
        result = test.plusYears(0);
        assertSame(test, result);
    }

    public void testPlusMonths_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.plusMonths(1);
        DateTime expected = new DateTime(2002, 6, 3, 1, 2, 3, 4);
        assertEquals(expected, result);
        
        result = test.plusMonths(0);
        assertSame(test, result);
    }

    public void testPlusWeeks_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.plusWeeks(1);
        DateTime expected = new DateTime(2002, 5, 10, 1, 2, 3, 4);
        assertEquals(expected, result);
        
        result = test.plusWeeks(0);
        assertSame(test, result);
    }

    public void testPlusDays_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.plusDays(1);
        DateTime expected = new DateTime(2002, 5, 4, 1, 2, 3, 4);
        assertEquals(expected, result);
        
        result = test.plusDays(0);
        assertSame(test, result);
    }

    public void testPlusHours_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.plusHours(1);
        DateTime expected = new DateTime(2002, 5, 3, 2, 2, 3, 4);
        assertEquals(expected, result);
        
        result = test.plusHours(0);
        assertSame(test, result);
    }

    public void testPlusMinutes_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.plusMinutes(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 3, 3, 4);
        assertEquals(expected, result);
        
        result = test.plusMinutes(0);
        assertSame(test, result);
    }

    public void testPlusSeconds_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.plusSeconds(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 2, 4, 4);
        assertEquals(expected, result);
        
        result = test.plusSeconds(0);
        assertSame(test, result);
    }

    public void testPlusMillis_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.plusMillis(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 2, 3, 5);
        assertEquals(expected, result);
        
        result = test.plusMillis(0);
        assertSame(test, result);
    }

    public void testMinusYears_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.minusYears(1);
        DateTime expected = new DateTime(2001, 5, 3, 1, 2, 3, 4);
        assertEquals(expected, result);
        
        result = test.minusYears(0);
        assertSame(test, result);
    }

    public void testMinusMonths_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.minusMonths(1);
        DateTime expected = new DateTime(2002, 4, 3, 1, 2, 3, 4);
        assertEquals(expected, result);
        
        result = test.minusMonths(0);
        assertSame(test, result);
    }

    public void testMinusDays_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.minusDays(1);
        DateTime expected = new DateTime(2002, 5, 2, 1, 2, 3, 4);
        assertEquals(expected, result);
        
        result = test.minusDays(0);
        assertSame(test, result);
    }

//    public void testMinusHours_int() {
//        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
//        DateTime result = test.minusHours(1);
//        DateTime expected = new DateTime(2002, 5, 3, 0, 2, 3, 4);
//        assertEquals(expected, result);
//        
//        result = test.minusHours(0);
//        assertSame(test, result);
//    }

    public void testMinusMinutes_int() {
        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
        DateTime result = test.minusMinutes(1);
        DateTime expected = new DateTime(2002, 5, 3, 1, 1, 3, 4);
        assertEquals(expected, result);
        
        result = test.minusMinutes(0);
        assertSame(test, result);
    }

//    public void testMinusSeconds_int() {
//        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
//        DateTime result = test.minusSeconds(1);
//        DateTime expected = new DateTime(2002, 5, 3, 1, 2, 2, 4);
//        assertEquals(expected, result);
//        
//        result = test.minusSeconds(0);
//        assertSame(test, result);
//    }
//
//    public void testMinusMillis_int() {
//        DateTime test = new DateTime(2002, 5, 3, 1, 2, 3, 4);
//        DateTime result = test.minusMillis(1);
//        DateTime expected = new DateTime(2002, 5, 3, 1, 2, 3, 3);
//        assertEquals(expected, result);
//        
//        result = test.minusMillis(0);
//        assertSame(test, result);
//    }

}

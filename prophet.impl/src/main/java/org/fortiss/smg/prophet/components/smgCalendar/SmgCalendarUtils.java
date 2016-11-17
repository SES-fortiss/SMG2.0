package org.fortiss.smg.prophet.components.smgCalendar;

import java.util.Date;

/**
 * This is a helper class providing calendar utilities
 * 
 * @author Orest Tarasiuk
 * 
 */
public class SmgCalendarUtils {

    public SmgCalendarUtils() {
    }

    /**
     * @return today as Date
     */
    public static Date getToday() {
        java.util.Calendar c = java.util.Calendar.getInstance();
        // set the calendar to start of today
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.set(java.util.Calendar.SECOND, 0);
        c.set(java.util.Calendar.MILLISECOND, 0);
        // return as a Date
        return c.getTime();
    }

    /**
     * @param date
     *            date to check against
     * @return next day as Date with the original time
     */
    public static Date getNextDay(Date date) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(date);
        // add one day
        c.add(java.util.Calendar.DAY_OF_YEAR, 1);
        // return as a Date
        return c.getTime();
    }

}

package org.fortiss.smg.prophet.components.smgCalendar;

import java.io.IOException;
import java.util.Date;

import org.fortiss.smg.prophet.impl.Config;
import org.slf4j.Logger;

import net.fortuna.ical4j.data.ParserException;

/**
 * Just a test class
 * 
 * @author Orest Tarasiuk
 * 
 */
public class SmgCalendarTest {

    private static SmgCalendar sc;

    public SmgCalendarTest() {
    }

    public static void main(String[] args) throws IOException, ParserException {
        // !DEPRECATED!

        System.out.println();

        Date today = SmgCalendarUtils.getToday();
        System.out.println("Today: " + today);
        Date tomorrow = SmgCalendarUtils.getNextDay(today);
        System.out.println("Tomorrow: " + tomorrow);

        // SmgCalendarIcsConverter cic = new
        // SmgCalendarIcsConverter("https://merkur.fortiss.org/home/dijkstra/calendar");
        // long dur = cic.getOccupiedDuration(tomorrow);
        // System.out.println("Duration: " + dur + " min");
        // cic.terminate();

        // logger missing
        SmgCalendar smgC = new SmgCalendar(null,
                "https://merkur.fortiss.org/home/dijkstra/calendar", 5000);
        double uti = smgC.getRoomUtilization(10 * 60, tomorrow);
        System.out.println("Utilization: " + uti);
        smgC.terminate();

        System.out.println();
        System.out.println("main complete!");
    }

    public static void testSc(Logger logger) throws IOException,
            ParserException {
        sc = new SmgCalendar(logger, Config.icsUrlStr,
                Config.calendarRefreshIntervalMs);
        double uti = sc.getRoomUtilization(Config.maxRoomDuration,
                Config.imminentForecastDate);
        logger.info("SmgCalendarTestResult: Utilization: " + uti);
    }

}

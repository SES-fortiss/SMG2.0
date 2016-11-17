package org.fortiss.smg.prophet.components.smgCalendar;

import java.io.IOException;
import java.util.Date;

import net.fortuna.ical4j.data.ParserException;

import org.slf4j.Logger;

/**
 * This class provides information about the utilization of a room based on
 * occupancy rates determined by looking at the room's calendar
 * 
 * @author Orest Tarasiuk
 * @thesisOT
 * 
 */
/*
 * TODO: Increase the forecast resolution by using 30 minute intervals instead
 * of the entire day
 */
public class SmgCalendar {

    private static Logger logger;
    private SmgCalendarIcsConverter cic;
    private String icsUrlStr;

    /**
     * @param logger
     *            log4j logger
     * @param icsUrlStr
     *            which URL to use as input iCal
     * @param refreshIntervalMs
     *            how often to re-fetch calendar data, in milliseconds
     * @throws IOException
     *             fetchCalendarFromIcsUrl() unsuccessful
     * @throws ParserException
     *             fetchCalendarFromIcsUrl() unsuccessful
     * @thesisOT
     */
    public SmgCalendar(Logger logger, String icsUrlStr, int refreshIntervalMs)
            throws IOException, ParserException {
        SmgCalendar.logger = logger;
        SmgCalendar.logger.trace("SmgCalendar: constructing");
        cic = new SmgCalendarIcsConverter(SmgCalendar.logger, icsUrlStr,
                refreshIntervalMs);
        this.icsUrlStr = icsUrlStr;
    }

    /**
     * @param maxDuration
     *            the duration in min for which utilization is to be considered
     *            to be at 100%
     * @param date
     *            day to be checked
     * @param calendar
     *            calendar for the room to be checked
     * @return room utilization, linear between 0 and 1
     * @thesisOT
     */
    public double getRoomUtilization(long maxDuration, Date date) {
        // System.out.println("maxDuration: " + maxDuration);
        long dur = cic.getOccupiedDuration(date);
        SmgCalendar.logger.trace("SmgCalendar: Duration: " + dur);
        if (maxDuration < dur) {
            dur = maxDuration;
        }
        return (double) dur / (double) maxDuration;
    }

    public void terminate() {
        SmgCalendar.logger.debug("SmgCalendar: terminating");
        cic.terminate();
    }

    public String getIcsUrlStr() {
        return icsUrlStr;
    }

    public void setIcsUrlStr(String icsUrlStr) throws IOException,
            ParserException {
        this.icsUrlStr = icsUrlStr;
        cic.setIcsUrlStr(icsUrlStr);
    }

}

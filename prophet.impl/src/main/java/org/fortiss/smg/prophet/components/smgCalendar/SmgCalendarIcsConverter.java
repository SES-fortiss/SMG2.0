package org.fortiss.smg.prophet.components.smgCalendar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;

import org.joda.time.Duration;
import org.slf4j.Logger;

/**
 * This class is a converter used for extracting a calendar from an ICS/iCal
 * file
 * 
 * @author Orest Tarasiuk
 * @thesisOT
 * 
 */
public class SmgCalendarIcsConverter {

    private static Logger logger;
    private String icsUrlStr;
    private SmgCalendarIcsConverterFetcher fetcher;
    private Calendar calendar;
    boolean fetcherActive;

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
    public SmgCalendarIcsConverter(Logger logger, String icsUrlStr,
            int refreshIntervalMs) throws IOException, ParserException {

        SmgCalendarIcsConverter.logger = logger;
        this.icsUrlStr = icsUrlStr;
        fetchCalendarFromIcsUrl();
        fetcher = new SmgCalendarIcsConverterFetcher(this, refreshIntervalMs);
        fetcherActive = true;
        fetcher.start();

    }

    /**
     * @param date
     *            day to be checked
     * @param calendar
     *            calendar for the room to be checked
     * @return number of minutes that the room is occupied on day date
     * @thesisOT
     */
    public long getOccupiedDuration(Date date) {

        java.util.Calendar c = java.util.Calendar.getInstance();
        // set the calendar to date
        c.setTime(date);
        // set calendar to 23:59:59 on the previous day
        c.set(java.util.Calendar.HOUR_OF_DAY, 0);
        c.set(java.util.Calendar.MINUTE, 0);
        c.add(java.util.Calendar.SECOND, -1);
        c.set(java.util.Calendar.MILLISECOND, 0);
        // update date
        date = c.getTime();
        // System.out.println("date: " + date);

        // get the date on the next day (this will be the boundary)
        c.setTime(SmgCalendarUtils.getNextDay(date));
        c.add(java.util.Calendar.SECOND, +1);
        Date date2 = c.getTime();
        // System.out.println("date2: " + date2);

        long occupied = 0;

        // iterate through components
        for (Iterator<?> i = calendar.getComponents().iterator(); i.hasNext();) {
            Component component = (Component) i.next();
            if (component.getName() != "VEVENT") {
                continue;
            }
            // System.out.println("Component [" + component.getName() + "]");

            // iterate through events between date and date2
            Period period = new Period(new DateTime(date), new DateTime(date2));
            PeriodList set = component.calculateRecurrenceSet(period);
            if (!set.isEmpty()) {
                // iterate through concrete events between date and date2
                // System.out.println("Event set: " + set);
                for (Iterator<?> j = set.iterator(); j.hasNext();) {
                    Period p = (Period) j.next();
                    DateTime start = p.getStart();
                    DateTime end = p.getEnd();

                    Duration d = new Duration(start.getTime(), end.getTime());
                    occupied += d.getStandardMinutes();
                }
            }
        }

        return occupied;

    }

    /**
     * @param icsUrlStr
     *            URL to the ICS file
     * @throws IOException
     * @throws ParserException
     * @thesisOT
     */
    void fetchCalendarFromIcsUrl() throws IOException, ParserException {

        // Create a new trust manager that trust all certificates
        // TODO: Get some checks...
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };

        // Activate the new trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

        // Password authenticate
        Properties properties = new Properties();
        // TODO: parametrize
        properties.setProperty("username", "smg");
        properties.setProperty("password", "!Fortiss");
        Authenticator.setDefault(new SmgCalendarIcsConverterAuth(properties));

        // Connect
        URL requestUrl = new URL(icsUrlStr);
        HttpsURLConnection con = (HttpsURLConnection) requestUrl
                .openConnection();

        // Create calendar
        BufferedReader br = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        CalendarBuilder builder = new CalendarBuilder();
        Calendar calendar = builder.build(br);
        br.close();

        SmgCalendarIcsConverter.logger
                .trace("SmgCalendarIcsConverter: Calendar fetched");

        this.calendar = calendar;
    }

    public void terminate() {
        fetcherActive = false;
    }

    public String getIcsUrlStr() {
        return icsUrlStr;
    }

    public void setIcsUrlStr(String icsUrlStr) throws IOException,
            ParserException {
        this.icsUrlStr = icsUrlStr;
        fetchCalendarFromIcsUrl();
    }

}

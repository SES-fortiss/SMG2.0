package org.fortiss.smg.prophet.components.smgCalendar;

import java.io.IOException;

import net.fortuna.ical4j.data.ParserException;

/**
 * This class provides a threaded loop for fetchCalendarFromIcsUrl
 * 
 * @author Orest Tarasiuk
 * @thesisOT
 * 
 */
public class SmgCalendarIcsConverterFetcher extends Thread {
    SmgCalendarIcsConverter cic;
    int interval;

    /**
     * @param cic
     *            SmgCalendarIcsConverter pointer
     * @param interval
     *            refresh interval in ms
     * @thesisOT
     */
    public SmgCalendarIcsConverterFetcher(SmgCalendarIcsConverter cic,
            int interval) {
        this.cic = cic;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (cic.fetcherActive) {
            try {
                cic.fetchCalendarFromIcsUrl();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (ParserException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

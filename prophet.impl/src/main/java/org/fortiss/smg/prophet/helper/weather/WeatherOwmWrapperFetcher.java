package org.fortiss.smg.prophet.helper.weather;

/**
 * This class provides a threaded loop for fetchJsonWeather
 * 
 * @author Orest Tarasiuk
 * @thesisOT
 * 
 */
public class WeatherOwmWrapperFetcher extends Thread {
    WeatherOwmWrapper wow;
    int interval;

    /**
     * @param weatherOwmWrapper
     *            wow pointer
     * @param interval
     *            refresh interval in ms
     * @thesisOT
     */
    public WeatherOwmWrapperFetcher(WeatherOwmWrapper weatherOwmWrapper,
            int interval) {
        wow = weatherOwmWrapper;
        this.interval = interval;
    }

    @Override
    public void run() {
        while (wow.fetcherActive) {
            wow.fetchJsonWeather();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}

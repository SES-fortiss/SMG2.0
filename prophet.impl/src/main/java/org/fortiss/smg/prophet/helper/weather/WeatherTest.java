package org.fortiss.smg.prophet.helper.weather;

import java.util.Date;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.prophet.impl.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Just a test class
 * 
 * @author Orest Tarasiuk
 * 
 */
public class WeatherTest {

	private static final Logger logger = LoggerFactory
            .getLogger(WeatherTest.class);
	
    private static WeatherConditions wc;
    private static Generation g;

    public WeatherTest() {
    }

    public static void main(String[] args) {
        // !DEPRECATED!

        System.out.println();

        Date now = new Date();
        // logger missing
        WeatherConditions wc = new WeatherConditions(logger, now, "Munich, de",
                5000);

        System.out.println();
        System.out.println("AC Necessity: " + wc.getAcNecessity(10, 20));
        System.out.println("Wind Potential: " + wc.getWindPotential());
        System.out.println("Solar Potential: " + wc.getSolarPotential());

        System.out.println();
        wc.terminate();
        System.out.println("main complete!");
    }

    public static void testWc(Logger logger) {
        wc = new WeatherConditions(logger, Config.imminentForecastDate,
                "Munich,de", Config.weatherRefreshIntervalMs);
        logger.debug("WeatherConditionsTestResult: AC Necessity: "
                + wc.getAcNecessity(10, 20));
        logger.debug("WeatherConditionsTestResult: Wind Potential: "
                + wc.getWindPotential());
        logger.debug("WeatherConditionsTestResult: Solar Potential: "
                + wc.getSolarPotential());
        // wc.terminate();
    }

    public static void testG(Logger logger) {
        g = new Generation(logger, Config.imminentForecastDate,
                Config.locationCity, Config.weatherRefreshIntervalMs,
                Config.dbSolarGenerationCapacityField);
        try {
			logger.trace("GenerationTestResult: " + g.getGenerationForecast());
		} catch (TimeoutException e) {
			logger.debug("GenerationTestException: Can't get Generation ");
			e.printStackTrace();
		}
    }

}

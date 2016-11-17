package org.fortiss.smg.prophet.helper.weather;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.*;
import org.slf4j.Logger;

/**
 * This class provides forecasts of energy generation by a local solar panel
 * 
 * @author Orest Tarasiuk
 * @thesisOT
 * 
 */
public class Generation {

    private static Logger logger;
    private Date date;
    private WeatherConditions wc;
    private String dbSolarGenerationCapacityField;

    private static InformationBrokerInterface broker;
    
    /**
     * @param logger
     *            log4j logger
     * @param date
     *            date for which weather forecasts are to be fetched
     * @param city
     *            location for which weather forecasts are to be fetched, e. g.
     *            "Munich,de"
     * @param weatherRefreshIntervalMs
     *            how often to re-fetch weather data, in milliseconds
     * @param dbSolarGenerationCapacityField
     *            where in the DB the generation capacity of the solar panel is
     *            stored
     * @thesisOT
     */
    public Generation(Logger logger, Date date, String city,
            int weatherRefreshIntervalMs, String dbSolarGenerationCapacityField) {
        Generation.logger = logger;
        this.date = date;
        wc = new WeatherConditions(logger, date, city, weatherRefreshIntervalMs);
        this.dbSolarGenerationCapacityField = dbSolarGenerationCapacityField;
        Generation.logger.trace("Generation: Constructing for " + date);
    }

    /**
     * @return Forecasted internal energy generation in Wh for the entire day
     * @throws TimeoutException 
     * @thesisOT
     */
    public double getGenerationForecast() throws TimeoutException {
        // WeatherConditions X SolarPanels
        double solarPotential = wc.getSolarPotential();
        
        List<DoublePoint> l = broker.getDoubleValue(
        		new DeviceId("solar_generator_watt", "solarlog.wrapper"), 
        			date.getTime(), date.getTime());

        double maxSolarGenerationCapacity = l.get(0).getValue();

        double generationForecast = maxSolarGenerationCapacity
                * wc.getDayLengthInH() * solarPotential;
        Generation.logger
                .debug("Generation: final generationForecast:                   *G* "
                        + generationForecast + " Wh");

        return generationForecast;
    }
    
    

    public void terminate() {
        wc.terminate();
    }

}

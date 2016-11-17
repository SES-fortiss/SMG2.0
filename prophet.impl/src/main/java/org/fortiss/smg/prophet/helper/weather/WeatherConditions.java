package org.fortiss.smg.prophet.helper.weather;

import java.util.Date;

import org.fortiss.smg.prophet.impl.Config;
import org.slf4j.Logger;

/**
 * This class provides (forecast) condition information based on weather data:
 * the potential for solar and wind energy generation in the local area as well
 * as the necessity to cool down or heat up the building
 * 
 * @author Orest Tarasiuk
 * @thesisOT
 * 
 */
/*
 * TODO: Increase the forecast resolution by using three-hourly forecasts
 * instead of daily ones, e. g.
 * http://api.openweathermap.org/data/2.5/forecast?lat=35&lon=139
 */
public class WeatherConditions {

    private static Logger logger;
    WeatherOwmWrapper wow;
    private Date weatherDate;
    private String locationCity;

    static final double maxSpeedDefault = 30;
    static final double tempMarginDefault = 10;
    static final double optimalTempDefault = 20;
    static final double longestDayInSeconds = 86400; // 24 h

    /**
     * @param logger
     *            log4j logger
     * @param date
     *            date for which weather forecasts are to be fetched
     * @param city
     *            location for which weather forecasts are to be fetched, e. g.
     *            "Munich,de"
     * @param refreshIntervalMs
     *            how often to re-fetch weather data, in milliseconds
     * @thesisOT
     */
    public WeatherConditions(Logger logger, Date date, String city,
            int refreshIntervalMs) {
        WeatherConditions.logger = logger;
        WeatherConditions.logger.trace("WeatherConditions: constructing");
        weatherDate = date;
        locationCity = city;
        wow = new WeatherOwmWrapper(logger, weatherDate, locationCity,
                refreshIntervalMs);
    }

    /**
     * @param date
     *            date for which weather forecasts are to be consulted
     * @thesisOT
     */
    public void changeDate(Date date) {
        weatherDate = date;
        wow.setWeatherDate(weatherDate);
    }

    /**
     * @return day length in milliseconds
     * @thesisOT
     */
    public double getDayLength() {
        double dayLength = wow.getSunSet().getTime()
                - wow.getSunRise().getTime();
        return dayLength;
    }

    /**
     * @return day length in hours
     * @thesisOT
     */
    public double getDayLengthInH() {
        return getDayLength() / 1000 / 60 / 60;
    }

    /**
     * @return forecasted potential for local solar energy generation, linear
     *         between 0 and 1
     * @thesisOT
     */
    public double getSolarPotential() {
        double lengthWeight = Config.solarPotentialLengthWeight;
        double sunnyWeight = Config.solarPotentialSunnyWeight;
        double warmWeight = Config.solarPotentialWarmWeight;
        double weightSum = lengthWeight + sunnyWeight + warmWeight;

        double dayLengthProportion = getDayLength() / 1000
                / WeatherConditions.longestDayInSeconds;
        WeatherConditions.logger
                .trace("WeatherConditions: solarPotential considering dayLength:    "
                        + dayLengthProportion);
        if (dayLengthProportion > 1) {
            throw new RuntimeException("dayLengthProportion > 1");
        }

        double sunny = 1 - wow.getClouds();
        WeatherConditions.logger
                .trace("WeatherConditions: solarPotential considering clouds:       "
                        + sunny);
        if (sunny > 1) {
            throw new RuntimeException("sunny > 1");
        }
        
        

        // TODO: Ask directly about the temperature
        double warm = getAcNecessity(20, 10); // -10 <-> +30
        WeatherConditions.logger
                .trace("WeatherConditions: solarPotential considering the temp:     "
                        + warm);
        if (warm > 1) {
            throw new RuntimeException("warm > 1");
        }

        double solarPotential = (dayLengthProportion * lengthWeight + sunny
                * sunnyWeight + warm * warmWeight)
                / weightSum;
        WeatherConditions.logger
                .trace("WeatherConditions: final solarPotential:                *S* "
                        + solarPotential);

        return solarPotential;
    }

    /**
     * @return forecasted potential for local wind energy generation, linear
     *         between 0 and 1, using default maxSpeed
     * @thesisOT
     */
    public double getWindPotential() {
        return getWindPotential(WeatherConditions.maxSpeedDefault);
    }

    /**
     * @param maxSpeedDefault
     *            the maximum wind speed in mps, used for scaling the potential,
     *            cannot be zero
     * @return forecasted potential for local wind energy generation, linear
     *         between 0 and 1
     * @thesisOT
     */
    public double getWindPotential(double maxSpeed) {
        if (maxSpeed == 0) {
            return -2;
        }
        double speed = wow.getSpeed();

        if (speed > maxSpeed) {
            speed = maxSpeed;
        }

        return speed / maxSpeed;
    }

    /**
     * @return forecasted necessity of air conditioning (cooling or heating),
     *         linear between 1 (full cooling) and 0 (full heating), using
     *         default tempMargin and optimalTemp
     * @thesisOT
     */
    public double getAcNecessity() {
        return getAcNecessity(WeatherConditions.tempMarginDefault,
                WeatherConditions.optimalTempDefault);
    }

    /**
     * @param tempMargin
     *            the temperature margin in ��C around optimalTemp, used for
     *            scaling the necessity, cannot be zero
     * @param optimalTemp
     *            the target AC temperature in ��C, used for scaling the
     *            necessity
     * @return forecasted necessity of air conditioning (cooling or heating),
     *         linear between 1 (full cooling) and 0 (full heating)
     * @thesisOT
     */
    public double getAcNecessity(double tempMargin, double optimalTemp) {
        if (tempMargin == 0) {
            throw new IllegalArgumentException();
        }
        double temp = wow.getTempDay();

        // TODO: cooling is more expensive than heating, so it should have more
        // weight (currently, it's 50/50)
        if (temp > optimalTemp + tempMargin) {
            temp = optimalTemp + tempMargin;
        } else if (temp < optimalTemp - tempMargin) {
            temp = optimalTemp - tempMargin;
        }

        double acN = ((temp - optimalTemp) / tempMargin + 1) / 2;
        WeatherConditions.logger.trace("WeatherConditions: AC Necessity: "
                + acN);
        return acN;
    }

    public Date getWeatherDate() {
        return weatherDate;
    }

    public void setWeatherDate(Date weatherDate) {
        this.weatherDate = weatherDate;
        wow.setWeatherDate(weatherDate);
    }

    public void terminate() {
        WeatherConditions.logger.trace("WeatherConditions: terminating");
        wow.terminate();
    }

}

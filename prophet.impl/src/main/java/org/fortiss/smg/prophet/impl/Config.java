package org.fortiss.smg.prophet.impl;

import java.util.Date;

import org.fortiss.smg.prophet.components.smgCalendar.SmgCalendarUtils;

/**
 * This file contains the configuration of the forecast subsystem
 * 
 * @author Orest Tarasiuk
 * @thesisOT
 * 
 */
public class Config {

    /*
     * Location
     */
    /**
     * location to be used for the openweathermap.org API forecast
     * 
     * @thesisOT
     */
    public static final String locationCity = "Munich,de";

    /*
     * Calendar
     */
    /**
     * ISC URL, indicates the room that is to be analyzed
     * 
     * @thesisOT
     */
    public static final String icsUrlStr = "https://merkur.fortiss.org/home/dijkstra/calendar";
    /**
     * calendar refresh interval; default: 0.5 h
     * 
     * @thesisOT
     */
    public static final int calendarRefreshIntervalMs = 1000 * 60 * 30;
    /**
     * max room utilization; default: 10.0 h
     * 
     * @thesisOT
     */
    public static final long maxRoomDuration = 10 * 60;

    /*
     * Consumption
     */
    /**
     * maximum allowed discrepancy between the utilization and TES based
     * forecast, in Wh; used as a sanity check
     * 
     * @thesisOT
     */
    public static final double consumptionMaxDiscrepancyWh = 50000;
    // weights for forecasting the consumption
    /**
     * impact of room utilization
     * 
     * @thesisOT
     */
    public static final double consumptionWeightUtil = 10;
    /**
     * impact of TES long-term forecast
     * 
     * @thesisOT
     */
    public static final double consumptionWeightTes = 5;

    /*
     * Energy Pricing
     */
    // weights for forecasting energy prices
    /**
     * impact of solar generation potential (supply)
     * 
     * @thesisOT
     */
    public static final double enerpricingWeightSolar = 10;
    /**
     * impact of wind generation potential (supply)
     * 
     * @thesisOT
     */
    public static final double enerpricingWeightWind = 5;
    /**
     * impact of AC necessity (demand)
     * 
     * @thesisOT
     */
    public static final double enerpricingWeightAc = 20;

    /*
     * Weather
     */
    // weights for forecasting the solar energy generation potential
    /**
     * impact of the length of the day (sunrise-sunset)
     * 
     * @thesisOT
     */
    public static final double solarPotentialLengthWeight = 2;
    /**
     * impact of the cloud density
     * 
     * @thesisOT
     */
    public static final double solarPotentialSunnyWeight = 2;
    /**
     * impact of the temperature
     * 
     * @thesisOT
     */
    public static final double solarPotentialWarmWeight = 1;
    // refresh interval
    /**
     * weather data refresh interval; default: 1.0 h
     * 
     * @thesisOT
     */
    public static final int weatherRefreshIntervalMs = 1000 * 60 * 60;

    /*
     * Date
     */
    /**
     * default date to be used for the forecasts
     * 
     * @thesisOT
     */
    public static final Date imminentForecastDate = SmgCalendarUtils
            .getNextDay(SmgCalendarUtils.getToday());

    /*
     * Battery
     */
    // prices influence when to (dis)charge the battery
    /**
     * the highest price considered to be low, in EUR
     * 
     * @thesisOT
     */
    public static final double priceThresholdLow = 0.20;
    /**
     * the lowest price considered to be high, in EUR
     * 
     * @thesisOT
     */
    public static final double priceThresholdHigh = 0.22;

    /*
     * Solar panel
     */
    // TODO: This is supposed to be the maximum generation capacity, not the
    // current generation; the field solarlogQ?GeneratorWerte is likely wrong
    /**
     * the database field that contains the maximum generation capacity of the
     * solar panel
     * 
     * @thesisOT
     */
    public static final String dbSolarGenerationCapacityField = "solarlogQ?GeneratorWerte";

}

package org.fortiss.smg.prophet.helper.weather;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.fortiss.smg.prophet.components.smgCalendar.SmgCalendarUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.neo4j.shell.util.json.JSONArray;
import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;
import org.slf4j.Logger;

/**
 * This class provides a wrapper to OpenWeatherMap used for fetching weather
 * forecasts for the local area
 * 
 * @author Orest Tarasiuk
 * @thesisOT
 * 
 */
public class WeatherOwmWrapper {
	
	/*
	 * OpenweatherData APPID API KEY
	 */
	private final String appid = "836eff57e29a562f6491e6a28e243b4e"; 

    private static Logger logger;
    private Date weatherDate, jsonWeatherDate;
    private String locationCity, jsonLocationCity;
    private double tempDay, speed, clouds;
    private JSONObject city, tomorrow, temp;
    private JSONArray list;
    private long dt;
    private WeatherOwmWrapperFetcher fetcher;
    public boolean fetcherActive;
	private long sunRise15;
	private long sunSet15;
	
	private List<Double> forecastFactorList;
	
	private WeatherOwmGenerationMapper mapper = new WeatherOwmGenerationMapper();

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
    WeatherOwmWrapper(Logger logger, Date date, String city,
            int refreshIntervalMs) {
        WeatherOwmWrapper.logger = logger;
        weatherDate = date;
        locationCity = city;
        fetchJsonWeather();
        fetcher = new WeatherOwmWrapperFetcher(this, refreshIntervalMs);
        fetcherActive = true;
        fetcher.start();
    }
    
    
    public WeatherOwmWrapper(Logger logger, Date date, String city) {
        WeatherOwmWrapper.logger = logger;
        weatherDate = date;
        locationCity = city;
        fetchJsonWeather();
    }

    /**
     * Get newest JSON weather data from OWM and store it in the wrapper
     * 
     * @thesisOT
     */
    public void fetchJsonWeather() {
        JSONObject weatherJson = null;

        int daysBetween = Days.daysBetween(
                new DateTime(SmgCalendarUtils.getToday()),
                new DateTime(weatherDate)).getDays();

        WeatherOwmWrapper.logger.trace("WeatherOwmWrapper: daysBetween: "
                + daysBetween);

        int dayCnt = daysBetween + 1; // get only today and tomorrow = 2
        if (dayCnt >= 14) {
            WeatherOwmWrapper.logger
                    .error("WeatherOwmWrapper: Error, OWM can't forecast more than 14 days into the future");
        } else {
            int dayIndex = daysBetween; // interested in tomorrow = 1
       
            String requestUrlStr = "http://api.openweathermap.org/data/2.5/forecast/daily?APPID="+appid+"&q="
                    + locationCity + "&mode=json&units=metric&cnt=" + dayCnt;

            try {
                URL requestUrl = new URL(requestUrlStr);
                Scanner scanner = new Scanner(requestUrl.openStream());
                String response = scanner.useDelimiter("\\Z").next();
                weatherJson = new JSONObject(response.toString());
                scanner.close();
                // System.out.println("JSON: " + weatherJson);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         
            if (weatherJson == null) {
                WeatherOwmWrapper.logger
                        .error("WeatherOwmWrapper: Error: weatherJson is null");
                return;
            }

            try {
                // location name
                city = weatherJson.getJSONObject("city");
                jsonLocationCity = city.getString("name");
                // System.out.println("Location: " + location);

                // owmDate
                list = weatherJson.getJSONArray("list");
                if (dayIndex > 0 && dayIndex <= list.length()) {
                // TODO: use date to choose an appropriate day
                tomorrow = list.getJSONObject(dayIndex);
                // System.out.println("Tomorrow: " + tomorrow);
                dt = tomorrow.getLong("dt");
                jsonWeatherDate = new Date(dt * 1000);
                // System.out.println("Date: " + owmDate);

                // daytime temperature
                temp = tomorrow.getJSONObject("temp");
                tempDay = temp.getDouble("day");
                WeatherOwmWrapper.logger.trace("WeatherOwmWrapper: Day Temp: "
                        + tempDay);

                // wind speed and clouds
                speed = tomorrow.getDouble("speed");
                WeatherOwmWrapper.logger
                        .trace("WeatherOwmWrapper: Wind Speed [mps]: " + speed);
                clouds = tomorrow.getDouble("clouds");
                WeatherOwmWrapper.logger
                        .trace("WeatherOwmWrapper: Clouds [%]: " + clouds);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
    
    
    public void fetchJsonWeather(String url) {
        JSONObject weatherJson = null;

        String requestUrlStr = "http://api.openweathermap.org/data/2.5/"+url+"&mode=json&units=metric&APPID="+appid+"";

        //requestUrlStr = "http://localhost/forecast.json" ;
        
            try {
                URL requestUrl = new URL(requestUrlStr);
                Scanner scanner = new Scanner(requestUrl.openStream());
                String response = scanner.useDelimiter("\\Z").next();
                weatherJson = new JSONObject(response.toString());
                scanner.close();
                // System.out.println("JSON: " + weatherJson);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         
            if (weatherJson == null) {
                WeatherOwmWrapper.logger
                        .error("WeatherOwmWrapper: Error: weatherJson is null");
                return;
            }

            try {
            	// weather call to get sunrise and sun set
            	
            	if (url.startsWith("weather")) {
            		            		
            		this.setSunRise15(weatherJson.getJSONObject("sys").getLong("sunrise")*1000+(24*60*60*1000));
            		this.setSunSet15(weatherJson.getJSONObject("sys").getLong("sunset")*1000+(24*60*60*1000));
            		
            	}
            	else if (url.startsWith("forecast")) {
            		
            	
            		JSONArray weatherforecast = weatherJson.getJSONArray("list");
            		
            		Calendar calendar = Calendar.getInstance();
                	calendar.setTime(new Date(this.getSunRise15()));
                	calendar.set(Calendar.HOUR, 0);
                	calendar.set(Calendar.MINUTE, 0);
                	calendar.set(Calendar.SECOND, 0);
                	calendar.set(Calendar.MILLISECOND, 0);
            		
            		
            		long time = calendar.getTimeInMillis();
            		int cnt = 0;
            		forecastFactorList = new ArrayList<Double>();
            		for (int i = 0; i < 96; i++ ) {
            			
            			while (weatherforecast.getJSONObject(cnt).getLong("dt")*1000 < time-(2*60*60*1000)) {
            				cnt++;
            			}
            			
            			
            			if (time < this.getSunRise15()) {
            				forecastFactorList.add(0.0);

            			}
            			else if (time <= this.getSunSet15()) {
            			// weather id in element 3
            						
            			JSONArray weather = weatherforecast.getJSONObject(cnt).getJSONArray("weather");
            				//System.out.println(mapper.getGenerationFactor(weather.getJSONObject(0).getInt("id")) + " " + weather);
            			//cloud date in element 4
            			JSONObject clouds = weatherforecast.getJSONObject(cnt).getJSONObject("clouds");
            				//System.out.println(mapper.getGenerationFactor(weather.getJSONObject(0).getInt("id"),clouds.getInt("all")) + " " + clouds);
            				//double factor = calculateGenerationFactor(weatherforecast.getJSONArray(2).getInt(0))
            			
            			
            			forecastFactorList.add(mapper.getGenerationFactor(weather.getJSONObject(0).getInt("id"),clouds.getInt("all")));
            			}
            			else {
            				//System.out.println(weatherforecast.getJSONObject(cnt).getLong("dt")*1000 +" <= " + this.getSunSet15());
            				forecastFactorList.add(0.0);
            			}
            			
            			//System.out.println(new Date(time) + " vs. " + new Date(weatherforecast.getJSONObject(cnt).getLong("dt")*1000)+ " " + i);
            			time = time + 15*60*1000;
            			if (time >= weatherforecast.getJSONObject(cnt).getLong("dt")*1000 + (3*60*60*1000)) {
                			cnt++;
                		}
            		}
            		
            		System.out.println(forecastFactorList.size() +" "+ forecastFactorList);
            		
            	}
            	
                
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        

    }
    

    public List<Double> getForecastFactorList() {
		return forecastFactorList;
	}


	public void terminate() {
        fetcherActive = false;
    }

    /**
     * @return sunrise
     * @thesisOT
     */
    public Date getSunRise() {
        // TODO: As of August 2013, OWM doesn't provide sunrise/sunset forecasts
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(weatherDate);
        c.set(java.util.Calendar.HOUR_OF_DAY, 7);
        c.set(java.util.Calendar.MINUTE, 13);
        return c.getTime();
    }
    
    public long getSunRise15() {
    
    	return this.sunRise15;
    
    }
    
    public long getSunSet15() {
        
    	return this.sunSet15;
    
    }

    public void setSunRise15(long rise) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date(rise));

    	int unroundedMinutes = calendar.get(Calendar.MINUTE);
    	int mod = unroundedMinutes % 15;
    	calendar.add(Calendar.MINUTE, mod < 8 ? -mod : (15-mod));
    	// because of the PV directions/orientation production starts 0h45 later
    	calendar.add(Calendar.MINUTE, 45);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	this.sunRise15 = calendar.getTimeInMillis();
    
    }
    
    public void setSunSet15(long set) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(new Date(set));

    	int unroundedMinutes = calendar.get(Calendar.MINUTE);
    	int mod = unroundedMinutes % 15;
    	calendar.add(Calendar.MINUTE, mod < 8 ? -mod : (15-mod));
    	// because of the PV directions/orientation production stops 0h45 earlier
    	calendar.add(Calendar.MINUTE, -45);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	this.sunSet15 = calendar.getTimeInMillis();;
    
    }
    
    /**
     * @return sunset
     * @thesisOT
     */
    public Date getSunSet() {
        // TODO: As of August 2013, OWM doesn't provide sunrise/sunset forecasts
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.setTime(weatherDate);
        c.set(java.util.Calendar.HOUR_OF_DAY, 20);
        c.set(java.util.Calendar.MINUTE, 42);
        return c.getTime();
    }

    /**
     * @return day temperature
     * @thesisOT
     */
    public double getTempDay() {
        return tempDay;
    }

    /**
     * @return speed
     * @thesisOT
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return cloud density
     * @thesisOT
     */
    public double getClouds() {
        return clouds / 100;
    }

    /**
     * @return forecast date
     * @thesisOT
     */
    public Date getWeatherDate() {
        return weatherDate;
    }

    /**
     * @param weatherDate
     *            new forecast date
     * @thesisOT
     */
    public void setWeatherDate(Date weatherDate) {
        this.weatherDate = weatherDate;
        fetchJsonWeather();
    }

    /**
     * @return forecast date extracted from the received JSON
     * @thesisOT
     */
    public Date getJsonWeatherDate() {
        return jsonWeatherDate;
    }

    /**
     * @return forecast location
     * @thesisOT
     */
    public String getLocationCity() {
        return locationCity;
    }

    /**
     * @param city
     *            new forecast location
     * @thesisOT
     */
    public void setLocationCIty(String city) {
        locationCity = city;
        fetchJsonWeather();
    }

    /**
     * @return forecast location extracted from the received JSON
     * @thesisOT
     */
    public String getJsonLocationCity() {
        return jsonLocationCity;
    }

}

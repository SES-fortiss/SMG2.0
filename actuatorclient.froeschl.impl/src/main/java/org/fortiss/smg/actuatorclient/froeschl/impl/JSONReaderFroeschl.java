package org.fortiss.smg.actuatorclient.froeschl.impl;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.concurrent.TimeoutException;

import org.slf4j.LoggerFactory; 
import org.apache.commons.codec.binary.Base64;
import org.neo4j.shell.util.json.JSONArray;
import org.neo4j.shell.util.json.JSONException;
import org.neo4j.shell.util.json.JSONObject;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.DoublePoint;
import org.fortiss.smg.informationbroker.api.InformationBrokerInterface;
import org.fortiss.smg.analyzer.api.AnalyzerInterface;

public class JSONReaderFroeschl {
	

	private int dailyEnergyConsumption = 0;
	private List<DoublePoint> lastDailyValue;
	private int referenceCounter = 0;
	private long start_current_day;
	private long end_current_day;
	private DeviceId dailyValueContainer = new DeviceId("FroeschlDailyEnergyConsumption", "froeschl.wrapper");
	
	
	private InformationBrokerInterface broker;

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(JSONReaderFroeschl.class);
	private Calendar lastEventTime;

	private List<String[]> eventDataList = new ArrayList<String[]>();
	private JSONObject froeschlJson;
	List<String[]> results;
	private ContainerManagerInterface container;
	private DeviceId deviceid;
	private AnalyzerInterface analyzer;
	
	public JSONReaderFroeschl() {
		// TODO Auto-generated constructor stub
		lastEventTime = new GregorianCalendar();
		lastEventTime.add(Calendar.HOUR, -1); // server is 1 hr slower than the current time.
		lastEventTime.add(Calendar.HOUR, -1); // to solve summer time difference between server and realtime
	}
	
	public JSONReaderFroeschl(InformationBrokerInterface broker) {
		this.broker = broker;
	}
	
	public JSONReaderFroeschl(ContainerManagerInterface container){
		this.container = container;
	}
	
	public JSONReaderFroeschl(AnalyzerInterface analyzer){
		this.analyzer = analyzer;
	}
	
	public JSONReaderFroeschl(DeviceId deviceid){
		this.deviceid = deviceid;
	}
	
	public Calendar getLastEventString() {
		return lastEventTime;
	}

	public void setLastEventString(Calendar lastEventString) {
		this.lastEventTime = lastEventString;
	}

	public List<String[]> getEventDataList() {
		return eventDataList;
	}

	public void setEventDataList(List<String[]> eventDataList) {
		this.eventDataList = eventDataList;
	}

/**
 *  @param requestUrlStr TODO
 * @return
 *  	[0] = Actual Consumption
 *  	[1] = Actual Consumption Unit
 *  	[2] = aktueller Zaehlerstand
 *  	[3] = aktueller Zaehlerstand Unit
 *      [4] = Tagesverbrauch
 *      [5] = Tagesverbrauch Unit

 */
    public String[] readJsonFroeschl(String requestUrlStr)  {

			String[] results;
			//String requestUrlStr = "http://192.168.21.214/GetMeasuredValue.cgi";
			String username = "user";
			String password = "user";
			JSONObject froeschlJson = null;
			JSONObject froeschlJsonDevice=null;
			JSONArray froeschlJsonArray = null; 
			results = null;
		    try {
		    	Authenticator.setDefault(new MyAuthenticator(username, password));
		    	URL requestUrl = new URL(requestUrlStr);
	
		    	Scanner scanner = new Scanner(requestUrl.openStream());
		        String response = scanner.useDelimiter("\\Z").next();
		        froeschlJson = new JSONObject(response.toString());
		        scanner.close();
		    	}
		    	catch (Exception e) {
		    		logger.info("JSONReaderFroeschl could not connect to the parsing webpage " +requestUrlStr+ " " + e);
			}
			
		    try {
		    	if (froeschlJson != null) {
			    	results = new String[6];
					froeschlJsonArray = (JSONArray) froeschlJson.get("entry");
					String froeschlTimestamp = froeschlJsonArray.getJSONObject(0).getString("timestamp");
					
					froeschlJsonArray = (JSONArray) froeschlJsonArray.getJSONObject(0).get("periodEntries");
					
					for(int i=0; i<froeschlJsonArray.length();i++ )
					{
						//Actual Value
						if(froeschlJsonArray.getJSONObject(i).getString("obis").contains("1.7.0"))	{
							results[0] = froeschlJsonArray.getJSONObject(i).getString("value");
							results[1] = froeschlJsonArray.getJSONObject(i).getString("unit");
						}
						//Aggregated Value
						if(froeschlJsonArray.getJSONObject(i).getString("obis").contains("1.8.0"))	{
							results[2] = froeschlJsonArray.getJSONObject(i).getString("value");
							results[3] = froeschlJsonArray.getJSONObject(i).getString("unit");
						}
						
						//Aggregated Daily Value
						if(froeschlJsonArray.getJSONObject(i).getString("obis").contains("1.8.0"))	{
							//results[4] = froeschlJsonArray.getJSONObject(i).getString("value");
							results[5] = froeschlJsonArray.getJSONObject(i).getString("unit");
							//initialize dates after start of system
							if (dailyEnergyConsumption == 0 && referenceCounter == 0){
																	
									try {
										start_current_day = getStartOfDayInMillis(froeschlTimestamp)/1000;
										end_current_day = getEndOfDayInMillis(froeschlTimestamp)/1000;
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									
									//check if there is a last value in the database for the day the system is started (after a reboot for example)
									//if there is already a last Seen value of that day in the database, take this as reference counter, else start a new one
									Map<Long,Double> map = new HashMap<Long, Double>();
									map = broker.getLastseen("FroeschlDailyEnergyConsumption", "");
									Iterator it= map.entrySet().iterator();
									Map.Entry pair = (Map.Entry) it.next();
									long lastSeen = (Long) pair.getKey();
									
								    System.out.println("############### Last Seen ###############" + lastSeen);
									if ((start_current_day <= lastSeen) && (lastSeen < end_current_day )){
										referenceCounter =  (Integer) pair.getValue();
									}
									else {
										referenceCounter = Integer.parseInt(results[2]);
									}
									
									//referenceCounter = Integer.parseInt(results[2]);
									
									DeviceId dev = new DeviceId("FroeschlDailyEnergyConsumption","froeschl.wrapper"); 
									System.out.println("############### dates ###############" + "start day " + start_current_day + "time " + froeschlTimestamp + "end_current_day " + end_current_day);
									//DoublePoint[] lv = getLatestValueFromDB(1,1,dailyValueContainer);
									//System.out.println("############### get Latest Values ###############" + lv);

								
							}
							// calculate the energy consumption for the current day
							if ((start_current_day <= Long.parseLong(froeschlTimestamp)) && (Long.parseLong(froeschlTimestamp) < end_current_day)){
								System.out.println("############### dates ###############" + "start day " + start_current_day + "time " + froeschlTimestamp + "end_current_day " + end_current_day + "reference Counter = " + referenceCounter);
								//current daily consumption is calculated from the overall consumption (which is always updated) minus the consumption at the start of the system/day
								dailyEnergyConsumption = Integer.parseInt(results[2]) - referenceCounter;
								results[4] = Integer.toString(dailyEnergyConsumption);
								System.out.println("############### daily Energy Consumption ###############" + results[4]);
								  
								//System.out.println("############### LastValue Produced ###############" + dailyValueContainer);
								//lastDailyValue = getLatestValueFromDB(start_current_day, end_current_day-1, dailyValueContainer );
								//System.out.println("############### LastValue Produced ###############" + lastDailyValue);
							}
							
							// if a new day starts, reset values and start again
							else if (Long.parseLong(froeschlTimestamp) > end_current_day){
								referenceCounter = 0;
								dailyEnergyConsumption = 0;
							}
						}
						
					}
					
					System.out.println("############### JSON Data parsed[Froeschl] ###############");

		    	}
		    	else {
		    		logger.debug("Froeschl URL connection Failed");
		    	}
				return results;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		    
			return new String[4];
    }
	
    static class MyAuthenticator extends Authenticator {
        private String username, password;

        public MyAuthenticator(String user, String pass) {
          username = user;
          password = pass;
        }

        protected PasswordAuthentication getPasswordAuthentication() {
        	String promptString = getRequestingPrompt();
            System.out.println(promptString);
            String hostname = getRequestingHost();
            System.out.println(hostname);
            InetAddress ipaddr = getRequestingSite();
            System.out.println(ipaddr);
            int port = getRequestingPort();
        	
        	
        	
        	return new PasswordAuthentication(username, password.toCharArray());
        }
      }
    /**
     * @param date the date in the format "yyyy-MM-dd"
     */
    public long getStartOfDayInMillis(String dayDate) throws ParseException {
    	//convert timestamps: unix time in String to Unix time in long
    	long temp = Long.parseLong(dayDate);
		System.out.println("############### timestamp ###############" + dayDate);
		//calculate Date from Unix timestamp
		Date convertDayDate = new java.util.Date((long)temp*1000);
		//DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		//System.out.println("############### timestamp in date ###############" + df.format(froeschlDateTime));
		
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	String stringDate = format.format(convertDayDate);
        Calendar calendar = Calendar.getInstance();
        System.out.println("############### timestamp in help method ###############" + stringDate);
        calendar.setTime(format.parse(stringDate));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * @param date the date in the format "yyyy-MM-dd"
     */
    public long getEndOfDayInMillis(String date) throws ParseException {
        //Add one day's time to the beginning of the day.
        //24 hours * 60 minutes * 60 seconds * 1000 milliseconds = 1 day
        return getStartOfDayInMillis(date) + (24 * 60 * 60 * 1000);
    }
    
    public DoublePoint[] getLatestValueFromDB(long startDay, long endDay, DeviceId device){
    	try {
    		System.out.println("############### Values ###############" + endDay + device.toString());
    		List<DoublePoint> lastDailyValue; 
    		lastDailyValue = broker.getDoubleValue(device, startDay, endDay);
    		
    	}
    	catch (TimeoutException e) {
			logger.warn("Timeout: No connection to database. "
					+ e.getMessage());
			e.printStackTrace();
			
		}
    	DoublePoint[] toArr = new DoublePoint[lastDailyValue.size()];
		
		DoublePoint[] resultArr = lastDailyValue.toArray(toArr);
		return resultArr;
    }
    
}

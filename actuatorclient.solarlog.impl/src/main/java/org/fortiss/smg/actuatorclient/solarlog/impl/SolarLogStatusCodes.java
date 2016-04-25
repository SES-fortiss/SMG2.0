package org.fortiss.smg.actuatorclient.solarlog.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeoutException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;


public class SolarLogStatusCodes {
	
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(SolarLooper.class);
	private static ActuatorClientImpl impl;
	static String codelist = "0-Erst.Einsch.,1-Warte Spannung,2-Warte Ausschlt,3-Konstantspann.,4-MPP,5-MPP,6-Warten,7-Warten,8-Relaistest,9-Fehlersuchbetr,11-Leistungsbegr,15-Nachtabschalt,20- ,25-Test L-Elektr.,26-Test Netzrelais,40-Schneeschmelzen,56- ,60-UDC &Uuml;ber,61-Power-Control,62-Inselbetrieb,63-Freq. Reduz.,64-IAC Max.,74-Ext. Blindlstg. Anford.,75-Selbsttest in Arbeit,76-Warten auf Wind,77-DC-Trennschalter,79-Isolationsmessung,107-&Uuml;bersp.schutz &uuml;berpr&uuml;fen,??? unknown, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , ,10-&Uuml;bertemp.Absc,12-&Uuml;berlastabsch,13-&Uuml;berspannab,14-Netzausfall,16-Betriebshemm.,17-Abschaltung Powador-protect,18-AFI Abschalt.,19-Iso zu gering,21-Schutzabschaltung PV-Str. 1,22-Schutzabschaltung PV-Str. 2,23-Schutzabschaltung PV-Str. 3,24-Fehler DSP,27-Erweiterter Selbsttest,28-HW-Fehler,29-Erdschluss DC,30-Fehler Messt,31-Fehler AFI-M,32-Fehler ST,33-Fehler DC,34-Fehler Komm.,35-Schutzabsch. SW,36-Schutzabsch. HW,37-Unbekannte HW ,38-Fehler PV-&Uuml;b,39-Temperatursensor defekt ,41-Netz U Unter L1,42-Netz U &Uuml;ber L1,43-Netz U Unter L2,44-Netz U &Uuml;ber L2,45-Netz U Unter L3,46-Netz U &Uuml;ber L3,47-Netz U Au&szlig;en,48-Netz F Unter,49-Netz F &Uuml;ber,50-Netz U Mittel,51-Netz MU UnterL1,52-Netz MU &Uuml;ber L1,53-Netz MU UnterL2,54-Netz MU &Uuml;ber L2,55-Fehler Zwischen,57-Warte Wiederein,58-Steuerk. T &Uuml;ber,59-Fehler Selbstt.,65-Fehler ROCOF,66-Fehler Plausib.,67-Fehler Leistungsteil 1,68-Fehler Leistungsteil 2,69-Fehler Leistungsteil 3,70-Fehler L&uuml;fter1 ,71-Fehler L&uuml;fter 2,72-Fehler L&uuml;fter 3,73-Fehler Inselnetzbetrieb,78-Fehlerstrom zu hoch,80-Isol.mess. nicht m&ouml;glich,81-Absch. Netzspg. L1,82-Absch. Netzspg. L2,83-Absch. Netzspg. L3,84-Absch. Unterspg. ZK,85-Absch. &Uuml;berspg. ZK,86-Absch. Unsymmetrie ZK,87-Absch. &Uuml;berstrom L1,88-Absch. &Uuml;berstrom L2,89-Absch. &Uuml;berstrom L3,90-Absch. Einbruch 5V,91-Absch. Einbruch 2.5V,92-Absch. Einbruch 1.5V,93-Fehler Selbsttest B1,94-Fehler Selbsttest B2,95-Fehler Selbsttest R1,96-Fehler Selbsttest R2,97-Absch. HW &Uuml;berstrom,98-Absch. HW Gate-Treiber,99-Absch. HW Buffer-Frei,100-Absch. HW &Uuml;beremp.,101-Fehler Plausi Temp.Sens.,102-Fehler Plausi Wirkungsgrad,103-Fehler Plausi Spannung,104-Fehler Plausi AFI-Modul,105-Fehler Plausi Relais-Spg,106-Fehler Plausi DC/DC,108-Krit. &Uuml;berspannung L1,109-Krit. &Uuml;berspannung L2,110-Krit. &Uuml;berspannung L3,111-Krit. Unterspannung L1,112-Krit. Unterspannung L2,113-Krit. Unterspannung L3,114-Kommunik. DC/DC-Wandler,115-Negativer PV-Strom 1,116-Negativer PV-Strom 2,117-Negativer PV-Strom 3,118-PV-&Uuml;berspannung 1,119-PV-&Uuml;berspannung 2,120-PV-&Uuml;berspannung 3";
	static String[] statusCode = codelist.split(","); 
	
	JSONObject valuesOfWR = null;
	
	public static String getStatusText(int number) {
		if (number == 255) {
			return "Offline";
		}
		else if (number <=153 && number >= 0) {
			return statusCode[number];
		}
		else {
			return "StatusCode " + number + " unknown";
		}
		
	}
	
	
/*	public static void main(String[] args) {
		
		int count = 0; 
		
		for (String code : statusCode) {
			System.out.println(count + " " + getStatusText(count));
			count++;
			
		}
		
	}
	*/
	
//	public static String getStatus(){
//		
//		try{
//			String url = impl.getHost()+"/getjp";
//			URL obj = new URL(url);
//			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//	 
//			//add reuqest header
//			con.setRequestMethod("POST");
//	 
//			String urlParameters = "{\"801\":{\"170\":null}}";
//			
//		}catch(FileNotFoundException fe){
//			logger.error("SolarLogConnector: runStatusCode: SolarLog Connection - File not found ", fe);
//		}catch(IOException e1){
//			logger.error("SolarLogConnector: runStatusCode: SolarLog Connection - IO Error ", e1);
//		}catch(TimeoutException e){
//			 logger.error("timeout sending to master", e);
//		}
//		catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return "";
//	}
	
}

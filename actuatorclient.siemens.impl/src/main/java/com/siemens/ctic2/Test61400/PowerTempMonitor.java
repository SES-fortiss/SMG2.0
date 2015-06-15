/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package com.siemens.ctic2.Test61400;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.swing.JFrame;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

import ch.iec._61400.ews._1.AssociateRequest;
import ch.iec._61400.ews._1.AssociateResponse;
import ch.iec._61400.ews._1.GetDataValuesRequest;
import ch.iec._61400.ews._1.GetDataValuesResponse;
import ch.iec._61400.ews._1.IECXMLService;
import ch.iec._61400.ews._1.TDataAttributeValue;
import ch.iec._61400.ews._1.TFC;
import ch.iec._61400.ews._1.TFcdFcdaType;
import ch.iec._61400.ews._1.TServiceError;
import ch.iec._61400.ews._1_0.ServicePortType;

import com.siemens.ctic2.iec61850.AnalogueValue;
import com.siemens.ctic2.iec61850.Vector;

public class PowerTempMonitor {
	private ServicePortType service;
	private DataPanel voltage;
	private DataPanel current;
	private DataPanel frequency;
	private DataPanel power;
	private DataPanel temperature;
	private static final boolean useTemp = false;

	private DecimalFormat fmt = new DecimalFormat("##0.000");
	private String host;
	private int port;
	private String associd;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PowerTempMonitor self = new PowerTempMonitor();
		self.start();
	}

	private void start() {
		HostDialog dialog = new HostDialog("Select host");
		dialog.setVisible(true);
		host = dialog.getHost();
		port = dialog.getPort();
		System.out.println("Connection to " + host + ":" + port);

		JFrame frame = new JFrame("PowerMonitor");
		frame.setSize(400, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container content = frame.getContentPane();
		content.setBackground(Color.darkGray);
		content.setLayout(new GridLayout(2, 2));

		voltage = new DataPanel("Voltage", new String[] { "A    % V",
				"B    % V", "C    % V" });
		current = new DataPanel("Current", new String[] { "A    % A",
				"B    % A", "C    % A" });
		frequency = new DataPanel("Frequency", new String[] { "", "% Hz", "" });
		power = new DataPanel("Power",
				new String[] { "%   W", "%  VA", "% VAr" });
		temperature = new DataPanel("Temperature", new String[] { "", "% C",
				"" });

		content.add(voltage);
		content.add(current);
		content.add(useTemp ? temperature : frequency);
		content.add(power);

		frame.setVisible(true);

		service = getService();

		if (service == null) {
			System.exit(1);
		}
		
		associd = associate();

		Timer timer = new Timer();
		timer.schedule(new Updater(), 0, 5000);
	}

	private String associate() {
		AssociateRequest req = new AssociateRequest();
		req.setMaxMessageSize(4096);
		req.setUserName("user");
		req.setPassword("passwd");
		req.setUUID(UUID.randomUUID().toString());
		
		AssociateResponse resp = service.associate(req);
		
		return resp.getAssocID();
	}
	
	private ServicePortType getService() {
		// create connection to Web Service
		URL wsdl;
		IECXMLService locator;

		try {
			wsdl = new URL("http://" + host + ":" + port + "/61400?wsdl");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return null;
		}

		ServicePortType srv;

		try {
			// WebServiceFeature feature = new FastInfosetFeature(true);
			locator = new IECXMLService(wsdl, new QName(
					"http://iec.ch/61400/ews/1.0/", "IECXMLService"));
			srv = locator.getIECXMLServicePort(/* feature */);
		} catch (WebServiceException e) {
			e.printStackTrace();
			return null;
		}

		return srv;
	}

	private class Updater extends TimerTask {

		@Override
		public void run() {
			// Build the request
			TFcdFcdaType ref = new TFcdFcdaType();
			ref.setRef("C1/MMXU1");
			ref.setFC(TFC.MX);
			GetDataValuesRequest request = new GetDataValuesRequest();
			request.setRef(ref);
			request.setUUID(UUID.randomUUID().toString());
			request.setAssocID(associd);

			// Call the Web Service and evaluate the response
			GetDataValuesResponse response;
			try {
				response = service.getDataValues(request);
			} catch (WebServiceException e1) {
				e1.printStackTrace();
				return;
			}

			if (response.getServiceError() != null
					&& response.getServiceError().size() > 0) {
				System.out.println("Error:");
				for (TServiceError error : response.getServiceError()) {
					System.out.println("  " + error.value());
				}
			} else {
				try {
					for (TDataAttributeValue attrVal : response
							.getDataAttrVal()) {
						String dataAttrRef = attrVal.getDataAttrRef();
						if (dataAttrRef.endsWith(".A.phsA.cVal")) {
							Vector v = new Vector(attrVal);
							current.setData(0, format(v.getMag().getF()));
						}
						if (dataAttrRef.endsWith(".A.phsB.cVal")) {
							Vector v = new Vector(attrVal);
							current.setData(1, format(v.getMag().getF()));
						}
						if (dataAttrRef.endsWith(".A.phsC.cVal")) {
							Vector v = new Vector(attrVal);
							current.setData(2, format(v.getMag().getF()));
						}

						if (dataAttrRef.endsWith(".PhV.phsA.cVal")) {
							Vector v = new Vector(attrVal);
							voltage.setData(0, format(v.getMag().getF()));
						}
						if (dataAttrRef.endsWith(".PhV.phsB.cVal")) {
							Vector v = new Vector(attrVal);
							voltage.setData(1, format(v.getMag().getF()));
						}
						if (dataAttrRef.endsWith(".PhV.phsC.cVal")) {
							Vector v = new Vector(attrVal);
							voltage.setData(2, format(v.getMag().getF()));
						}

						if (dataAttrRef.endsWith(".Hz.mag")) {
							AnalogueValue a = new AnalogueValue(attrVal);
							frequency.setData(1, format(a.getF()));
						}

						if (dataAttrRef.endsWith(".TotW.mag")) {
							AnalogueValue a = new AnalogueValue(attrVal);
							power.setData(0, format(a.getF()));
						}
						if (dataAttrRef.endsWith(".TotVA.mag")) {
							AnalogueValue a = new AnalogueValue(attrVal);
							power.setData(1, format(a.getF()));
						}
						if (dataAttrRef.endsWith(".TotVAr.mag")) {
							AnalogueValue a = new AnalogueValue(attrVal);
							power.setData(2, format(a.getF()));
						}
					}
				} catch (Exception e) {
				}
			}

			if (useTemp) {
				getTemp();
			}

		}

		private String format(Float f) {
			if (f != null) {
				return fmt.format(f);
			} else {
				return "--";
			}
		}

		private void getTemp() {
			// Build the request
			TFcdFcdaType ref = new TFcdFcdaType();
			ref.setRef("C1/MTMP1");
			ref.setFC(TFC.MX);
			GetDataValuesRequest request = new GetDataValuesRequest();
			request.setRef(ref);
			request.setUUID(UUID.randomUUID().toString());
			request.setAssocID("associd");

			// Call the Web Service and evaluate the response
			GetDataValuesResponse response;
			response = service.getDataValues(request);

			if (response.getServiceError() != null
					&& response.getServiceError().size() > 0) {
				System.out.println("Error:");
				for (TServiceError error : response.getServiceError()) {
					System.out.println("  " + error.value());
				}
			} else {
				try {
					for (TDataAttributeValue attrVal : response
							.getDataAttrVal()) {
						String dataAttrRef = attrVal.getDataAttrRef();
						if (dataAttrRef.endsWith(".Tmp.mag")) {
							Vector v = new Vector(attrVal);
							temperature.setData(1, format(v.getMag().getF()));
						}
					}
				} catch (Exception e) {
				}
			}
		}
	}
}

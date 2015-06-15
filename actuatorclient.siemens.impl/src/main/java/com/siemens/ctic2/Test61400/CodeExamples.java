/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package com.siemens.ctic2.Test61400;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

import com.siemens.ctic2.iec61850.ING;
import com.siemens.ctic2.iec61850.MV;

import ch.iec._61400.ews._1.AbortResponse;
import ch.iec._61400.ews._1.AddSubscriptionRequest;
import ch.iec._61400.ews._1.AddSubscriptionResponse;
import ch.iec._61400.ews._1.AssociateRequest;
import ch.iec._61400.ews._1.AssociateResponse;
import ch.iec._61400.ews._1.GetDataSetValuesRequest;
import ch.iec._61400.ews._1.GetDataSetValuesResponse;
import ch.iec._61400.ews._1.GetDataValuesRequest;
import ch.iec._61400.ews._1.GetDataValuesResponse;
import ch.iec._61400.ews._1.GetLogicalDeviceDirectoryRequest;
import ch.iec._61400.ews._1.GetLogicalDeviceDirectoryResponse;
import ch.iec._61400.ews._1.GetLogicalNodeDirectoryRequest;
import ch.iec._61400.ews._1.GetLogicalNodeDirectoryResponse;
import ch.iec._61400.ews._1.GetServerDirectoryRequest;
import ch.iec._61400.ews._1.GetServerDirectoryResponse;
import ch.iec._61400.ews._1.IECXMLService;
import ch.iec._61400.ews._1.ReleaseRequest;
import ch.iec._61400.ews._1.RemoveSubscriptionRequest;
import ch.iec._61400.ews._1.RemoveSubscriptionResponse;
import ch.iec._61400.ews._1.ReportRequest;
import ch.iec._61400.ews._1.ReportResponse;
import ch.iec._61400.ews._1.SetDataValuesRequest;
import ch.iec._61400.ews._1.SetDataValuesResponse;
import ch.iec._61400.ews._1.TDataAttributeValue;
import ch.iec._61400.ews._1.TFC;
import ch.iec._61400.ews._1.TFcdFcdaType;
import ch.iec._61400.ews._1.TObjectClass;
import ch.iec._61400.ews._1.TIEMcls;
import ch.iec._61400.ews._1.TRCBType;
import ch.iec._61400.ews._1.TReportFormat;
import ch.iec._61400.ews._1.TTrgCond;
import ch.iec._61400.ews._1_0.ServicePortType;

public class CodeExamples {
	private ServicePortType service;
	
	private String associate(String user, String pw) {
		AssociateRequest req = new AssociateRequest();
		req.setMaxMessageSize(4096);
		req.setUserName(user);
		req.setPassword(pw);
		req.setUUID(UUID.randomUUID().toString());
		
		AssociateResponse resp = service.associate(req);
		
		if (resp.getServiceError() != null) {
			// TODO error handling
		}
		
		return resp.getAssocID();
	}

	@SuppressWarnings("unused")
	private void release(String associd) {
		ReleaseRequest req = new ReleaseRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		
		AbortResponse resp = service.release(req);
	}
	
	@SuppressWarnings("unused")
	private List<String> getServerDirectory(String associd) {
		GetServerDirectoryRequest req = new GetServerDirectoryRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		req.setObjClass(TObjectClass.LD);
		
		GetServerDirectoryResponse resp = service.getServerDirectory(req);
		
		return resp.getLDRef();
	}
	
	@SuppressWarnings("unused")
	private List<String> getLogicalDeviceDirectory(String associd, String ld) {
		GetLogicalDeviceDirectoryRequest req = new GetLogicalDeviceDirectoryRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		req.setLDRef(ld);
		
		GetLogicalDeviceDirectoryResponse resp = service.getLogicalDeviceDirectory(req);
		
		return resp.getLNRef();
	}
	
	@SuppressWarnings("unused")
	private List<String> getLogicalNodeDirectory(String associd, String ln) {
		GetLogicalNodeDirectoryRequest req = new GetLogicalNodeDirectoryRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		req.setLNRef(ln);
		req.setIEMcls(TIEMcls.DATA);
		
		GetLogicalNodeDirectoryResponse resp = service.getLogicalNodeDirectory(req);
		
		return resp.getDATAname();
	}
	
	private List<TDataAttributeValue> getDataValues(String associd, String dataref, TFC fc) {
		GetDataValuesRequest req = new GetDataValuesRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		
		TFcdFcdaType ref = new TFcdFcdaType();
		ref.setRef(dataref);
		ref.setFC(fc);
		
		req.setRef(ref);
		
		GetDataValuesResponse resp = service.getDataValues(req);
		
		return resp.getDataAttrVal();
	}
	
	@SuppressWarnings("unused")
	private MV getMeasuredValue(String associd, String dataref) {
		// Helper method to extract a measured value from a
		// list of TDataAttributes

		List<TDataAttributeValue> data = getDataValues(associd, dataref, TFC.MX);
		return MV.extract(dataref, data);
	}
	
	@SuppressWarnings("unused")
	private void setDataValues(String associd, String dataref, TFC fc, List<TDataAttributeValue> data) {
		SetDataValuesRequest req = new SetDataValuesRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		TFcdFcdaType fcd = new TFcdFcdaType();
		fcd.setFC(fc);
		fcd.setRef(dataref);
		req.setRef(fcd);
		req.getDataAttrVal().addAll(data);
		
		SetDataValuesResponse resp = service.setDataValues(req);
	}
	
	private void setING(String associd, ING ing) {
		// Helper method to set an Integer setpoint
		List<TDataAttributeValue> data = new ArrayList<TDataAttributeValue>();
		String dataref = ing.getDataRef();
		ing.addTo(dataref, data);
		
		setDataValues(associd, dataref, TFC.SP, data);
	}
	
	@SuppressWarnings("unused")
	private List<TDataAttributeValue> getDataSetValues(String associd, String datasetref) {
		GetDataSetValuesRequest req = new GetDataSetValuesRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		req.setDSRef(datasetref);
		
		GetDataSetValuesResponse resp = service.getDataSetValues(req);
		
		return resp.getDataAttrVal();
	}

	@SuppressWarnings("unused")
	private void addSubscription(String associd, String rcbref, String rptid, String datasetref) {
		AddSubscriptionRequest req = new AddSubscriptionRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		req.setRCBRef(rcbref);
		req.setRCBType(TRCBType.BRCB);
		req.setRptID(rptid);
		req.setRptEna(true);
		
		TTrgCond trgCond = new TTrgCond();
		trgCond.setDchg(true);
		req.setTrgOp(trgCond);
		req.setDatSet(datasetref);
		
		AddSubscriptionResponse resp = service.addSubscription(req);
	}

	@SuppressWarnings("unused")
	private void removeSubscription(String associd, String rcbref) {
		RemoveSubscriptionRequest req = new RemoveSubscriptionRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		req.setRCBRef(rcbref);
		
		RemoveSubscriptionResponse resp = service.removeSubscription(req);
	}

	@SuppressWarnings("unused")
	private void report(String associd, String rcbref) {
		ReportRequest req = new ReportRequest();
		req.setAssocID(associd);
		req.setUUID(UUID.randomUUID().toString());
		
		DatatypeFactory datatypeFactory;
		
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		Duration maxRespTime = datatypeFactory.newDuration(60000);
		req.setMaxResponseTime(maxRespTime );
		
		Duration minRespTime = datatypeFactory.newDuration(1000);
		req.setMinResponseTime(minRespTime);
		
		while (true) {
			ReportResponse resp = service.report(req);
			
			List<TReportFormat> reports = resp.getReportFormat();
			
			for (TReportFormat report : reports) {
				// TODO handle the reported event
			}
			
			Duration minRequestTime = resp.getMinRequestTime();
			Date date = new Date();
			
			try {
				Thread.sleep(minRequestTime.getTimeInMillis(date));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ServicePortType getService() {
		// create connection to Web Service
		URL wsdl;
		IECXMLService locator;

		try {
			wsdl = new URL("http://192.168.2.215:8080/61400?wsdl");
//			wsdl = new URL("http://localhost:8080/61400?wsdl");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
			return null;
		}

		ServicePortType srv;

		try {
			locator = new IECXMLService(wsdl, new QName(
					"http://iec.ch/61400/ews/1.0/", "IECXMLService"));
			srv = locator.getIECXMLServicePort(/* feature */);
		} catch (WebServiceException e) {
			e.printStackTrace();
			return null;
		}

		return srv;
	}

	public static void main(String[] argv) {
		new CodeExamples().test();
	}
	
	private void test() {
		service = getService();
		String associd = associate("user", "Password");
		
		ING ing = new ING("C1/DRCT1.DERTyp");
		ing.setSetVal(102);
		
		setING(associd, ing);
		
		List<TDataAttributeValue> data = getDataValues(associd, "C1/DRCT1", TFC.SP);
		
		ING ing2 = ING.extract("C1/DRCT1.DERTyp", data);
		
		System.out.println("Typ=" + ing2.getSetVal());
	}
}

/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.rulesystem.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.drools.core.ClockType;
import org.fortiss.smg.actuatormaster.api.IActuatorListener;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.informationbroker.api.IDatabase;
import org.fortiss.smg.rulesystem.api.RuleSystemDBInterface;
import org.fortiss.smg.rulesystem.impl.RuleSystemDBImpl;
import org.fortiss.smg.rulesystem.api.RuleSystemDroolsInterface;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieRuntime;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.io.ResourceFactory;
import org.slf4j.Logger;
import org.fortiss.smg.rulesystem.api.RuleSystemDBInterface;


public class RuleSystemDroolsImpl implements RuleSystemDroolsInterface, IActuatorListener
{
	private Logger logger;
	String drlResourcePath;
	KieSession kSession;
	IDatabase database;
	RuleSystemDBImpl impl;
	
	public RuleSystemDroolsImpl(IDatabase database) {
		this.database = database;
		impl = new RuleSystemDBImpl(database);
	}
	
	private void readDrlFile() throws FileNotFoundException{
		//		KieServices ks = KieServices.Factory.get();
		// 	    KieContainer kContainer = ks.getKieClasspathContainer();
		//     	KieSession kSession = kContainer.newKieSession("ksession-rules");

		File file = new File("/Users/pragyagupta/Projects/SMG_GIT/smg2/rulesystem.impl/src/main/resources/rules.drl");
		boolean readStatus = file.canRead();
		System.out.println(file.getAbsolutePath());
		kSession = createKieSession("/Users/pragyagupta/Projects/SMG_GIT/smg2/rulesystem.impl/src/main/resources/rules.drl");
	}
	
	@Override
	public boolean loadRulesFromDB() {
		try {
//			RuleSystemDBImpl impl  = new RuleSystemDBImpl(database);
			List<Map<String, Object>> ruleList = impl.getAllRulesFromDB();
//			System.out.println(ruleList.size());			
			if ( ruleList.size()>0)
				return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	@Override
	public boolean isComponentAlive() throws TimeoutException {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
     * Inserts all the object into the KieRuntime and returns a Map with a
     * relationship between each object and their corresponding FactHandle
     * 
     * @param runtime
     * @param objects
     * @return a Map with a relationship between each object and their
     *         corresponding FactHandle
     */
    public static Map<Object, FactHandle> insertAll(KieRuntime runtime, Object... objects) {
        Map<Object, FactHandle> factHandles = new HashMap<Object, FactHandle>(objects.length);
        for (Object o : objects) {
            FactHandle factHandle = runtime.insert(o);
            factHandles.put(o, factHandle);
        }
        return factHandles;
    }

    /**
     * Creates a new KieSession (Stateful) that will be used for the rules. Its
     * KieBase contains the drl files sent by parameter.
     * 
     * @return the new KieSession
     */
    public static KieSession createKieSession(String drlResourcesPath) {
        KieServices ks = KieServices.Factory.get();
        KieContainer kcontainer = createKieContainer(ks, drlResourcesPath);

        // Configure and create the KieBase
        KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
        KieBase kbase = kcontainer.newKieBase(kbconf);

        // Configure and create the KieSession
        KieSessionConfiguration ksconf = ks.newKieSessionConfiguration();
        
        ksconf.setOption( ClockTypeOption.get(ClockType.REALTIME_CLOCK.getId()));
        
        System.out.println("returning from createKieSession method");
        
        return kbase.newKieSession(ksconf, null);
    }
    /**
     * Creates a new KieContainer, which will include a KieModule with the DRL
     * files sent as parameter
     * 
     * @param ks
     *            KieServices
     * @param drlResourcesPaths
     *            DRL files that will be included
     * @return the new KieContainer
     */
    private static KieContainer createKieContainer(KieServices ks, String drlResourcesPaths) {
        // Create the in-memory File System and add the resources files to it
        KieFileSystem kfs = ks.newKieFileSystem();
        
        // in case of reading multiple drl files
        
//        for (String path : drlResourcesPaths) {
        	String path = drlResourcesPaths;
        	System.out.println(path);
            kfs.write(ResourceFactory.newFileResource(path));
            
//        }
     
        // Create the builder for the resources of the File System
        KieBuilder kbuilder = ks.newKieBuilder(kfs);
        // Build the Kie Bases
        kbuilder.buildAll();
        // Check for errors
        if (kbuilder.getResults().hasMessages(Level.ERROR)) {
        	System.out.println(kbuilder.getResults().toString());
            throw new IllegalArgumentException(kbuilder.getResults().toString());
        }
        // Get the Release ID (mvn style: groupId, artifactId,version)
        ReleaseId relId = kbuilder.getKieModule().getReleaseId();
        
        System.out.println("returning from createKieContainer method");
        
        // Create the Container, wrapping the KieModule with the given ReleaseId
        return ks.newKieContainer(relId);
        
    }

    /**
     * Disposes the KieSession sent as parameter
     * 
     * @param session
     */
    public static void dispose(KieSession session) {
        if (session != null) {
            session.dispose();
        }
    }
    

	@Override
	public boolean addRule(String rule) {
		boolean executed = false;
		try {
			Message message = new Message("Hello World",Message.HELLO );
            kSession.insert(message);
            kSession.fireAllRules();
            executed= true;

		} catch (Exception e) {
            logger.debug("exception"+ e);
        }
		return executed;
		
	}
	
	@Override
	public void modifyRule(String rulename, String ruleString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeRule(String rule) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDoubleEventReceived(DoubleEvent ev, DeviceId dev, String client) {
		
		String containerID = dev.getDevid();
		double value = ev.getValue();
		double maxAbsError = ev.getMaxAbsError();
		Events eventObj = new Events (containerID, value, maxAbsError);
		kSession.insert( eventObj);
		kSession.fireAllRules();
		
//		System.out.println("Event value "+ ev.getValue()+ " from DeviceId " +dev.getDevid()+ " WrapperId " +dev.getWrapperId());
		
	}

	@Override
	public void onDeviceEventReceived(DeviceEvent ev, String client) {
		// TODO Auto-generated method stub
		
	}

	
	
	

}

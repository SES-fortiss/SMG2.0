package org.fortiss.smg.rulesystem.impl.executor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.drools.core.ClockType;
import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorListener;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.informationbroker.api.IDatabase;		
import org.fortiss.smg.rulesystem.impl.persistor.RuleSystemDBImpl;
import org.fortiss.smg.rulesystem.api.Events;
import org.fortiss.smg.rulesystem.api.Rule;
import org.fortiss.smg.rulesystem.api.RuleSystemDroolsInterface;
import org.fortiss.smg.smgschemas.commands.*;
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
import org.slf4j.LoggerFactory;


public class RuleSystemDroolsImpl implements RuleSystemDroolsInterface, IActuatorListener
{
	private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger( RuleSystemDroolsImpl.class );
	String drlResourcePath;
	static KieSession kSession;
	IDatabase database;
	RuleSystemDBImpl impl;
	IActuatorClient actuatorClient ;
	DoubleCommand dblCommand ;
	ContainerManagerInterface containerManagerClient;
	Commander commander = new Commander();
	SendEmail mail = new SendEmail();
	
	public static String drlResourcesPath = "/opt/felix/drools.drl";
	

	public RuleSystemDroolsImpl(IDatabase database) {
		logger = LoggerFactory
				.getLogger(RuleSystemDroolsImpl.class);
		this.database = database;
		impl = new RuleSystemDBImpl(database);
		
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			drlResourcesPath = "c:/opt/felix/drools.drl";
		}
		
		kSession = createKieSession();
		
		/*
		 * Setting the value of globals declared in .drl file
		 */
		kSession.setGlobal("logger", logger);
		kSession.setGlobal("actuatorClient", actuatorClient);
		kSession.setGlobal("dblCommand",dblCommand );
		kSession.setGlobal("commander", commander);
		kSession.setGlobal("mail", mail);
		logger.info("##############Logging works");
		
	}

	//	private void readDrlFile() throws FileNotFoundException{
	//		//		KieServices ks = KieServices.Factory.get();
	//		// 	    KieContainer kContainer = ks.getKieClasspathContainer();
	//		//     	KieSession kSession = kContainer.newKieSession("ksession-rules");
	//
	//		File file = new File("src/main/resources/rules.drl");
	//		boolean readStatus = file.canRead();
	//		System.out.println(file.getAbsolutePath());
	//		kSession = createKieSession("src/main/resources/rules.drl");
	//	}

	//	@Override
	//	public boolean loadRulesFromDB() {
	//		try {
	////			RuleSystemDBImpl impl  = new RuleSystemDBImpl(database);
	//			List<Map<String, Object>> ruleList = impl.getAllRulesFromDB();
	////			System.out.println(ruleList.size());			
	//			if ( ruleList.size()>0)
	//				return true;
	//		} catch (Exception e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		
	//		return false;
	//	}

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
//	public static KieSession createKieSession() {
//		KieServices ks = KieServices.Factory.get();
//		KieContainer kc = ks.getKieClasspathContainer();
//		KieSession kieS = kc.newKieSession();
//		System.out.println("returning from createKieContainer method");
//		return kieS;
//	}
	
	public static KieSession createKieSession() {
		KieServices ks = KieServices.Factory.get();
		KieContainer kcontainer = createKieContainer(ks);

		// Configure and create the KieBase
		KieBaseConfiguration kbconf = ks.newKieBaseConfiguration();
		KieBase kbase = kcontainer.newKieBase(kbconf);

		// Configure and create the KieSession
		KieSessionConfiguration ksconf = ks.newKieSessionConfiguration();

		ksconf.setOption( ClockTypeOption.get(ClockType.REALTIME_CLOCK.getId()));

		logger.info("##############Logging works");
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
	private static KieContainer createKieContainer(KieServices ks) {
		// Create the in-memory File System and add the resources files to it
		KieFileSystem kfs = ks.newKieFileSystem();

		// in case of reading multiple drl files

		//        for (String path : drlResourcesPaths) {
		//        	String path = drlResourcesPath;
		logger.info("##############Logging works");
		System.out.println(drlResourcesPath);
		kfs.write(ResourceFactory.newFileResource(drlResourcesPath));

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
		logger.info("##############Logging works");
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


	public static void execute() {
		logger.info("##############Logging works");
		kSession.getAgenda().getAgendaGroup("active").setFocus();
		int result = kSession.fireAllRules();
		
		System.out.println ( "Fire all rules: " + result);
//		kSession.fireAllRules(new RuleNameEqualsAgendaFilter("active", false));
	
//		kSession.dispose();
	}
	

	public static boolean addRule(Rule rule) {

//				String body = "rule " + "\"" + rule.getName() + "\"" +
//						" when " + rule.getRuleCondition() +
//						" then " + rule.getConsequence() + ";" +
//						" end";
		try {
			//			Message message = new Message(body, Message.counter++);
			//            kSession.insert(message);
		
			kSession.insert(rule.toDRL());
		
//			kSession.fireAllRules();
			return true;
		} catch (Exception e) {
			e.setStackTrace(null);
			logger.debug("exception"+ e);
		}
		return false;	
	}


	public static boolean modifyRule(Rule rule) {
		String ruleName = rule.getName();
		if(removeRule(ruleName) && addRule(rule))
			return true;
		return false;

	}	


	public static boolean removeRule(String ruleName) {
		try{
		
			FactHandle fact = kSession.getFactHandle(ruleName);
			kSession.delete(fact);	
			return true;
		} catch (Exception e) {
			e.setStackTrace(null);
			logger.debug("exception"+ e);
		}
		return false;
	}

	@Override
	public void onDoubleEventReceived(DoubleEvent ev, DeviceId dev, String client) {
		logger.info("##############Logging is working");
		System.out.println("RuleSystemDroolsImpl:: onDoubleEventReceived method");
		String containerID = dev.getDevid();
		double value = ev.getValue();
		double maxAbsError = ev.getMaxAbsError();
		Events eventObj = new Events (containerID, value, maxAbsError);
		kSession.insert( eventObj);
		execute();
//		kSession.fireAllRules();
		System.out.println("Event value "+ ev.getValue()+ " from DeviceId " +dev.getDevid()+ " WrapperId " +dev.getWrapperId());

	}

	@Override
	public void onDeviceEventReceived(DeviceEvent ev, String client) {
		// TODO Auto-generated method stub

	}

}

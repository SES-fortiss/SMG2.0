package org.fortiss.smg.rulesystem.impl.executor;

import java.awt.Toolkit;
import java.util.Iterator;
import java.util.Scanner;

import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
//import org.fortiss.smg.recommender.impl.persistor.Persistor;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Commander {
	private Logger logger = LoggerFactory
			.getLogger(Commander.class);
	DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
			ContainerManagerInterface.class,
			ContainerManagerQueueNames
					.getContainerManagerInterfaceQueryQueue(), 50000);
	ContainerManagerInterface containerManagerClient;
	/**
	 * 
	 */
	public Commander() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setDeviceValue(String devId, String wrapperId, double val ) throws Exception{
		
		DoubleCommand command = new DoubleCommand(val);
		System.out.println ( "command: "+ command.getValue());
		logger.info("Command value: " + command.getValue());
		
		DeviceId id = new DeviceId(devId, wrapperId);
		System.out.println ("wrapperID: "+ id.getWrapperId()+ "devID: "+ id.getDevid());
		logger.info("wrapperID: "+ id.getWrapperId()+ "devID: "+ id.getDevid());

		containerManagerClient = clientInfo.init();
		containerManagerClient.sendCommand(new DoubleCommand (command.getValue()), new DeviceId (devId, wrapperId));
		System.out.println("COMMAND:: issued to Device :: value " + command.getValue() + " sent to " + id.getDevid());


	}
	public void setContainerValue(String containerId, String type, double val ) throws Exception{
		
		System.out.println ( "containerid: " +containerId);
		
		SIDeviceType.fromString(type);
		System.out.println ( "SIDeviceType: " + SIDeviceType.fromString(type));
		
		DoubleCommand command = new DoubleCommand(val);
		System.out.println ( "command: "+ command.getValue());
		logger.info("Command value: " + command.getValue());
		
		containerManagerClient = clientInfo.init();		
//		containerManagerClient.sendCommandToContainer(new DoubleCommand(command.getValue()), containerId, SIDeviceType.fromString(type));
		System.out.println("COMMAND:: issued to Container :: value " + command.getValue() + " :: sent to container " 
				+ containerId+ ":: SIDeviceType "+ SIDeviceType.fromString(type));
	}
	
	public void doSomething(){
		System.out.println ( "Something must be done!!");
	}
	
	public void beep() {
		for (int i = 0; i < 100; i++) {
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	public void recommender(String devId , String wrapperId, double value, String condition) {
		System.out.println("Send command to the device?\n");
		//Read from console.
		Scanner scanner = new Scanner(System.in);
		String answer = scanner.next();
		if (answer.startsWith("y")) {
			//Send a command to devid with value.
			try {
				setDeviceValue(devId, wrapperId, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
//			Persisitor.updateRuleStatus(condition, "accepted"); 
		} else {
//			Persisitor.updateRuleStatus(condition, "rejected");
		}
		scanner.close();
	}

}

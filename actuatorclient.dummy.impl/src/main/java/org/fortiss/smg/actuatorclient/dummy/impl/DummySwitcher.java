package org.fortiss.smg.actuatorclient.dummy.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.LoggerFactory;

public class DummySwitcher implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(DummySwitcher.class);

	private ActuatorClientImpl actuatorClientImpl;
	private double lastDoubleCommand = 0.0; // off

	public DummySwitcher(ActuatorClientImpl actuatorClientImpl) {
		this.actuatorClientImpl = actuatorClientImpl;
	}

	
	public void setCommand(Double command) {
		this.lastDoubleCommand = command;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		double switcher = Math.random();

		if (switcher >= 0.5) {
			
			//usually only the dummy switch should be used
			//here we also switch between grid connected and island mode
			
			double connected = this.actuatorClientImpl.gridconnected.getStatus();
			if (connected > 0) {
				this.actuatorClientImpl.gridconnected.setStatus(0.0);
			}
			else {
				this.actuatorClientImpl.gridconnected.setStatus(1.0);
			}

			
			DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
					ContainerManagerInterface.class,
					ContainerManagerQueueNames
							.getContainerManagerInterfaceQueryQueue(), 50000);
			
			logger.info("try to init CM interface");
			ContainerManagerInterface containerManagerClient = null;
			try {
				containerManagerClient = clientInfo.init();
				// Check what type of device it is through parsing
				double newcommand = 1.0;
//				if (actuatorClientImpl.switcher.getStatus() == 1.0) {
//					newcommand = 0.0;
//
//				}

				DoubleCommand command = new DoubleCommand(newcommand);
				logger.info("Send Command " + command.getValue()
						+ " to Device "
						+ actuatorClientImpl.devices.get(4).getDeviceId());

				logger.debug("got connection to CM ? " + containerManagerClient.isComponentAlive());
				DeviceId devId = actuatorClientImpl.devices.get(4).getDeviceId();
								
			
				containerManagerClient.sendCommand(command ,devId);
				logger.info("Command sent " + command.getValue() + " to ContainerManager");
				

			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				clientInfo.destroy();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			logger.info("Not sending command this round");
		}
	}

}

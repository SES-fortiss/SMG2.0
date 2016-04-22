package org.fortiss.smg.actuatorclient.dummy.impl;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.dummy.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.ContainerManagerInterface;
import org.fortiss.smg.containermanager.api.ContainerManagerQueueNames;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.containermanager.api.devices.SIDeviceType;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.LoggerFactory;

public class DummyLooper implements Runnable {

	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(DummyLooper.class);

	/*
	 * global consumption 10k 50k with light
	 */
	private double consumtion_global_wo_light = 10000.0;
	private double consumtion_global_w_light = 50000.0;
	private double chargerate = 5000.0;

	private ActuatorClientImpl impl;
	private DeviceId origin;
	private DoubleEvent ev;

	private double consumption = 0.0;
	private double generation = 0.0;
	private double batterypercentage = 1.0; // 1 = 100%
	private double lightson = 0.0;

	DummyLooper(ActuatorClientImpl impl) {
		this.impl = impl;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		readDummyValues();
	}

	private void readDummyValues() {
		try {

			/*
			 * read values from devices (enocean)
			 */
//			DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
//					ContainerManagerInterface.class,
//					ContainerManagerQueueNames
//							.getContainerManagerInterfaceQueryQueue(), 5000);
//			ContainerManagerInterface containerManagerClient = null;

			try {
				//containerManagerClient = clientInfo.init();
				lightson = impl.containerManagerClient.getMeanByType(
						"enoceanUSB.wrapper.PowerPlug", SIDeviceType.Powerplug);
				if (lightson == Double.NaN) {
					lightson = 0.0;

				}
				logger.info("Lightstatus is " + lightson);

//			} catch (IOException e) {
//				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			try {
//				clientInfo.destroy();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

			// also add a random value to generation 
			generation = impl.generation.getStatus() + 5000*Math.random();
			batterypercentage = impl.getBatteryPercentage();

			/*
			 * Consumption (for 5 instances) in connected mode: battery full if
			 * light on 50k if light off 10k
			 * 
			 * battery not full if light on 100k if light off 60k
			 * 
			 * minus PV generation 100% = 80k if in grid and battery < 100 %
			 * 80k-100k (light on/off)
			 */
			
			
			/* Example for grid connected/disconnected
			
			if (impl.gridconnected.getStatus() == 1.0
					&& impl.energyfrombatteryrequest.getStatus() == 0.0) {
				if (batterypercentage == 1.0) {
					if (lightson == 0.0) {
						consumption = consumtion_global_wo_light;
					} else {
						consumption = consumtion_global_w_light;
					}
				} else {
					
					if ((impl.getBattery() + chargerate)
							/ impl.batterycapacitymax > 1.0) {
						chargerate = impl.batterycapacitymax
								- impl.getBattery();
					}
					if (lightson == 0.0) {
						consumption = consumtion_global_wo_light + chargerate;
					} else {
						consumption = consumtion_global_w_light + chargerate;
					}

					impl.setBattery(impl.getBattery() + chargerate);
					batterypercentage = impl.getBattery()
							/ impl.batterycapacitymax;
				}

			} else if (impl.energyfrombatteryrequest.getStatus() == 1.0) {
				if (batterypercentage >= 0.5) {
					generation = impl.generation.getStatus() + chargerate;
					impl.setBattery(impl.getBattery() - chargerate);
					batterypercentage = impl.getBattery()
							/ impl.batterycapacitymax;
				}
				else {
					impl.energyfrombatteryrequest.setStatus(0.0);
					
				}

			} else if (impl.gridconnected.getStatus() == 0.0) {
				
//				  not in grid connected mode
//				  
//				  energy requests are not allowed
				 

				impl.energyfrombatteryrequest.setStatus(0.0);

				if (lightson == 0.0) {
					consumption = consumtion_global_wo_light;
				} else {
					consumption = consumtion_global_w_light;
				}

				if (consumption <= impl.getBattery() ) {
//					
//					  Switch off light
//					 
					impl.setBattery(impl.getBattery() - consumption/8.0);
					batterypercentage = impl.getBattery()
							/ impl.batterycapacitymax;

				} 
				else if (consumption > impl.getBattery() * 0.50  && lightson == 1.0) {
					lightOff();
					impl.setBattery(impl.getBattery() - consumption/8.0);
					batterypercentage = impl.getBattery()
							/ impl.batterycapacitymax;
				}
				else {
					impl.gridconnected.setStatus(1.0);
				}

			}
			*/
			//here we add a random value to the consumption
			double consumptionVariance = Math.random()*10000;
			
		
			/*
			 * Energy from Array
			 */
			double[] arrayEnergy = impl.getEnergyvalues();
			
			consumption = consumption - consumptionVariance;			
			
			if (lightson == 0.0) {
				consumption = (arrayEnergy[0]/4.0)-540;
			} else {
				consumption = arrayEnergy[0]/4.0;
			}
			
			impl.setConsumption(consumption);
			origin = impl.getDeviceSpecs().get(0).getDeviceId();
			if (impl.gridconnected.getStatus() == 0.0) {
				ev = new DoubleEvent(0.0);
			} else {
				ev = new DoubleEvent(consumption);
			}
			impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
			logger.info("DummyDevice: run(): getEventHandler - new Event from "
					+ origin + " value (c) " + consumption);

			//impl.setGeneration(generation);
			origin = impl.getDeviceSpecs().get(1).getDeviceId();
			/* Grid example
			if (impl.gridconnected.getStatus() == 0.0) {
				ev = new DoubleEvent(0.0);
			} else {
				ev = new DoubleEvent(generation);
			}
			*/
			generation = (arrayEnergy[1]/4.0)*4.5;
			ev = new DoubleEvent(generation);
			impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
			logger.info("DummyDevice: run(): getEventHandler - new Event from "
					+ origin + " value (g) " + generation);

			batterypercentage = batterypercentage + (generation/100 - consumption/100);
			logger.debug("Battery : " + batterypercentage);
			
			
			if (batterypercentage > 250) {
				batterypercentage = 250;
			}
			else if (batterypercentage < 0) {
				batterypercentage = 0;
			}
			else {
				
			}
				
			if ( 80 > batterypercentage  && lightson == 1.0) {
				lightOff();
			}
			
			
			impl.setBatteryPercentage(batterypercentage);
			
			
			origin = impl.getDeviceSpecs().get(2).getDeviceId();
			ev = new DoubleEvent(batterypercentage * 100.0);
			impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
			logger.info("DummyDevice: run(): getEventHandler - new Event from "
					+ origin + " value " + batterypercentage * 100);

		} catch (TimeoutException e) {
			logger.error("timeout sending to master", e);
		}
	}

	private void lightOff() {
//		DefaultProxy<ContainerManagerInterface> clientInfo = new DefaultProxy<ContainerManagerInterface>(
//				ContainerManagerInterface.class,
//				ContainerManagerQueueNames
//						.getContainerManagerInterfaceQueryQueue(), 5000);
//		ContainerManagerInterface containerManagerClient = null;

		try {
			//containerManagerClient = clientInfo.init();
			this.impl.containerManagerClient.sendCommand(new DoubleCommand(0.0),
					new DeviceId("PowerPlug", "enoceanUSB.wrapper"));

			if (lightson == Double.NaN) {
				lightson = 0.0;

			}
			logger.debug("Lightstatus is " + lightson);

//		} catch (IOException e) {
//			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		try {
//			clientInfo.destroy();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

	}
}

package org.fortiss.smg.actuatormaster.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.fortiss.smg.actuatormaster.api.IActuatorClient;
import org.fortiss.smg.actuatormaster.api.IActuatorListener;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.actuatormaster.api.events.DeviceEvent;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.ambulance.api.HealthCheck;
import org.fortiss.smg.config.lib.WrapperConfig;
import org.fortiss.smg.config.lib.WrapperConfigManager;
import org.fortiss.smg.containermanager.api.devices.DeviceContainer;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.fortiss.smg.smgschemas.commands.DoubleCommand;
import org.slf4j.LoggerFactory;

public class ActuatorMasterImpl implements IActuatorMaster {

	private static final int LASTEVENTSTOSTORE = 50;

	private static final int TIMEOUTSHORT = 2000;
	private static final int TIMEOUTLONG = 5000;

	private ConcurrentHashMap<String, String> clients;
	private ConcurrentHashMap<String, Integer> notRespondingClients;

	private SecureRandom random = new SecureRandom();
	private ConcurrentHashMap<String, String> listeners;
	private ArrayDeque<Triple<DeviceId, DoubleEvent, Long>> latestEvents;
	private Map<String, ArrayList<DeviceContainer>> registeredDevices;
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ActuatorMasterImpl.class);

	private static ExecutorService executors; // = Executors.newFixedThreadPool(512);

	static WrapperConfigManager wrapperConfigManager = WrapperConfigManager
			.getInstance();

	public ActuatorMasterImpl(String configpath) {

		clients = new ConcurrentHashMap<String, String>();
		notRespondingClients = new ConcurrentHashMap<String, Integer>();
		listeners = new ConcurrentHashMap<String, String>();
		latestEvents = new ArrayDeque<Triple<DeviceId, DoubleEvent, Long>>();
		registeredDevices = new ConcurrentHashMap<String, ArrayList<DeviceContainer>>();
		//executors = Executors.newFixedThreadPool(256);
		executors = Executors.newCachedThreadPool();
		wrapperConfigManager.setConfigPath(configpath);
//		wrapperConfigManager.setConfigPath("/Users/sajjad/opt/felix/wrapperconfig.xml");
	}

	@Override
	public synchronized boolean isComponentAlive() throws TimeoutException {
		
		
		Set<String> availableClients = clients.keySet();
		
		String loggingoutput = "";
		for (String clientQueue : availableClients) {
			loggingoutput = loggingoutput + clients.get(clientQueue) + "(" + clientQueue + ") ";
			
		}
		
		logger.debug("Available clients/queues: " + loggingoutput);

		if (clients.isEmpty()) {
			return true;
		}
		boolean bFlag = true;
		// call all clients

		/**
		 * check if queue is working:
		 */
		for (String queue : availableClients) {
			boolean recover = false;

			DefaultProxy<HealthCheck> healthCheck = new DefaultProxy<HealthCheck>(
					HealthCheck.class, queue, TIMEOUTLONG);
			try {
				if (healthCheck != null) {
				HealthCheck proxy = healthCheck.init();
				if (proxy != null) {
				proxy.isComponentAlive();
				if (notRespondingClients.containsKey(queue)) {
					notRespondingClients.remove(queue);
				}
				}
				}
			} catch (IOException e) {
				logger.info("Healthcheck for " + queue
						+ " failed. No connection");
				recover = checkNotRespondingClients(queue);
				bFlag = false;
			} catch (TimeoutException e) {
				logger.info("Healthcheck for " + queue
						+ " failed. Timeout exceded");
				recover = checkNotRespondingClients(queue);
				bFlag = false;
			} catch (NullPointerException e) {
				logger.info("Healthcheck for " + queue + " failed. Nullpointer");
				recover = checkNotRespondingClients(queue);
				bFlag = false;
			}
				try {
					healthCheck.destroy();
				} catch (IOException e) {
					logger.info("Unable to close con. for queue:" + queue);
					bFlag = false;
				}
			if (bFlag == false && recover == true) {
				/**
				 * Restore wrapper config of faulty component (actuatorclient)
				 * and remove it from the clients
				 */
				logger.debug("try to recover config for: " + clients.get(queue));
				try {
					String recoverConfig = clients.get(queue);
					wrapperConfigManager.recoverConfig(recoverConfig);
					clients.remove(queue);
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					logger.debug("ERROR: Could not remove wrapper config and/or actuatorclient");
					e.printStackTrace();
				}
			}
		}
		return bFlag;
	}

	private boolean checkNotRespondingClients(String queue) {
		boolean remove = false;
		logger.info("Not responding Clients: " + notRespondingClients.keySet()
				+ " " + notRespondingClients.contains(queue));
		if (notRespondingClients.containsKey(queue)) {
			if (notRespondingClients.get(queue) < 2) {
				logger.debug("Heathcheck failed 2nd time: "
						+ clients.get(queue));
				notRespondingClients.put(queue, 2);
			} else {
				logger.debug("Heathcheck failed 3rd time start recover: "
						+ clients.get(queue));
				notRespondingClients.remove(queue);
				remove = true;
			}
		} else {
			logger.debug("Heathcheck failed 1st time: " + clients.get(queue));
			notRespondingClients.put(queue, 1);
		}
		return remove;
	}

	private String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	@Override
	public String registerClient(String name) throws TimeoutException {
		String id = "";
		do {
			id = nextSessionId();
		} while (clients.containsKey(id));
		clients.put(id, name);
		logger.debug("Client: " + name + "(" + id + ") logged on @Master.");
		return id;
	}

	@Override
	public boolean isRegisteredClient(String clientName)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return (clients.containsValue(clientName));
	}

	@Override
	public void registerListener(String clientName, String queueName)
			throws TimeoutException {
		listeners.put(queueName, clientName);
		logger.debug("Client: " + clientName + "(" + queueName
				+ ") logged on @Master/Listener.");
	}

	@Override
	public boolean isRegisteredListener(String clientName, String queueName)
			throws TimeoutException {
		// TODO Auto-generated method stub
		return (listeners.containsKey(queueName) && listeners
				.containsValue(clientName));

	}

	@Override
	public void sendDoubleCommand(DoubleCommand command, DeviceId dev)
			throws TimeoutException {
		logger.debug("Doublecommand " + command.getValue() + " from:"
				+ dev.getDevid() + "- Wrapper: " + dev.getWrapperId());
		for (String queue : clients.keySet()) {
			DefaultProxy<IActuatorClient> actClient = new DefaultProxy<IActuatorClient>(
					IActuatorClient.class, "" + queue, TIMEOUTSHORT);
			try {
				IActuatorClient proxy = actClient.init();
				proxy.onDoubleCommand(command, dev);

			} catch (TimeoutException e) {
				logger.info("Timeout for " + queue + ".");
			} catch (IOException e) {
				logger.info("No conection to " + queue + ".");

			} 
			finally {
				try {
					actClient.destroy();
				} catch (IOException e) {
					logger.info("Unable to close con. for queue:" + queue);
				}
			}
		}
	}

	@Override
	public void sendDoubleEvent(final DoubleEvent ev, final DeviceId dev,
			final String client) throws TimeoutException {
		
		logger.info("Doubleevent " + ev.getValue() + " from:" + dev.getDevid()
				+ ", Wrapper: " + dev.getWrapperId());

		for (final String queue : listeners.keySet()) {
			Runnable task = new Runnable() {

				@Override
				public void run() {
					DefaultProxy<IActuatorListener> listenerProxy = new DefaultProxy<IActuatorListener>(
							IActuatorListener.class, queue, TIMEOUTLONG);
					try {
						IActuatorListener proxy = listenerProxy.init();
						proxy.onDoubleEventReceived(ev, dev, client);
					} catch (IOException e) {
						logger.info("No conection to " + queue + ".");

					} catch (TimeoutException e) {
						logger.info("Timeout for " + queue + ".");
					}
					try {
							listenerProxy.destroy();
							

					} catch (IOException e) {
							logger.info("Unable to close con. for queue:"
									+ queue);
					}
					try {
						this.finalize();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};

			// Executors.newCachedThreadPool().submit(task);
			executors.submit(task);

			Triple<DeviceId, DoubleEvent, Long> trip = new MutableTriple<DeviceId, DoubleEvent, Long>(
					dev, ev, new Date().getTime());
			latestEvents.add(trip);

			// remove old events
			while (latestEvents.size() > LASTEVENTSTOSTORE) {
				latestEvents.removeLast();
			}

		}
	}

	@Override
	public void sendDeviceEvent(final DeviceEvent devEvent, final String client)
			throws TimeoutException {

		DeviceContainer ev = devEvent.getValue();

		// TODO Auto-generated method stub
		logger.info("Received Devicespecs from " + ev.getDeviceId());

		/*
		 * Store Registered Devices by ClientID in a DeviceContainer-Array
		 */
		if (!registeredDevices.containsKey(client)) {
			ArrayList<DeviceContainer> tmp = new ArrayList<DeviceContainer>();
			tmp.add(ev);
			registeredDevices.put(client, tmp);
		} else {
			if (!registeredDevices.get(client).contains(ev)) {
				registeredDevices.get(client).add(ev);
			}
		}

		for (final String queue : listeners.keySet()) {
			Runnable task = new Runnable() {

				@Override
				public void run() {
					DefaultProxy<IActuatorListener> listenerProxy = new DefaultProxy<IActuatorListener>(
							IActuatorListener.class, queue, TIMEOUTLONG);
					try {
						IActuatorListener proxy = listenerProxy.init();
						proxy.onDeviceEventReceived(devEvent, client);
					} catch (IOException e) {
						logger.info("No conection to " + queue + ".");

					} catch (TimeoutException e) {
						logger.info("Timeout for " + queue + ".");
					} 
					
						try {
							listenerProxy.destroy();
							this.finalize();
							

						} catch (IOException e) {
							logger.info("Unable to close con. for queue:"
									+ queue);
						} catch (Throwable e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
				}
			};

			// Executors.newCachedThreadPool().submit(task);
			executors.submit(task);
		}
	}

	@Override
	public ArrayList<Triple<DeviceId, DoubleEvent, Long>> getLatestNumber(
			int number) throws TimeoutException {

		ArrayList<Triple<DeviceId, DoubleEvent, Long>> events = new ArrayList<Triple<DeviceId, DoubleEvent, Long>>(
				10);
		Iterator<Triple<DeviceId, DoubleEvent, Long>> nextEvent = latestEvents
				.iterator();
		for (int i = 0; i < 10; i++) {
			if (!nextEvent.hasNext()) {
				break;
			}
			events.add(nextEvent.next());
		}
		return events;
	}

	@Override
	public void resendRegisteredDevices() throws TimeoutException {
		// TODO Auto-generated method stub
		if (!registeredDevices.isEmpty()) {
			for (String clientID : registeredDevices.keySet()) {
				for (DeviceContainer deviceContainer : registeredDevices
						.get(clientID)) {
					sendDeviceEvent(new DeviceEvent(deviceContainer), clientID);
				}
			}
		}
	}

	@Override
	public ArrayList<WrapperConfig> getWrapperConfig(String key)
			throws TimeoutException {
		if (wrapperConfigManager != null) {
			logger.info("requesting config for key: " + key);
			return wrapperConfigManager.getConfig(key);
		} else {
			return new ArrayList<WrapperConfig>();
		}
	}

}

package org.fortiss.smg.actuatorclient.solarlog.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.solarlog.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatormaster.api.AbstractClient;
import org.fortiss.smg.actuatormaster.api.ActuatorMasterQueueNames;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.config.lib.WrapperConfig;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



public class TestActuatorClientSimple extends AbstractClient  {

	private static org.fortiss.smg.testing.MockOtherBundles mocker;
	private ActuatorClientImpl implClient;
	IActuatorMaster master4config = null;
	private List<ActuatorClientImpl> clients = new ArrayList<ActuatorClientImpl>();

	@Before
	public void setUp() throws Exception{
		ArrayList<String> bundles = new ArrayList<String>();
		
		bundles.add("Ambulance");
		bundles.add("InformationBroker");
		bundles.add("ActuatorMaster");
		bundles.add("ContainerManager");
		
		
		try {
			mocker = new org.fortiss.smg.testing.MockOtherBundles(bundles);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// register here your services etc.
				// DO NOT start heavy operations here - use threads
				/*
				 * Try to connect to Master to get the wrapper's config file
				 */
				ArrayList<WrapperConfig> configList = new ArrayList<WrapperConfig>();

				DefaultProxy<IActuatorMaster> proxyMaster = new DefaultProxy<IActuatorMaster>(
						IActuatorMaster.class,
						ActuatorMasterQueueNames.getActuatorMasterInterfaceQueue(),
						5000);
				try {
					master4config = proxyMaster.init();
				} catch (TimeoutException e) {
					System.out.println("ActuatorClient: Unable to connect to master (Timeout).");
				}

				/*
				 * If we have connection try to get the wrapper's config file
				 */
				if (master4config != null) {
					try {
						configList = master4config.getWrapperConfig("solarlog");
					} catch (TimeoutException e) {
						System.out.println("ActuatorClient: Unable to connect to master (Timeout).");
					} finally {
						try {
							proxyMaster.destroy();
						} catch (IOException e) {
							System.out.println("Unable to close con. for queue:"
									+ this.clientId);
						}
					}

					/*
					 * For each received wrapper config instance (possibly the same
					 * wrapper is used for multiple (physical) devices
					 */
					if (configList.size() > 0) {
						for (WrapperConfig config : configList) {

							final String clientKey = config.getKey();
							final String clientIDextension = config.getHost();

							implClient = new ActuatorClientImpl(config.getProtocol()+"://"+config.getHost(),
									config.getPort(), config.getWrapperID(),
									config.getPollingfrequency(), config.getUsername(),
									config.getPassword());
							// Register at Actuator Master (self, human readable name
							// for
							// device)
							registerAsClientAtServer(implClient,
									config.getWrapperName(), new IOnConnectListener() {

										@Override
										public void onSuccessFullConnection() {
											implClient.setMaster(master);
											implClient.setClientId(clientId);
											implClient.activate();
											System.out.println("ActuatorClient[" + clientKey
													+ "-" + clientIDextension
													+ "] is alive");
											clients.add(implClient);

										}
									});

						}
						System.out.println("Solarlog Wrapper started");
					} else {
						System.out.println("No Configuration available");
					}
				} else {
					proxyMaster.destroy();
					System.out.println("Solarlog Wrapper could not read config from Master");
					//this.stop(mocker.getContext());
				}
	}

	@After
	public void tearDown() {
		// TODO do some cleanup
	}

	@Test(timeout = 5000)
	public void testYourMethod() throws TimeoutException {
		assertEquals(true, implClient.isComponentAlive());
	}
}

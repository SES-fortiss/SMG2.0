package org.fortiss.smg.actuatorclient.ipswitch.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.actuatorclient.ipswitch.impl.ActuatorClientImpl;
import org.fortiss.smg.actuatormaster.api.AbstractClient;
import org.fortiss.smg.actuatormaster.api.AbstractConnector.IOnConnectListener;
import org.fortiss.smg.actuatormaster.api.ActuatorMasterQueueNames;
import org.fortiss.smg.actuatormaster.api.IActuatorMaster;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.internal.runners.statements.Fail;
import org.slf4j.LoggerFactory;
import org.fortiss.smg.config.lib.*;
import org.fortiss.smg.config.*;
import org.fortiss.smg.remoteframework.lib.DefaultProxy;
/*
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.OptionUtils;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)*/
public class TestActuatorClientSimple extends AbstractClient {

	private static final int CONN_TIMEOUT = 5000;
	private static org.fortiss.smg.actuatorclient.ipswitch.test.MockOtherBundles mocker;
	private ActuatorClientImpl impl;
	private ActuatorClientImpl implClient;
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(ActuatorClientImpl.class);
	private List<ActuatorClientImpl> clients = new ArrayList<ActuatorClientImpl>();

	IActuatorMaster master4config = null;
/*
	@Configuration
	public Option[] config() {
		// this is used to build up a default OSGi Container and inject the SMG
		// scope
		// add here all API-libraries of the smg project on which your api &
		// impl depend on
		Option[] defaultSpace = Ops4JTestTime.getOptions();
		Option[] currentSpace = options(
				mavenBundle("org.fortiss.smartmicrogrid",
						"actuatormaster.api", "1.0-SNAPSHOT"),
				mavenBundle("org.fortiss.smartmicrogrid",
						"actuatorclient.hexabus	.impl", "1.0-SNAPSHOT")
						);

		return OptionUtils.combine(defaultSpace, currentSpace);
	}
*/
	
	@BeforeClass
	public static void setUpDataBase() throws SQLException,
			ClassNotFoundException {
		mocker = new MockOtherBundles();
	}

	
	@Before
	public void setUp() {
		//implClient = new ActuatorClientImpl("http://192.168.21.217:8080/devicetree.json" , "8080", "hexabus.wrapper", 10, "", "");
		// Register at Actuator Master (self, human readable name for device)
		 
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
            logger.error("ActuatorClient: Unable to connect to master (Timeout).");
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        /*
         * If we have connection try to get the wrapper's config file
         */
        if (master4config != null) {
            try {
                configList = master4config.getWrapperConfig("ipswitch");
            } catch (TimeoutException e) {
                logger.error("ActuatorClient: Unable to connect to master (Timeout).");
            } finally {
                try {
                    proxyMaster.destroy();
                } catch (IOException e) {
                    logger.info("Unable to close con. for queue:" 
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

                    implClient = new ActuatorClientImpl(config.getProtocol(),
                            config.getHost(),config.getPath(),
                            config.getPort(),
                            config.getWrapperID(),
                            config.getPollingfrequency(), config.getUsername(),
                            config.getPassword());
                    // Register at Actuator Master (self, human readable name
                    // for
                    // device)
                    try {
						registerAsClientAtServer(implClient,
						        config.getWrapperName(), new IOnConnectListener() {

						            @Override
						            public void onSuccessFullConnection() {
						                implClient.setMaster(master);
						                implClient.setClientId(clientId);
						                implClient.activate();
						                logger.info("ActuatorClient[" + clientKey
						                        + "-" + clientIDextension
						                        + "] is alive");
						                clients.add(implClient);

						            }
						        });
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                }
                logger.info("IPswitch Wrapper started");
            } else {
                logger.info("No Configuration available");
            }
        } else {
            try {
				proxyMaster.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            logger.debug("IPswitch Wrapper could not read config from Master");
        }
        /*
		try {
			registerAsClientAtServer(impl, "awesome-abstract-client-ipswitch", new IOnConnectListener() {
				
				@Override
				public void onSuccessFullConnection() {
					implClient.setClientId(clientId);
					implClient.setMaster(master);
					implClient.activate();			
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	@After
	public void tearDown() {
		// TODO do some cleanup
	}

	@Test(timeout = 50000)
	public void shouldConnectToCounterPage() throws TimeoutException {
		
		URLConnection counterPage = null;
		try {
			URL counterUrl = new URL(impl.getProtocol()+"://" + impl.getHost() + "/" + impl.getPath());
			counterPage = counterUrl.openConnection();
			counterPage.setConnectTimeout(CONN_TIMEOUT);
			counterPage.setReadTimeout(CONN_TIMEOUT);
		} catch (Exception e) {
			fail("Connection to counterPage failed");
		}
	}
	
	@Test(timeout = 500000)
	public void shouldConnectToTemperaturePage() throws TimeoutException {
		
		URLConnection temperaturePage = null;
		try {
			URL temperatureUrl = new URL(impl.getProtocol()+"://" + impl.getHost() + "/?Password=");
			temperaturePage = temperatureUrl.openConnection();
			temperaturePage.setConnectTimeout(CONN_TIMEOUT);
			temperaturePage.setReadTimeout(CONN_TIMEOUT);
		} catch (Exception e) {
			fail("Connection to counterPage failed");
		}
	}
}

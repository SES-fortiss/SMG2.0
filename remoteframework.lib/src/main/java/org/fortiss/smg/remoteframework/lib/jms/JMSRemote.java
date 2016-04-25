package org.fortiss.smg.remoteframework.lib.jms;

import org.osgi.framework.BundleContext;

public class JMSRemote {
	/*
	private static final Logger logger = (Logger) LoggerFactory
			.getLogger(JMSRemote.class);

	private JMSRemote() {
		// utility class, do not instantiate
	}

//	
//	  Exposes a service over JMS.
//	  
//	  @param address
//	             the JMS address to bind to. e.g. jms:queue:someQueueName or
//	             jms:topic:someTopicName
//	  @param implementor
//	             the class that implements the services interface
//	  @param interfce
//	             the services interface
//	  @return a reference to the server-instance of the running service. use
//	          this to stop the service.
//	 
	public static <T> Server exposeServiceOnJMS(String address, T implementor,
			Class<T> interfce) {
		if (!interfce.isInterface()) {
			throw new IllegalArgumentException(
					"given interface class is not an interface");
		}

		String fullAddress = address + "?" + MiscConstants.JNDI_OPTS
				+ addUsernamePasswordIfDefined();

		if (!fullAddress.startsWith(MiscConstants.QUEUE_PREFIX)
				&& !fullAddress.startsWith(MiscConstants.TOPIC_PREFIX)) {
			throw new IllegalArgumentException("Queue/Topic prefix missing");
		}

		JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();

		svrFactory.setServiceClass(interfce);
		svrFactory.setAddress(fullAddress);
		svrFactory
				.setTransportId(JMSSpecConstants.SOAP_JMS_SPECIFICATION_TRANSPORTID);
		svrFactory.setServiceBean(implementor);

		svrFactory.setBindingId("http://schemas.xmlsoap.org/wsdl/soap12/");

		svrFactory.getFeatures().add(new WSAddressingFeature());

		Server server = svrFactory.create();

		
//		  JMSDestination jmsDestination =
//		  (JMSDestination)server.getDestination(); JMSConfiguration
//		  serverConfig = new JMSConfiguration(); //
//		  serverConfig.setTimeToLive(60000l);
//		  
//		  jmsDestination.setJmsConfig(serverConfig);
		 

		// interceptor for ws-security
		// server.getEndpoint().getInInterceptors()
		// .add(new PermissionsInInterceptor());

		logger.debug("Exposed {} on address {}", implementor.getClass()
				.getName(), address);
		return server;
	}

	private static String addUsernamePasswordIfDefined() {

		String username;
		String password;
		//BundleContext ctx = SharedActivator.getContext();
		BundleContext ctx= null;
		if (ctx != null) {
			username = ctx.getProperty("activemqUser");
			password = ctx.getProperty("activemqPassword");
		} else {
			username = null;
			password = null;
			logger.warn("Could not get BundleContext. This is normal in tests but should not happen in Production.");
		}
		String result = "";
		if (username != null && !username.trim().isEmpty()) {
			result += "&jndi-userName=" + username;
		}

		if (password != null && !password.trim().isEmpty()) {
			result += "&jndi-password=" + password;
		}
		return result;
	}

	private static JMSCache cache = new JMSCache();

	@SuppressWarnings("unchecked")
	public static <T> T getProxy(String queue, Class<T> interfce,
			String sessionCookie) {

		T cached = cache.get(queue, interfce);
		if (cached != null) {
			return cached;
		}

		// String optionalReplyQueueOption;
		// if (replyQueue != null) {
		// optionalReplyQueueOption = "&replyToName=" + replyQueue;
		// } else {
		// optionalReplyQueueOption = "";
		// }
		String address = queue + "?" + MiscConstants.JNDI_OPTS
				+ addUsernamePasswordIfDefined();
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setTransportId(JMSSpecConstants.SOAP_JMS_SPECIFICATION_TRANSPORTID);
		factory.setUsername("system");
		factory.setPassword("password");
		factory.setServiceClass(interfce);
		factory.setAddress(address);
		T clientProxy = (T) factory.create();

		Client client = ClientProxy.getClient(clientProxy);
		// interceptor for ws-security
		// client.getEndpoint().getOutInterceptors()
		// .add(new PermissionsOutInterceptor(sessionCookie));

		JMSConduit jmsConduit = (JMSConduit) client.getConduit();

		// Edit by Sebastian: Set Timeout a little bit higher as we got errors
		// with it e.g. locationmgr

		// jmsConduit.getJmsConfig().setReceiveTimeout(20000l);

		jmsConduit.getJmsConfig().setReceiveTimeout(60000l);
		jmsConduit.getJmsConfig().setServerReceiveTimeout(180000l);

		cache.put(queue, clientProxy);
		return clientProxy;
	}
	*/
}

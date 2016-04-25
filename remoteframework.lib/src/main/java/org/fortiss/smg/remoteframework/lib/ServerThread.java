package org.fortiss.smg.remoteframework.lib;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.remoteframework.lib.jsonrpc.JsonRpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ServerThread {

	private static final int TIMEOUTSHORT = 2000;
	private static final int TIMEOUTLONG = 5000;
	
	private JsonRpcServer server;
	private final ExecutorService executor = Executors
			.newSingleThreadExecutor();
	private static final ConnectionFactory factory = new ConnectionFactory();
	private Connection connection;
	private Channel channel = null;
	
	private final static Logger logger = (Logger) LoggerFactory.getLogger(ServerThread.class.getCanonicalName()); 

	public  ServerThread(Class<?> klass, Object impl, String queue)
			throws IOException {
		connection = factory.newConnection();
		if (connection != null && !connection.isOpen()) {
			connection = factory.newConnection();
		}
		if (connection != null) {
			channel = connection.createChannel();

			Map<String, Object> args = new HashMap<String, Object>();
			args.put("x-message-ttl", TIMEOUTLONG);
			channel.queueDeclare(queue, false, false, false, args);
			JsonRpcServer server = new JsonRpcServer(channel, queue, klass,
				impl);
			this.server = server;
			executor.submit(new ServerThreadRunnable());
		}
	}
	
	public void stop() throws IOException {
		logger.trace("Trying to stop server ... ");
		// here we could delete the queue
				// 		channel.queueDelete(queue)
			
		channel.close();
		/*if (connection != null) {
			connection.close();
		}*/
		server.close();
		executor.shutdown(); // gracefully shuts down the executor
		logger.debug("Server-shutdown: "+ executor.isShutdown());
		}
	
	private class ServerThreadRunnable  implements Runnable {

		
		public void run() {
			try {
				logger.trace("Starting server");
				server.mainloop();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			logger.trace("Server stopped");
		}
	}

}

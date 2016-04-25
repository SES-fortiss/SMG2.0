package org.fortiss.smg.remoteframework.lib;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.remoteframework.lib.except.JsonRpcException;
import org.fortiss.smg.remoteframework.lib.jsonrpc.JsonRpcClient;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitRPCProxy<T> extends GenericProxy<T> {

	
	private ConnectionFactory factory = new ConnectionFactory();
	private Connection connection;
	
	public RabbitRPCProxy(Class<T> klass, String queue, int timeout) {
		super(klass, queue, timeout);
	}

	@Override
	public T init() throws IOException, TimeoutException {
		
		connection = factory.newConnection();


		Channel channel = connection.createChannel();
		JsonRpcClient client;
		try {
			client = new JsonRpcClient(channel, "", queue, timeout);
		} catch (JsonRpcException e) {
			throw new IOException(e.message);
		}
		return (T) client.createProxy(klass);
	}
	
	@Override
	public T initLoop() throws IOException {

		connection = factory.newConnection();
	

		Channel channel = connection.createChannel();
		JsonRpcClient client;
		
		while(true){
			try {
				client = new JsonRpcClient(channel, "", queue, timeout);
				break;
			} catch (JsonRpcException e) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				throw new IOException(e.message);
			} catch (TimeoutException e) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return (T) client.createProxy(klass);
	}


	@Override
	public void destroy() throws IOException {
		connection.close();
}

}

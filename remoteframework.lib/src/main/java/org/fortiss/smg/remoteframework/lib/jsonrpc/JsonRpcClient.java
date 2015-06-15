/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
//  The contents of this file are subject to the Mozilla Public License
//  Version 1.1 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License
//  at http://www.mozilla.org/MPL/
//
//  Software distributed under the License is distributed on an "AS IS"
//  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
//  the License for the specific language governing rights and
//  limitations under the License.
//
//  The Original Code is RabbitMQ.
//
//  The Initial Developer of the Original Code is GoPivotal, Inc.
//  Copyright (c) 2007-2013 GoPivotal, Inc.  All rights reserved.
//

package org.fortiss.smg.remoteframework.lib.jsonrpc;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.fortiss.smg.remoteframework.lib.except.JsonRpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.RpcClient;
import com.rabbitmq.client.ShutdownSignalException;
import com.rabbitmq.tools.json.JSONReader;
import com.rabbitmq.tools.json.JSONWriter;

/**
 * <a href="http://json-rpc.org">JSON-RPC</a> is a lightweight RPC mechanism
 * using <a href="http://www.json.org/">JSON</a> as a data language for request
 * and reply messages. It is rapidly becoming a standard in web development,
 * where it is used to make RPC requests over HTTP. RabbitMQ provides an AMQP
 * transport binding for JSON-RPC in the form of the <code>JsonRpcClient</code>
 * class.
 * 
 * JSON-RPC services are self-describing - each service is able to list its
 * supported procedures, and each procedure describes its parameters and types.
 * An instance of JsonRpcClient retrieves its service description using the
 * standard <code>system.describe</code> procedure when it is constructed, and
 * uses the information to coerce parameter types appropriately. A JSON service
 * description is parsed into instances of <code>ServiceDescription</code>.
 * Client code can access the service description by reading the
 * <code>serviceDescription</code> field of <code>JsonRpcClient</code>
 * instances.
 * 
 * @see #call(String, Object[])
 * @see #call(String[])
 */
public class JsonRpcClient extends RpcClient implements InvocationHandler {
	/** Holds the JSON-RPC service description for this client. */
	private ServiceDescription serviceDescription;
	private ObjectMapper mapper;
	private final static Logger logger = (Logger) LoggerFactory.getLogger(JsonRpcClient.class.getCanonicalName()); 

	/**
	 * Construct a new JsonRpcClient, passing the parameters through to
	 * RpcClient's constructor. The service description record is retrieved from
	 * the server during construction.
	 * 
	 * @throws TimeoutException
	 *             if a response is not received within the timeout specified,
	 *             if any
	 */
	public JsonRpcClient(Channel channel, String exchange, String routingKey,
			int timeout) throws IOException, JsonRpcException, TimeoutException {
		super(channel, exchange, routingKey, timeout);
		retrieveServiceDescription();
		init();
	}

	public JsonRpcClient(Channel channel, String exchange, String routingKey)
			throws IOException, JsonRpcException, TimeoutException {
		this(channel, exchange, routingKey, RpcClient.NO_TIMEOUT);
		init();
	}

	private void init() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		//mapper.enableDefaultTyping();
	}

	/**
	 * Private API - parses a JSON-RPC reply object, checking it for exceptions.
	 * 
	 * @return the result contained within the reply, if no exception is found
	 *         Throws JsonRpcException if the reply object contained an
	 *         exception
	 */
	public static Object checkReply(Map<String, Object> reply)
			throws JsonRpcException {
		if (reply.containsKey("error")) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) reply.get("error");
			// actually a Map<String, Object>
			throw new JsonRpcException(map);
		}

		Object result = reply.get("result");
		logger.debug("Client-Reply:" + result);
		// System.out.println(new JSONWriter().write(result));
		return result;
	}

	/**
	 * Public API - builds, encodes and sends a JSON-RPC request, and waits for
	 * the response.
	 * 
	 * @param proxy
	 * @return the result contained within the reply, if no exception is found
	 * @throws JsonRpcException
	 *             if the reply object contained an exception
	 * @throws TimeoutException
	 *             if a response is not received within the timeout specified,
	 *             if any
	 * @throws SQLException 
	 */
	public Object call_gson(Method method, Object[] params) throws IOException,
			JsonRpcException, TimeoutException, SQLException {
		HashMap<String, Object> request = new HashMap<String, Object>();
		request.put("id", null);
		request.put("method", method.getName());
		request.put("version", ServiceDescription.JSON_RPC_VERSION);
		//request.put("params", (params == null) ? new Object[0] : params);
		//request.put("params", params);
		
		String requestStr = new JSONWriter().write(request);
		// normally we would use that,but we can't because of polymorphic typing
		// names of the class are erased
		// String requestStr = mapper.writeValueAsString(request);
		
		if(params != null){
			StringBuilder strBuilder = new StringBuilder(",\"params\":[");
			for(int i=0; i< params.length; i++){
				strBuilder.append(mapper.writeValueAsString(params[i]));
				// no comma at last element
				if(i != params.length -1){
					strBuilder.append(",");
				}
			}
			strBuilder.append("]}");
			requestStr= requestStr.substring(0, requestStr.length()-1) + strBuilder;
		}
		
		
		

//		ObjectNode node = mapper.createObjectNode();
//		node.put("id", "");
//		node.put("method", method.getName());
//		node.put("version", ServiceDescription.JSON_RPC_VERSION);
		//ArrayNode par = node.putArray("params");
		//par.
//		for(Object p : params){
//			System.out.println(p.getClass());
//		}
//		
//	
//		node.put( "params" , mapper.getNodeFactory().POJONode(params));
		
		

		
		logger.debug("Request-Client: " + requestStr);
	
		
		try {
			String replyStr = this.stringCall(requestStr);
		
			// TODO : maybe we should make void calls asynchronous
			if(!method.getReturnType().equals(Void.TYPE) ){
			
			JsonNode rootNode = mapper.readTree(replyStr);

			JavaType type = mapper.getTypeFactory().constructType(method.getGenericReturnType(), method.getReturnType());
			logger.debug("Type: "+ method.getGenericReturnType());
			Object response = mapper.readValue(rootNode.get("result").traverse(), type);

			return response;
			}else{
				return null;
			}
		} catch (ShutdownSignalException ex) {
			throw new IOException(ex.getMessage()); // wrap, re-throw
		}
		catch(TimeoutException e){
			throw new TimeoutException(e.getMessage());
		}
	

	}

	/**
	 * Public API - builds, encodes and sends a JSON-RPC request, and waits for
	 * the response.
	 * 
	 * @param proxy
	 * @return the result contained within the reply, if no exception is found
	 * @throws JsonRpcException
	 *             if the reply object contained an exception
	 * @throws TimeoutException
	 *             if a response is not received within the timeout specified,
	 *             if any
	 */
	
	public Object call(String method, Object[] params) throws IOException,
			JsonRpcException, TimeoutException {
		HashMap<String, Object> request = new HashMap<String, Object>();
		request.put("id", null);
		request.put("method", method);
		request.put("version", ServiceDescription.JSON_RPC_VERSION);
		request.put("params", (params == null) ? new Object[0] : params);
		String requestStr = new JSONWriter().write(request);
		logger.debug("Request-Client: " + requestStr);
		try {
			String replyStr = this.stringCall(requestStr);
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) (new JSONReader()
					.read(replyStr));

			return checkReply(map);
		} catch (ShutdownSignalException ex) {
			throw new IOException(ex.getMessage()); // wrap, re-throw
		}

	}

	/**
	 * Public API - implements InvocationHandler.invoke. This is useful for
	 * constructing dynamic proxies for JSON-RPC interfaces.
	 */
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable, TimeoutException {
		return call_gson(method, args);
	}

	/**
	 * Public API - gets a dynamic proxy for a particular interface class.
	 */
	public Object createProxy(Class<?> klass) throws IllegalArgumentException {
		return Proxy.newProxyInstance(klass.getClassLoader(),
				new Class[] { klass }, this);
	}

	/**
	 * Private API - used by {@link #call(String[])} to ad-hoc convert strings
	 * into the required data types for a call.
	 */
	public static Object coerce(String val, String type)
			throws NumberFormatException {
		System.out.println("val=" + val + " type=" + type);
		if ("bit".equals(type)) {
			return Boolean.getBoolean(val) ? Boolean.TRUE : Boolean.FALSE;
		} else if ("num".equals(type)) {
			try {
				return new Integer(val);
			} catch (NumberFormatException nfe) {
				return new Double(val);
			}
		} else if ("str".equals(type)) {
			return val;
		} else if ("arr".equals(type) || "obj".equals(type)
				|| "any".equals(type)) {
			return new JSONReader().read(val);
		} else if ("nil".equals(type)) {
			return null;
		} else {
			throw new IllegalArgumentException("Bad type: " + type);
		}
	}

	/**
	 * Public API - as {@link #call(String,Object[])}, but takes the method name
	 * from the first entry in <code>args</code>, and the parameters from
	 * subsequent entries. All parameter values are passed through coerce() to
	 * attempt to make them the types the server is expecting.
	 * 
	 * @return the result contained within the reply, if no exception is found
	 * @throws JsonRpcException
	 *             if the reply object contained an exception
	 * @throws NumberFormatException
	 *             if a coercion failed
	 * @throws TimeoutException
	 *             if a response is not received within the timeout specified,
	 *             if any
	 * @see #coerce
	 */
	public Object call(String[] args) throws NumberFormatException,
			IOException, JsonRpcException, TimeoutException {

		if (args.length == 0) {
			throw new IllegalArgumentException(
					"First string argument must be method name");
		}

		String method = args[0];
		int arity = args.length - 1;
		ProcedureDescription proc = serviceDescription.getProcedure(method,
				arity);
		ParameterDescription[] params = proc.getParams();

		Object[] actuals = new Object[arity];
		for (int count = 0; count < params.length; count++) {
			actuals[count] = coerce(args[count + 1], params[count].type);
		}

		return call(method, actuals);
	}

	/**
	 * Public API - gets the service description record that this service loaded
	 * from the server itself at construction time.
	 */
	public ServiceDescription getServiceDescription() {
		return serviceDescription;
	}

	/**
	 * Private API - invokes the "system.describe" method on the server, and
	 * parses and stores the resulting service description in this object. TODO:
	 * Avoid calling this from the constructor.
	 * 
	 * @throws TimeoutException
	 *             if a response is not received within the timeout specified,
	 *             if any
	 */
	private void retrieveServiceDescription() throws IOException,
			JsonRpcException, TimeoutException {
		@SuppressWarnings("unchecked")
		Map<String, Object> rawServiceDescription = (Map<String, Object>) call(
				"system.describe", null);
		serviceDescription = new ServiceDescription(rawServiceDescription);
	}
}

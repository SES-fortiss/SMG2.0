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
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.StringRpcServer;
import com.rabbitmq.tools.json.JSONReader;
import com.rabbitmq.tools.json.JSONWriter;

/**
 * JSON-RPC Server class.
 * 
 * Given a Java {@link Class}, representing an interface, and an implementation
 * of that interface, JsonRpcServer will reflect on the class to construct the
 * {@link ServiceDescription}, and will route incoming requests for methods on
 * the interface to the implementation object while the mainloop() is running.
 * 
 * @see com.rabbitmq.client.RpcServer
 * @see JsonRpcClient
 */
public class JsonRpcServer extends StringRpcServer {
	private static ObjectMapper staticMapper;
	/** Holds the JSON-RPC service description for this client. */
	public ServiceDescription serviceDescription;
	/** The interface this server implements. */
	public Class<?> interfaceClass;
	/** The instance backing this server. */
	public Object interfaceInstance;
	private ObjectMapper mapper;
	private final static Logger logger = (Logger) LoggerFactory.getLogger(JsonRpcServer.class.getCanonicalName()); 

	/**
	 * Construct a server that talks to the outside world using the given
	 * channel, and constructs a fresh temporary queue. Use getQueueName() to
	 * discover the created queue name.
	 * 
	 * @param channel
	 *            AMQP channel to use
	 * @param interfaceClass
	 *            Java interface that this server is exposing to the world
	 * @param interfaceInstance
	 *            Java instance (of interfaceClass) that is being exposed
	 * @throws IOException
	 *             if something goes wrong during an AMQP operation
	 */
	public JsonRpcServer(Channel channel, Class<?> interfaceClass,
			Object interfaceInstance) throws IOException {
		super(channel);
		init(interfaceClass, interfaceInstance);
	}

	private void init(Class<?> interfaceClass, Object interfaceInstance) {
		this.interfaceClass = interfaceClass;
		this.interfaceInstance = interfaceInstance;
		this.serviceDescription = new ServiceDescription(interfaceClass);

		// init gson factories
		mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		//mapper.enableDefaultTyping();
	}

	public static ObjectMapper getMapper(){
		if(staticMapper == null){
			staticMapper = new ObjectMapper();
			staticMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			staticMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			staticMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			staticMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		}
		return staticMapper;
	}
	
	/**
	 * Construct a server that talks to the outside world using the given
	 * channel and queue name. Our superclass, RpcServer, expects the queue to
	 * exist at the time of construction.
	 * 
	 * @param channel
	 *            AMQP channel to use
	 * @param queueName
	 *            AMQP queue name to listen for requests on
	 * @param interfaceClass
	 *            Java interface that this server is exposing to the world
	 * @param interfaceInstance
	 *            Java instance (of interfaceClass) that is being exposed
	 * @throws IOException
	 *             if something goes wrong during an AMQP operation
	 */
	public JsonRpcServer(Channel channel, String queueName,
			Class<?> interfaceClass, Object interfaceInstance)
			throws IOException {
		super(channel, queueName);
		init(interfaceClass, interfaceInstance);
	}

	/**
	 * Override our superclass' method, dispatching to doCall.
	 */
	public String handleStringCall(String requestBody,
			AMQP.BasicProperties replyProperties) {
		String replyBody = null;
		try {
			replyBody = doCall(requestBody);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			logger.error("JSONProcessing", e.fillInStackTrace());
		} catch (IOException e) {
			logger.error("IOException", e.fillInStackTrace());
		}
		return replyBody;
	}

	/**
	 * Runs a single JSON-RPC request.
	 * 
	 * @param requestBody
	 *            the JSON-RPC request string (a JSON encoded value)
	 * @return a JSON-RPC response string (a JSON encoded value)
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public String doCall(String requestBody) throws JsonProcessingException, IOException {
		logger.debug("Request-Server: " + requestBody);
		Object id = null;
		String method;
		Object[] params;
		try {

			if (requestBody.length() < 1) {
				return errorResponse(null, 400, "Bad Request", null);
			}

			//JsonObject request = jsonParser.parse(requestBody)

			JsonNode rootNode = mapper.readTree(requestBody);	
			

			if (!ServiceDescription.JSON_RPC_VERSION.equals(rootNode.path("version").textValue())) {
				return errorResponse(null, 505,
						"JSONRPC version not supported", null);
			}

			method = rootNode.path("method").textValue();
			logger.debug("Method: " + method + "()");
			
			// id = request.get("id").getAsString();
			if (method.startsWith("system.")) {
				@SuppressWarnings("unchecked")
				Map<String, Object> request2 = (Map<String, Object>) new JSONReader()
						.read(requestBody);
				id = request2.get("id");
				List<?> parmList = (List<?>) request2.get("params");
				params = parmList.toArray();

			} else {
				JsonNode jsonParams = rootNode.path("params");
				 Iterator<JsonNode> jsonArray = jsonParams.elements();

				 int arrSize = 0;
				 
				for(JsonNode node: jsonParams){
					arrSize++;
				}
				 
				Method met = matchingMethod(method, arrSize);

				params = new Object[arrSize];

				logger.debug("Method: " + met.getName() + "()");
				Type[] gen_types = met.getGenericParameterTypes();
				Class<?>[] stat_types = met.getParameterTypes();

				int j = 0;
				for (JsonNode element : jsonParams) {
					logger.debug("\tParameter: " + gen_types[j]);
					JavaType type = mapper.getTypeFactory().constructType(gen_types[j], stat_types[j]);
					params[j] = mapper.readValue(element.traverse(), type);
					j++;
				}

			}

		} catch (ClassCastException cce) {
			// Bogus request!
			return errorResponse(null, 400, "Bad Request", null);
		}

		if (method.equals("system.describe")) {
			return resultResponse(id, serviceDescription);
		} else if (method.startsWith("system.")) {
			return errorResponse(id, 403, "System methods forbidden", null);
		} else {
			Object result = "";
			try {
				Method meth = matchingMethod(method, params);
				if (meth != null) {
				result = meth.invoke(
						interfaceInstance, params);
				}
			}catch(IllegalArgumentException e ){
				e.printStackTrace();
				return errorResponse(id, 500, "IllegalArgumentException", e);
			} catch (Throwable t) {
				t.printStackTrace();
				return errorResponse(id, 500, "Internal Server Error", t);
			}
			return resultResponse(id, result);
		}
	}

	/**
	 * Retrieves the best matching method for the given method name and
	 * parameters.
	 * 
	 * Subclasses may override this if they have specialised dispatching
	 * requirements, so long as they continue to honour their
	 * ServiceDescription.
	 */
	public  Method matchingMethod(String methodName, int size) {
		ProcedureDescription proc = serviceDescription.getProcedure(methodName,
				size);
		return proc.internal_getMethod();
	}

	/**
	 * Retrieves the best matching method for the given method name and
	 * parameters.
	 * 
	 * Subclasses may override this if they have specialised dispatching
	 * requirements, so long as they continue to honour their
	 * ServiceDescription.
	 */
	public Method matchingMethod(String methodName, Object[] params) {
		return matchingMethod(methodName, params.length);
	}

	/**
	 * Construct and encode a JSON-RPC error response for the request ID given,
	 * using the code, message, and possible (JSON-encodable) argument passed
	 * in.
	 */
	public static String errorResponse(Object id, int code, String message,
			Object errorArg) {
		Map<String, Object> err = new HashMap<String, Object>();
		err.put("name", "JSONRPCError");
		err.put("code", code);
		err.put("message", message);
		err.put("error", errorArg);
		return response(id, "error", err);
	}

	/**
	 * Construct and encode a JSON-RPC success response for the request ID
	 * given, using the result value passed in.
	 */
	public static String resultResponse(Object id, Object result) {
		return response(id, "result", result);
	}

	/**
	 * Private API - used by errorResponse and resultResponse.
	 */
	public static String response(Object id, String label, Object value) {
		Map<String, Object> resp = new HashMap<String, Object>();
		resp.put("version", ServiceDescription.JSON_RPC_VERSION);
		if (id != null) {
			resp.put("id", id);
		}
		resp.put(label, value);
		//String respStr = new JSONWriter().write(resp);
		
		String respStr = "";
		try {
			respStr = getMapper().writeValueAsString(resp);
		} catch (JsonProcessingException e) {
			logger.error("JSON deserialze error" + e);
		}	
		logger.debug("Result-Server: "+ respStr);
		return respStr;
	}

	/**
	 * Public API - gets the service description record that this service built
	 * from interfaceClass at construction time.
	 */
	public ServiceDescription getServiceDescription() {
		return serviceDescription;
	}
	
	@Override
	public void close() throws IOException {
		if (super.getChannel() != null && super.getChannel().isOpen()) {
			super.getChannel().close();
			super.close();
		}
	}
}

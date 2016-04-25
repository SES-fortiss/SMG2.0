package org.fortiss.smg.webrest.impl.openhab;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.WebApplicationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
 
@Provider
@Produces("application/xml")
public class DoubleMessageBodyWriter implements MessageBodyWriter<Double> {

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		// TODO Auto-generated method stub
		return type == Double.class;
	}

	@Override
	public long getSize(Double t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeTo(Double t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		 StringBuilder sb = new StringBuilder();
	        sb.append("<double>").append(t.toString()).append("</double>");
	        DataOutputStream dos = new DataOutputStream(entityStream);
	        dos.writeUTF(sb.toString());
		
	}}

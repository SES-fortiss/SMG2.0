package org.fortiss.smg.webrest.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.fortiss.smg.sqltools.lib.TestingDatabase;
import org.fortiss.smg.sqltools.lib.utils.TestingDBUtil;
import org.fortiss.smg.usermanager.api.KeyManagerInterface;
import org.fortiss.smg.usermanager.api.Tuple;
import org.fortiss.smg.usermanager.dbutil.UserManagerDBUtil;
import org.fortiss.smg.usermanager.impl.key.KeyManagerImpl;
import org.fortiss.smg.webrest.test.server.Const;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.owlike.genson.ext.jaxrs.GensonJsonConverter;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class ClientHelper {

    public static final String ACCEPT_JSON = "application/json";
    public static final String ACCEPT_XML = "application/xml";

    private static final Logger logger = LoggerFactory
            .getLogger(ClientHelper.class);
    private static KeyManagerInterface manager;
//    public static UserManagerDBUtil dbUtil = new UserManagerDBUtil(TestingDatabase.getDBUrl(), TestingDatabase.getDBUser(), TestingDatabase.getDBPassword(),logger);

	
    public static <E> E checkEquality(String url, Class<E> class1, int port) {
        ClientConfig cfg = new DefaultClientConfig(GensonJsonConverter.class);
        Client client = Client.create(cfg);
        WebResource webResourceJSON = null;
        WebResource webResourceXML = null;

        try {
            webResourceJSON = client.resource(AuthenticationHelper
                    .generateValidURL(url , port));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail("Unsupported encoding");
        }

        E responseJSON = webResourceJSON.accept("application/json").get(class1);

        try {
            webResourceXML = client.resource(AuthenticationHelper
                    .generateValidURL(url, port));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail("Unsupported encoding");
        }
        E responseXML = webResourceXML.accept("application/xml").get(class1);

        if (responseJSON.equals(responseXML)) {
            return responseJSON;
        } else {
            if (responseJSON.getClass().isArray()) {
                Arrays.deepEquals((Object[]) responseJSON,
                        (Object[]) responseXML);
                return responseJSON;
            } else {
                System.out.println("JSON: " + responseJSON);
                System.out.println("XML: " + responseXML);
                Assert.fail("Responses of JSON and XML are not equal.");
                return null;
            }

        }
    }

    /**
     * Checks if we got a response
     * 
     * @param url
     * @param port 
     * @return
     * @throws UnsupportedEncodingException 
     */
    public static boolean checkResponse(String url, int port) {
        Client client = Client.create();
        WebResource webResourceJSON = null;
        try {
            webResourceJSON = client.resource(AuthenticationHelper
                    .generateValidURL(url, port));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail("Unsupported encoding");
        }

        try {
			logger.debug("URL:"+ AuthenticationHelper.generateValidURL(url, port));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
			 Assert.fail("Unsupported encoding");
		}
        System.out.println(webResourceJSON);
        boolean b = true;
        ClientResponse responseJSON = webResourceJSON
                .accept("application/json").get(ClientResponse.class);

        if (responseJSON.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + responseJSON.getStatus());
        } else {
            b = b && true;
        }

        WebResource webResourceXML = null;
        try {
            webResourceXML = client.resource(AuthenticationHelper
                    .generateValidURL(url, port));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail("Unsupported encoding");
        }

        ClientResponse responseXML = webResourceXML.accept("application/xml")
                .get(ClientResponse.class);

        if (responseXML.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + responseXML.getStatus());
        } else {
            b = b && true;
        }
        return b;
    }

    /**
     * @param args
     * @return
     */
    public static <E> E fetchResponse(String url, Class<E> class1, int port) {
        ClientConfig cfg = new DefaultClientConfig(GensonJsonConverter.class);
        Client client = Client.create(cfg);
        WebResource webResourceJSON = null;

        try {
            webResourceJSON = client.resource(AuthenticationHelper
                    .generateValidURL(url, port));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail("Unsupported encoding");
        }

        E pojo = webResourceJSON.accept(MediaType.APPLICATION_JSON).get(class1);
        return pojo;
    }

    /**
     * @param args
     * @return
     */
    public static String fetchStringResponse(String url, String accepts, int port) {
        String output = "";
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(
                    AuthenticationHelper.generateValidURL(url, port));
            getRequest.addHeader("accept", accepts);

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String output_temp;
            while ((output_temp = br.readLine()) != null) {
                output += output_temp;
            }

            ClientHelper.logger.debug("Output from: " + url + "\n" + output);

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {

            Assert.fail(e.getMessage());
            e.printStackTrace();
            return "";

        } catch (IOException e) {
            Assert.fail(e.getMessage());
            e.printStackTrace();
            return "";
        }

        return output;

    }

    public static void generateNewKeys() {
        Tuple genKey = null;
		try {
			// Check for SetDevID in Keymanager - is it necessary ?
			genKey = ClientHelper.getMockKeyManager().generateKeys(
			        Const.getUserId(),0); // TODO: need a valid deviceId!!!!!!!!!!!!!!!!!!!!!!

		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Key generation failed");
		}
        Const.setAccessKey(genKey.getTupleKey());
        Const.setSecretKey(genKey.getValue());
    }

    public static KeyManagerInterface getMockKeyManager() {
        if (ClientHelper.manager == null) {

            ClientHelper.manager = new KeyManagerImpl(new TestingDBUtil());

            ClientHelper.generateNewKeys();
        }
        return ClientHelper.manager;
    }
}

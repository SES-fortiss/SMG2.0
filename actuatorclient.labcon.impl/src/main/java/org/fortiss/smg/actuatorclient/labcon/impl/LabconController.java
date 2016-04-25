package org.fortiss.smg.actuatorclient.labcon.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.fortiss.smg.actuatormaster.api.events.DoubleEvent;
import org.fortiss.smg.containermanager.api.devices.DeviceId;
import org.slf4j.Logger;
//import org.apache.commons.codec.binary.Base64;
import org.slf4j.LoggerFactory;

public class LabconController { // implements LabconInterface{
	
	private ActuatorClientImpl impl;
	
	private static org.slf4j.Logger logger = LoggerFactory
			.getLogger(LabconController.class);

	private String url;
	private String username;
	private String password;
	
	private DefaultHttpClient httpclient; 
	private HttpParams params;
	
	private URLConnection conn;
	private SSLContext sslContext;
	private SSLSocketFactory sslSocketFactory;

	private boolean reconnect; 
	
	
	private DeviceId origin;
 	private DoubleEvent ev;
	
	public LabconController(ActuatorClientImpl impl, String url, String username, String password) {
		this.impl = impl;
		this.url = url;
		this.username = username;
		this.password = password;
		
		
		new HttpHost(this.url);
		params = new BasicHttpParams();
		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
		params.setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true); 
		//this how tiny it might seems, is actually absoluty needed. otherwise http client lags for 2sec.
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		httpclient = new DefaultHttpClient(params);
		new BasicHttpContext();
		
		/*
		 * Initialize Control Connection
		 */
		initializeControlConnection();
		
		reconnect = false;
	}
	
	/*public void controlDevice(String device, boolean command) {
		controlCoffeeMachine(command);
		
	}*/
	
	public void controlCoffeeMachine(double commandParameter){
		try {
			boolean proceed = true; 
			if (reconnect)  {
				logger.info("LabConController: controlCoffeeMachine: ############### Labcon controller reconnect needed ###############");
				initializeControlConnection();
			}
			logger.info("LabConController: controlCoffeeMachine: ############### Labcon controller authentication done ###############");
			logger.info("LabConController: controlCoffeeMachine: ############### Labcon controller turn it on ? "+commandParameter+" ###############");
			String httpCommand = "";
			if(commandParameter == 1.0){
				logger.info("LabConController: controlCoffeeMachine: ############### Initializing + ON ###############"+ commandParameter);
				httpCommand = "/index.pyc?send=vX1XmeasPOW%22on&page=zbsconfig&address=0013a200409c5958&pid=ZBS-110&infoID=&vX1XthreshTXT=&vX1XthreshHBEAT=";
				
			} 
			else if (commandParameter == 0.0) {
				logger.info("LabConController: controlCoffeeMachine: ############### Initializing + OFF ###############"+ commandParameter);
				httpCommand = "/index.pyc?send=vX1XmeasPOW%22off&page=zbsconfig&address=0013a200409c5958&pid=ZBS-110&infoID=&vX1XthreshTXT=&vX1XthreshHBEAT=";
				
			}
			else {
				proceed = false;
			}
			
			if (proceed) { 
			Long before = new Date().getTime();

			//conn = (HttpsURLConnection) new URL(conn.getURL()+httpCommand).openConnection();
			conn = new URL(conn.getURL()+httpCommand).openConnection();
			/*conn.setSSLSocketFactory(sslSocketFactory);
			conn.setHostnameVerifier(new HostnameVerifier()
					{
						@Override
						public boolean verify(String arg0, SSLSession arg1) {
							return true;
						}
					});
			//System.out.println("orig " + conn.getURL());
			conn.setInstanceFollowRedirects(true);
			*/
			/*
			 * accept redirect, otherwise no command will be executed
			 */
            String redirect = conn.getHeaderField("Location");
	        if (redirect != null) {
	        	conn = (URLConnection) new URL(redirect).openConnection();
	        }
	        InputStream is = conn.getInputStream();
	        is.close();
	       // System.out.println("redirect " + conn.getURL());
	        Long executionTime = new Date().getTime() - before;
			
	        /*
	         * generate Event
	         */
	        origin = impl.getDeviceSpecs().get(0).getDeviceId();
        	ev = new DoubleEvent(commandParameter);
        	impl.getMaster().sendDoubleEvent(ev, origin, impl.getClientId());
			logger.info("LabconWrapper: run(): getEventHandler - new Event from " + origin + " value " + commandParameter);
			
			logger.debug("LabConController: controlCoffeeMachine: ############################\nCommand took "+ executionTime + " ms execution time \n############################");
			
			}
	        			
		} catch (Exception e) {
			logger.info("LabConController: controlCoffeeMachine: ############### Labcon controller operation failed ############### " + e);
			e.printStackTrace();
		}
		finally {
		    reconnect = true;
		}
		
	}
	
	

	private void initializeControlConnection() {


		// set default authenticator
		Authenticator.setDefault(new Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password.toCharArray());
		    }
		});

		try {
			URL url2 = new URL("http://"+url);
			
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

		        @Override
		        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		            return null;
		        }
				@Override
				public void checkClientTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
				}
				@Override
				public void checkServerTrusted(
						java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
				}
		    } };
		    
		    // Install the all-trusting trust manager
		    
			/*sslContext = SSLContext.getInstance( "SSL" );
		    sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
		    // Create an ssl socket factory with our all-trusting manager
		    sslSocketFactory = sslContext.getSocketFactory();
		    */
			
		    //conn = (HttpsURLConnection) url2.openConnection();
		    conn = url2.openConnection();
			/*conn.setSSLSocketFactory(sslSocketFactory);
			conn.setHostnameVerifier(new HostnameVerifier()
					{
						@Override
						public boolean verify(String arg0, SSLSession arg1) {
							return true;
						}
					});
			//System.out.println("orig " + conn.getURL());
			conn.setInstanceFollowRedirects(true);
			//conn.setDoOutput(true);
			*/
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*
		 * SSL Integration
		 */
		/*
		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance("TLS");
	
		X509TrustManager tm = new X509TrustManager() {

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return null;
		}

		@Override
		public void checkClientTrusted(
				java.security.cert.X509Certificate[] chain,
				String authType)
				throws java.security.cert.CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(
				java.security.cert.X509Certificate[] chain,
				String authType)
				throws java.security.cert.CertificateException {
			// TODO Auto-generated method stub
			
		}

		};
		ctx.init(null, new TrustManager[]{tm}, null);
		SSLSocketFactory ssf = new SSLSocketFactory(ctx);
		ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


		ClientConnectionManager ccm = httpclient.getConnectionManager();
		
		SchemeRegistry sr = ccm.getSchemeRegistry();
		sr.register(new Scheme("https", ssf, 443));
		
		
		
		httpclient.getCredentialsProvider().setCredentials(
		        new AuthScope(targetHost),
		        new UsernamePasswordCredentials(username, password));

		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);

		// Add AuthCache to the execution context
		localHTTPContext.setAttribute(ClientContext.AUTH_CACHE, authCache);
		
		
		} catch (Exception e) {
			logger.info("LabConController: : ############### Labcon controller initialize connection failed ############### " + e);
			e.printStackTrace();
		}
		*/
		
	}
	
}

package org.fortiss.smg.webrest.test.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.KeyManager;

import org.fortiss.smg.usermanager.api.KeyManagerInterface;
import org.fortiss.smg.webrest.impl.Constants;
import org.fortiss.smg.webrest.test.server.Const;
import org.junit.Assert;

public class AuthenticationHelper {

    public static KeyManagerInterface keyManager;

    public static String generateValidURL(String urlInput, int port)
            throws UnsupportedEncodingException {
        String url = "";
        Random rand = new Random();
        if (urlInput.contains("?")) {
            url = Const.getServerURL(port) + urlInput + "&accesskey="
                    + Const.getAccessKey();
        } else {
            url = Const.getServerURL(port) + urlInput + "?accesskey="
                    + Const.getAccessKey();

        }
        url = url + "&timestamp=" + new Date().getTime() / 1000  + "&authversion=1"
                + "&nonce=" + Math.abs(rand.nextLong());
        String signature = calcSignature(
                Const.getSecretKey(), url);
        signature = java.net.URLEncoder.encode(signature, "UTF8");
        url = url + "&signature=" + signature;
        //return url + "&signature=" + signature;
        return url;

    }

    public static KeyManagerInterface getKeyManager() {
        if (AuthenticationHelper.keyManager == null) {
            AuthenticationHelper.keyManager = ClientHelper.getMockKeyManager();
        }
        return AuthenticationHelper.keyManager;
    }

    public static String testResourceAtUrl(String s) throws Exception {
        return AuthenticationHelper.testResourceAtUrl(new URL(s));
    }

    public static String testResourceAtUrl(URL url) throws Exception {

        try {
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));
            String firstLineOfText = reader.readLine();// you can also read the
                                                       // whole thing and then
                                                       // test
            System.out.println("Read: " + firstLineOfText);

            System.out
                    .println("System was initialized correctly. About to run actual tests...");

            connection.disconnect();

            return firstLineOfText;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("No Acess/file for " + url.toString());
        }

        throw new Exception("could not establish connection to "
                + url.toExternalForm());
    }

    /**
     * returns some possible wrong logins should a lead to NO login in api
     * 
     * @param s
     * @return
     */
    public static ArrayList<String> wrongLogins(String s, int port) {
        ArrayList<String> url = new ArrayList<String>();

        String url_temp = "";
        String signature;

        Random rand = new Random();

        // WRONG timestamp
        url_temp = "";

        int max = Constants.MAX_TIMESTAMP_DIFFERENCE + 12;
        if (s.contains("?")) {
            url_temp = Const.getServerURL(port) + s + "&accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + (new Date().getTime() - max) + "&nonce="
                    + rand.nextLong();
        } else {
            url_temp = Const.getServerURL(port) + s + "?accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + (new Date().getTime() - max) + "&nonce="
                    + rand.nextLong();
        }
        signature = calcSignature(
                Const.getSecretKey(), url_temp);
        url_temp = url_temp + "&signature=" + signature;

        url.add(url_temp);

        // WRONG public key
        url_temp = "";
        if (s.contains("?")) {
            url_temp = Const.getServerURL(port) + s + "&accesskey="
                    + "abcedgefghh" + "&timestamp=" + new Date().getTime()
                    + "&nonce=" + rand.nextLong();
        } else {
            url_temp = Const.getServerURL(port) + s + "?accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + new Date().getTime() + "&nonce=" + rand.nextLong();
        }
        signature = calcSignature(
                Const.getSecretKey(), url_temp);
        url_temp = url_temp + "&signature=" + signature;

        url.add(url_temp);

        // WRONG signature
        url_temp = "";
        if (s.contains("?")) {
            url_temp = Const.getServerURL(port) + s + "&accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + new Date().getTime() + "&nonce=" + rand.nextLong();
        } else {
            url_temp = Const.getServerURL(port) + s + "?accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + new Date().getTime() + "&nonce=" + rand.nextLong();
        }
        signature = "i_am_a_test";
        url_temp = url_temp + "&signature=" + signature;

        url.add(url_temp);

        // WRONG signature
        url_temp = "";
        if (s.contains("?")) {
            url_temp = Const.getServerURL(port) + s + "&accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + new Date().getTime() + "&nonce=" + rand.nextLong();
        } else {
            url_temp = Const.getServerURL(port) + s + "?accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + new Date().getTime() + "&nonce=" + rand.nextLong();
        }
        signature = calcSignature(
        		Const.getSecretKey(), s);
        url_temp = url_temp + "&signature=" + signature;

        url.add(url_temp);

        // without signature
        url_temp = "";
        if (s.contains("?")) {
            url_temp = Const.getServerURL(port) + s + "&accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + new Date().getTime() + "&nonce=" + rand.nextLong();
        } else {
            url_temp = Const.getServerURL(port) + s + "?accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + new Date().getTime() + "&nonce=" + rand.nextLong();
        }
        signature = calcSignature(
        		Const.getSecretKey(), url_temp);
        // url_temp = url_temp + "&signature=" + signature;

        url.add(url_temp);

        // without public key
        url_temp = "";
        if (s.contains("?")) {
            url_temp = Const.getServerURL(port) + s + "&timestamp="
                    + new Date().getTime() + "&nonce=" + rand.nextLong();
        } else {
            url_temp = Const.getServerURL(port) + s + "&timestamp="
                    + new Date().getTime() + "&nonce=" + rand.nextLong();
        }
        signature = calcSignature(
        		Const.getSecretKey(), url_temp);
        url_temp = url_temp + "&signature=" + signature;

        url.add(url_temp);

        // without timestamp
        url_temp = "";
        if (s.contains("?")) {
            url_temp = Const.getServerURL(port) + s + "&accesskey="
                    + Const.getAccessKey();
        } else {
            url_temp = Const.getServerURL(port) + s + "?accesskey="
                    + Const.getAccessKey();
        }
        signature = calcSignature(
        		Const.getSecretKey(), url_temp);
        url_temp = url_temp + "&signature=" + signature;

        url.add(url_temp);

        // without anything
        url_temp = "";
        url_temp = s;

        url.add(url_temp);

        // tiny difference in signature
        url_temp = "";
        if (s.contains("?")) {
            url_temp = Const.getServerURL(port) + s + "&accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + new Date().getTime();
        } else {
            url_temp = Const.getServerURL(port) + s + "?accesskey="
                    + Const.getAccessKey() + "&timestamp="
                    + new Date().getTime();
        }
        signature = calcSignature(
        		Const.getSecretKey(), url_temp + "a");
        url_temp = url_temp + "&signature=" + signature;

        url.add(url_temp);

        return url;
    }
    
    private static String calcSignature(String key, String data){
    	try {
			return AuthenticationHelper.getKeyManager().calcSignature(key, data);
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Failed to calculate Signature");
		}
		return "";
    }

}

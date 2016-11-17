package org.fortiss.smg.prophet.components.smgCalendar;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Properties;

/**
 * This class aids in authenticating https iCal requests
 * 
 * @author Orest Tarasiuk
 * @thesisOT
 * 
 */
public class SmgCalendarIcsConverterAuth extends Authenticator {
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    private final PasswordAuthentication authentication;

    public SmgCalendarIcsConverterAuth(Properties properties) {
        String userName = properties
                .getProperty(SmgCalendarIcsConverterAuth.USERNAME_KEY);
        String password = properties
                .getProperty(SmgCalendarIcsConverterAuth.PASSWORD_KEY);
        if (userName == null || password == null) {
            authentication = null;
        } else {
            authentication = new PasswordAuthentication(userName,
                    password.toCharArray());
        }
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return authentication;
    }
}

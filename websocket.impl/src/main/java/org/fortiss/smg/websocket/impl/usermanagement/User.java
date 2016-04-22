package org.fortiss.smg.websocket.impl.usermanagement;

public class User {

    private String password;
    private String userName;
    private String mac;
    public User(String password, String userName) {
        super();
        this.password = password;
        this.userName = userName;
    }
    public User(String mac) {
        super();
        this.mac = mac;
    }
    public User() {
        // TODO Auto-generated constructor stub
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    
    
}

package org.fortiss.smg.usermanager.impl.key;

import org.fortiss.smg.usermanager.api.Key;

public class KeyDB extends Key implements Cloneable {

    private String privateKey;

    @Override
    public KeyDB clone() {
	KeyDB key = (KeyDB) super.clone();
	key.setPublicKey(privateKey);
	return key;
    }

    public String getPrivateKey() {
	return privateKey;
    }

    public void setPrivateKey(String privateKey) {
	this.privateKey = privateKey;
    }

}
/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
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
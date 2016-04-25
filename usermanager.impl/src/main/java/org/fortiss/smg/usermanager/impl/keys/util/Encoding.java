package org.fortiss.smg.usermanager.impl.keys.util;

import org.apache.commons.codec.binary.Base64;

public class Encoding {
    /**
     * Performs base64-encoding of input bytes.
     * 
     * @param rawData
     *            * Array of bytes to be encoded.
     * @return * The base64 encoded string representation of rawData.
     */
    public static String EncodeBase64(byte[] rawData) {
        return Base64.encodeBase64String(rawData);
    }
}
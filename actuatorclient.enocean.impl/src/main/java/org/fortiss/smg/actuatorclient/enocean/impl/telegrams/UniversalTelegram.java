/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.telegrams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniversalTelegram {

    protected static final int SENSOR_ID_LENGTH = 4;
    protected static final int DATA_BYTE_LENGTH = 4;

    protected EnOceanOrigin org;
    protected char[] databytes;
    protected char[] id;
    protected char status;

    protected char prefixAddition;

    private static final Logger logger = LoggerFactory
            .getLogger(UniversalTelegram.class);

    public UniversalTelegram() {

        org = EnOceanOrigin.UNKNOWN;
        status = (char) 0x00;

        databytes = new char[4];
        id = new char[4];

        // init with zero
        for (int i = 0; i < 4; i++) {
            databytes[i] = 0x00;
            id[i] = 0x00;
        }
    }

    public char getDataByte(int i) {
        return databytes[i];
    }

    public char getIdByte(int i) {
        return id[i];
    }

    public String getTelegramString() {
        return org.name() + " " + getDataString() + " " + getIdString() + " "
                + StringByteUtils.byteCharToHexString(status);
    }

    public void setDataByte(char b, int position) {
        if (position >= 0 && position < databytes.length) {
            databytes[position] = b;
        } else {
            logger.debug("We tried to access the array at " + position
                    + ". Pls try to look at the enocean protocoll");
        }
    }

    public void setIdByte(char b, int position) {
        id[position] = b;
    }

    public void setIdHexString(String id, int channel)
            throws IllegalArgumentException {

        if (channel > 127) {
            throw new IllegalArgumentException(
                    "Valid channels for EnOcean-Devices are [0, 127]. Input was "
                            + channel);
        }

        if (id.length() == 8) {
            setIdByte((char) (Integer.valueOf(id.substring(0, 2), 16)
                    .intValue() & 0xFF), 3);
            setIdByte((char) (Integer.valueOf(id.substring(2, 4), 16)
                    .intValue() & 0xFF), 2);
            setIdByte((char) (Integer.valueOf(id.substring(4, 6), 16)
                    .intValue() & 0xFF), 1);
            setIdByte((char) ((Integer.valueOf(id.substring(6, 8), 16)
                    .intValue() + channel) & 0xFF), 0);
        }
    }

    public int getIdInt() {
        int value = 0;
        for (int i = 3; i >= 0; i--) {
            value = value << 8;
            value += getIdByte(i);
        }
        return value;
    }

    public int getDataInt() {
        int value = 0;
        for (int i = 0; i <= 3; i++) {
            value = value << 8;
            value += getDataByte(i);
        }
        return value;
    }

    /**
     * Get sensor ID as a string in reverse order like it is being sent over the
     * wire.
     * 
     * @return the sensor id
     */
    public String getIdString() {
        return StringByteUtils.byteCharToHexString(id[3])
                + StringByteUtils.byteCharToHexString(id[2])
                + StringByteUtils.byteCharToHexString(id[1])
                + StringByteUtils.byteCharToHexString(id[0]);
    }

    public String getDataString() {
        return StringByteUtils.byteCharToHexString(databytes[3])
                + StringByteUtils.byteCharToHexString(databytes[2])
                + StringByteUtils.byteCharToHexString(databytes[1])
                + StringByteUtils.byteCharToHexString(databytes[0]);
    }

    @Override
    public int hashCode() {
        return getTelegramString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UniversalTelegram other = (UniversalTelegram) obj;
        return this.getTelegramString().equals(other.getTelegramString());
    }

    @Override
    public String toString() {
        return "AbstractTelegram [telegramBytes=" + getTelegramString() + "]";
    }

    public EnOceanOrigin getOrg() {
        return org;
    }

    public char getStatus() {
        return status;
    }

    public void setOrg(EnOceanOrigin org) {
        this.org = org;
    }

    public void setStatus(char state) {
        this.status = state;
    }

    public void setStatus(char status, char tc, char rpc) {
        setStatus((char) (((status << 4) + (tc << 2) + (rpc)) & 0xFF));
    }
}

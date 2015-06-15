/*
 * Copyright (c) 2011-2015, fortiss GmbH.
 * Licensed under the Apache License, Version 2.0.
 *
 * Use, modification and distribution are subject to the terms specified
 * in the accompanying license file LICENSE.txt located at the root directory
 * of this software distribution.
 */
package org.fortiss.smg.actuatorclient.enocean.impl.telegrams;

import jonelo.jacksum.algorithm.Crc8;

public class ESP3Telegram extends UniversalTelegram {

    private static final int ORG_OFFSET = 6;
    private static final int DATA_OFFSET = 7;
    private static int STATUS_OFFSET = 12;

    char dBm;
    char[] header;
    char[] optional;

    public ESP3Telegram(String byteString, boolean incoming)
            throws IllegalTelegramException {

        this(StringByteUtils.readBytes(byteString), incoming);

    }

    public ESP3Telegram() {
        dBm = 0xFF;
        header = new char[5];
        createHeader();
        optional = new char[7];
        createOptionalDefaultData();
    }

    public ESP3Telegram(UniversalTelegram telegram) {
        this();
        this.databytes = telegram.databytes;
        this.id = telegram.id;
        this.org = telegram.org;
        this.status = telegram.status;
    }

    public ESP3Telegram(char[] telegrambytes, boolean incoming) {
        this();

        switch (telegrambytes[ORG_OFFSET]) {
            case 0x08:
                // TODO: check this. (ESP3 spec, 1.11.10): Packet type 0x05,
                // data: 0x08
                // checked by Sebi - but why do we need
                // "Function: Enables/Disables postmaster function of device."
                setOrg(EnOceanOrigin.ID);
                break;
            case 0xF6:
                STATUS_OFFSET = 12;
                setOrg(EnOceanOrigin.EEP_RPS);
                break;
            case 0xD5:
                STATUS_OFFSET = 12;
                setOrg(EnOceanOrigin.EEP_1BS);
                break;
            case 0xA5:
                STATUS_OFFSET = 15;
                setOrg(EnOceanOrigin.EEP_4BS);
                break;
            case 0x00:
                setOrg(EnOceanOrigin.ID_RESP);
                break;
            default:
                setOrg(EnOceanOrigin.UNKNOWN);
                break;
        }

        int dataLength1 = telegrambytes[1];
        int dataLength2 = telegrambytes[2];

        // why the heck not *2 ??
        int dataLength = (dataLength1 << 1) + dataLength2;

        int payloadDataLength = dataLength - 6;

        for (int i = 0; i < payloadDataLength; i++) {
            int dbindex;
            if (org.equals(EnOceanOrigin.EEP_1BS)) {
                dbindex = i;
            } else {
                dbindex = 3 - i;
            }
            if (DATA_OFFSET + i < telegrambytes.length) {
                setDataByte(telegrambytes[DATA_OFFSET + i], dbindex);
            }
        }
        int idOffset = DATA_OFFSET + payloadDataLength;
        for (int i = 0; i < 4; i++) {
            if (idOffset + i < telegrambytes.length) {
                setIdByte(telegrambytes[idOffset + i], 3 - i);
            }
        }
        setStatus(telegrambytes[STATUS_OFFSET]);

    }

    @Override
    public String toString() {
        return "DataTelegramIn [org=" + getOrg().toString() + ", dataByte="
                + getDataString() + ", sensorId=" + getIdInt() + "("
                + getIdString() + "), status="
                + Integer.toHexString(getStatus()) + /*
                                                      * ", channel=" +
                                                      * Integer.toHexString
                                                      * (getChannel()) +
                                                      */"]";
    }

    private char telegramChecksum(boolean header) {

        char[] tobechecked;
        if (header) {
            tobechecked = new char[this.header.length - 1]; // remove sync byte
            for (int i = 0; i < tobechecked.length; i++) {
                tobechecked[i] = this.header[i + 1];
            }
        } else {
            tobechecked = StringByteUtils.readBytes(getBodyString()
                    + getOptionalDataString());
        }

        Crc8 crc8 = new Crc8();
        for (char b : tobechecked) {
            crc8.update(b);
        }
        return StringByteUtils.byteToChar(crc8.getByteArray()[0]);
    }

    protected char translateOrg() {
        switch (getOrg()) {
            case ID:
                // TODO see above
                return 0x08;
            case EEP_RPS:
                return 0xF6;
            case EEP_1BS:
                return 0xD5;
            case EEP_4BS:
                return 0xA5;
            default:
                return 0x00;
        }
    }

    public String getTelegramString() {
        return getHeaderString()
                + StringByteUtils.byteCharToHexString(telegramChecksum(true))
                + getBodyString() + getOptionalDataString()
                + StringByteUtils.byteCharToHexString(telegramChecksum(false));
    }

    public char[] getTelegramBytes() {
        return StringByteUtils.readBytes(getTelegramString()); // TODO: dirty
                                                               // hack..
    }

    private String getBodyString() {
        String dataString = getDataString();
        if (!EnOceanOrigin.EEP_4BS.equals(getOrg())) {
            dataString = dataString.substring(0, 2);
        }
        return StringByteUtils.byteCharToHexString(translateOrg()) + dataString
                + getIdString()
                + StringByteUtils.byteCharToHexString(getStatus());
    }

    protected String getHeaderString() {
        createHeader();
        return StringByteUtils.byteCharsToHexString(header);
    }

    private String getOptionalDataString() {
        createOptionalDefaultData();
        return StringByteUtils.byteCharsToHexString(optional);
    }

    private void createOptionalDefaultData() {
        optional[0] = 0x03; // subtelegram number SEND 3 RECEIVE 1!!! TODO TODO
                            // receive necessary?
        optional[1] = 0xFF;
        optional[2] = 0xFF;
        optional[3] = 0xFF;
        optional[4] = 0xFF;
        optional[5] = dBm; // signal strength
        optional[6] = 0x00; // Crypto
    }

    private void createHeader() {
        header[0] = 0x55;
        header[1] = 0x00;
        if (EnOceanOrigin.EEP_4BS.equals(getOrg())) {
            header[2] = 0x0A;
        } else {
            header[2] = 0x07;
        }
        header[3] = 0x07;
        header[4] = 0x01;

        // TODO see top and check this
        if (EnOceanOrigin.ID.equals(getOrg())) {
            header[2] = 0x01;
            header[3] = 0x00;
            header[4] = 0x05;
        }
    }

}
/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to Generate a Cript Hash.
 * @author Felipe de Andrade Batista.
 */
public class CriptografiaUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CriptografiaUtil.class);

    private static final char[] HEXADECIMAL = new char[]{
        '0', '1', '2', '3', '4',
        '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Generate a SHA256 Hash to criptograph.
     * @param value
     * @return
     */
    public static String gerarHashSHA256(String value) {
        String hash = null;
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = algorithm.digest(value.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", 255 & b));
            }
            hash = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("ERROR: A particular cryptographic algorithm is"
                    + " requested but is not available in the environment.", e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("ERROR: The Character Encoding is not supported.", e);
        }
        return hash;
    }

    /**
     * Generate a Hash to criptograph.
     * @param value
     * @param msgDigest
     * @return
     */
    public static String gerarHash(String value, MessageDigest msgDigest) {
        msgDigest.reset();
        byte[] bytes = msgDigest.digest(value.getBytes());
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (int i = 0; i < bytes.length; ++i) {
            int low = bytes[i] & 15;
            int high = (bytes[i] & 240) >> 4;
            sb.append(HEXADECIMAL[high]);
            sb.append(HEXADECIMAL[low]);
        }
        return sb.toString();
    }
}

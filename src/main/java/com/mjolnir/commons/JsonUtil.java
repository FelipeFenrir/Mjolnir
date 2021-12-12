/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe Util para Conversão de Objeto Json.
 * Util class to do:
 *
 * - MARSHALLING: convert string json or file into Object
 * return an object
 *
 * - UNMARSHALLING: convert Object into string json
 * return a string json
 *
 * @author Felipe de Andrade Batista
 */
public class JsonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * Contrutor.
     */
    public JsonUtil() {
    }

    /**
     * Convert Jason to Object.
     * @param <T>
     * @param str
     * @param clazz
     * @return Object.
     * @throws IOException
     */
    public static <T> T json2Object(String str, Class<T> clazz)
            throws IOException {
        LOGGER.debug("Conversão de JSON: ");
        LOGGER.debug(str);
        LOGGER.debug("Para Object (" + clazz.getName() + ")");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(str, clazz);
    }

    /**
     * Convert Object to JSON String.
     * @param <T>
     * @param obj
     * @return String in JSON format.
     * @throws IOException
     */
    public static <T> String object2Json(T obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String str = objectMapper.writeValueAsString(obj);
        LOGGER.debug("Conversão de Object (" + obj.getClass().getName()
                + ")");
        LOGGER.debug("Para JSON: ");
        LOGGER.debug(str);
        return str;
    }

    /**
     * Convert Json File in Object.
     * @param <T>
     * @param fileName
     * @param clazz
     * @return Object.
     * @throws IOException
     */
    public static <T> T jsonFile2Object(String fileName, Class<T> clazz)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        //Ignoring missing fields in model objects
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(new File(concatenate(fileName)), clazz);
    }

    /**
     * Concatenate Path to resources for test with File Name.
     * @param fileName
     * @return Path to resources for test.
     */
    private static String concatenate(String fileName) {
        return "src/test/resources/" + fileName;
    }
}

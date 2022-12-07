/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.toolbox.stardart;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
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
@Slf4j
@NoArgsConstructor
public class JsonUtil {

    public static <T> T json2Object(String str, Class<T> clazz)
            throws IOException {
        log.debug("Json to convert: ");
        log.debug(str);
        log.debug(" to object fom class (" + clazz.getName() + ")");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper.readValue(str, clazz);
    }

    public static <T> String object2Json(T obj) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String str = objectMapper.writeValueAsString(obj);
        log.debug("Convert the Object (" + obj.getClass().getName() + ")");
        log.debug("to JSON: ");
        log.debug(str);
        return str;
    }

    public static <T> T jsonFile2Object(String fileName, Class<T> clazz)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        //Ignoring missing fields in model objects
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(new File(concatenate(fileName)), clazz);
    }

    private static String concatenate(String fileName) {
        return "src/test/resources/" + fileName;
    }
}

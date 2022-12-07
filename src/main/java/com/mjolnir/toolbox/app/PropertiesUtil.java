/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.toolbox.app;

import com.mjolnir.toolbox.stardart.StringUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>
 *     Auxiliary class to read config file.
 * </p>
 *
 * @author Felipe de Andrade Batista
 */
@Slf4j
@Getter
public final class PropertiesUtil {

    private static final String CONFIG_FILE = "config.properties";

    /**
     * <p>
     *     Begin a instance.
     * </p>
     */
    private static PropertiesUtil instance = new PropertiesUtil();

    private final Properties prop = new Properties();
    private InputStream input = null;

    /**
     * <p>
     *     No args constructor.
     * </p>
     */
    PropertiesUtil() {
        init();
    }

    /**
     * <p>
     *     Get instance method.
     * </p>
     * @return PropertiesUtil instance.
     */
    public static synchronized PropertiesUtil getInstance() {
        if (instance == null) {
            instance = new PropertiesUtil();
        }
        return instance;
    }

    /**
     * <p>
     *     Load the config file in resources path.
     * </p>
     */
    public void init() {
        try {
            input = PropertiesUtil.class.getClassLoader()
                    .getResourceAsStream(CONFIG_FILE);
            if (input == null) {
                log.info("Sorry, unable to find file: " + CONFIG_FILE);
                return;
            }
            prop.clear();
            prop.load(input);

//            if (LOGGER.isDebugEnabled()) {
//                Enumeration<?> e = prop.propertyNames();
//                while (e.hasMoreElements()) {
//                    String key = (String) e.nextElement();
//                    String value = getPropertieByKey(key);
//                    System.out.println("Key : " + key + ", Value : " + value);
//                }
//            }
        } catch (IOException ex) {
            log.error("Error to load Resource as Stream.", ex);
        }   finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    log.error("Error to close the InputStream.", e);
                }
            }
        }
    }

    /**
     * <p>
     *     Verify if property exists.
     * </p>
     * @param key Property key.
     * @return True, if property exists else False
     */
    public Boolean isValidPropertie(String key) {
        if (!StringUtil.isNullOrBlank(key)) {
            return prop.containsKey(key);
        } else {
            return false;
        }
    }

    /**
     * <p>
     *     Get property value.
     * </p>
     * @param key Property key.
     * @return Property value.
     */
    public String getPropertieByKey(String key) {
        if (isValidPropertie(key)) {
            return prop.getProperty(key);
        } else {
            throw new IllegalArgumentException("This property does not exists.");
        }
    }
}

/*
 * Copyright (c) 2021. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */

package com.mjolnir.commons;

import java.util.HashMap;
import java.util.Map;

/**
 * Class of environment utils.
 *
 * @author Felipe de Andrade Batista
 */
public class EnvUtil {
    /**
     * HTTP type.
     */
    public static final String HTTP_SCHEME = "http";
    /**
     * HTTPS type.
     */
    public static final String HTTPS_SCHEME = "https";

    /**
     * Static variable single_instance of type EnvUtil.
     */
    private static EnvUtil singleInstance = null;

    private String serverPort;
    private String httpPort;
    private String hostAddress;
    private String hostName;
    private String applicationPath;

    /**
     * Private constructor restricted to this class itself.
     */
    protected EnvUtil() {
    }

    /**
     * Static method to create instance of Singleton class.
     *
     * @return Instance of EnvUtil.
     */
    public static synchronized EnvUtil getInstance() {
        if (singleInstance == null) {
            singleInstance = new EnvUtil();
        }

        return singleInstance;
    }

    /**
     * <p>Get Server port.</p>
     * <p>
     * Ex: 8080.
     * </p>
     * @return Server Port.
     */
    public static String getServerPort() {
        return getInstance().serverPort;
    }

    /**
     * <p>Set Server Port.</p>
     * @param serverPort Server Port.
     */
    public static void setServerPort(String serverPort) {
        getInstance().serverPort = serverPort;
    }

    /**
     * <p>Get Host Address.</p>
     * <p>
     * Ex: 172.0.0.1
     * </p>
     * @return Server Port.
     */
    public static String getHostAddress() {
        return getInstance().hostAddress;
    }

    /**
     * <p>Set Host Address.</p>
     * @param hostAddress Address of Host.
     */
    public static void setHostAddress(String hostAddress) {
        getInstance().hostAddress = hostAddress;
    }

    /**
     * <p>Get Host Name.</p>
     * <p>
     * Ex: localhost.
     * </p>
     * @return Server Port.
     */
    public static String getHostName() {
        return getInstance().hostName;
    }

    /**
     * <p>Set Host Name.</p>
     * @param hostName Name of Host.
     */
    public static void setHostName(String hostName) {
        getInstance().hostName = hostName;
    }

    /**
     * <p>Get Application Path.</p>
     * <p>
     * Ex: .../path_for_paplication/...
     * </p>
     * @return Server Port.
     */
    public static String getApplicationPath() {
        return getInstance().applicationPath;
    }

    /**
     * <p>Set Application Path.</p>
     * @param applicationPath Name of Application Path.
     */
    public static void setApplicationPath(String applicationPath) {
        getInstance().applicationPath = applicationPath;
    }

    /**
     * <p>Get Http Port.</p>
     * <p>
     * Ex: 8080
     * </p>
     * @return Server Port.
     */
    public static String getHttpPort() {
        return getInstance().httpPort;
    }

    /**
     * <p>Set Http port.</p>
     * @param httpPort Name of Http port.
     */
    public static void setHttpPort(String httpPort) {
        getInstance().httpPort = httpPort;
    }

    /**
     * <p>Get complete Host Name and Server Port.</p>
     * <p>Exe: localhost:8080</p>
     * <p>Exe: localhost:8080/auth</p>
     * @return Complete HostName.
     */
    public Map<String, String> getCompleteNetHost() {
        return getCompleteHostURL(false);
    }

    /**
     * <p>Get complete Host Address and Server Port.</p>
     * <p>Exe: 172.0.0.1:8080</p>
     * <p>Exe: 172.0.0.1:8080/auth</p>
     * @return Complete HostAddress.
     */
    public Map<String, String> getCompleteNetAddress() {
        return getCompleteHostURL(true);
    }

    /**
     * Get Complete URL of HOST.
     * @param inNetAddress True is Net Address and False is HostName.
     * @return Complete HOST Url.
     */
    private Map<String, String> getCompleteHostURL(boolean inNetAddress) {
        final Map<String, String> type = new HashMap<>();

        if (StringUtil.isNullOrBlank(getHttpPort())) {
            type.put(HTTP_SCHEME, "http://"
                    .concat(inNetAddress ? getHostAddress() : getHostName())
                    .concat(":")
                    .concat(getServerPort()));
        } else {
            type.put(HTTP_SCHEME, "http://"
                    .concat(inNetAddress ? getHostAddress() : getHostName())
                    .concat(":")
                    .concat(getHttpPort())
            );
            type.put(HTTPS_SCHEME, "https://"
                    .concat(inNetAddress ? getHostAddress() : getHostName())
                    .concat(":")
                    .concat(getServerPort()));
        }

        if (!StringUtil.isNullOrBlank(getApplicationPath())) {
            for (Map.Entry<String, String> entry : type.entrySet()) {
                entry.setValue(entry.getValue().concat(getApplicationPath()));
            }
        }
        return type;
    }
}

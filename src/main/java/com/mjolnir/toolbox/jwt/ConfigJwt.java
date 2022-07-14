package com.mjolnir.toolbox.jwt;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConfigJwt {
    private static final String ALG_KEY = "alg";
    private static final String TYP_KEY = "typ";
    private static final String TYP_VAL = "JWT";

    private String algorithm;
    private String secretKey;
    private final Map<String, Object> configMap = new HashMap<>();

    public ConfigJwt(String algorithm) {
        log.debug("DEBUG: ConfigAuth Object Create for JwtUtil utilization.");
        this.algorithm = algorithm.toUpperCase();
        configMap.put(ALG_KEY, this.algorithm);
        configMap.put(TYP_KEY, TYP_VAL);
    }

    public ConfigJwt(String secretKey, String algorithm) {
        log.debug("DEBUG: ConfigAuth Object Create for JwtUtil utilization.");
        this.secretKey = secretKey;
        this.algorithm = algorithm.toUpperCase();
        configMap.put(ALG_KEY, this.algorithm);
        configMap.put(TYP_KEY, TYP_VAL);
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm.toUpperCase();
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * <p>
     *     This Method return a Map with Basic Configuration for Jwt.
     * </p>
     * @return Map with config information.
     */
    public Map<String, Object> getConfigAuthMap() {
        log.debug("DEBUG: Get Map of Informations in ConfigAuth Object.");
        return this.configMap;
    }
}

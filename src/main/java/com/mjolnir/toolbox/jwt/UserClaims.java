package com.mjolnir.toolbox.jwt;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UserClaims {
    private String userID;
    private String issuerName;
    private String audienceName;
    private String userName;
    private String json;
    private Map<String, String> permissions;
    private Map<String, Map<?,?>> roles;

    /**
     * Constructor.
     */
    public UserClaims() {
        log.debug("DEBUG: UserClaims Object Create for JwtUtil utilization.");
    }

    /**
     * Constructor.
     * @param userID The user identification
     * @param issuerName Issuer Name
     * @param audienceName Token recipient
     */
    public UserClaims(String userID, String issuerName, String audienceName) {
        log.debug("DEBUG: UserClaims Object Create for JwtUtil utilization.");
        this.userID = userID;
        this.issuerName = issuerName;
        this.audienceName = audienceName;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getIssuerName() {
        return this.issuerName;
    }

    public String getAudienceName() {
        return this.audienceName;
    }

    public void setAudienceName(String audienceName) {
        this.audienceName = audienceName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

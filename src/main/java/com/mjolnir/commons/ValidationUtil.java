/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *     Commons validations utils.
 * </p>
 *
 * @author Felipe de Andrade Batista
 */
@Slf4j
public class ValidationUtil {

    private static final String EMAIL_PATTERN = RegExUtil.REGEX_EMAIL_PATTERN;
    private static final String DOMAIN_PATTERN = RegExUtil.REGEX_DOMAIN_PATTERN;

    private static final Pattern pEmailOnly;
    private static final Pattern pDomainOnly;

    /*
     * SetUp Patterns.
     */
    static {
        pEmailOnly = Pattern.compile(EMAIL_PATTERN);
        pDomainOnly = Pattern.compile(DOMAIN_PATTERN);
    }

    /**
     * <p>
     *     Validate email.
     * </p>
     *
     * @param email Email to validate
     * @return True if valid email
     */
    public static boolean isEmail(String email) {
        if (StringUtil.isNullOrBlank(email)) {
            return false;
        }
        //Pattern p = Pattern.compile(EMAIL_PATTERN);
        //Matcher m = p.matcher(email);
        Matcher m = pEmailOnly.matcher(email);
        return m.matches();
    }

    /**
     * <p>
     *     Validate domain.
     * </p>
     *
     * @param domain Domain
     * @return True if domain is valid
     */
    public static boolean isDomain(String domain) {
        if (StringUtil.isNullOrBlank(domain)) {
            return false;
        }
        return pDomainOnly.matcher(domain).find();
    }
}

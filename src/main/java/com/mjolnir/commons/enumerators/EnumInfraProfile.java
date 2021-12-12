/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons.enumerators;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *      Enumerador de Perfis de Infraestrutura.
 * </p>
 * <p>
 *      São eles:
 * </p>
 * <br>
 * <ul>
 *      <li>native - Local na maquina do desenvolvedor.</li>
 *      <li>development - Ambiente de Desenvolvimento.</li>
 *      <li>staging - Ambiente de Qualidade e Homologação.</li>
 * </ul>
 *
 * @author Felipe de Andrade Batista
 */
@AllArgsConstructor
@Getter
public enum EnumInfraProfile {

    /**
     * <p>
     *     Valores para o enumerador.
     *     TEST: (test)
     * </p>
     */
    TEST(ProfileConstants.TEST_ENVIRONMENT),
    /**
     * <p>
     *     Valores para o enumerador.
     *     DEFAULT: (default)
     * </p>
     */
    DEFAULT(ProfileConstants.DEFAULT_ENVIRONMENT),
    /**
     * <p>
     *     Valores para o enumerador.
     *     NATIVE: (native)
     * </p>
     */
    NATIVE(ProfileConstants.NATIVE_ENVIRONMENT),
    /**
     * <p>
     *     Valores para o enumerador.
     *     DEV: (development)
     * </p>
     */
    DEV(ProfileConstants.DEV_ENVIRONMENT),
    /**
     * <p>
     *     Valores para o enumerador.
     *     STAGING: (staging)
     * </p>
     */
    STAGING(ProfileConstants.STAGING_ENVIRONMENT);

    private final String name;

    /**
     * <p>
     *     Retorna uma lista de valores do Enum.
     * </p>
     *
     * @return Lista de valores de Profiles
     */
    public static List<EnumInfraProfile> asList() {
        return Arrays.asList(EnumInfraProfile.values());
    }

    /**
     * <p>
     *     Class of Profile Constants.
     * </p>
     */
    public static class ProfileConstants {

        /**
         * <p>
         *     Test (Unit Test).
         * </p>
         */
        public static final String TEST_ENVIRONMENT = "test";

        /**
         * <p>
         *     Default (Developer Machine).
         * </p>
         */
        public static final String DEFAULT_ENVIRONMENT = "default";

        /**
         * <p>
         *     Native (Developer Machine).
         * </p>
         */
        public static final String NATIVE_ENVIRONMENT = "native";

        /**
         * <p>
         *     Development.
         * </p>
         */
        public static final String DEV_ENVIRONMENT = "development";

        /**
         * <p>
         *     Staging (Homologation).
         * </p>
         */
        public static final String STAGING_ENVIRONMENT = "staging";
    }
}

/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ColecaoUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ColecaoUtil.class);

    private ColecaoUtil() {}

    public static Boolean ehNuloOuVazio(Collection<?> colecao) {
        Boolean ret = Boolean.TRUE;
        if (colecao != null && !colecao.isEmpty()) {
            ret = Boolean.FALSE;
        }
        return ret;
    }

    /**
     * Converte uma Coleção para uma Coleção do tipo Long.
     *
     * @param colecao
     * @return Coleção Long {@link Collection<Long>}
     */
    public static Collection<Long> converteParaColecaoLong(Collection<?> colecao) {
        if (ColecaoUtil.ehNuloOuVazio(colecao)) {
            return null;
        }
        ArrayList<Long> resposta = new ArrayList<>();
        try {
            colecao.forEach((obj) -> {
                resposta.add(Long.parseLong(obj.toString()));
            });
            return resposta;
        } catch (Exception e) {
            LOGGER.info("INFO: Não foi possivel converter a coleção em uma coleção do tipo Long." + e);
            return null;
        }
    }
}

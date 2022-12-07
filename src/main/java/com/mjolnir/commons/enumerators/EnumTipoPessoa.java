/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons.enumerators;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *     Enumerador de Tipos de Pessoas para Registro de Empresas.
 * </p>
 * @author Felipe de Andrade Batista
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum EnumTipoPessoa {
    /**
     * <p>
     *     Valores para o enumerador.
     * </p>
     * </br>PESSOA_FISICA: (PF)
     * </br>PESSOA_JURIDICA: (PJ)
     */
    PESSOA_FISICA("PF", "Pessoa Física"), PESSOA_JURIDICA("PJ", "Pessoa Juridica");

    private String sigla;
    private String label;

    /**
     * <p>
     *     Retorna uma lista de valores do Enum.
     * </p>
     * @return List of values
     */
    public static List<EnumTipoPessoa> asList() {
        return Arrays.asList(EnumTipoPessoa.values());
    }
}

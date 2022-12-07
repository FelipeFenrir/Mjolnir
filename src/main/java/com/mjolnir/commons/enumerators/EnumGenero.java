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
 *     Enumerador de Tipos de Generos.
 * </p>
 *
 * @author Felipe de Andrade Batista
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum EnumGenero {
    /**
     * <p>
     *     Valores para o enumerador.
     *     <br>MASCULINO: (M)
     *     <br>FEMININO: (F)
     * </p>
     */
    MASCULINO("M", "Masculino"), FEMININO("F", "Feminino");

    private String sigla;
    private String label;

    /**
     * Retorna uma lista de valores do Enum.
     * @return List
     */
    public static List<EnumGenero> asList() {
        return Arrays.asList(EnumGenero.values());
    }
}

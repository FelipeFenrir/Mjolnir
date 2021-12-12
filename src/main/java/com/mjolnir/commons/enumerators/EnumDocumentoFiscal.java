/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons.enumerators;

import com.mjolnir.commons.PropertiesUtil;
import com.mjolnir.commons.StringUtil;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *     Enumerador de Tipos de Documentos Fiscais.
 * </p>
 *
 * @author Felipe de Andrade Batista
 */
@NoArgsConstructor
@AllArgsConstructor
public enum EnumDocumentoFiscal {
    /**
     * <p>
     *     Valores para o enumerador.
     *     NFE: (NFe,Nota Fiscal Eletrônica,1,44,BRAZIL)
     * </p>
     */
    NFE("NFe", "Nota Fiscal Eletronica", 1L, getInteger(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.documentofiscal.nfe.max.length")), "BRAZIL"),
    /**
     * <p>
     *     Valores para o enumerador.
     *     NFCE: (NFCe,Nota Fiscal de Consumidor Eletrônica,2,44,BRAZIL).
     * </p>
     */
    NFCE("NFCe", "Nota Fiscal de Consumidor Eletronica", 2L, getInteger(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.documentofiscal.nfe.max.length")), "BRAZIL"),
    /**
     * <p>
     *     Valores para o enumerador.
     *     CTE: (CTe,Conhecimento de Transporte Eletrônico,3,44,BRAZIL).
     * </p>
     */
    CTE("CTe", "Conhecimento de Transporte Eletrônico", 3L, getInteger(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.documentofiscal.nfe.max.length")), "BRAZIL"),
    /**
     * <p>
     *     Valores para o enumerador.
     *     CTEOS: (CTeOS,Conhecimento de Transporte Eletrônico para Outros Serviço,4,44,BRAZIL).
     * </p>
     */
    CTEOS("CTeOS", "Conhecimento de Transporte Eletrônico para Outros Serviço", 4L,
            getInteger(PropertiesUtil.getInstance()
                    .getPropertieByKey("util.brazil.documentofiscal.nfe.max.length")), "BRAZIL"),
    /**
     * <p>
     *     Valores para o enumerador.
     *     MDFE: (MDFe,Manifesto de Documento Fiscal Eletrônico,5,44,BRAZIL).
     * </p>
     */
    MDFE("MDFe", "Manifesto de Documento Fiscal Eletrônico", 5L, getInteger(PropertiesUtil
            .getInstance().getPropertieByKey("util.brazil.documentofiscal.nfe.max.length")), "BRAZIL");

    private String sigla;
    private String label;
    private Long id;
    private Integer maxLength;
    private String pais;

    /**
     * <p>
     *     Método de recuperação da sigla do documento fiscal.
     * </p>
     * @return Nome da sigla representante do documento fiscal
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * <p>
     *     Método de recuperação do nome do documento fiscal.
     * </p>
     * @return Label representante do documento fiscal
     */
    public String getLabel() {
        return label;
    }

    /**
     * <p>
     *     Método de recuperação de identificador interno do documento fiscal.
     * </p>
     * @return Identificador interno
     */
    public Long getId() {
        return id;
    }

    /**
     * <p>
     *     Método responsável por trazer a contagem de caracteres do documento fiscal.
     * </p>
     * @return Quantidade de caracteres do documento fiscal
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * <p>
     *     Método responsável por recuperar o país ao qual o documento fiscal pertence.
     * </p>
     * @return Recupera o País referente ao documento fiscal
     */
    public String getPais() {
        return pais;
    }

    /**
     * <p>
     *     Retorna uma lista de valores do Enum.
     *     @return List of values
     * </p>
     */
    public static List<EnumDocumentoFiscal> asList() {
        return Arrays.asList(EnumDocumentoFiscal.values());
    }

    private static int getInteger(String value) {
        if (StringUtil.isNullOrBlank(value)) {
            throw new IllegalArgumentException("ERRO: Valor não pode ser convertido para Integer.");
        }
        return Integer.parseInt(value);
    }
}

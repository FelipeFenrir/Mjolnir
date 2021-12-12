/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons;

import com.mjolnir.commons.enumerators.EnumDocumentoFiscal;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     Classe utilitária para documentos e padrões Brasileiros.
 * </p>
 *
 * @author Felipe de Andrade Batista.
 */
@Slf4j
public class BrazilUtil {

    private static final String UNFORMAT_MASK_CPF = PropertiesUtil.getInstance()
            .getPropertieByKey("util.string.pattern.brazil.unformat.cpf");
    private static final String UNFORMAT_MASK_CNPJ = PropertiesUtil.getInstance()
            .getPropertieByKey("util.string.pattern.brazil.unformat.cnpj");
    private static final Integer MAX_LENGTH_CPF = Integer.parseInt(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.cpf.max.length"));
    private static final Integer MAX_LENGTH_CNPJ = Integer.parseInt(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.cnpj.max.length"));
    private static final Integer MAX_LENGTH_MODEL_A_IESUFRAMA = Integer.parseInt(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.iesuframa.model.a.max.length"));
    private static final Integer MAX_LENGTH_MODEL_B_IESUFRAMA = Integer.parseInt(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.iesuframa.model.b.max.length"));
    private static final Integer MIN_LENGTH_BACEN = Integer.parseInt(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.pais.codbacen.min.length"));
    private static final Integer MAX_LENGTH_BACEN = Integer.parseInt(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.pais.codbacen.max.length"));
    private static final Integer LENGTH_IBGE = Integer.parseInt(PropertiesUtil.getInstance()
            .getPropertieByKey("util.brazil.municipio.codibge.length"));
    //private static final String MAX_LENGTH_DOCUMENTO_FISCAL = PropertiesUtil.getInstance()
    //        .getPropertieByKey("util.brazil.documentofiscal.nfe.max.length");

    /**
     * <p>
     *     Formata uma string numérica para o padrão de CNPJ.
     * </p>
     *
     * @param documento Documento a ser formatado
     * @return CNPJ formatado
     */
    private static String formatarCNPJ(String documento) {
        final int groupOneCNPJMaxLength = 2;
        final int groupTwoCNPJMaxLength = 5;
        final int groupTreeCNPJMaxLength = 8;
        final int groupFourCNPJMaxLength = 12;
        final int groupFiveCNPJMaxLength = MAX_LENGTH_CNPJ;
        if (documento == null) {
            return null;
        }

        StringBuilder auxCNPJ = new StringBuilder(documento.trim());

        while (auxCNPJ.length() < MAX_LENGTH_CNPJ) {
            auxCNPJ.insert(0, "0");
        }

        auxCNPJ = new StringBuilder(auxCNPJ.substring(0, groupOneCNPJMaxLength) + StringUtil.PONTO
                + auxCNPJ.substring(groupOneCNPJMaxLength, groupTwoCNPJMaxLength) + StringUtil.PONTO
                + auxCNPJ.substring(groupTwoCNPJMaxLength, groupTreeCNPJMaxLength) + StringUtil.BARRA
                + auxCNPJ.substring(groupTreeCNPJMaxLength, groupFourCNPJMaxLength) + StringUtil.SEPARADOR
                + auxCNPJ.substring(groupFourCNPJMaxLength, groupFiveCNPJMaxLength));
        return auxCNPJ.toString();
    }

    /**
     * <p>
     *     Formata uma string numérica para o padrão de CPF.
     * </p>
     *
     * @param documento Documento a ser formatado
     * @return CPF formatado
     */
    private static String formatarCPF(String documento) {
        final int groupOneCPFMaxLength = 3;
        final int groupTwoCPFMaxLength = 6;
        final int groupTreeCPFMaxLength = 9;
        final int groupFourCPFMaxLength = MAX_LENGTH_CPF;
        if (documento == null) {
            return null;
        }

        String auxCPF = documento.trim();

        while (auxCPF.length() < MAX_LENGTH_CPF) {
            auxCPF = "0" + auxCPF;
        }

        auxCPF = auxCPF.substring(0, groupOneCPFMaxLength) + StringUtil.PONTO
                + auxCPF.substring(groupOneCPFMaxLength, groupTwoCPFMaxLength) + StringUtil.PONTO
                + auxCPF.substring(groupTwoCPFMaxLength, groupTreeCPFMaxLength) + StringUtil.SEPARADOR
                + auxCPF.substring(groupTreeCPFMaxLength, groupFourCPFMaxLength);
        return auxCPF;
    }

    /**
     * <p>
     *     Remove a mascara de documento CPF.
     * </p>
     *
     * @param documento Documento formatado
     * @return Documento sem formatação
     */
    private static String removerMascaraCPF(String documento) {
        return documento.replaceAll(UNFORMAT_MASK_CPF, "");
    }

    /**
     * Remove os simbolos comuns para documentos.
     *
     * @param documento Documento formatado
     * @return Documento sem formatação
     */
    private static String removerMascaraCNPJ(String documento) {
        return documento.replaceAll(UNFORMAT_MASK_CNPJ, "");
    }

    /**
     * <p>
     *     Formata uma string numérica para o padrão de CNPJ (###.###.###/####-##).
     * </p>
     *
     * @param cnpj Documento a ser formatado
     * @return CNPJ formatado
     */
    public static String getCnpjFormatado(String cnpj) {
        if (!StringUtil.isNullOrBlank(cnpj) && !cnpj.contains(StringUtil.PONTO)) {
            cnpj = formatarCNPJ(cnpj);
        }
        return cnpj;
    }

    /**
     * Remove a mascara (###.###.###/####-##) de documento CNPJ.
     *
     * @param cnpj Documento formatado
     * @return Documento sem formatação
     */
    public static String getCnpjSemFormatacao(String cnpj) {
        if (!StringUtil.isNullOrBlank(cnpj) && cnpj.contains(StringUtil.PONTO)) {
            cnpj = removerMascaraCNPJ(cnpj);
        }
        return cnpj;
    }

    /**
     * <p>
     *     Formata uma string numérica para o padrão de CPF (###.###.###-##) .
     * </p>
     *
     * @param cpf Documento a ser formatado
     * @return CPF formatado
     */
    public static String getCpfFormatado(String cpf) {
        if (!StringUtil.isNullOrBlank(cpf) && !cpf.contains(StringUtil.PONTO)) {
            cpf = formatarCPF(cpf);
        }
        return cpf;
    }

    /**
     * <p>
     *     Remove a mascara (###.###.###-##) de documento CPF.
     * </p>
     *
     * @param cpf Documento formatado
     * @return Documento sem formatação
     */
    public static String getCpfSemFormatacao(String cpf) {
        if (!StringUtil.isNullOrBlank(cpf) && cpf.contains(StringUtil.PONTO)) {
            cpf = removerMascaraCPF(cpf);
        }
        return cpf;
    }

    /**
     * <p>
     *     Formata o código de endereçamento postal (CEP) com a mascara padrão: ####-###.
     * </p>
     *
     * @param cep Número do CEP
     * @return CEP formatado
     */
    public static String formatarCEP(String cep) {
        final int groupOneCepMaxLength = 5;
        if (cep == null || cep.isEmpty()) {
            return "";
        }

        String cepAux = cep.trim();
        return cepAux.substring(0, groupOneCepMaxLength) + StringUtil.SEPARADOR
                + cepAux.substring(groupOneCepMaxLength);
    }

    /**
     * <p>
     *     Remove a mascara (####-###) de string numerica com padrão de CEP.
     * </p>
     *
     * @param cepFormatado CEP formatado
     * @return CEP sem mascara.
     */
    public static String removerFormatacaoCEP(String cepFormatado) {
        if (cepFormatado.contains(StringUtil.PONTO)) {
            cepFormatado = cepFormatado.replace(StringUtil.PONTO, "");
        }
        if (cepFormatado.contains(StringUtil.SEPARADOR)) {
            cepFormatado = cepFormatado.replace(StringUtil.SEPARADOR, "");
        }
        return cepFormatado;
    }

    /**
     * <p>
     *     Formatar o numero de telefone no formato Brasileiro.
     * </p>
     *
     * @param fone Numero de Telefone.
     * @return Numero Formatado.
     */
    public static String formatarFone(String fone) {
        final int maxLengthPhone = 8;
        final int maxLengthCellPhone = 9;
        final int maxLengthPhoneWithDDD = 10;
        final int maxLengthCellPhoneWithDDD = 11;
        if (fone == null || fone.isEmpty()) {
            return "";
        }

        String foneAux = fone.trim();
        String formatedFone;

        switch (foneAux.length()) {
            case maxLengthPhoneWithDDD: //1333481341 = (13)3348-1341
                final int groupOneMaxLengthPhoneWithDDD = 2;
                final int groupTwoMaxLengthPhoneWithDDD = 6;
                formatedFone = "(" + foneAux.substring(0, groupOneMaxLengthPhoneWithDDD) + ")"
                        + foneAux.substring(groupOneMaxLengthPhoneWithDDD,
                                groupTwoMaxLengthPhoneWithDDD) + StringUtil.SEPARADOR
                        + foneAux.substring(groupTwoMaxLengthPhoneWithDDD);
                break;
            case maxLengthCellPhoneWithDDD: //13997843354 = (13)99784-3354
                final int groupOneMaxLengthCellPhoneWithDDD = 2;
                final int groupTwoMaxLengthCellPhoneWithDDD = 7;
                formatedFone = "(" + foneAux.substring(0, groupOneMaxLengthCellPhoneWithDDD) + ")"
                        + foneAux.substring(groupOneMaxLengthCellPhoneWithDDD,
                                groupTwoMaxLengthCellPhoneWithDDD) + StringUtil.SEPARADOR
                        + foneAux.substring(groupTwoMaxLengthCellPhoneWithDDD);
                break;
            case maxLengthPhone: //33481341 = 3348-1341
                final int groupOneMaxLengthPhone = 4;
                formatedFone = foneAux.substring(0, groupOneMaxLengthPhone) + StringUtil.SEPARADOR
                        + foneAux.substring(groupOneMaxLengthPhone);
                break;
            case maxLengthCellPhone: //997843354 = 99784-3354
                final int groupOneMaxLengthCellPhone = 5;
                formatedFone = foneAux.substring(0, groupOneMaxLengthCellPhone) + StringUtil.SEPARADOR
                        + foneAux.substring(groupOneMaxLengthCellPhone);
                break;
            default:
                formatedFone = foneAux;
        }
        return formatedFone;
    }

    /**
     * <p>
     *     Remover formatação de uma número de telefone no formato Brasileiro.
     * </p>
     *
     * @param telefoneFormatado Número de Telefone
     * @return Numero desformatado
     */
    public static String removerFormatacaoTelefone(String telefoneFormatado) {
        if (telefoneFormatado == null) {
            throw new IllegalStateException("Telefone não pode ser nulo");
        }
        if (telefoneFormatado.contains("(")) {
            telefoneFormatado = telefoneFormatado.replace("(", "");
        }
        if (telefoneFormatado.contains(")")) {
            telefoneFormatado = telefoneFormatado.replace(")", "");
        }
        if (telefoneFormatado.contains(StringUtil.SEPARADOR)) {
            telefoneFormatado = telefoneFormatado.replace(StringUtil.SEPARADOR, "");
        }
        if (telefoneFormatado.contains(" ")) {
            telefoneFormatado = telefoneFormatado.replace(" ", "");
        }
        return telefoneFormatado;
    }

    /**
     * <p>
     *     Valida o Digito de IE Suframa (Inscrição Estadual Superintendência da Zona Franca de Manaus) para
     *     cadastro de empresas.
     * </p>
     *
     * @param suframa Número SUFRAMA
     * @return True, se válido, do contrario False.
     */
    public static boolean validaDigitoIESuframa(String suframa) {
        int digito;
        int digitoAux;
        int valorAux = 0;
        final int moduloOnze = 11;

        if (suframa == null || suframa.trim().length() < MAX_LENGTH_MODEL_A_IESUFRAMA
                || suframa.trim().length() > MAX_LENGTH_MODEL_B_IESUFRAMA) {
            return false;
        }

        String campoSuframa = suframa.substring(0, suframa.length() - 1);

        try {
            if (suframa.trim().length() == MAX_LENGTH_MODEL_B_IESUFRAMA) {
                valorAux = Integer.parseInt(campoSuframa.substring(0, 1)) * 9
                        + Integer.parseInt(campoSuframa.substring(1, 2)) * 8
                        + Integer.parseInt(campoSuframa.substring(2, 3)) * 7
                        + Integer.parseInt(campoSuframa.substring(3, 4)) * 6
                        + Integer.parseInt(campoSuframa.substring(4, 5)) * 5
                        + Integer.parseInt(campoSuframa.substring(5, 6)) * 4
                        + Integer.parseInt(campoSuframa.substring(6, 7)) * 3
                        + Integer.parseInt(campoSuframa.substring(7, 8)) * 2;
            } else if (suframa.trim().length() == MAX_LENGTH_MODEL_A_IESUFRAMA) {
                valorAux = Integer.parseInt(campoSuframa.substring(0, 1)) * 8
                        + Integer.parseInt(campoSuframa.substring(1, 2)) * 7
                        + Integer.parseInt(campoSuframa.substring(2, 3)) * 6
                        + Integer.parseInt(campoSuframa.substring(3, 4)) * 5
                        + Integer.parseInt(campoSuframa.substring(4, 5)) * 4
                        + Integer.parseInt(campoSuframa.substring(5, 6)) * 3
                        + Integer.parseInt(campoSuframa.substring(6, 7)) * 2;
            }

            int resto = valorAux % moduloOnze;

            if (resto == 0 || resto == 1) {
                digito = 0;
            } else {
                digitoAux = moduloOnze - resto;

                if (digitoAux > MAX_LENGTH_MODEL_B_IESUFRAMA) {
                    String campoDigito = String.valueOf(digitoAux);
                    digito = Integer.parseInt(campoDigito.substring(0, campoDigito.length() - 1));
                } else {
                    digito = digitoAux;
                }
            }
        } catch (NumberFormatException e) {
            log.error("ApiError ao validar o IE Suframa. " + e);
            return false;
        }

        campoSuframa = campoSuframa + digito;

        return campoSuframa.equals(suframa);
    }

    /**
     * <p>
     *      Valida o digito verificador de Chave de Acesso dos documentos fiscais,
     *      tais como a Nota Fiscal Eletrônica (NFe), Nota Fiscal de Consumidor
     *      Eletrônica (NFCe), Conhecimento de Transporte Eletrônico (CTe),
     *      Conhecimento de Transporte Eletrônico para Outros Serviços (CTe OS) e
     *      Manifesto de Documento Fiscal Eletrônico (MDFe) possuem a mesma estrutura
     *      de composição da chave de acesso. Utiliza o Modulo 11 para Calculo.
     * </p>
     *
     * @param vNum String de Documento Fiscal
     * @param docFiscal {@link EnumDocumentoFiscal} Tipo de documento fiscal
     * @return Digito veificado ou não
     */
    public static boolean verificaDigitoVerificadorChaveAcesso(String vNum, EnumDocumentoFiscal docFiscal) {
        boolean verify;
        switch (docFiscal) {
            case NFE:
                verify = verificadorDigitoNFE(vNum);
                break;
            case NFCE:
            case CTE:
            case CTEOS:
            case MDFE:
            default:
                verify = verificadorDigitoNFE(vNum);
        }
        return verify;
    }

    private static boolean verificadorDigitoNFE(String chaveAcesso) {
        //Integer maxLengthvNum = Integer.parseInt(MAX_LENGTH_DOCUMENTO_FISCAL);
        Integer maxLengthvNum = EnumDocumentoFiscal.NFE.getMaxLength();
        final int moduloOnze = 11;
        final int verificadorDez = 10;
        final int multiplicadorMaximo = 9;
        final int conversorChar = 48;

        //if (vNum == null || vNum.length() < 44 || vNum.length() > 44) {
        if (chaveAcesso == null || chaveAcesso.length() < maxLengthvNum || chaveAcesso.length() > maxLengthvNum) {
            return false;
        }

        int vNumDigito = Character.digit(chaveAcesso.charAt(chaveAcesso.length() - 1), verificadorDez);
        String vNumSemDigito = chaveAcesso.substring(0, chaveAcesso.length() - 1);
        int vSomaProduto = 0;
        int vMultiplicador = 2; //Multiplicadores 2,3,4,5,6,7,8,9
        int vProduto = 0;

        for (int x = vNumSemDigito.length() - 1; x >= 0; --x) {
            //Multiplicamos cada algarismo da direita para esquerda pelos multiplicadores.
            vProduto = (vNumSemDigito.charAt(x) - conversorChar) * vMultiplicador;
            vSomaProduto += vProduto;
            if (vMultiplicador < multiplicadorMaximo) {
                ++vMultiplicador;
                continue;
            }
            vMultiplicador = 2;
        }

        //O Dígito Verificador será o resultado da diferença entre 11 e o resultado da divisão (11 – resto).
        int vResto = vSomaProduto - Integer.parseInt("" + vSomaProduto / moduloOnze, verificadorDez) * moduloOnze;
        int vDigito = moduloOnze - vResto;

        if (vDigito == moduloOnze || vDigito == verificadorDez) {
            vDigito = 0;
        }

        return vNumDigito == vDigito;
    }

    /**
     * <p>
     *     Metodo para verificar o digito do Código de Pais do BACEN. Exemplo: 1058
     *     Brasil.
     * </p>
     *
     * @param campo Código do Bacen para o País
     * @return True, para correto e false, para errado
     */
    public static boolean digitoVerificadorCodgPaisBACEN(String campo) {
        final int moduloOnze = 11;
        if (campo == null || campo.length() < MIN_LENGTH_BACEN || campo.length() > MAX_LENGTH_BACEN) {
            return false;
        }

        if (campo.equals("1504")
                || campo.equals("1508")
                || campo.equals("4525")
                || campo.equals("3595")
                || campo.equals("4985")
                || campo.equals("6781")
                || campo.equals("7370")) {
            return true;
        }

        int digVer = 0;
        int digito1 = 0;
        int digito2 = 0;
        int digito3 = 0;

        switch (campo.length()) {
            case 2:
                digVer = Integer.parseInt(campo.substring(1, 2));
                digito1 = Integer.parseInt(campo.substring(0, 1)) * 2;
                break;
            case 3:
                digVer = Integer.parseInt(campo.substring(2, 3));
                digito1 = Integer.parseInt(campo.substring(0, 1)) * 3;
                digito2 = Integer.parseInt(campo.substring(1, 2)) * 2;
                break;
            case 4:
                digVer = Integer.parseInt(campo.substring(3, 4));
                digito1 = Integer.parseInt(campo.substring(0, 1)) * 4;
                digito2 = Integer.parseInt(campo.substring(1, 2)) * 3;
                digito3 = Integer.parseInt(campo.substring(2, 3)) * 2;
                break;
            default:
                return false;
        }

        final int somatorio = digito1 + digito2 + digito3;
        final int resto = somatorio % moduloOnze;

        if (resto == 0 || resto == 1) {
            return digVer == 0;
        }

        final int digVerCalculado = moduloOnze - resto;

        return digVerCalculado == digVer;
    }

    /**
     * <p>
     *     Metodo para verificar o digito do Código de Municipio do IBGE.
     * </p>
     *
     * @param campo Código do IBGE do Municipio
     * @return True, para correto e false, para errado
     */
    public static boolean digitoVerificadorCodgMunicipioIBGE(String campo) {
        final int moduloDez = 10;
        final int digito2;
        final int digito3;
        final int digito6;
        final int digito4;
        final int digito5;

        if (campo == null || campo.length() != LENGTH_IBGE) {
            return false;
        }

        if (campo.equals("4305871")
                || campo.equals("2201919")
                || campo.equals("2202251")
                || campo.equals("2201988")
                || campo.equals("2611533")
                || campo.equals("3117836")
                || campo.equals("3152131")
                || campo.equals("5203939")
                || campo.equals("5203962")) {
            return true;
        }

        int digVer = Integer.parseInt(campo.substring(LENGTH_IBGE - 1, LENGTH_IBGE)) * 1;
        int digito1 = calculoDigito(Integer.parseInt(campo.substring(0, 1)) * 1);
        digito2 = calculoDigito(Integer.parseInt(campo.substring(1, 2)) * 2);
        digito3 = calculoDigito(Integer.parseInt(campo.substring(2, 3)) * 1);
        digito4 = calculoDigito(Integer.parseInt(campo.substring(3, 4)) * 2);
        digito5 = calculoDigito(Integer.parseInt(campo.substring(4, 5)) * 1);
        digito6 = calculoDigito(Integer.parseInt(campo.substring(5, 6)) * 2);
        int somatorio = digito1 + (digito2)
                + (digito3)
                + (digito4)
                + (digito5)
                + (digito6);
        int resto = somatorio % moduloDez;

        if (resto == 0) {
            if (digVer == resto) {
                return true;
            }
            return false;
        }

        int digVerCalculado = moduloDez - resto;

        return digVerCalculado == digVer;
    }

    private static int calculoDigito(int campo) {
        if (campo > 9) {
            String digitoAux = String.valueOf(campo);
            campo = Integer.parseInt(digitoAux.substring(0, 1)) + Integer.parseInt(digitoAux.substring(1, 2));
        }
        return campo;
    }

    /**
     * Calculo do Digito verificador CEAN.
     * <br>
     * <p>
     * O que é o cEAN:
     * <p>
     * Código de barras GTIN (antigo código EAN) do produto que está sendo
     * faturado na NF-e. O GTIN poderá ser GTIN-8 (antigo EAN-8), GTIN-12
     * (antigo UPC), GTIN-13 (antigo EAN), GTIN-14 (antigo DUN-14).
     * <br>
     * <p>
     * O que é o GTIN:
     * <p>
     * GTIN, acrônimo para Global Trade Item Number é um identificador para
     * itens comerciais desenvolvido e controlado pela GS1, antiga EAN/UCC.
     * GTINs, anteriormente chamado códigos EAN, são atribuidos para qualquer
     * item (produto ou serviço) que pode ser precificado, pedido ou faturado em
     * qualquer ponto da cadeia de suprimentos. O GTIN é utilizado para
     * recuperar informação pré-definida e abrange desde as matérias primas até
     * produtos acabados. GTIN é um termo “guarda-chuva” para descrever toda a
     * familia de identificação das estruturas de dados GS1 para itens
     * comerciais (produgos e serviços). GTINs podem ter o tamanho de 8, 12, 13
     * ou 14 digitos e podem ser construidos utilizando qualquer uma das quatro
     * estruturas de numeração dependendo da aplicação. O GTIN-8 é codificado no
     * código de barras EAN-8. GTIN-12 é mais comumente utilizado no código de
     * barras UPC-A, o GTIN-13 é codificado no EAN-13 e o GTIN-14 no ITF-14.
     * <br>
     * Para saber mais:
     * https://www.fazenda.sp.gov.br/nfe/perguntas_frequentes/respostas_X.asp
     *
     * @param cean Código de Barras cEAN.
     * @return True para verificado, e False para incorreto.
     */
    public static boolean digitoVerificadorCean(String cean) {
        if (cean == null || cean.trim().length() == 0
                || cean.equals("00000000")
                || cean.equals("000000000000")
                || cean.equals("0000000000000")
                || cean.equals("00000000000000")) {
            return false;
        }
        int tamanho = cean.length();
        if (tamanho == 8 || tamanho >= 12 && tamanho <= 14) {
            int fator = 3;
            int soma = 0;
            int digitoInformado = Integer.parseInt(cean.substring(tamanho - 1));
            String numero = cean.substring(0, tamanho - 1);
            for (int i = tamanho - 2; i >= 0; --i) {
                if (!Character.isDigit(numero.charAt(i))) {
                    return false;
                }
                soma += Integer.parseInt(String.valueOf(numero.charAt(i))) * fator;
                fator = 4 - fator;
            }
            int digitoVerificado = (1000 - soma) % 10;
            return digitoInformado == digitoVerificado;
        }
        return false;
    }

    /**
     * Calcula parcelas de Boletos e/ou pagamentos.
     *
     * @param qtdeParcelas Quantidade de parcelas
     * @param valor Valor total a ser parcelado
     * @return Lista de Parcelas
     */
    public static List<BigDecimal> calcularParcelas(Integer qtdeParcelas, BigDecimal valor) {
        return calcularParcelas(qtdeParcelas, valor, RoundingMode.HALF_UP);
    }

    private static List<BigDecimal> calcularParcelas(Integer qtdeParcelas,
            BigDecimal valor, RoundingMode mode) {
        ArrayList<BigDecimal> listaValores = new ArrayList<>();
        BigDecimal valorDasParcelas = valor.divide(new BigDecimal(qtdeParcelas.toString()), 2, mode);
        if (!valorDasParcelas.remainder(valor).equals(BigDecimal.ZERO)) {
            BigDecimal valorDasParcelasSemAUltimaParcela = valorDasParcelas
                    .multiply(new BigDecimal(qtdeParcelas).subtract(BigDecimal.ONE));
            BigDecimal valorUltimaParcela = valor.subtract(valorDasParcelasSemAUltimaParcela);
            for (int i = 0; i < qtdeParcelas - 1; ++i) {
                listaValores.add(valorDasParcelas);
            }
            listaValores.add(valorUltimaParcela);
        } else {
            for (int i = 0; i < qtdeParcelas - 1; ++i) {
                listaValores.add(valorDasParcelas);
            }
        }
        return listaValores;
    }
}

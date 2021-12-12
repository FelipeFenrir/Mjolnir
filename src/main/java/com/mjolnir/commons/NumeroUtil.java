/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *     Util class for numbers.
 * </p>
 *
 * @author Felipe de Andrade Batista
 */
public class NumeroUtil {

    private static final Logger LOG = LoggerFactory.getLogger(NumeroUtil.class);

    private static final int ROUNDING_MODE = 3;

    /**
     * <p>
     *     Adjust to double.
     * </p>
     * @param value Value to adjust
     * @param local Locale
     * @return Adjusted double
     */
    public static double adjustToDouble(double value, Locale local) {
        return Double.parseDouble(NumeroUtil.transformWithoutThousand(value, 2, local)
                .replace(',', '.'));
    }

    /**
     * <p>
     *     Transform a number to String without a thousand symbol.
     * </p>
     * @param value Number value
     * @param decimais
     * @param local
     * @return String do value formatada.
     */
    public static final String transformWithoutThousand(double value, int decimais, Locale local) {

        DecimalFormat formatador;
        String mascara = "################0";
        StringBuilder buf = new StringBuilder();

        for (int i = 1; i <= decimais; ++i) {
            buf.append("0");
        }

        if (decimais > 0) {
            buf.insert(0, ".");
        }

        try {
            formatador = (DecimalFormat) NumberFormat.getInstance(local);
        } catch (ClassCastException e) {
            LOG.error("ERROR: Falha ao transformar objeto. ", e);
            return "";
        }

        String padrao = buf.toString();
        formatador.applyPattern(mascara + padrao);

        return formatador.format(value);
    }

    /**
     * Formata o valor numerico para uma string.
     * @param valor
     * @param decimaisMaximos
     * @param decimaisMinimos
     * @param local
     * @return String do valor formatado.
     */
    public static final String formatar(double valor, int decimaisMaximos,
            int decimaisMinimos, Locale local) {

        int i;
        DecimalFormat formatador;
        StringBuilder buf = new StringBuilder();

        for (i = 1; i <= decimaisMinimos; ++i) {
            buf.append("0");
        }

        for (i = 0; i < decimaisMaximos - decimaisMinimos; ++i) {
            buf.append("#");
        }

        if (decimaisMaximos > 0) {
            buf.insert(0, ".");
        }

        try {
            formatador = (DecimalFormat) NumberFormat.getInstance(local);
        } catch (ClassCastException e) {
            LOG.error("ERROR: Falha ao transformar objeto. ", e);
            return "";
        }

        String padrao = buf.toString();
        formatador.applyPattern("##,###,###,###,###,##0" + padrao);
        return formatador.format(valor);
    }

    /**
     * Formata o valor numerico para uma string.
     * @param valor
     * @param decimais
     * @param local
     * @return String do valor formatado.
     */
    public static final String formatar(double valor, int decimais, Locale local) {
        return NumeroUtil.formatar(valor, decimais, decimais, local);
    }

    /**
     * Formata o valor numerico para uma string.
     * Detalhe:
     * Para Bilhões: acrecenta Bi no final do Numero.
     * Para Milhões: acrecenta M no final do Numero.
     * Para Milhar: acrecenta K no final do Numero.
     * @param valor
     * @param decimaisMaximos
     * @param decimaisMinimos
     * @param local
     * @return String do valor formatado.
     */
    public static final String formatarComMilhar(double valor,
            int decimaisMaximos, int decimaisMinimos, Locale local) {
        final double mil = 1000.0;
        final double milhao = 1000000.0;
        final double bilhao = 1.0E9;
        if (valor >= bilhao) {
            return NumeroUtil.formatar(valor / bilhao, decimaisMaximos,
                    decimaisMinimos, local) + "Bi";
        }

        if (valor >= milhao) {
            return NumeroUtil.formatar(valor / milhao, decimaisMaximos,
                    decimaisMinimos, local) + "M";
        }

        if (valor >= mil) {
            return NumeroUtil.formatar(valor / mil, decimaisMaximos,
                    decimaisMinimos, local) + "K";
        }

        return NumeroUtil.formatar(valor, decimaisMaximos, decimaisMinimos, local);
    }

    /**
     * Formata o valor numerico para uma string.
     * @param valor
     * @param decimais
     * @param local
     * @return String do valor formatado.
     */
    public static final String formatar(String valor, int decimais, Locale local) {
        if (valor == null) {
            return null;
        }

        return NumeroUtil.formatar(
                (double) Double.valueOf(valor.replace(',', '.')),
                decimais,
                local);
    }

    /**
     * Formata o valor numerico para uma string.
     * @param numero
     * @param mascara
     * @param local
     * @return String do valor formatado.
     */
    public static final String formatar(double numero, String mascara, Locale local) {
        DecimalFormat formatador;

        if (mascara == null) {
            return "";
        }

        try {
            formatador = (DecimalFormat) NumberFormat.getInstance(local);
        } catch (ClassCastException e) {
            LOG.error("ERROR: Falha ao transformar objeto. ", e);
            return "";
        }

        formatador.applyPattern(mascara);
        return formatador.format(numero);
    }

    /**
     * Formata o valor numerico para uma string.
     * @param numero
     * @param mascara
     * @param local
     * @return String do valor formatado.
     */
    public static final String formatar(String numero, String mascara, Locale local) {
        if (numero == null) {
            return "";
        }

        if (numero.trim().equals("")) {
            numero = "0";
        }

        try {
            return NumeroUtil.formatar(Double.parseDouble(numero), mascara, local);
        } catch (NumberFormatException e) {
            LOG.error("ERROR: Falha ao formatar numero. ", e);
            return "";
        }
    }

    /**
     * Formata o valor numerico para uma string.
     * @param numero
     * @param mascara
     * @param local
     * @return String do valor formatado.
     */
    public static final String formatar(int numero, String mascara, Locale local) {
        return NumeroUtil.formatar((double) numero, mascara, local);
    }

    /**
     * Formata o valor numerico para uma string.
     * @param numero
     * @param mascara
     * @param local
     * @return String do valor formatado.
     */
    public static final String formatar(long numero, String mascara, Locale local) {
        return NumeroUtil.formatar((double) numero, mascara, local);
    }

    /**
     * Converte o Numero (int) em Long.
     * @param numero
     * @return Long.
     */
    public static Long toLong(int numero) {
        return Long.parseLong(String.valueOf(numero));
    }

    /**
     * Retorna um padrão de Formatação de String.
     * @param maxInt Quantidade de Digitos.
     * @param maxDec Quantidade de Digitos Decimais.
     * @param digAgrupa
     * @return String.
     */
    public static String getPattern(int maxInt, int maxDec, boolean digAgrupa) {
        int x;
        int i;

        StringBuilder bufMaskIntNum = new StringBuilder();
        StringBuilder bufMaskDecNum = new StringBuilder();

        String newMask = "";

        for (i = 0; i < maxInt - maxDec - 1; ++i) {
            bufMaskIntNum.append("#");
        }

        bufMaskIntNum.append("0");

        for (i = 0; i < maxDec; ++i) {
            bufMaskDecNum.append("0");
        }

        String maskIntNum = bufMaskIntNum.toString();
        String maskDecNum = bufMaskDecNum.toString();

        final int maxLenght = 3;

        for (x = maskIntNum.length(); x > maxLenght; x -= maxLenght) {
            newMask = digAgrupa ? ",".concat(maskIntNum.substring(x - maxLenght, x))
                    .concat(newMask) : maskIntNum.substring(x - maxLenght, x)
                            .concat(newMask);
        }

        newMask = maskIntNum.substring(0, x).concat(newMask);
        return newMask.concat(maskDecNum.trim().length() > 0 ? ".".concat(maskDecNum) : maskDecNum);
    }

    /**
     * Preenche a String numerica com zeros a esquerda.
     * @param numero
     * @param len
     * @return String.
     */
    public static String preencheZeroEsquerda(String numero, int len) {
        String n = numero;
        n = n == null || n.trim().length() == 0 ? "0" : n.trim();
        BigInteger x = new BigInteger(n);
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setGroupingUsed(false);
        formatter.setMinimumIntegerDigits(len);
        return formatter.format(x);
    }

    /**
     * Converte para Bigecimal a String Numerica.
     * @param numero
     * @param maxDec
     * @param local
     * @return Numero em formato BigDecimal.
     */
    public static BigDecimal getNumber(String numero, int maxDec, Locale local) {
        numero = numero == null || numero.trim().length() == 0 ? "0" : numero.trim();
        BigDecimal bd = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getNumberInstance(local);
        try {
            bd = new BigDecimal(nf.parse(numero).toString());
        } catch (ParseException ex) {
            LOG.error("ERROR: Falha ao realizar PARSE. ", ex);
        }
        bd = bd.setScale(maxDec, ROUNDING_MODE);
        return bd;
    }

    /**
     * Converte para String de um BigDecimal.
     * @param valor
     * @param maxDec
     * @param local
     * @return Numero em formato String.
     */
    public static String getNumberFromBigDecimalConverter(String valor, int maxDec, Locale local) {
        BigDecimal numero = valor == null || valor.trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(valor);
        final int maxDigits = 10;
        final int maxDecimals = 2;
        return NumeroUtil.formatar(numero, maxDigits, maxDecimals, false, local);
    }

    /**
     * Formata o valor numerico para uma string.
     * @param numero
     * @param maxInt
     * @param maxDec
     * @param digAgrupa
     * @param local
     * @return String do valor formatado.
     */
    public static String formatar(String numero, int maxInt, int maxDec, boolean digAgrupa, Locale local) {
        numero = numero == null || numero.trim().length() == 0 ? "0" : numero.trim();
        DecimalFormat dc = (DecimalFormat) DecimalFormat.getInstance(local);
        dc.applyPattern(NumeroUtil.getPattern(maxInt, maxDec, digAgrupa));
        BigDecimal bd = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getNumberInstance(local);
        try {
            bd = new BigDecimal(nf.parse(numero).toString());
        } catch (ParseException ex) {
            LOG.error("ERROR: Falha ao realizar PARSE. ", ex);
        }
        return dc.format(bd.setScale(maxDec, ROUNDING_MODE));
    }

    /**
     * Formata o valor numerico para uma string.
     * @param bd
     * @param maxInt
     * @param maxDec
     * @param digAgrupa
     * @param local
     * @return String do valor formatado.
     */
    public static String formatar(BigDecimal bd, int maxInt, int maxDec, boolean digAgrupa, Locale local) {
        bd = bd == null ? BigDecimal.valueOf(0) : bd;
        DecimalFormat dc = (DecimalFormat) DecimalFormat.getInstance(local);
        dc.applyPattern(NumeroUtil.getPattern(maxInt, maxDec, digAgrupa));
        return dc.format(bd.setScale(maxDec, ROUNDING_MODE));
    }

    /**
     * Retira os zeros a esquerda.
     * @param numero
     * @return Numero sem zeros a esquerda.
     */
    public static String retirarZeroEsquerda(String numero) {
        return Long.valueOf(numero).toString();
    }

    /**
     * Converte para String de um BigDecimal.
     * @param numero
     * @param quantidadeCasasDecimais
     * @param local
     * @return Numero em formato BigDecimal.
     * @throws ParseException
     */
    public static BigDecimal getNumero(String numero, Integer quantidadeCasasDecimais, Locale local)
            throws ParseException {
        NumberFormat nf = NumberFormat.getNumberInstance(local);
        nf.setMaximumFractionDigits(quantidadeCasasDecimais);
        String novoNumero = nf.parse(numero).toString();
        BigDecimal numeroBD = new BigDecimal(novoNumero);
        return numeroBD;
    }

    /**
     * Verifica se o valor é zero ou nullo.
     * @param numero
     * @return Boolean.
     */
    public static Boolean ehZeroOuNulo(BigDecimal numero) {
        Boolean retorno = Boolean.FALSE;
        if (numero == null) {
            retorno = Boolean.TRUE;
            return retorno;
        }
        if (numero.compareTo(BigDecimal.ZERO) <= 0) {
            retorno = Boolean.TRUE;
            return retorno;
        }
        return retorno;
    }

    /**
     * Formata um BigDecimal para somente duas casas decimais.
     * O Formato respeitado esta de acordo com o locale da maquina servidora.
     * @param numero
     * @param locale
     * @return String.
     */
    public static String formatarBigDecimalCom2CasasDecimais(BigDecimal numero,
            Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat decimalFormat = (DecimalFormat) nf;
        //DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return decimalFormat.format(numero);
    }

    /**
     * Verifica se o valor é um Número Inteiro.
     * @param param
     * @return boolean.
     */
    public static boolean ehNumero(String param) {
        boolean retorno = true;
        if (StringUtil.isNullOrBlank(param)) {
            return false;
        }
        for (int i = 0; i < param.length(); ++i) {
            char c = param.charAt(i);
            if (String.valueOf(c).matches("[0-9]")) {
                continue;
            }
            retorno = false;
            break;
        }
        return retorno;
    }

    /**
     * Verifica se o valor é zero ou nullo.
     * @param numero
     * @return Boolean.
     */
    public static Boolean ehZeroOuNulo(Integer numero) {
        return numero == null || numero == 0;
    }

    /**
     * Verifica se o valor é nullo.
     * @param value
     * @return BigDecimal.
     */
    public static BigDecimal nullSafeBigDecimal(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}

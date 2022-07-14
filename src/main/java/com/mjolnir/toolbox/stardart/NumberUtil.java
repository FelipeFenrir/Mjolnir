/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.toolbox.stardart;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

import java.util.Locale;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NumberUtil {

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
        return Double.parseDouble(NumberUtil.transformWithoutThousand(value, 2, local)
                .replace(',', '.'));
    }

    /**
     * <p>
     *     Transform a number to String without a thousand symbol.
     * </p>
     * @param value Number value
     * @param decimals Decimals value
     * @param local Locale
     * @return String number without symbols
     */
    public static String transformWithoutThousand(double value, int decimals, Locale local) {

        DecimalFormat formatter;
        String mask = "################0";
        StringBuilder buf = new StringBuilder();

        /*
         * for (int i = 1; i <= decimals; ++i) {
         *    buf.append("0");
         * }
         */
        buf.append("0".repeat(Math.max(0, decimals)));

        if (decimals > 0) {
            buf.insert(0, ".");
        }

        try {
            formatter = (DecimalFormat) NumberFormat.getInstance(local);
        } catch (ClassCastException e) {
            log.error("Fail to transform object. ", e);
            return "";
        }

        String standart = buf.toString();
        formatter.applyPattern(mask + standart);

        return formatter.format(value);
    }

    /**
     * <p>
     *     Transform numeric value in formatted string.
     * </p>
     *
     * @param value Numeric value
     * @param maxDecimals Max Decimals
     * @param minDecimals Minimum Decimals
     * @param local Locale
     * @return Formatted string
     */
    public static String numericToFormattedString(double value, int maxDecimals, int minDecimals, Locale local) {
        int i;
        DecimalFormat formatter;
        StringBuilder buf = new StringBuilder();

        for (i = 1; i <= minDecimals; ++i) {
            buf.append("0");
        }

        for (i = 0; i < maxDecimals - minDecimals; ++i) {
            buf.append("#");
        }

        if (maxDecimals > 0) {
            buf.insert(0, ".");
        }

        try {
            formatter = (DecimalFormat) NumberFormat.getInstance(local);
        } catch (ClassCastException e) {
            log.error("Fail to transform object. ", e);
            return "";
        }

        String standart = buf.toString();
        formatter.applyPattern("##,###,###,###,###,##0" + standart);
        return formatter.format(value);
    }

    /**
     * <p>
     *     Transform numeric value in formatted string.
     * </p>
     * @param value Numeric value
     * @param decimals Decimals
     * @param locale Locale
     * @return Formatted string
     */
    public static String numericToFormattedString(double value, int decimals, Locale locale) {
        return NumberUtil.numericToFormattedString(value, decimals, decimals, locale);
    }

    /**
     * <p>
     *     Transform numeric value in formatted string.
     * </p>
     * <p>
     *     Details:
     *     </br> For Billions: add Bi in the end of Number string.
     *     </br> For Millions: add M in the end of Number string.
     *     </br> For Thousand: add K in the end of Number string.
     * </p>
     * @param value Numeric value
     * @param maxDecimals Max Decimals
     * @param minDecimals Minimum Decimals
     * @param locale Locale
     * @return Formatted string
     */
    public static String transformWithoutThousand(double value, int maxDecimals, int minDecimals, Locale locale) {
        final double thousand = 1000.0;
        final double millions = 1000000.0;
        final double billions = 1.0E9;

        if (value >= billions) {
            return NumberUtil.numericToFormattedString(value / billions, maxDecimals,
                    minDecimals, locale) + "Bi";
        }

        if (value >= millions) {
            return NumberUtil.numericToFormattedString(value / millions, maxDecimals,
                    minDecimals, locale) + "M";
        }

        if (value >= thousand) {
            return NumberUtil.numericToFormattedString(value / thousand, maxDecimals,
                    minDecimals, locale) + "K";
        }

        return NumberUtil.numericToFormattedString(value, maxDecimals, minDecimals, locale);
    }

    /**
     * <p>
     *     Transform numeric value in formatted string.
     * </p>
     * @param value Numeric value
     * @param decimals Decimals
     * @param locale Locale
     * @return Formatted string
     */
    public static String numericToFormattedString(String value, int decimals, Locale locale) {
        if (value == null) {
            return null;
        }

        return NumberUtil.numericToFormattedString(
                Double.parseDouble(value.replace(',', '.')),
                decimals,
                locale);
    }

    /**
     * <p>
     *     Transform numeric value in formatted string.
     * </p>
     * @param value Numeric value
     * @param mask String Mask
     * @param locale Locale
     * @return Formatted string
     */
    public static String numericToFormattedString(double value, String mask, Locale locale) {
        DecimalFormat formatter;

        if (mask == null) {
            return "";
        }

        try {
            formatter = (DecimalFormat) NumberFormat.getInstance(locale);
        } catch (ClassCastException e) {
            log.error("Fail to transform object. ", e);
            return "";
        }

        formatter.applyPattern(mask);
        return formatter.format(value);
    }

    /**
     * <p>
     *     Transform numeric value in formatted string.
     * </p>
     * @param value Numeric value
     * @param mask String Mask
     * @param locale Locale
     * @return Formatted string
     */
    public static String numericToFormattedString(String value, String mask, Locale locale) {
        if (value == null) {
            return "";
        }

        if (value.trim().equals("")) {
            value = "0";
        }

        try {
            return NumberUtil.numericToFormattedString(Double.parseDouble(value), mask, locale);
        } catch (NumberFormatException e) {
            log.error("Fail to format value. ", e);
            return "";
        }
    }

    /**
     * <p>
     *     Transform numeric value in formatted string.
     * </p>
     * @param value Numeric value
     * @param mask String Mask
     * @param locale Locale
     * @return Formatted string
     */
    public static String numericToFormattedString(int value, String mask, Locale locale) {
        return NumberUtil.numericToFormattedString((double) value, mask, locale);
    }

    /**
     * <p>
     *     Transform numeric value in formatted string.
     * </p>
     * @param value Numeric value
     * @param mask String Mask
     * @param locale Locale
     * @return Formatted string
     */
    public static String numericToFormattedString(long value, String mask, Locale locale) {
        return NumberUtil.numericToFormattedString((double) value, mask, locale);
    }

    /**
     * <p>
     *     Convert Integer to Long.
     * </p>
     * @param number Integer Number
     * @return Long Number.
     */
    public static Long toLong(int number) {
        return Long.parseLong(String.valueOf(number));
    }

    /**
     * <p>
     *     Get Pattern mask on Number value.
     * </p>
     * @param numberOfIntegers Number of Integers digits
     * @param numberOfDecimals Number of Decimals digits
     * @param grouperDigit Grouper digit
     * @return Pattern mask String
     */
    public static String getPattern(int numberOfIntegers, int numberOfDecimals, boolean grouperDigit) {
        int x;
        int i;

        StringBuilder bufMaskIntNum = new StringBuilder();
        StringBuilder bufMaskDecNum = new StringBuilder();

        String newMask = "";

        for (i = 0; i < numberOfIntegers - numberOfDecimals - 1; ++i) {
            bufMaskIntNum.append("#");
        }

        bufMaskIntNum.append("0");

        for (i = 0; i < numberOfDecimals; ++i) {
            bufMaskDecNum.append("0");
        }

        String maskIntNum = bufMaskIntNum.toString();
        String maskDecNum = bufMaskDecNum.toString();

        final int maxLength = 3;

        for (x = maskIntNum.length(); x > maxLength; x -= maxLength) {
            String integerMaskFragment = maskIntNum.substring(x - maxLength, x);
            newMask = grouperDigit ? ",".concat(integerMaskFragment)
                    .concat(newMask) : integerMaskFragment
                            .concat(newMask);
        }

        newMask = maskIntNum.substring(0, x).concat(newMask);
        return newMask.concat(maskDecNum.trim().length() > 0 ? ".".concat(maskDecNum) : maskDecNum);
    }

    /**
     * <p>
     *     Fill Numeric String with leading zeros.
     * </p>
     * @param value Numeric String
     * @param length Number Length
     * @return Numeric String with zeros on left
     */
    public static String fillNumericStringWithLeadingZeros(String value, int length) {
        String n = value;
        n = n == null || n.trim().length() == 0 ? "0" : n.trim();
        BigInteger x = new BigInteger(n);
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setGroupingUsed(false);
        formatter.setMinimumIntegerDigits(length);
        return formatter.format(x);
    }

    /**
     * <p>
     *     Convert Numeric String into BigDecimal.
     * </p>
     * @param value Numeric string
     * @param maxDecimal Maximum decimal
     * @param locale Locale
     * @return BigDecimal number
     */
    public static BigDecimal toBigDecimal(String value, int maxDecimal, Locale locale) {
        value = value == null || value.trim().length() == 0 ? "0" : value.trim();
        BigDecimal bd = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        try {
            bd = new BigDecimal(nf.parse(value).toString());
        } catch (ParseException ex) {
            log.error("Fail to parse value. ", ex);
        }
        bd = bd.setScale(maxDecimal, ROUNDING_MODE);
        return bd;
    }

    /**
     * <p>
     *     Convert numeric string to formatted string in big decimal pattern.
     * </p>
     * @param value Numeric value
     * @param locale Locale
     * @return Formatted numeric string
     */
    public static String numericToFormattedString(String value, Locale locale) {
        BigDecimal number = value == null || value.trim().isEmpty() ? BigDecimal.ZERO : new BigDecimal(value);
        final int maxDigits = 10;
        final int maxDecimals = 2;
        return NumberUtil.numericToFormattedString(number, maxDigits, maxDecimals, false, locale);
    }

    /**
     * <p>
     *     Convert Numeric string to formatted string.
     * </p>
     * @param number Numeric String
     * @param maxIntegers Maximum of Integers characters
     * @param maxDecimals Maximum of Decimals characters
     * @param groupDigits Group Digits
     * @param locale Locale
     * @return Formatted Numeric String
     */
    public static String numericToFormattedString(String number, int maxIntegers, int maxDecimals, boolean groupDigits, Locale locale) {
        number = number == null || number.trim().length() == 0 ? "0" : number.trim();
        DecimalFormat dc = (DecimalFormat) DecimalFormat.getInstance(locale);
        dc.applyPattern(NumberUtil.getPattern(maxIntegers, maxDecimals, groupDigits));
        BigDecimal bd = BigDecimal.ZERO;
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        try {
            bd = new BigDecimal(nf.parse(number).toString());
        } catch (ParseException ex) {
            log.error("Fail to parse. ", ex);
        }
        return dc.format(bd.setScale(maxDecimals, ROUNDING_MODE));
    }

    /**
     * <p>
     *     Convert Big Decimal number to formatted string.
     * </p>
     * @param bigDecimal Big Decimal number
     * @param maxIntegers Maximum of Integers characters
     * @param maxDecimals Maximum of Decimals characters
     * @param groupDigits Group Digits
     * @param locale Locale
     * @return Formatted Numeric String
     */
    public static String numericToFormattedString(BigDecimal bigDecimal, int maxIntegers, int maxDecimals,
                                                  boolean groupDigits, Locale locale) {
        bigDecimal = bigDecimal == null ? BigDecimal.valueOf(0) : bigDecimal;
        DecimalFormat dc = (DecimalFormat) DecimalFormat.getInstance(locale);
        dc.applyPattern(NumberUtil.getPattern(maxIntegers, maxDecimals, groupDigits));
        return dc.format(bigDecimal.setScale(maxDecimals, RoundingMode.FLOOR));
    }

    /**
     * <p>
     *     Remove the leading zeros.
     * </p>
     * @param number Numeric String
     * @return Numeric string without zeros on left
     */
    public static String removeZerosOnLeft(String number) {
        return Long.valueOf(number).toString();
    }

    /**
     * <p>
     *     Convert a numeric string to Big Decimal number.
     * </p>
     * @param number Numeric String
     * @param amountOfDecimalPlaces Amount of Decimal places in numeric string
     * @param locale Locale
     * @return Formatted Big Decimal
     * @throws ParseException in Fail on Parse number
     */
    public static BigDecimal toBigDecimal(String number, Integer amountOfDecimalPlaces, Locale locale)
            throws ParseException {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMaximumFractionDigits(amountOfDecimalPlaces);
        String novoNumero = nf.parse(number).toString();
        return new BigDecimal(novoNumero);
    }

    /**
     * <p>
     *     Check if Big Decimal number is null or zero.
     * </p>
     * @param value Big Decimal number
     * @return Boolean
     */
    public static Boolean isNullOrZero(BigDecimal value) {
        if (value == null) {
            return Boolean.TRUE;
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * <p>
     *     Convert a Big Decimal number to String with two decimal places and server locale.
     * </p>
     * @param number Big Decimal number
     * @param locale Locale
     * @return String
     */
    public static String numericToFormattedString(BigDecimal number,
                                                  Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat decimalFormat = (DecimalFormat) nf;
        //DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        return decimalFormat.format(number);
    }

    /**
     * <p>
     *     Check if String is a Integer number.
     * </p>
     * @param value String to check
     * @return Boolean
     */
    public static boolean isIntegerNumber(String value) {
        boolean retorno = true;
        if (StringUtil.isNullOrBlank(value)) {
            return false;
        }
        for (int i = 0; i < value.length(); ++i) {
            char c = value.charAt(i);
            if (String.valueOf(c).matches("[0-9]")) {
                continue;
            }
            retorno = false;
            break;
        }
        return retorno;
    }

    /**
     * <p>
     *     Null safe Integer verification.
     * </p>
     * @param value Integer value
     * @return Boolean
     */
    public static Boolean isNullOrZero(Integer value) {
        return value == null || value == 0;
    }

    /**
     * <p>
     *     Null safe Big Decimal verification.
     * </p>
     * @param value Big Decimal value
     * @return Big Decimal value or Zero
     */
    public static BigDecimal nullSafeBigDecimal(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}

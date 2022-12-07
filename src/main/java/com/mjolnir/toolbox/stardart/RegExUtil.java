/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.toolbox.stardart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mjolnir.toolbox.app.PropertiesUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *     Class with regular expressions.
 * </p>
 *
 * @author Felipe de Andrade Batista
 * @see <a href="https://regex101.com/">regex101.com</a>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegExUtil {

    private static final PropertiesUtil PROPERTIE = PropertiesUtil.getInstance();

    /**
     * <p>
     *     BRAZIL Date pattern Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>
     * ^(((0[1-9]|[12][0-9]|3[01])([/])(0[13578]|10|12)([/])(\d{4}))|(([0][1-9]|
     * [12][0-9]|30)([/])(0[469]|11)([/])(\d{4}))|((0[1-9]|1[0-9]|2[0-8])([/])(0
     * 2)([/])(\d{4}))|((29)(\.|-|\/)(02)([/])([02468][048]00))|((29)([/])(02)([
     * /])([13579][26]00))|((29)([/])(02)([/])([0-9][0-9][0][48]))|((29)([/])(02
     * )([/])([0-9][0-9][2468][048]))|((29)([/])(02)([/])([0-9][0-9][13579][26])
     * ))
     * </pre>
     */
    //public static final String REGEX_DATA_BRASIL = "^(((0[1-9]|[12][0-9]|3[01])"
    //        + "([/])(0[13578]|10|12)([/])(\\d{4}))|(([0][1-9]|[12][0-9]|30)([/])"
    //        + "(0[469]|11)([/])(\\d{4}))|((0[1-9]|1[0-9]|2[0-8])([/])(02)([/])"
    //        + "(\\d{4}))|((29)(\\.|-|\\/)(02)([/])([02468][048]00))|((29)([/])(0"
    //        + "2)([/])([13579][26]00))|((29)([/])(02)([/])([0-9][0-9][0][48]))|("
    //        + "(29)([/])(02)([/])([0-9][0-9][2468][048]))|((29)([/])(02)([/])([0"
    //        + "-9][0-9][13579][26])))";
    public static final String REGEX_DATA_BRASIL = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.data.brazil");

    /**
     * <p>
     *     EUA Date pattern Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>
     * ^\d{4}(\-)(((0)[0-9])|((1)[0-2]))(\-)([0-2][0-9]|(3)[0-1])$
     * </pre>
     */
    //public static final String REGEX_DATA_EUA = "^\\d{4}(\-)(((0)[0-9])|((1"
    //        + ")[0-2]))(\-)([0-2][0-9]|(3)[0-1])$";
    public static final String REGEX_DATA_EUA = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.data.eua");

    /**
     * <p>
     *     BRAZIL Date and Hours pattern Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>
     * ^(((0[1-9]|[12][0-9]|3[01])([/])(0[13578]|10|12)([/])(\d{4}))|(([0][1-9]|
     * [12][0-9]|30)([/])(0[469]|11)([/])(\d{4}))|((0[1-9]|1[0-9]|2[0-8])([/])(0
     * 2)([/])(\d{4}))|((29)(\.|-|\/)(02)([/])([02468][048]00))|((29)([/])(02)([
     * /])([13579][26]00))|((29)([/])(02)([/])([0-9][0-9][0][48]))|((29)([/])(02
     * )([/])([0-9][0-9][2468][048]))|((29)([/])(02)([/])([0-9][0-9][13579][26])
     * )) ([0-1][0-9]|[2][0-3])(:([0-5][0-9])){1,2}$
     * </pre>
     */
    //public static final String REGEX_DATA_HORA_BRASIL = "^(((0[1-9]|[12][0-9]|3["
    //        + "01])([/])(0[13578]|10|12)([/])(\\d{4}))|(([0][1-9]|[12][0-9]|30)("
    //        + "[/])(0[469]|11)([/])(\\d{4}))|((0[1-9]|1[0-9]|2[0-8])([/])(02)([/"
    //        + "])(\\d{4}))|((29)(\\.|-|\\/)(02)([/])([02468][048]00))|((29)([/])"
    //        + "(02)([/])([13579][26]00))|((29)([/])(02)([/])([0-9][0-9][0][48]))"
    //        + "|((29)([/])(02)([/])([0-9][0-9][2468][048]))|((29)([/])(02)([/])("
    //        + "[0-9][0-9][13579][26]))) ([0-1][0-9]|[2][0-3])(:([0-5][0-9])){1,2}$";
    public static final String REGEX_DATA_HORA_BRASIL = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.datahora.brazil");

    /**
     * <p>
     *     EUA Date and Hours pattern Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>
     * ^\d{4}(\-)(((0)[0-9])|((1)[0-2]))(\-)([0-2][0-9]|(3)[0-1])$
     * ([0-1][0-9]|[2][0-3])(:([0-5][0-9])){1,2}$
     * </pre>
     */
    //public static final String REGEX_DATA_HORA_EUA = "^\\d{4}(\-)(((0)[0-9])|"
    //        + "((1)[0-2]))(\-)([0-2][0-9]|(3)[0-1])$ ([0-1][0-9]|[2][0-3])(:([0-"
    //        + "5][0-9])){1,2}$";
    public static final String REGEX_DATA_HORA_EUA = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.datahora.eua");

    /**
     * <p>
     *     EUA Date and Hours pattern Regular Expression with time separator.
     * </p>
     *
     * REG EX:
     * <pre>
     * ^\d{4}(\-)(((0)[0-9])|((1)[0-2]))(\-)([0-2][0-9]|(3)[0-1])(\T)([0-1][0-9]
     * |[2][0-3])(:([0-5][0-9])){1,2}$
     * </pre>
     */
    //public static final String REGEX_DATA_HORA_V2_EUA = "^\\d{4}(\-)(((0)[0-9])"
    //        + "|((1)[0-2]))(\-)([0-2][0-9]|(3)[0-1])(\T)([0-1][0-9]|[2][0-3])"
    //        + "(:([0-5][0-9])){1,2}$";
    public static final String REGEX_DATA_HORA_V2_EUA = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.datahora.v2.eua");

    /**
     * <p>
     *     BRAZIL Hours pattern Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>([0-1][0-9]|[2][0-3])(:([0-5][0-9])){1,2}$</pre>
     */
    //public static final String REGEX_HORA_BRASIL = "([0-1][0-9]|[2][0-3])(:([0-5"
    //        + "][0-9])){1,2}$";
    public static final String REGEX_HORA_BRASIL = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.hora.brazil");

    /**
     * <p>
     *     EUA Hours pattern Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>([0-1][0-9]|[2][0-3])(:([0-5][0-9])){1,2}$</pre>
     */
    //public static final String REGEX_HORA_EUA = "([0-1][0-9]|[2][0-3])(:([0-5"
    //        + "][0-9])){1,2}$";
    public static final String REGEX_HORA_EUA = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.hora.brazil");

    /**
     * <p>
     *     Only Numbers Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>^[0-9\\b]+$</pre>
     */
    //public static final String REGEX_NUMBER = "^[0-9\\\\b]+$";
    public static final String REGEX_NUMBER = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.number");

    /**
     * <p>
     *     Lowercase Letters Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>^[ a-z\\b]+$</pre>
     */
    //public static final String REGEX_MINUSCULO = "^[ a-z\\\\b]+$";
    public static final String REGEX_MINUSCULO = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.minusculo");

    /**
     * Uppercase Letters Regular Expression.
     *
     * REG EX:
     * <pre>^[ A-Z\\b]+$</pre>
     */
    //public static final String REGEX_MAIUSCULO = "^[ A-Z\\\\b]+$";
    public static final String REGEX_MAIUSCULO = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.maiusculo");

    /**
     * <p>
     *     Letters Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>^[ a-zA-Z\\b]+$</pre>
     */
    //public static final String REGEX_MAIUSCULO_MINUSCULO = "^[ a-zA-Z\\\\b]+$";
    public static final String REGEX_MAIUSCULO_MINUSCULO = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.letters");

    /**
     * <p>
     *     Numbers and Letters Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>^[ 0-9a-zA-Z\\b]+$</pre>
     */
    //public static final String REGEX_MAIUSCULO_MINUSCULO_NUMBER = "^[ 0-9a-zA"
    //        + "-Z\\\\b]+$";
    public static final String REGEX_MAIUSCULO_MINUSCULO_NUMBER = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.all");

    /**
     * <p>
     *     Domain Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>^((?!-)[A-Za-z0-9-]{1,63}(?&lt;!-)\\.)+[A-Za-z]{2,6}$</pre>
     */
    public static final String REGEX_DOMAIN_PATTERN = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.domain");

    /**
     * <p>
     *     Email Regular Expression.
     * </p>
     *
     * REG EX:
     * <pre>^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.
     * [0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$</pre>
     */
    public static final String REGEX_EMAIL_PATTERN = PROPERTIE
            .getPropertieByKey("util.string.pattern.regex.email");

    /**
     * <p>
     *     Create a {@link Matcher} object.
     * </p>
     *
     * @param expression Regular expression used
     * @param information Information to validate
     * @return Matcher.
     */
    public static Matcher getMatcher(final String expression, final String information) {
        final Pattern pattern = Pattern.compile(expression, Pattern.MULTILINE);
        return pattern.matcher(information);
    }

    /**
     * <p>
     *     Validate information using regular expression.
     * </p>
     *
     * @param expression Regular expression used
     * @param information Information to validate
     * @return True if information is valid with Regular Expression
     */
    public static Boolean validateWithRegEx(final String expression,
                                            final String information) {
        Matcher matcher = getMatcher(expression, information);
        return matcher.matches();
    }

    /**
     * <p>
     *     Enable debug for appling regular expression in String text with prints of groups finds on expression.
     * </p>
     *
     * @param expression Regular expression used
     * @param information Information to validate
     */
    public static void debugRegEx(final String expression, final String information) {
        if (log.isDebugEnabled()) {
            Matcher matcher = getMatcher(expression, information);
            while (matcher.find()) {
                log.debug("Full match: " + matcher.group(0));
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    log.debug("Group " + i + ": " + matcher.group(i));
                }
            }
        }
    }

    /**
     * <p>
     *     Enable debug for appling regular expression in String text with prints of groups finds on expression.
     * </p>
     *
     * @param expression Regular expression used
     * @param information Information to validate
     * @param idLog Log Id.
     */
    public static void debugRegEx(final String expression, final String information,
            final String idLog) {
        if (log.isDebugEnabled()) {
            log.debug("---BEGIN--- : " + idLog);
            debugRegEx(expression, information);
            log.debug("---END---");
        }
    }
}

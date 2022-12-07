/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.toolbox.stardart;

import com.mjolnir.toolbox.app.PropertiesUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.Normalizer;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

    //private static final String REG_EX_ASCII = "[^a-zA-Z0-9]+";
    private static final String REG_EX_ASCII = PropertiesUtil.getInstance()
            .getPropertieByKey("util.string.pattern.regex.ascii");

    /**
     * Blank space.
     */
    public static final String BRANCO = " ";
    /**
     * Dot.
     */
    public static final String PONTO = ".";
    /**
     * Minus.
     */
    public static final String SEPARADOR = "-";
    /**
     * Bar.
     */
    public static final String BARRA = "/";

    /**
     * <p>
     *     Delete blank spaces in right side of string.
     * </p>
     *
     * @param yourText Your text.
     * @return Your text without blank spaces.
     */
    public static String rightTrim(String yourText) {
        char[] chr = yourText.toCharArray();
        int lastIndexSpaceChar = 0;
        for (int i = yourText.length() - 1; i >= 0; --i) {
            if (Character.isSpaceChar(chr[i])) {
                continue;
            }
            lastIndexSpaceChar = i + 1;
            break;
        }
        return yourText.substring(0, lastIndexSpaceChar);
        //return yourText.replaceAll("\\s+$", "");
    }

    /**
     * <p>
     *     Delete blank spaces in left side of string.
     * </p>
     *
     * @param yourText Your text.
     * @return Your text without blank spaces.
     */
    public static String leftTrim(String yourText) {
        return yourText.replaceAll("^\\s+", "");
    }

    /**
     * <p>
     *     Verify if your text is Null or Blank Space.
     * </p>
     *
     * @param yourText Your Text
     * @return Boolean
     */
    public static Boolean isNullOrBlank(String yourText) {
        Boolean ret = Boolean.TRUE;
        if (yourText != null && yourText.trim().length() > 0) {
            ret = Boolean.FALSE;
        }
        return ret;
    }

    /**
     * <p>
     *     Remove special characters in text.
     * </p>
     *
     * @param yourText Your Text
     * @return Text widhout special characters
     */
    public static String removeSpecialCharacters(String yourText) {
        if (!StringUtil.isNullOrBlank(yourText)) {
            yourText = Normalizer.normalize(yourText, Normalizer.Form.NFD);
            return yourText.replaceAll(REG_EX_ASCII, "");
        }
        return yourText;
    }

    /**
     * <p>
     *     Replace blank space with another character.
     * </p>
     *
     * @param yourText Your Text
     * @param newCharacter Your new character
     * @return New text with character
     */
    public static String replaceBlankSpaceWithAnotherCharacter(String yourText, String newCharacter) {
        return yourText.replace(BRANCO, newCharacter);
    }

    /**
     * <p>
     *     Add a character in your text on left pad.
     * </p>
     *
     * @param yourText Your Text
     * @param width Pad Lenght
     * @param ch Character
     * @return Text with character
     */
    public static String lpadTo(String yourText, int width, char ch) {
        String strPad;
        StringBuilder sb = new StringBuilder(yourText.trim());
        while (sb.length() < width) {
            sb.insert(0, ch);
        }
        strPad = sb.toString();
        if (strPad.length() > width) {
            strPad = strPad.substring(0, width);
        }
        return strPad;
    }

    /**
     * <p>
     *     Add a character in your text on right pad.
     * </p>
     *
     * @param yourText Your Text
     * @param width Pad Lenght
     * @param ch Character
     * @return Text with character
     */
    public static String rPadTo(String yourText, int width, char ch) {
        StringBuilder ret = new StringBuilder("");
        ret.append(yourText != null ? yourText : "");
        while (ret.length() < width) {
            ret.append(ch);
        }
        return ret.toString();
    }

    /**
     * <p>
     *     Remove a character on your text.
     * </p>
     *
     * @param yourText Your Text
     * @param characterToRemove Character to remove
     * @return Text widhout character
     */
    public static String removeCharacter(String yourText, String characterToRemove) {
        if (yourText.contains(characterToRemove)) {
            yourText = yourText.replace(characterToRemove, "");
        }
        return yourText;
    }

    /**
     * <p>
     *     Remove dots of text.
     * </p>
     *
     * @param yourText Your Text
     * @return Text widhout Dots
     */
    public static String removeDots(String yourText) {
        return removeCharacter(yourText, PONTO);
    }

    /**
     * <p>
     *     Strip the letters of text with underline character
     * </p>
     *
     * @param yourText Texto a ser alterado.
     * @return Texto com separacao de anderline entre as letras.
     */
    public static String stripSpecialCharacters(String yourText) {
        yourText = Normalizer.normalize(yourText, Normalizer.Form.NFD);
        yourText = yourText.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        yourText = yourText.replaceAll(REG_EX_ASCII, "_");
        return yourText;
    }

    /**
     * <p>
     *     Apply a mask in your String text. For utilization of this method you need to input:
     * </p>
     * <p>The text to apply mask</p>
     * <p>The mask</p>
     * <p>The characters for the mask</p>
     * <p>Example:</p>
     * <br>
     * <p>applyMaskInText("11111111111","###.###.###-##", ".-");</p>
     * <p>applyMaskInText("1122223333","(##)####-####", "()-");</p>
     *
     * @param yourText Text to masked
     * @param mask Mask
     * @param controlCharacterMask Characters control of mask.
     * @return Masked Text.
     */
    public static String applyMaskInText(String yourText, String mask, String controlCharacterMask) {
        // se valor nulo ou menor que 1
        if (yourText == null || yourText.length() < 1) {
            // retorna vazio
            return "";
        }
        int nCount = 0;
        String valorFormatado = "";
        // realiza interação em todas as posições da mascara
        for (int i = 0; i <= mask.length() - 1; i++) {
            try {
                char caracter = ' ';
                // captura caracter da mascara no indice informado
                caracter = mask.charAt(i);
                // verifica se caracter capturado é igual aos caracteres de controle
                boolean bolMask = controlCharacterMask.contains(caracter + "");
                // se for igual a caracter de controle
                if (bolMask) {
                    // adiciona caracter de controle
                    valorFormatado += caracter + "";
                } else {
                    // senão irá adicionar o valor capturado a partir do indice informado
                    valorFormatado += yourText.charAt(nCount);
                    nCount++;
                }
            } catch (StringIndexOutOfBoundsException e) {
//                LOGGER.error("ERRO: Não foi possivel formatar a String com a"
//                        + " mascara proposta.", e);
                // quando a formatação for concluída lança exception
                return valorFormatado;
            }
        }
        return valorFormatado;
    }
}

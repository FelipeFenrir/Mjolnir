/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.commons;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Classe de Métodos Utilitários para Trabalhar com Datas.
 *
 * @author Felipe de Andrade Batista
 */
public class DataUtil {

    private long horas = 0;
    private long minutos = 0;
    private long segundos = 0;
    private long elapsedTimeInMillis;

    /**
     * Fator de Segundo = 1000 Segundos.
     */
    public static final long FATOR_SEGUNDO = 1000;
    /**
     * Fator de Minuto = 60000 Segundos.
     */
    public static final long FATOR_MINUTO = 60000;
    /**
     * Fator de Hora = 3600000 Segundos.
     */
    public static final long FATOR_HORA = 3600000;

    /**
     * BRAZIL LANGUAGE.
     */
    public static final String BRAZIL_LANGUAGE = "pt";
    /**
     * BRAZIL LOCALE.
     */
    public static final String BRAZIL_LOCALE = "BR";
    /**
     * BRAZIL TIMEZONE.
     */
    public static final TimeZone ZONE_PT_BR = TimeZone.getTimeZone("GMT-03:00");
    /**
     * BRAZIL LOCALE OBJECT.
     */
    public static final Locale LOCALE_PT_BR = new Locale(BRAZIL_LANGUAGE, BRAZIL_LOCALE);

    private static final Float TRANSFORMAR_SEGUNDOS = 1000.0f;
    private static final double TRANSFORMAR_DIAS = 8.64E7;
    private static final Integer REEF_DECADA_NOVENTA = 1900;
    private static final Integer MAX_HORA_DIA = 24;
    private static final Integer MAX_MINUTO_HORA = 60;
    private static final Integer MAX_SEGUNDO_MINUTO = 60;
    private static final Integer MAX_DIA_MES_TRINTA = 30;
    private static final Integer MAX_DIA_MES_TRINTA_UM = 31;
    private static final Integer MAX_DIA_MES_VINTE_OITO = 28;
    private static final Integer MAX_DIA_MES_VINTE_NOVE = 29;

    private static final String BRAZIL_DATE_PATTERN = PropertiesUtil.getInstance()
            .getPropertieByKey("util.date.pattern.brazil");
    private static final String EUA_DATE_PATTERN = PropertiesUtil.getInstance()
            .getPropertieByKey("util.date.pattern.eua");
    private static final String BRAZIL_XML_DATETIME_PATTERN = PropertiesUtil.getInstance()
            .getPropertieByKey("util.date.pattern.brazil.xml");
    private static final String EUA_XML_DATETIME_PATTERN = PropertiesUtil.getInstance()
            .getPropertieByKey("util.date.pattern.eua.xml");

    private static final SimpleDateFormat XML_DATETIME_FORMAT
            = new SimpleDateFormat(EUA_XML_DATETIME_PATTERN);
    private static final SimpleDateFormat EUA_DATEFORMAT
            = new SimpleDateFormat(EUA_DATE_PATTERN);
    private static final SimpleDateFormat BRAZIL_DATEFORMAT
            = new SimpleDateFormat(BRAZIL_DATE_PATTERN);

    private static final DateTimeFormatter BRAZIL_DATETIMEFORMAT
            = DateTimeFormatter.ofPattern(BRAZIL_DATE_PATTERN);
    private static final DateTimeFormatter EUA_DATETIMEFORMAT
            = DateTimeFormatter.ofPattern(EUA_DATE_PATTERN);

    private static final String NAME_MONTH = PropertiesUtil.getInstance()
            .getPropertieByKey("util.date.pattern.month");

    private static final Logger LOG = LoggerFactory.getLogger(DataUtil.class);

    /**
     * Instancia.
     */
    private static DataUtil instance = new DataUtil();

    /**
     * Construtor.
     */
    private DataUtil() {
    }

    /**
     * Retorna a instancia.
     *
     * @return DataUtil.
     */
    public static synchronized DataUtil getInstance() {
        if (instance == null) {
            instance = new DataUtil();
        }
        return instance;
    }

    private static Calendar getCalendarPtBr() {
        return getCalendar(ZONE_PT_BR, LOCALE_PT_BR);
    }

    private static Calendar getCalendar(TimeZone zone, Locale locale) {
        Calendar c = Calendar.getInstance(zone, locale);
        return c;
    }

    private static GregorianCalendar getGregorianCalendarPtBr() {
        return getGregorianCalendar(ZONE_PT_BR, LOCALE_PT_BR);
    }

    private static GregorianCalendar getGregorianCalendar(TimeZone zone, Locale locale) {
        GregorianCalendar cal = new GregorianCalendar(zone, locale);
        return cal;
    }

    /**
     * Calcula a diferença de dias entre duas datas.
     *
     * @param dataInicial
     * @param dataFinal
     * @return Número de dias entre datas.
     */
    private static BigDecimal getDiferencaEntreDatas(Date dataInicial,
            Date dataFinal) {
        try {
            double diferencaEmMs = dataFinal.getTime() - dataInicial.getTime();
            BigDecimal diferencaDias = new BigDecimal(diferencaEmMs / TRANSFORMAR_DIAS);
            return diferencaDias;
        } catch (Exception ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return null;
        }
    }

    /**
     * Calcula o número de dias entre duas datas, arredondando o numero de dias
     * para Baixo.
     *
     * @param dataInicial Data inicial
     * @param dataFinal Data final
     * @return Integer: Numero de Dias.
     */
    public static Integer getDiferencaEmDiasArrendodada(Date dataInicial,
            Date dataFinal) {
        try {
            BigDecimal diferencaDias = DataUtil.getDiferencaEntreDatas(dataInicial, dataFinal);
            MathContext mc = new MathContext(0, RoundingMode.DOWN);
            return diferencaDias.round(mc).intValue();
        } catch (Exception ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return null;
        }
    }

    /**
     * Calcula o número de dias entre duas datas.
     *
     * @param dataInicial Data inicial
     * @param dataFinal Data final
     * @return Float: Numero de Dias, podendo conter casas decimais.
     */
    public static Float getDiferencaEmDias(Date dataInicial, Date dataFinal) {
        try {
            BigDecimal diferencaDias = DataUtil.getDiferencaEntreDatas(dataInicial, dataFinal);
            return diferencaDias.floatValue();
        } catch (Exception ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return null;
        }
    }

    /**
     * Calcula o número de segundos entre duas datas.
     *
     * @param dataInicial Data inicial
     * @param dataFinal Data final
     * @return Float: Numero de segundos, podendo conter casas decimais.
     */
    public static Float getDiferencaEmSegundos(Date dataInicial,
            Date dataFinal) {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(("DataInicial:" + BRAZIL_DATEFORMAT.format(dataInicial)));
                LOG.debug(("DataFinal:" + BRAZIL_DATEFORMAT.format(dataFinal)));
            }
            float dif = (float) (dataFinal.getTime() - dataInicial.getTime()) / TRANSFORMAR_SEGUNDOS;
            if (LOG.isDebugEnabled()) {
                LOG.debug(("Diferen\u00e7a entre datas = " + dif));
            }
            return dif;
        } catch (Exception e) {
            LOG.error(e.toString(), (Throwable) e);
            return null;
        }
    }

    /**
     * Verifica se a Data final é maior que a Data inicial.
     *
     * @param dataInicial Data inicial.
     * @param dataFinal Data final.
     * @return Boolean
     */
    public boolean dataFimMaiorIni(Date dataInicial, Date dataFinal) {
        try {
            return DataUtil.getDiferencaEmDias(dataInicial, dataFinal) > 0.0f;
        } catch (Exception ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * Verifica se a Data final é maior ou igual a Data inicial.
     *
     * @param dataInicial Data inicial.
     * @param dataFinal Data final.
     * @return Boolean
     */
    public boolean dataFimMaiorIgualIni(Date dataInicial,
            Date dataFinal) {
        try {
            return DataUtil.getDiferencaEmDias(dataInicial, dataFinal) >= 0.0f;
        } catch (Exception ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * Verifica se a Data final é maior que a Data inicial considerando os
     * segundos.
     *
     * @param dataInicial Data inicial.
     * @param dataFinal Data final.
     * @return Boolean
     */
    public boolean dataFimMaiorIniEmSegundos(Date dataInicial,
            Date dataFinal) {
        try {
            return DataUtil.getDiferencaEmSegundos(dataInicial, dataFinal) > 0.0f;
        } catch (Exception ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * Verifica se a Data final é maior ou igual a Data inicial considerando os
     * segundos.
     *
     * @param dataInicial Data inicial.
     * @param dataFinal Data final.
     * @return Boolean
     */
    public boolean dataFimMaiorOuIgualIniEmSegundos(Date dataInicial,
            Date dataFinal) {
        try {
            return DataUtil.getDiferencaEmSegundos(dataInicial, dataFinal) >= 0.0f;
        } catch (Exception ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * Movimenta a data conforme a mudança de anos, para o futuro ou passado.
     *
     * @param dataReferencia Data usada como Referencia.
     * @param numeroAnosMinimos Anos
     * @param noPassado
     * @return Data no futuro ou passado.
     */
    public Date dataConformeAnos(Date dataReferencia, long numeroAnosMinimos,
            boolean noPassado) {
        GregorianCalendar calendar = getGregorianCalendarPtBr();
        return dataConformeAnos(dataReferencia, numeroAnosMinimos, noPassado, calendar);
    }

    /**
     * Movimenta a data conforme a mudança de anos, para o futuro ou passado.
     *
     * @param dataReferencia Data usada como Referencia.
     * @param numeroAnosMinimos Anos
     * @param noPassado
     * @param zone Objeto TimeZone
     * @param locale Objeto Locale
     * @return Data no futuro ou passado.
     */
    public Date dataConformeAnos(Date dataReferencia, long numeroAnosMinimos,
            boolean noPassado, TimeZone zone, Locale locale) {
        GregorianCalendar calendar = getGregorianCalendar(zone, locale);
        return dataConformeAnos(dataReferencia, numeroAnosMinimos, noPassado, calendar);
    }

    private Date dataConformeAnos(Date dataReferencia, long numeroAnosMinimos,
            boolean noPassado, GregorianCalendar calendar) {
        if (dataReferencia == null) {
            return null;
        }
        calendar.setTime(dataReferencia);
        calendar.roll(Calendar.YEAR, (int) (noPassado ? numeroAnosMinimos * -1 : numeroAnosMinimos));
        return calendar.getTime();
    }

    /**
     * Calcula o ultimo dia do Mês para o Calendario Brasileiro.
     *
     * @param dataAtual Data Atual
     * @return Data referente ao ultimo dia do Mês
     */
    public Date getDataFimMesCorrente(Date dataAtual) {
        return getDataFimMesCorrente(dataAtual, ZONE_PT_BR, LOCALE_PT_BR);
    }

    /**
     * Calcula o ultimo dia do Mês para o Calendario.
     *
     * @param dataAtual Data Atual
     * @param tz Objeto TimeZone
     * @param lc Objeto Locale
     * @return Data referente ao ultimo dia do Mês
     */
    public Date getDataFimMesCorrente(Date dataAtual, TimeZone tz, Locale lc) {
        final int finalHour = 23;
        final int finalSecond = 59;
        final int finalMilesecond = finalSecond;
        if (dataAtual == null) {
            return null;
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(dataAtual);
        Calendar cal = Calendar.getInstance(tz, lc);
        cal.set(c.get(Calendar.YEAR) - REEF_DECADA_NOVENTA, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        int diaFinal = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        GregorianCalendar dataFinal = new GregorianCalendar();
        dataFinal.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), diaFinal, finalHour, finalSecond, finalMilesecond);
        if (LOG.isDebugEnabled()) {
            LOG.debug(("O dia final para o mes = " + c.get(Calendar.MONTH)
                    + " é = " + dataFinal.getTime()));
        }
        return dataFinal.getTime();
    }

    /**
     * Retorna um Objeto do Tipo Date, sem as informações de Hora, minuto e
     * segundo.
     *
     * @param ano
     * @param mes
     * @param dia
     * @return Objeto que representa a data.
     */
    public static Date getDate(int ano, int mes, int dia) {
        return DataUtil.getDate(ano, mes, dia, 0, 0, 0);
    }

    /**
     * Retorna um Objeto do Tipo Date, sem as informações de Hora, minuto e
     * segundo.
     *
     * @param ano
     * @param mes
     * @param dia
     * @param zone Objeto TimeZone
     * @param locale Objeto Locale
     * @return Objeto que representa a data.
     */
    public static Date getDate(int ano, int mes, int dia, TimeZone zone,
            Locale locale) {
        GregorianCalendar calendar = getGregorianCalendar(zone, locale);
        return DataUtil.getDate(ano, mes, dia, 0, 0, 0, calendar);
    }

    /**
     * Retorna um Objeto do Tipo Date.
     *
     * @param ano
     * @param mes
     * @param dia
     * @param hora
     * @param minuto
     * @param segundo
     * @return Objeto que representa a data.
     */
    public static Date getDate(int ano, int mes, int dia, int hora, int minuto,
            int segundo) {
        GregorianCalendar calendar = GregorianCalendar
                .from(ZonedDateTime.now());
        return getDate(ano, mes, dia, hora, minuto, segundo, calendar);
    }

    /**
     * Retorna um Objeto do Tipo Date.
     *
     * @param ano
     * @param mes
     * @param dia
     * @param hora
     * @param minuto
     * @param segundo
     * @param calendar Objeto GregorianCalendar
     * @return Objeto que representa a data.
     */
    private static Date getDate(int ano, int mes, int dia, int hora, int minuto,
            int segundo, GregorianCalendar calendar) {
        if (!DataUtil.validarData(ano, mes, dia, hora, minuto, segundo, calendar)) {
            return null;
        }
        calendar.set(ano, mes - 1, dia);
        calendar.set(GregorianCalendar.SECOND, segundo);
        calendar.set(GregorianCalendar.MINUTE, minuto);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, hora);
        calendar.set(GregorianCalendar.MILLISECOND, 0);
        return convertToDate(calendar);
    }

    /**
     * Retorna um Objeto Date a partir de uma Data em formato texto (Não
     * Localizado).
     *
     * @param data Data em formato texto.
     * @param patternTo Padrãoem String para retorno da data.
     * @return Objeto que representa a data.
     */
    public static Date getDate(String data, String patternTo) {
        if (data == null || patternTo == null || data.trim().equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(patternTo);
        sdf.setLenient(false);
        Date novaData = null;
        try {
            novaData = sdf.parse(data);
        } catch (ParseException ex) {
            LOG.error(ex.toString(), (Throwable) ex);
        }
        return novaData;
    }

    /**
     * Retorna um Objeto Date a partir de uma Data em formato texto (Localizado
     * pt-BR).
     *
     * @param data Data em formato texto.
     * @param patternTo Padrãoem String para retorno da data.
     * @return Objeto que representa a data.
     */
    public static String getDate(Date data, String patternTo) {
        return getDate(data, patternTo, new Locale(BRAZIL_LANGUAGE, BRAZIL_LOCALE));
    }

    /**
     * Retorna um Objeto Date a partir de uma Data em formato texto (Não
     * Localizado).
     *
     * @param data Data em formato texto.
     * @param patternTo Padrãoem String para retorno da data.
     * @param locale Objeto de localização.
     * @return Objeto que representa a data.
     */
    public static String getDate(Date data, String patternTo, Locale locale) {
        if (data == null || patternTo == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(patternTo, locale);
        sdf.setLenient(false);
        sdf.applyPattern(patternTo);
        return sdf.format(data);
    }

    /**
     * Retorna um Objeto Date a partir de uma Data em formato texto (Localizado
     * pt-BR).
     *
     * @param data Data em formato texto.
     * @param dateFormat Padrãoem Int para retorno da data.
     * @return Objeto que representa a data.
     * @throws java.lang.Exception
     */
    public static Date getDate(String data, int dateFormat) throws Exception {
        try {
            return getDate(data, dateFormat, new Locale(BRAZIL_LANGUAGE, BRAZIL_LOCALE));
        } catch (Exception e) {
            LOG.error(e.toString(), (Throwable) e);
            return null;
        }
    }

    /**
     * Retorna um Objeto Date a partir de uma Data em formato texto (Não
     * Localizado).
     *
     * @param data Data em formato texto.
     * @param dateFormat Padrãoem Int para retorno da data.
     * @param locale Objeto de localização.
     * @return Objeto que representa a data.
     * @throws java.lang.Exception
     */
    public static Date getDate(String data, int dateFormat, Locale locale)
            throws Exception {
        try {
            DateFormat df = DateFormat.getDateInstance(dateFormat, locale);
            df.setLenient(false);
            return df.parse(data);
        } catch (ParseException e) {
            LOG.error(e.toString(), (Throwable) e);
            return null;
        }
    }

    /**
     * Altera o padrão de uma data para outro padrão. EXemplo: 09/01/1989 para
     * 1989-01-09
     *
     * @param data Data a ser alterada.
     * @param patternOf Padrão Atual.
     * @param patternTo Padrão para alteração.
     * @return Data com Padrão alterado.
     */
    public static String transformDateToAnotherPattern(String data,
            String patternOf, String patternTo) {
        LOG.debug("###################### Entrou no evento transformPattern");
        try {
            if (data.trim().length() == 0) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(patternOf);
            sdf.setLenient(false);
            Date df = sdf.parse(data);
            LOG.debug("O padrão (DE) = " + patternOf + " formatou a data"
                    + " para o padrão = " + df);
            sdf.applyPattern(patternTo);
            final String result = sdf.format(df);
            LOG.debug("O padrão (PARA) = " + patternTo + " formatou a data"
                    + " para o padrão = " + result);
            return result;
        } catch (ParseException e) {
            LOG.error(e.toString(), (Throwable) e);
            return "";
        }
    }

    /**
     * Retorna o Nome do Mês da Data Passada como Parametro.
     *
     * @param data Data para extração do nome do Mês.
     * @param locale Objeto de Localização.
     * @return Nome do Mês.
     */
    public String getNomeMes(Date data, Locale locale) {
        if (data == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(NAME_MONTH, locale);
        return f.format(data);
    }

    /**
     * Retorna o Nome do Mês da Data Passada como Parametro (Localizado pt-BR).
     *
     * @param data Data para extração do nome do Mês.
     * @return Nome do Mês.
     */
    public String getNomeMes(Date data) {
        return getNomeMes(data, new Locale(BRAZIL_LANGUAGE, BRAZIL_LOCALE));
    }

    /**
     * Retorna uma lista com as datas entre duas outras datas.
     *
     * @param inicio Data de Inicio.
     * @param fim Data de Fim.
     * @return Lista de Datas entre as datas passadas.
     */
    public List getListaDeDatas(Date inicio, Date fim) {
        Calendar calendar = getCalendarPtBr();
        return getListaDeDatas(inicio, fim, calendar);
    }

    /**
     * Retorna uma lista com as datas entre duas outras datas.
     *
     * @param inicio Data de Inicio.
     * @param fim Data de Fim.
     * @param zone Objeto TimeZone
     * @param locale Objeto Locale
     * @return Lista de Datas entre as datas passadas.
     */
    public List getListaDeDatas(Date inicio, Date fim, TimeZone zone, Locale locale) {
        Calendar calendar = getCalendar(zone, locale);
        return getListaDeDatas(inicio, fim, calendar);
    }

    /**
     * Retorna uma lista com as datas entre duas outras datas.
     *
     * @param inicio Data de Inicio.
     * @param fim Data de Fim.
     * @param calendar Objeto Calendar
     * @return Lista de Datas entre as datas passadas.
     */
    private List getListaDeDatas(Date inicio, Date fim, Calendar calendar) {
        if (inicio == null || fim == null) {
            return new ArrayList();
        }
        HashMap<Date, Date> lista = new HashMap<>();
        Float dias = DataUtil.getDiferencaEmDias(inicio, fim);
        calendar.setTime(inicio);
        lista.put(calendar.getTime(), calendar.getTime());
        int i = 1;
        while (dias.intValue() > i - 1) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            lista.put(calendar.getTime(), calendar.getTime());
            ++i;
        }
        ArrayList listaRetorno = new ArrayList(lista.values());
        Collections.sort(listaRetorno);
        return listaRetorno;
    }

    /**
     * Verifica se a data é valida.
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @param calendar Objeto GregorianCalendar
     * @return Boolean
     */
    private static boolean validarData(int year, int month, int day, int hour,
            int minute, int second, GregorianCalendar calendar) {
        calendar.set(year, --month, day, hour, minute, second);
        int testYear = calendar.get(Calendar.YEAR);
        int testMonth = calendar.get(Calendar.MONTH);
        int testDay = calendar.get(Calendar.DAY_OF_MONTH);
        int testHour = calendar.get(Calendar.HOUR_OF_DAY);
        int testMinute = calendar.get(Calendar.MINUTE);
        int testSecond = calendar.get(Calendar.SECOND);
        if (month != testMonth || day != testDay || year != testYear
                || hour != testHour || minute != testMinute || second != testSecond) {
            return false;
        }
        return true;
    }

    /**
     * Retorna o Numero representando o Ano da data passada como Parametro.
     *
     * @param data
     * @return Numero representando o Ano.
     */
    public static int getAno(Date data) {
        return getAno(data, ZONE_PT_BR, LOCALE_PT_BR);
    }

    /**
     * Retorna o Numero representando o Ano da data passada como Parametro.
     *
     * @param data
     * @param zone Objeto TimeZone
     * @param locale Objeto Locale
     * @return Numero representando o Ano.
     */
    public static int getAno(Date data, TimeZone zone, Locale locale) {
        Calendar cal = getCalendar(zone, locale);
        cal.setTime(data);
        return cal.get(Calendar.YEAR);
    }

    /**
     * Calcula a diferença em Horasentre duas datas.
     *
     * @param dataInicial
     * @param dataFinal
     * @return valor em Horas de diferença entre as datas.
     */
    public static BigDecimal getDiferencaHoras(Date dataInicial, Date dataFinal) {
        BigDecimal dia = BigDecimal.valueOf(3600000);
        BigDecimal di = BigDecimal.valueOf(dataInicial.getTime());
        BigDecimal df = BigDecimal.valueOf(dataFinal.getTime());
        BigDecimal diferenca = df.subtract(di).divide(dia, 5, 0);
        return diferenca;
    }

    /**
     * Subtrai a data os valores de parametros.
     *
     * @param data
     * @param dias
     * @param hora
     * @param minuto
     * @param segundo
     * @return Nova Data.
     */
    public static Date subtraiData(Date data, int dias, int hora, int minuto,
            int segundo) {
        return subtraiData(data, dias, hora, minuto, segundo, ZONE_PT_BR, LOCALE_PT_BR);
    }

    /**
     * Subtrai a data os valores de parametros.
     *
     * @param data
     * @param dias
     * @param hora
     * @param minuto
     * @param segundo
     * @param zone Objeto TimeZone
     * @param locale Objeto Locale
     * @return Nova Data.
     */
    public static Date subtraiData(Date data, int dias, int hora, int minuto,
            int segundo, TimeZone zone, Locale locale) {
        Calendar calendar = getCalendar(zone, locale);
        calendar.setTime(data);
        calendar.add(Calendar.DAY_OF_YEAR, -dias);
        calendar.set(Calendar.HOUR, hora);
        calendar.set(Calendar.MINUTE, minuto);
        calendar.set(Calendar.SECOND, segundo);
        Date dataMenosDias = calendar.getTime();
        return dataMenosDias;
    }

    /**
     * Soma a data os valores de parametros.
     *
     * @param data
     * @param dias
     * @param hora
     * @param minuto
     * @param segundo
     * @return Nova Data.
     */
    public static Date somaData(Date data, int dias, int hora, int minuto,
            int segundo) {
        return somaData(data, dias, hora, minuto, segundo, ZONE_PT_BR, LOCALE_PT_BR);
    }

    /**
     * Soma a data os valores de parametros.
     *
     * @param data
     * @param dias
     * @param hora
     * @param minuto
     * @param segundo
     * @param zone Objeto TimeZone
     * @param locale Objeto Locale
     * @return Nova Data.
     */
    public static Date somaData(Date data, int dias, int hora, int minuto,
            int segundo, TimeZone zone, Locale locale) {
        Calendar cal = getCalendar(zone, locale);
        cal.setTime(data);
        cal.add(Calendar.DAY_OF_YEAR, dias);
        cal.set(Calendar.HOUR, hora);
        cal.set(Calendar.MINUTE, minuto);
        cal.set(Calendar.SECOND, segundo);
        Date dataMenosDias = cal.getTime();
        return dataMenosDias;
    }

    /**
     * Adiciona Minutos em uma Data.
     *
     * @param data Data a ser adicionado os Minutos.
     * @param numberMin Quantidade de Minutos.
     * @return Data Calculada.
     */
    public static Date incMin(Date data, int numberMin) {
        if (data != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);
            cal.add(Calendar.MINUTE, numberMin);
            data = cal.getTime();
        }
        return data;
    }

    /**
     * Registra no Objeto Data um Horario passado como Parametro.
     *
     * @param data
     * @param hora Horario a ser registrado.
     * @return Data com Horario registrado.
     */
    public static Date setHora(Date data, int hora) {
        if (data != null && hora >= 0 && hora < MAX_HORA_DIA) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);
            cal.set(Calendar.HOUR_OF_DAY, hora);
            data = cal.getTime();
        }
        return data;
    }

    /**
     * Registra no Objeto Data o Minuto passado como Parametro.
     *
     * @param data
     * @param minutos Minutos a serem registrado.
     * @return Data com Minutos registrados.
     */
    public static Date setMinutos(Date data, int minutos) {
        if (data != null && minutos >= 0 && minutos < MAX_MINUTO_HORA) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);
            cal.set(Calendar.MINUTE, minutos);
            data = cal.getTime();
        }
        return data;
    }

    /**
     * Registra no Objeto Data o Segundo passado como Parametro.
     *
     * @param data
     * @param segundos Segundos a serem registrado.
     * @return Data com Segundos registrados.
     */
    public static Date setSegundos(Date data, int segundos) {
        if (data != null && segundos >= 0 && segundos < MAX_SEGUNDO_MINUTO) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(data);
            cal.set(Calendar.SECOND, segundos);
            data = cal.getTime();
        }
        return data;
    }

    /**
     * Registra no Objeto Data a Hora, Minuto e Segundos passados como
     * Parametros.
     *
     * @param data Data
     * @param hora Hora
     * @param min Minutos
     * @param seg Segundos
     * @return Data com Parametros registrados.
     */
    public static Date setHoraMinutoSegundo(Date data, int hora, int min,
            int seg) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(data);
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, seg);
        cal.set(Calendar.MILLISECOND, 0);
        data = cal.getTime();
        return data;
    }

    /**
     * Retorna uma String representando a Data Atual, padrão de formatação EUA.
     * Padrão: yyyy-MM-dd HH:mm:ss
     *
     * @return Data em texto.
     */
    public static String sysDate() {
        return toFormat(DataUtil.now(), EUA_DATE_PATTERN);
    }

    /**
     * Retorna uma String representando a Data Atual, formatada em um padrão.
     * <br>
     * Caso a String não seja o suficiente para retornar um formato valido é
     * retornado o formato Padrão: yyyy-MM-dd'T'HH:mm:ss
     *
     * @param mascara Padrão para formatação.
     * @return Data em texto.
     */
    public static String sysDate(String mascara) {
        return toFormat(DataUtil.now(), mascara);
    }

    /**
     * Retorna uma {@link LocalDateTime} representando a Data Atual.
     *
     * @return Data em texto.
     */
    public static LocalDateTime now() {
        LocalDateTime today = LocalDateTime.now();
        return today;
    }

    /**
     * Retorna uma {@link LocalDateTime} representando a Data Atual, formatada.
     * <br>
     * Caso a String não seja o suficiente para retornar um formato valido é
     * retornado o formato Padrão: yyyy-MM-dd'T'HH:mm:ss
     *
     * @param date
     * @param format
     * @return Data em texto.
     */
    public static String toFormat(LocalDateTime date, String format) {
        return date.format(getFormater(format));
    }

    /**
     * Retorna um Formatador de Data {@link DateTimeFormatter}.
     * <br>
     * Caso a String não seja o suficiente para retornar um formato valido é
     * retornado o formato Padrão: yyyy-MM-dd'T'HH:mm:ss
     *
     * @param format String representando um formato.
     * @return
     */
    public static DateTimeFormatter getFormater(String format) {
        DateTimeFormatter formatador;
        if (format != null && format.trim().length() > 0) {
            try {
                formatador = DateTimeFormatter.ofPattern(format);
                return formatador;
            } catch (IllegalArgumentException ex) {
                return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            }
        } else {
            return DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            //throw new IllegalArgumentException("Format inconmpatible.");
        }
    }

    /**
     * Retorna um Formatador de Data {@link DateTimeFormatter}.
     * <br>
     * Caso a String não seja o suficiente para retornar um formato valido é
     * retornado o formato Padrão: yyyy-MM-dd'T'HH:mm:ss
     *
     * @return
     */
    public static DateTimeFormatter getFormater() {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .optionalStart()
                .appendLiteral('T')
                .optionalEnd()
                .optionalStart()
                .appendLiteral(' ')
                .optionalEnd()
                .optionalStart()
                .append(DateTimeFormatter.ISO_TIME)
                .optionalEnd()
                .toFormatter();

        return formatter;
    }

    /**
     * Converte Objeto String em {@link LocalDate}.
     * <br>
     * Caso a String não seja o suficiente para retornar um formato valido é
     * retornado o formato Padrão: yyyy-MM-dd
     *
     * @param dateTime
     * @return Retorna Objeto {@link LocalDate}.
     */
    public static LocalDate convertToLocalDate(String dateTime) {
        DateTimeFormatter formatter = getFormater();
        return LocalDate.parse(dateTime, formatter);
    }

    /**
     * Converte Objeto String em {@link LocalDateTime}.
     * <br>
     * Caso a String não seja o suficiente para retornar um formato valido é
     * retornado o formato Padrão: yyyy-MM-dd'T'HH:mm:ss
     *
     * @param dateTime
     * @param format
     * @return Retorna Objeto {@link LocalDateTime}.
     */
    public static LocalDateTime convertToLocalDateTime(String dateTime,
            String format) {
        DateTimeFormatter formatter = getFormater(format);
        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * Converte Objeto String em {@link LocalDateTime}.
     * <br>
     * Caso a String não seja o suficiente para retornar um formato valido é
     * retornado o formato Padrão: yyyy-MM-dd'T'HH:mm:ss
     *
     * @param dateTime
     * @return Retorna Objeto {@link LocalDateTime}.
     */
    public static LocalDateTime convertToLocalDateTime(String dateTime) {
        DateTimeFormatter formatter = getFormater();
        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * Converte Objeto Date em {@link LocalDateTime}.
     * <br>
     * Caso a String não seja o suficiente para retornar um formato valido é
     * retornado o formato Padrão: yyyy-MM-dd'T'HH:mm:ss
     *
     * @param date
     * @return Retorna Objeto {@link LocalDateTime}.
     */
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return convertToLocalDateTime(date, ZoneId.systemDefault());
    }

    /**
     * Converte Objeto Date em {@link LocalDateTime}, utilizando {@link ZoneId}.
     *
     * @param date
     * @param zone
     * @return Retorna Objeto {@link LocalDateTime}.
     */
    public static LocalDateTime convertToLocalDateTime(Date date, ZoneId zone) {
        return date.toInstant().atZone(zone).toLocalDateTime();
    }

    /**
     * Converte Objeto {@link LocalDateTime} em Date.
     *
     * @param localDateTime
     * @return Retorna Objeto Date.
     */
    public static Date convertToDate(LocalDateTime localDateTime) {
        return convertToDate(localDateTime, ZoneId.systemDefault());
    }

    /**
     * Converte Objeto {@link LocalDateTime} em Date, utilizando uma
     * {@link ZoneId}.
     *
     * @param localDateTime
     * @param zone
     * @return Retorna Objeto Date.
     */
    public static Date convertToDate(LocalDateTime localDateTime, ZoneId zone) {
        return Date.from(localDateTime.atZone(zone).toInstant());
    }

    /**
     * Converte Objeto Calendar em Date.
     *
     * @param dtCalendar
     * @return Retorna Objeto Date.
     */
    public static Date convertToDate(Calendar dtCalendar) {
        return new Date(dtCalendar.getTimeInMillis());
    }

    /**
     * Converte String de Data em Objeto Date.
     *
     * @param dtString String de Data.
     * @return Objeto Date.
     */
    public static Date convertToDate(String dtString) {
        Boolean resultEUA = RegExUtil
                .validateWithRegEx(RegExUtil.REGEX_DATA_HORA_EUA, StringUtil.leftTrim(dtString));
        Boolean resultV2EUA = RegExUtil
                .validateWithRegEx(RegExUtil.REGEX_DATA_HORA_V2_EUA,
                        StringUtil.leftTrim(StringUtil.rightTrim(dtString)));
        if (resultEUA || resultV2EUA) {
            LocalDateTime ldate = convertToLocalDateTime(StringUtil.leftTrim(dtString));
            Date date = Date.from(ldate.atZone(ZoneId.systemDefault())
                    .toInstant());
            return date;
        } else {
            LocalDate ldate = DataUtil.convertToLocalDate(StringUtil.leftTrim(dtString));
            Date date = Date.from(ldate.atStartOfDay(ZoneId.systemDefault())
                    .toInstant());
            return date;
        }
    }

    /**
     * Converte String de Data em Objeto Date, utilizando um Padrão.
     *
     * @param dtString String de Data.
     * @param pattern String de Padrão de conversão.
     * @return Objeto Date.
     * @throws ParseException
     */
    public static Date convertToDate(String dtString, String pattern)
            throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.parse(dtString);
    }

    /**
     * Converte Objeto XMLGregorianCalendar em Date.
     *
     * @param xmlGregorianCalendar Objeto XMLGregorianCalendar para Conversão.
     * @return Retorna Objeto Date.
     */
    public static Date convertToDate(XMLGregorianCalendar xmlGregorianCalendar) {
        return xmlGregorianCalendar.toGregorianCalendar().getTime();
    }

    /**
     * Converte Objeto Date em Calendar.
     *
     * @param date
     * @return Retorna Objeto Calendar.
     */
    public static Calendar convertToCalendar(Date date) {
        Calendar dtCalendar = Calendar.getInstance();
        dtCalendar.setTime(date);
        return dtCalendar;
    }

    /**
     * Converte String de Data em Objeto Calendar, utilizando um Padrão.
     *
     * @param dtString String de Data.
     * @param pattern String de Padrão de conversão.
     * @return Objeto Calendar.
     * @throws ParseException
     */
    public static Calendar convertToCalendar(String dtString, String pattern)
            throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return convertToCalendar(df.parse(dtString));
    }

    /**
     * Converte String de Data em Objeto Calendar.
     *
     * @param xmlDateTimeString String de DateTime.
     * @return Objeto Calendar.
     * @throws ParseException
     */
    public static Calendar convertToCalendar(String xmlDateTimeString)
            throws ParseException {
        Date date = XML_DATETIME_FORMAT.parse(xmlDateTimeString);
        return convertToCalendar(date);
    }

    /**
     * Converte LocalTime em Objeto Calendar.
     *
     * @param localTime.
     * @return Objeto Calendar.
     */
    public static Calendar convertToCalendar(LocalTime localTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        //assuming year/month/date information is not important
        calendar.set(0, 0, 0, localTime.getHour(), localTime.getMinute(),
                localTime.getSecond());
        return calendar;
    }

    /**
     * Converte LocalDate em Objeto Calendar.
     *
     * @param localDate.
     * @return Objeto Calendar.
     */
    public static Calendar convertToCalendar(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        //assuming start of day
        calendar.set(localDate.getYear(), localDate.getMonthValue() - 1,
                localDate.getDayOfMonth());
        return calendar;
    }

    /**
     * Converte LocalDateTime em Objeto Calendar.
     *
     * @param localDateTime.
     * @return Objeto Calendar.
     */
    public static Calendar convertToCalendar(LocalDateTime localDateTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(localDateTime.getYear(), localDateTime.getMonthValue() - 1,
                localDateTime.getDayOfMonth(), localDateTime.getHour(),
                localDateTime.getMinute(), localDateTime.getSecond());
        return calendar;
    }

    /**
     * Converte Objeto Date em XMLGregorianCalendar.
     *
     * @param date Objeto Date para Conversão.
     * @return Retorna Objeto XMLGregorianCalendar.
     */
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        LOG.debug("###################### Entrou no evento"
                + " convertToXMLGregorianCalendar");
        if (date == null) {
            LOG.debug("A data passada é NULL");
            return null;
        } else {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            try {
                return DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException e) {
                LOG.error(e.toString(), (Throwable) e);
                throw new RuntimeException("ApiError ao converter Date em"
                        + " XMLGregorianCalendar", e);
            }
        }
    }

    /**
     * Converte a Data em String.
     *
     * @param dtCalendar Data para Conversão.
     * @param pattern Padrão.
     * @return Data formatada.
     */
    public static String convertToString(Calendar dtCalendar, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(convertToDate(dtCalendar));
    }

    /**
     * Converte a Data em String.
     *
     * @param date Data para Conversão.
     * @param pattern Padrão.
     * @return Data formatada.
     */
    public static String convertToString(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * Formata a Data em Formato Universal e localizado via REGEX.
     *
     * @param date Data a ser formatada.
     * @return Data em formato String.
     */
    public static String convertToXmlDateTimeString(Date date) {
        return XML_DATETIME_FORMAT.format(date);
    }

    /**
     * Converte a Data em String.
     * <br>
     * Caso a String não seja o suficiente para retornar um formato valido é
     * retornado o formato Padrão: yyyy-MM-dd'T'HH:mm:ss
     *
     * @param date Data para Conversão.
     * @param pattern Padrão.
     * @return Data formatada.
     */
    public static String convertToString(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = getFormater(pattern);
        return date.format(formatter);
    }

    /**
     * Calcula a idade utilizando a data de Nascimento.
     *
     * @param dtNascimento
     * @return Retorna a Idade.
     */
    public static int calcularIdade(Date dtNascimento) {
        Calendar dtNascimentoAsCalendar = convertToCalendar(dtNascimento);
        Calendar hoje = Calendar.getInstance();
        int idade = hoje.get(Calendar.YEAR) - dtNascimentoAsCalendar
                .get(Calendar.YEAR);
        dtNascimentoAsCalendar.add(Calendar.YEAR, idade);
        if (hoje.before(dtNascimentoAsCalendar)) {
            idade--;
        }
        LOG.debug("Com o nascimento em: "
                + convertToString(dtNascimento, BRAZIL_DATE_PATTERN)
                //+ convertToString(dtNascimento, EUA_DATE_PATTERN)
                + " A idade hoje é de: "
                + idade
                + " ano(s)");
        return idade;
    }

    /**
     * Altera a hora do Objeto Date para a hora Inicial.
     * <br>
     * Exemplo: 09/01/1989 05:30:10:500 para 09/01/1989 00:00:00:000
     *
     * @param date Data a ser alterada.
     * @return Data alterada.
     */
    public static Date floorHorario(Date date) {
        Calendar dateAsCalendar = convertToCalendar(date);
        return floorHorario(dateAsCalendar);
    }

    /**
     * Altera a hora do Objeto LocalDateTime para a hora Inicial.
     * <br>
     * Exemplo: 09/01/1989 05:30:10:500 para 09/01/1989 00:00:00:000
     *
     * @param date LocalDateTime a ser alterada.
     * @return Date alterada.
     */
    public static Date floorHorario(LocalDateTime date) {
        Calendar dateAsCalendar = convertToCalendar(date);
        return floorHorario(dateAsCalendar);
    }

    /**
     * Altera a hora do Objeto Calendar para a hora Inicial.
     * <br>
     * Exemplo: 09/01/1989 05:30:10:500 para 09/01/1989 00:00:00:000
     *
     * @param date Calendar a ser alterada.
     * @return Date alterada.
     */
    public static Date floorHorario(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return convertToDate(date);
    }

    /**
     * Altera a hora do Objeto Date para a hora final.
     * <br>
     * Exemplo: 09/01/1989 05:30:10:500 para 09/01/1989 23:59:59:999
     *
     * @param date Data a ser alterada.
     * @return Data alterada.
     */
    public static Date ceilHorario(Date date) {
        Calendar dateAsCalendar = convertToCalendar(date);
        return ceilHorario(dateAsCalendar);
    }

    /**
     * Altera a hora do Objeto LocalDateTime para a hora final.
     * <br>
     * Exemplo: 09/01/1989 05:30:10:500 para 09/01/1989 23:59:59:999
     *
     * @param date Data a ser alterada.
     * @return Data alterada.
     */
    public static Date ceilHorario(LocalDateTime date) {
        Calendar dateAsCalendar = convertToCalendar(date);
        return ceilHorario(dateAsCalendar);
    }

    /**
     * Altera a hora do Objeto Calendar para a hora final.
     * <br>
     * Exemplo: 09/01/1989 05:30:10:500 para 09/01/1989 23:59:59:999
     *
     * @param date Data a ser alterada.
     * @return Data alterada.
     */
    public static Date ceilHorario(Calendar date) {
        final int horaFinal = 23;
        final int minutoSegundoFinal = 59;
        final int milisegundoFinal = 999;
        date.set(Calendar.HOUR_OF_DAY, horaFinal);
        date.set(Calendar.MINUTE, minutoSegundoFinal);
        date.set(Calendar.SECOND, minutoSegundoFinal);
        date.set(Calendar.MILLISECOND, milisegundoFinal);
        return convertToDate(date);
    }

    /**
     * Validar a String de Data de acordo com o Padrão Passado.
     *
     * @param strDate Data a ser validada em String.
     * @param pattern Padrão de Validação em String.
     * @return Boolean.
     */
    public static boolean validarStringData(String strDate, String pattern) {
        LOG.debug("###################### Entrou no evento validarData");
        try {
            final String defaultPattern = "dd/MM/yyyy";
            final String dateTransformed = transformDateToAnotherPattern(strDate, pattern, defaultPattern);
            Pattern p = Pattern.compile("(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d");
            Matcher m = p.matcher(dateTransformed);
            if (pattern.equals(defaultPattern) && !m.matches()) {
                LOG.debug("A data: "
                        + dateTransformed
                        + "não é valida para o padrão."
                        + pattern);
                return false;
            } else {
                //SimpleDateFormat df = new SimpleDateFormat(pattern);
                //df.parse(dateTransformed);

                final int dia = Integer.parseInt(dateTransformed.substring(0, 2));
                final int mes = Integer.parseInt(dateTransformed.substring(3, 5));
                final int ano = Integer.parseInt(dateTransformed.substring(6));

                LOG.debug("o Integer que representa dia é: "
                        + String.valueOf(dia));
                LOG.debug("o Integer que representa mes é: "
                        + String.valueOf(mes));
                LOG.debug("o Integer que representa ano é: "
                        + String.valueOf(ano));

                switch (mes - 1) {
                    case Calendar.JANUARY:
                    case Calendar.MARCH:
                    case Calendar.MAY:
                    case Calendar.JULY:
                    case Calendar.AUGUST:
                    case Calendar.OCTOBER:
                    case Calendar.DECEMBER:
                        if (dia <= MAX_DIA_MES_TRINTA_UM) {
                            LOG.debug("A data esta em um Mês de 31 dias");
                            LOG.debug("A data é valida");
                            return true;
                        }
                        break;
                    case Calendar.APRIL:
                    case Calendar.JUNE:
                    case Calendar.SEPTEMBER:
                    case Calendar.NOVEMBER:
                        if (dia <= MAX_DIA_MES_TRINTA) {
                            LOG.debug("A data esta em um Mês de 30 dias");
                            LOG.debug("A data é valida");
                            return true;
                        }
                        break;
                    case Calendar.FEBRUARY:
                        final int[] modsBissexto = {4, 100, 400};
                        boolean bissexto = false;
                        if ((ano % modsBissexto[0] == 0)
                                || (ano % modsBissexto[1] == 0)
                                || (ano % modsBissexto[2] == 0)) {
                            LOG.debug("O ano é bissexto");
                            LOG.debug("A data é valida");
                            bissexto = true;
                        }
                        if ((bissexto) && (dia <= MAX_DIA_MES_VINTE_NOVE)) {
                            LOG.debug("O mes é fevereiro bissexto");
                            LOG.debug("A data é valida");
                            return true;
                        }
                        if ((!bissexto) && (dia <= MAX_DIA_MES_VINTE_OITO)) {
                            LOG.debug("O mes é fevereiro não bissexto");
                            LOG.debug("A data é valida");
                            return true;
                        }
                        break;
                    default:
                        if (dia <= MAX_DIA_MES_TRINTA_UM) {
                            LOG.debug("A data esta em um Mês de 31 dias");
                            LOG.debug("A data é valida");
                            return true;
                        }
                        break;
                }
                return false;
            }
            //} catch (NumberFormatException | ParseException ex) {
        } catch (NumberFormatException ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * Validador de Intervalo de datas, verificando se a Data final é menor que
     * a data inicial.
     *
     * @param dtInicial Data Inicial.
     * @param dtFinal Data Final.
     * @return Boolean
     */
    public static boolean validarIntervaloDatas(Date dtInicial, Date dtFinal) {
        LOG.debug("###################### Entrou no evento"
                + " validarIntervaloDatas");
        try {
            if (dtFinal.getTime() >= dtInicial.getTime()) {
                LOG.debug("A data final é maior ou igual a data inicial");
                return true;
            } else {
                LOG.debug("A data final é menor que a data inicial");
                return false;
            }
        } catch (Exception ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * Validador de Intervalo de datas, verificando se a Data final é menor que
     * a data inicial, levando em consideração um padrão de formatação.
     *
     * @param strDataInicial Data Inicial.
     * @param strDataFinal Data Final.
     * @param pattern Padrão de formatação das datas.
     * @return Boolean
     */
    public static boolean validarIntervaloDatas(String strDataInicial,
            String strDataFinal, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date dataInicial = dateFormat.parse(strDataInicial);
            Date dataFinal = dateFormat.parse(strDataFinal);
            return validarIntervaloDatas(dataInicial, dataFinal);
        } catch (ParseException ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * Validador de Intervalo de datas, verificando se a Data final é menor que
     * a data inicial, levando em consideração um padrão de formatação.
     *
     * @param strDataInicial Data Inicial.
     * @param strDataFinal Data Final.
     * @param pattern Padrão de formatação das datas.
     * @param zone Objeto TimeZone
     * @return Boolean
     */
    public static boolean validarIntervaloDatas(String strDataInicial,
            String strDataFinal, String pattern, TimeZone zone) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setTimeZone(zone);
            Date dataInicial = dateFormat.parse(strDataInicial);
            Date dataFinal = dateFormat.parse(strDataFinal);
            return validarIntervaloDatas(dataInicial, dataFinal);
        } catch (ParseException ex) {
            LOG.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * Converte um Objeto Date para um Objeto Timestamp.
     *
     * @param data Objeto Date a ser convertido.
     * @return Objeto Timestamp.
     */
    public static Timestamp convertToTimestamp(Date data) {
        return new Timestamp(data.getTime());
    }
}

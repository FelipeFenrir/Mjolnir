/*
 * Copyright (c) 2020. Fenrir Solucoes em Tecnologia. All rights reserved.
 *  Fenrir Systems, Odin System and All the Programing Code of this softwares are private.
 */
package com.mjolnir.toolbox.stardart;

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

import com.mjolnir.toolbox.app.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *     Utility class of Date and Time.
 * </p>
 *
 * @author Felipe de Andrade Batista
 */
@Slf4j
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
    public static final long HOUR_FACTOR = 3600000;

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

    private static DataUtil instance = new DataUtil();

    private DataUtil() {}

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
        return Calendar.getInstance(zone, locale);
    }

    private static GregorianCalendar getGregorianCalendarPtBr() {
        return getGregorianCalendar(ZONE_PT_BR, LOCALE_PT_BR);
    }

    private static GregorianCalendar getGregorianCalendar(TimeZone zone, Locale locale) {
        return new GregorianCalendar(zone, locale);
    }

    /**
     * <p>
     *     Get number of days between two dates.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @return Days between dates.
     */
    private static BigDecimal getNumberOfDaysBetweenDates(Date initialDate,
                                                          Date endDate) {
        try {
            double millisecondValue = endDate.getTime() - initialDate.getTime();
            return new BigDecimal(millisecondValue / TRANSFORMAR_DIAS);
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return null;
        }
    }

    /**
     * <p>
     *     Get number of days between two dates rounded down.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @return Number of Days
     */
    public static Integer getRoundedNumberOfDaysBetweenDates(Date initialDate,
                                                             Date endDate) {
        try {
            BigDecimal millisecondValue = DataUtil.getNumberOfDaysBetweenDates(initialDate, endDate);
            MathContext mc = new MathContext(0, RoundingMode.DOWN);
            if (millisecondValue != null) {
                return millisecondValue.round(mc).intValue();
            } else {
                throw new NullPointerException("Error to obtain number of days between dates.");
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return null;
        }
    }

    /**
     * <p>
     *     Get number of days between two dates.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @return Days between dates.
     */
    public static Float getDaysBetweenDates(Date initialDate, Date endDate) {
        try {
            BigDecimal days = DataUtil.getNumberOfDaysBetweenDates(initialDate, endDate);
            if (days != null) {
                return days.floatValue();
            } else {
                throw new NullPointerException("Error to obtain number of days between dates.");
            }
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return null;
        }
    }

    /**
     * <p>
     *     Get seconds between two dates.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @return Seconds between dates
     */
    public static Float getSecondsBetweenDates(Date initialDate, Date endDate) {
        try {
            if (log.isDebugEnabled()) {
                log.debug(("Initial Date:" + BRAZIL_DATEFORMAT.format(initialDate)));
                log.debug(("End Date:" + BRAZIL_DATEFORMAT.format(endDate)));
            }
            float dif = (float) (endDate.getTime() - initialDate.getTime()) / TRANSFORMAR_SEGUNDOS;
            if (log.isDebugEnabled()) {
                log.debug(("Difference between dates = " + dif));
            }
            return dif;
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return null;
        }
    }

    /**
     * <p>
     *     End date is greater than start date.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @return Boolean
     */
    public boolean isEndDateHigher(Date initialDate, Date endDate) {
        try {
            var days = DataUtil.getDaysBetweenDates(initialDate, endDate);
            return (days != null ? days : 0.0f) > 0.0f;
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return false;
        }
    }

    /**
     * <p>
     *     End date is greater than or equals to start date.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @return Boolean
     */
    public boolean isEndDateHigherOrEquals(Date initialDate,
                                           Date endDate) {
        try {
            var days = DataUtil.getDaysBetweenDates(initialDate, endDate);
            return (days != null ? days : 0.0f) >= 0.0f;
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return false;
        }
    }

    /**
     * <p>
     *     End date is greater than start date considering seconds.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @return Boolean
     */
    public boolean isEndDateHigherInSeconds(Date initialDate,
                                            Date endDate) {
        try {
            var days = DataUtil.getSecondsBetweenDates(initialDate, endDate);
            return (days != null ? days : 0.0f) > 0.0f;
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return false;
        }
    }

    /**
     * <p>
     *     End date is greater than or equals to start date considering seconds.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @return Boolean
     */
    public boolean isEndDateHigherOrEqualsInSeconds(Date initialDate,
                                                    Date endDate) {
        try {
            var days = DataUtil.getSecondsBetweenDates(initialDate, endDate);
            return (days != null ? days : 0.0f) >= 0.0f;
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return false;
        }
    }

    /**
     * <p>
     *     Moves the date as the years change, into the future or past.
     * </p>
     *
     * @param referenceDate Reference date
     * @param numberMinimumYears Number of minimum years
     * @param inThePast In the Past
     * @return New date in future or past
     */
    public Date dateInYear(Date referenceDate, long numberMinimumYears,
                           boolean inThePast) {
        GregorianCalendar calendar = getGregorianCalendarPtBr();
        return dateInYear(referenceDate, numberMinimumYears, inThePast, calendar);
    }

    /**
     * <p>
     *     Moves the date as the years change, into the future or past.
     * </p>
     *
     * @param referenceDate Reference date
     * @param numberMinimumYears Number of minimum years
     * @param inThePast In the Past
     * @param zone Timezone object
     * @param locale Locale object
     * @return New date in future or past
     */
    public Date dateInYear(Date referenceDate, long numberMinimumYears,
                           boolean inThePast, TimeZone zone, Locale locale) {
        GregorianCalendar calendar = getGregorianCalendar(zone, locale);
        return dateInYear(referenceDate, numberMinimumYears, inThePast, calendar);
    }

    private Date dateInYear(Date referenceDate, long numberMinimumYears,
                            boolean inThePast, GregorianCalendar calendar) {
        if (referenceDate == null) {
            return null;
        }
        calendar.setTime(referenceDate);
        calendar.roll(Calendar.YEAR, (int) (inThePast ? numberMinimumYears * -1 : numberMinimumYears));
        return calendar.getTime();
    }

    /**
     * <p>
     *     Get the last day in month on Brazilian Calendar.
     * </p>
     *
     * @param date Reference date
     * @return Date object in the last day
     */
    public Date getLastDayInMonth(Date date) {
        return getLastDayInMonth(date, ZONE_PT_BR, LOCALE_PT_BR);
    }

    /**
     * <p>
     *     Get the last day in month on Brazilian Calendar.
     * </p>
     *
     * @param date Reference date
     * @param tz Timezone object
     * @param locale Locale
     * @return Date object in the last day
     */
    public Date getLastDayInMonth(Date date, TimeZone tz, Locale locale) {
        final int finalHour = 23;
        final int finalSecond = 59;
        if (date == null) {
            return null;
        }
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(date);
        Calendar cal = Calendar.getInstance(tz, locale);
        cal.set(c.get(Calendar.YEAR) - REEF_DECADA_NOVENTA, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        int diaFinal = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        GregorianCalendar dataFinal = new GregorianCalendar();
        dataFinal.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), diaFinal, finalHour, finalSecond, finalSecond);
        if (log.isDebugEnabled()) {
            log.debug(("The last day in month on date " + c.get(Calendar.MONTH)
                    + " is " + dataFinal.getTime()));
        }
        return dataFinal.getTime();
    }

    /**
     * <p>
     *     Get Date object without Hour, minute and second.
     * </p>
     *
     * @param year Year
     * @param month Month
     * @param day Day
     * @return Date object
     */
    public static Date getDate(int year, int month, int day) {
        return DataUtil.getDate(year, month, day, 0, 0, 0);
    }

    /**
     * <p>
     *     Get Date object without Hour, minute and second.
     * </p>
     *
     * @param year Year
     * @param month Month
     * @param day Day
     * @param zone TimeZone object
     * @param locale Locale object
     * @return Date object
     */
    public static Date getDate(int year, int month, int day, TimeZone zone,
            Locale locale) {
        GregorianCalendar calendar = getGregorianCalendar(zone, locale);
        return DataUtil.getDate(year, month, day, 0, 0, 0, calendar);
    }

    /**
     * <p>
     *     Get Date object.
     * </p>
     *
     * @param year Year
     * @param month Month
     * @param day Day
     * @param hour Hour
     * @param minute Minute
     * @param seconds Seconds
     * @return Date object
     */
    public static Date getDate(int year, int month, int day, int hour, int minute,
            int seconds) {
        GregorianCalendar calendar = GregorianCalendar
                .from(ZonedDateTime.now());
        return getDate(year, month, day, hour, minute, seconds, calendar);
    }

    /**
     * <p>
     *     Get Date object.
     * </p>
     *
     * @param year Year
     * @param month Month
     * @param day Day
     * @param hour Hour
     * @param minute Minute
     * @param seconds Seconds
     * @param calendar GregorianCalendar object
     * @return Date object
     */
    private static Date getDate(int year, int month, int day, int hour, int minute,
            int seconds, GregorianCalendar calendar) {
        final int monthCheck = 1;
        if (!DataUtil.isValidDate(year, month, day, hour, minute, seconds, calendar)) {
            return null;
        }
        calendar.set(year, month - monthCheck, day);
        calendar.set(GregorianCalendar.SECOND, seconds);
        calendar.set(GregorianCalendar.MINUTE, minute);
        calendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
        calendar.set(GregorianCalendar.MILLISECOND, 0);
        return convertToDate(calendar);
    }

    /**
     * <p>
     *     Get Date with a no localized pattern.
     * </p>
     *
     * @param date Date
     * @param patternTo String Pattern
     * @return Date object
     */
    public static Date getDate(String date, String patternTo) {
        if (date == null || patternTo == null || date.trim().equals("")) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(patternTo);
        sdf.setLenient(false);
        Date novaData = null;
        try {
            novaData = sdf.parse(date);
        } catch (ParseException ex) {
            log.error(ex.toString(), ex);
        }
        return novaData;
    }

    /**
     * <p>
     *     Get Date with a pt-BR localized pattern.
     * </p>
     *
     * @param date Date.
     * @param patternTo String pattern
     * @return String date
     */
    public static String getDate(Date date, String patternTo) {
        return getDate(date, patternTo, new Locale(BRAZIL_LANGUAGE, BRAZIL_LOCALE));
    }

    /**
     * <p>
     *     Get Date with a no localized string pattern.
     * </p>
     *
     * @param date Date
     * @param patternTo String pattern
     * @param locale Locale object
     * @return String date
     */
    public static String getDate(Date date, String patternTo, Locale locale) {
        if (date == null || patternTo == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(patternTo, locale);
        sdf.setLenient(false);
        sdf.applyPattern(patternTo);
        return sdf.format(date);
    }

    /**
     * <p>
     *     Get Date with a localized pt-BR pattern.
     * </p>
     *
     * @param date Date
     * @param intPattern Pattern
     * @return Date object
     * @throws Exception to obtain date object
     */
    public static Date getDate(String date, int intPattern) throws Exception {
        try {
            return getDate(date, intPattern, new Locale(BRAZIL_LANGUAGE, BRAZIL_LOCALE));
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
            return null;
        }
    }

    /**
     * <p>
     *     Get Date with a no localized pattern.
     * </p>
     *
     * @param date Date
     * @param intPattern Pattern
     * @param locale Locale
     * @return Date object
     * @throws Exception to obtain date object
     */
    public static Date getDate(String date, int intPattern, Locale locale)
            throws Exception {
        try {
            DateFormat df = DateFormat.getDateInstance(intPattern, locale);
            df.setLenient(false);
            return df.parse(date);
        } catch (ParseException ex) {
            log.error(ex.toString(), ex);
            return null;
        }
    }

    /**
     * <p>
     *     Alter pattern of Date object to another pattern.
     *     </br> ex: 09/01/1989 to 1989-01-09
     * </p>
     *
     * @param date Date
     * @param patternOf Actual pattern
     * @param patternTo Future pattern
     * @return Date in the new pattern
     */
    public static String toAnotherPattern(String date, String patternOf, String patternTo) {
        log.debug("###################### Begin the method transformPattern");
        try {
            if (date.trim().length() == 0) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(patternOf);
            sdf.setLenient(false);
            Date df = sdf.parse(date);
            log.debug("The pattern (OF) = " + patternOf + " formate the Date"
                    + " to pattern = " + df);
            sdf.applyPattern(patternTo);
            final String result = sdf.format(df);
            log.debug("The pattern (TO) = " + patternTo + " formate the Date"
                    + " to pattern = " + result);
            return result;
        } catch (ParseException e) {
            log.error(e.toString(), (Throwable) e);
            return "";
        }
    }

    /**
     * <p>
     *     Get month name of Date.
     * </p>.
     *
     * @param data Date
     * @param locale Locale
     * @return Month name
     */
    public String getMonthName(Date data, Locale locale) {
        if (data == null) {
            return null;
        }
        SimpleDateFormat f = new SimpleDateFormat(NAME_MONTH, locale);
        return f.format(data);
    }

    /**
     * <p>
     *     Get month name of Date in Brazilian Locale.
     * </p>.
     *
     * @param data Date
     * @return Month name
     */
    public String getMonthName(Date data) {
        return getMonthName(data, new Locale(BRAZIL_LANGUAGE, BRAZIL_LOCALE));
    }

    /**
     * <p>
     *     Obtain a list with dates between two dates.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @return List of Date between two dates
     */
    public List<Date> getDatesInInterval(Date initialDate, Date endDate) {
        Calendar calendar = getCalendarPtBr();
        return getDatesInInterval(initialDate, endDate, calendar);
    }

    /**
     * <p>
     *     Obtain a list with dates between two dates.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @param zone TimeZone
     * @param locale Locale
     * @return List of Date between two dates
     */
    public List<Date> getDatesInInterval(Date initialDate, Date endDate, TimeZone zone, Locale locale) {
        Calendar calendar = getCalendar(zone, locale);
        return getDatesInInterval(initialDate, endDate, calendar);
    }

    /**
     * <p>
     *     Obtain a list with dates between two dates.
     * </p>
     *
     * @param initialDate Initial Date
     * @param endDate End Date
     * @param calendar Calendar object
     * @return List of Date between two dates
     */
    private List<Date> getDatesInInterval(Date initialDate, Date endDate, Calendar calendar) {
        if (initialDate == null || endDate == null) {
            return new ArrayList<>();
        }
        HashMap<Date, Date> lista = new HashMap<>();
        Float days = DataUtil.getDaysBetweenDates(initialDate, endDate);
        calendar.setTime(initialDate);
        lista.put(calendar.getTime(), calendar.getTime());
        int i = 1;
        if(days == null)
            return new ArrayList<>();
        while (days.intValue() > i - 1) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            lista.put(calendar.getTime(), calendar.getTime());
            ++i;
        }
        ArrayList<Date> listOfDates = new ArrayList<>(lista.values());
        Collections.sort(listOfDates);
        return listOfDates;
    }

    /**
     * <p>
     *     Validate the date.
     * </p>
     *
     * @param year year
     * @param month month
     * @param day day
     * @param hour hour
     * @param minute minute
     * @param second second
     * @param calendar GregorianCalendar object
     * @return True if date is valid
     */
    private static boolean isValidDate(int year, int month, int day, int hour, int minute, int second,
                                       GregorianCalendar calendar) {
        calendar.set(year, --month, day, hour, minute, second);
        int testYear = calendar.get(Calendar.YEAR);
        int testMonth = calendar.get(Calendar.MONTH);
        int testDay = calendar.get(Calendar.DAY_OF_MONTH);
        int testHour = calendar.get(Calendar.HOUR_OF_DAY);
        int testMinute = calendar.get(Calendar.MINUTE);
        int testSecond = calendar.get(Calendar.SECOND);
        return month == testMonth && day == testDay && year == testYear
                && hour == testHour && minute == testMinute && second == testSecond;
    }

    /**
     * <p>
     *     Get Year integer reference of Date.
     * </p>
     *
     * @param date Date
     * @return Year integer reference.
     */
    public static int getYear(Date date) {
        return getYear(date, ZONE_PT_BR, LOCALE_PT_BR);
    }

    /**
     * <p>
     *     Get Year integer reference of Date.
     * </p>
     *
     * @param date Date
     * @param zone TimeZone
     * @param locale Locale
     * @return Year integer reference.
     */
    public static int getYear(Date date, TimeZone zone, Locale locale) {
        Calendar cal = getCalendar(zone, locale);
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * <p>
     *     Get Hours between two Dates.
     * </p>
     *
     * @param beginDate Initial Date
     * @param endDate End Date
     * @return Hours between two dates
     */
    public static BigDecimal getHoursBetweenDates(Date beginDate, Date endDate) {
        BigDecimal dia = BigDecimal.valueOf(3600000);
        BigDecimal di = BigDecimal.valueOf(beginDate.getTime());
        BigDecimal df = BigDecimal.valueOf(endDate.getTime());
        return df.subtract(di).divide(dia, 5, RoundingMode.UP);
    }

    /**
     * <p>
     *     Subtract days, hours, minutes, seconds from date.
     * </p>
     *
     * @param data Date
     * @param dias Days
     * @param hora Hours
     * @param minute Minute
     * @param segundo Seconds
     * @return New date.
     */
    public static Date subtractDate(Date data, int dias, int hora, int minute, int segundo) {
        return subtractDate(data, dias, hora, minute, segundo, ZONE_PT_BR, LOCALE_PT_BR);
    }

    /**
     * <p>
     *     Subtract days, hours, minutes, seconds from date.
     * </p>
     *
     * @param date Date
     * @param days Days
     * @param hour Hours
     * @param minute Minute
     * @param second Seconds
     * @param zone TimeZone
     * @param locale Locale
     * @return New date.
     */
    public static Date subtractDate(Date date, int days, int hour, int minute, int second, TimeZone zone,
                                    Locale locale) {
        Calendar calendar = getCalendar(zone, locale);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        return calendar.getTime();
    }

    /**
     * <p>
     *     Sum days, hours, minutes, seconds from date.
     * </p>
     *
     * @param date Date
     * @param days Days
     * @param hour Hours
     * @param minute Minute
     * @param second Seconds
     * return New date.
     */
    public static Date sumDate(Date date, int days, int hour, int minute, int second) {
        return sumDate(date, days, hour, minute, second, ZONE_PT_BR, LOCALE_PT_BR);
    }

    /**
     * Soma a data os valores de parametros.
     *
     * @param date Date
     * @param days Days
     * @param hour Hours
     * @param minute Minutes
     * @param second Seconds
     * @param zone TimeZone
     * @param locale Locale
     * @return New Date.
     */
    public static Date sumDate(Date date, int days, int hour, int minute, int second, TimeZone zone, Locale locale) {
        Calendar cal = getCalendar(zone, locale);
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, days);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
        return cal.getTime();
    }

    /**
     * <p>
     *     Add minutes in Date.
     * </p>
     *
     * @param date Date
     * @param qtdMinutes Minutes to add
     * @return date.
     */
    public static Date addMinutes(Date date, int qtdMinutes) {
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MINUTE, qtdMinutes);
            date = cal.getTime();
        }
        return date;
    }

    /**
     * <p>
     *     Set Hour in date.
     * </p>
     *
     * @param date Date
     * @param hour Hour
     * @return Date
     */
    public static Date setHour(Date date, int hour) {
        if (date != null && hour >= 0 && hour < MAX_HORA_DIA) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, hour);
            date = cal.getTime();
        }
        return date;
    }

    /**
     * <p>
     *     Set Minutes in date.
     * </p>
     *
     * @param date Date
     * @param minutes Minutes to set
     * @return Date
     */
    public static Date setMinutes(Date date, int minutes) {
        if (date != null && minutes >= 0 && minutes < MAX_MINUTO_HORA) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.MINUTE, minutes);
            date = cal.getTime();
        }
        return date;
    }

    /**
     * <p>
     *     Set Seconds in date.
     * </p>
     *
     * @param date Date
     * @param seconds Seconds to set
     * @return Date
     */
    public static Date setSeconds(Date date, int seconds) {
        if (date != null && seconds >= 0 && seconds < MAX_SEGUNDO_MINUTO) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.SECOND, seconds);
            date = cal.getTime();
        }
        return date;
    }

    /**
     * <p>
     *     Get Date with a Hour, Minute and Second
     * </p>
     *
     * @param date Date
     * @param hour Hour
     * @param min Minutes
     * @param sec Seconds
     * @return Date.
     */
    public static Date setHourMinuteSeconds(Date date, int hour, int min, int sec) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, min);
        cal.set(Calendar.SECOND, sec);
        cal.set(Calendar.MILLISECOND, 0);
        date = cal.getTime();
        return date;
    }

    /**
     * <p>
     *     Return a String representing the Date Now formatted.
     * </p>
     * <br>
     * <p>
     *     In default format: yyyy-MM-dd'T'HH:mm:ss
     * </p>
     *
     * @return String Date.
     */
    public static String sysDate() {
        return toFormat(DataUtil.now(), EUA_DATE_PATTERN);
    }

    /**
     * <p>
     *     Return a String representing the Date Now formatted.
     * </p>
     *
     * @param mask Format String.
     * @return String Date.
     */
    public static String sysDate(String mask) {
        return toFormat(DataUtil.now(), mask);
    }

    /**
     * Retorna uma {@link LocalDateTime} representando a Data Atual.
     *
     * @return String Date.
     */
    public static LocalDateTime now() {
        LocalDateTime today = LocalDateTime.now();
        return today;
    }

    /**
     * <p>
     *     Return a {@link LocalDateTime} representing the Date Now formatted.
     * </p>
     * <br>
     * <p>
     *     In default format: yyyy-MM-dd'T'HH:mm:ss
     * </p>
     *
     * @param date LocalDateTime object.
     * @param format String format date.
     * @return String Date.
     */
    public static String toFormat(LocalDateTime date, String format) {
        return date.format(getFormater(format));
    }

    /**
     * <p>
     *     Get a DateTimeFormatter.
     * </p>
     *
     * @param format Formatter String.
     * @return DateTimeFormatter object.
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
     * <p>
     *     Get a Date Formatter.
     * </p>
     *
     * @return DateTimeFormatter object.
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
     * <p>
     *     Convert String Date object in {@link LocalDate}.
     * </p>
     * <p>
     *     In default format: yyyy-MM-dd'T'HH:mm:ss
     * </p>
     *
     * @param dateTime String object.
     * @return LocalDate object.
     */
    public static LocalDate convertToLocalDate(String dateTime) {
        DateTimeFormatter formatter = getFormater();
        return LocalDate.parse(dateTime, formatter);
    }

    /**
     * <p>
     *     Convert String Date object in {@link LocalDateTime}.
     * </p>
     * <p>
     *     In default format: yyyy-MM-dd'T'HH:mm:ss
     * </p>
     *
     * @param dateTime String object.
     * @param format String format.
     * @return LocalDateTime object.
     */
    public static LocalDateTime convertToLocalDateTime(String dateTime,
            String format) {
        DateTimeFormatter formatter = getFormater(format);
        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * <p>
     *     Convert String Date object in {@link LocalDateTime}.
     * </p>
     * <p>
     *     In default format: yyyy-MM-dd'T'HH:mm:ss
     * </p>
     *
     * @param dateTime String object.
     * @return LocalDateTime object.
     */
    public static LocalDateTime convertToLocalDateTime(String dateTime) {
        DateTimeFormatter formatter = getFormater();
        return LocalDateTime.parse(dateTime, formatter);
    }

    /**
     * <p>
     *     Convert Date object in {@link LocalDateTime}.
     * </p>
     * <p>
     *     In default format: yyyy-MM-dd'T'HH:mm:ss
     * </p>
     *
     * @param date Date object.
     * @return LocalDateTime object.
     */
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return convertToLocalDateTime(date, ZoneId.systemDefault());
    }

    /**
     * <p>
     *     Convert in Date object a {@link LocalDateTime} object with a {@link ZoneId}.
     * </p>
     *
     * @param date Date object,
     * @param zone Zone object.
     * @return LocalDateTime object.
     */
    public static LocalDateTime convertToLocalDateTime(Date date, ZoneId zone) {
        return date.toInstant().atZone(zone).toLocalDateTime();
    }

    /**
     * <p>
     *     Convert {@link LocalDateTime} in Date object with a {@link ZoneId}.
     * </p>
     *
     * @param localDateTime LocalDateTime object,
     * @return Date object.
     */
    public static Date convertToDate(LocalDateTime localDateTime) {
        return convertToDate(localDateTime, ZoneId.systemDefault());
    }

    /**
     * <p>
     *     Convert {@link LocalDateTime} in Date object with a {@link ZoneId}.
     * </p>
     *
     * @param localDateTime LocalDateTime object,
     * @param zone Zone object.
     * @return Date object.
     */
    public static Date convertToDate(LocalDateTime localDateTime, ZoneId zone) {
        return Date.from(localDateTime.atZone(zone).toInstant());
    }

    /**
     * <p>
     *     Convert Calndar object on Date object.
     * </p>
     *
     * @param dtCalendar Calendar object.
     * @return Date object.
     */
    public static Date convertToDate(Calendar dtCalendar) {
        return new Date(dtCalendar.getTimeInMillis());
    }

    /**
     * <p>
     *     Convert Date String on Date Object.
     * </p>
     *
     * @param dtString Date String.
     * @return Date Object.
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
     * <p>
     *     Convert Date String to Date Object with a pattern.
     * </p>
     *
     * @param dtString Date String.
     * @param pattern Pattern String.
     * @return Date object.
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
     * <p>
     *     Convert Date in Calendar.
     * </p>
     *
     * @param date Date.
     * @return Calendar.
     */
    public static Calendar convertToCalendar(Date date) {
        Calendar dtCalendar = Calendar.getInstance();
        dtCalendar.setTime(date);
        return dtCalendar;
    }

    /**
     * <p>
     *     Convert String date in Calendar.
     * </p>
     *
     * @param dtString DateTime string.
     * @param pattern Pattern string.
     * @return Calendar.
     */
    public static Calendar convertToCalendar(String dtString, String pattern)
            throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return convertToCalendar(df.parse(dtString));
    }

    /**
     * <p>
     *     Convert String date in Calendar.
     * </p>
     *
     * @param xmlDateTimeString DateTime string.
     * @return Calendar.
     */
    public static Calendar convertToCalendar(String xmlDateTimeString)
            throws ParseException {
        Date date = XML_DATETIME_FORMAT.parse(xmlDateTimeString);
        return convertToCalendar(date);
    }

    /**
     * <p>
     *     Convert LocalTime in Calendar.
     * </p>
     *
     * @param localTime LocalTime.
     * @return Calendar.
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
     * <p>
     *     Convert LocalDate in Calendar.
     * </p>
     *
     * @param localDate LocalDate.
     * @return Calendar.
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
     * <p>
     *     Convert LocalDateTime in Calendar.
     * </p>
     *
     * @param localDateTime LocalDateTime.
     * @return Calendar.
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
     * <p>
     *     Convert Date in XMLGregorianCalendar.
     * </p>
     *
     * @param date Date.
     * @return XMLGregorianCalendar object.
     */
    public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {
        log.debug("###################### int the "
                + " convertToXMLGregorianCalendar method.");
        if (date == null) {
            log.debug("Date is NULL");
            return null;
        } else {
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            try {
                return DatatypeFactory.newInstance()
                        .newXMLGregorianCalendar(gregorianCalendar);
            } catch (DatatypeConfigurationException e) {
                log.error(e.toString(), (Throwable) e);
                throw new RuntimeException("Error to convert date in "
                        + " XMLGregorianCalendar", e);
            }
        }
    }

    /**
     * <p>
     *     Convert Calendar to String with Pattern.
     * </p>
     *
     * @param dtCalendar Date to Format.
     * @param pattern Pattern.
     * @return String date.
     */
    public static String convertToString(Calendar dtCalendar, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(convertToDate(dtCalendar));
    }

    /**
     * <p>
     *     Convert Date to String with Pattern.
     * </p>
     *
     * @param date Date to Format.
     * @param pattern Pattern.
     * @return String date.
     */
    public static String convertToString(Date date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * <p>
     *     Format the Date with Universal Pattern in REGEX.
     * </p>
     *
     * @param date Date to format.
     * @return String date.
     */
    public static String convertToXmlDateTimeString(Date date) {
        return XML_DATETIME_FORMAT.format(date);
    }

    /**
     * <p>
     *     Convert Date in String.
     * </p>
     * <br>
     * <p>
     *     If String Pattern param is not a valid format then the default format is: yyyy-MM-dd'T'HH:mm:ss
     * </p>
     *
     * @param date Date to Convert.
     * @param pattern String Pattern.
     * @return Formatted String date.
     */
    public static String convertToString(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = getFormater(pattern);
        return date.format(formatter);
    }

    /**
     * <p>
     *     Obtain Age from Birthdate.
     * </p>
     *
     * @param birthdate BirthDate
     * @return Return the Age.
     */
    public static int obtainAge(Date birthdate) {
        Calendar dtNascimentoAsCalendar = convertToCalendar(birthdate);
        Calendar hoje = Calendar.getInstance();
        int idade = hoje.get(Calendar.YEAR) - dtNascimentoAsCalendar
                .get(Calendar.YEAR);
        dtNascimentoAsCalendar.add(Calendar.YEAR, idade);
        if (hoje.before(dtNascimentoAsCalendar)) {
            idade--;
        }
        log.debug("Birthdate in: "
                + convertToString(birthdate, BRAZIL_DATE_PATTERN)
                //+ convertToString(birthdate, EUA_DATE_PATTERN)
                + " The Age today is: "
                + idade
                + " year(s) old");
        return idade;
    }

    /**
     * <p>
     *     Alter the Hour in Date Object to initial hour.
     * </p>
     * <br>
     * <p>
     *     Example: 09/01/1989 05:30:10:500 para 09/01/1989 00:00:00:000
     * </p>
     *
     * @param date Date object
     * @return Date
     */
    public static Date floorHours(Date date) {
        Calendar dateAsCalendar = convertToCalendar(date);
        return floorHours(dateAsCalendar);
    }

    /**
     * <p>
     *     Alter the Hour in LocalDateTime Object to initial hour.
     * </p>
     * <br>
     * <p>
     *     Example: 09/01/1989 05:30:10:500 para 09/01/1989 00:00:00:000
     * </p>
     *
     * @param date LocalDateTime object
     * @return Date
     */
    public static Date floorHours(LocalDateTime date) {
        Calendar dateAsCalendar = convertToCalendar(date);
        return floorHours(dateAsCalendar);
    }

    /**
     * <p>
     *     Alter the Hour in Calendar Object to initial hour.
     * </p>
     * <br>
     * <p>
     *     Example: 09/01/1989 05:30:10:500 para 09/01/1989 00:00:00:000
     * </p>
     *
     * @param date Calendar object
     * @return Date
     */
    public static Date floorHours(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return convertToDate(date);
    }

    /**
     * <p>
     *     Changes the time of the Calendar Object to the end time.
     * </p>
     * <br>
     * <p>
     *     Example: 09/01/1989 05:30:10:500 to 09/01/1989 23:59:59:999
     * </p>
     *
     * @param date Date
     * @return Data
     */
    public static Date ceilHour(Date date) {
        Calendar dateAsCalendar = convertToCalendar(date);
        return ceilHour(dateAsCalendar);
    }

    /**
     * <p>
     *     Changes the time of the Calendar Object to the end time.
     * </p>
     * <br>
     * <p>
     *     Example: 09/01/1989 05:30:10:500 to 09/01/1989 23:59:59:999
     * </p>
     *
     * @param date Date
     * @return Data
     */
    public static Date ceilHour(LocalDateTime date) {
        Calendar dateAsCalendar = convertToCalendar(date);
        return ceilHour(dateAsCalendar);
    }

    /**
     * <p>
     *     Changes the time of the Calendar Object to the end time.
     * </p>
     * <br>
     * <p>
     *     Example: 09/01/1989 05:30:10:500 to 09/01/1989 23:59:59:999
     * </p>
     *
     * @param date Date
     * @return Data
     */
    public static Date ceilHour(Calendar date) {
        final int finalHour = 23;
        final int finalMinute = 59;
        final int finalMillisecond = 999;
        date.set(Calendar.HOUR_OF_DAY, finalHour);
        date.set(Calendar.MINUTE, finalMinute);
        date.set(Calendar.SECOND, finalMinute);
        date.set(Calendar.MILLISECOND, finalMillisecond);
        return convertToDate(date);
    }

    /**
     * <p>
     *     Validate date with pattern.
     * </p>
     *
     * @param strDate Date to validate
     * @param pattern Pattern in validation
     * @return Boolean
     */
    public static boolean isValidDateString(String strDate, String pattern) {
        log.debug("###################### Begin method isValidDateString");
        try {
            final String regexPattern = "(0[1-9]|[12][0-9]|3[01])[/](0[1-9]|1[012])[/](19|20)\\d\\d";
            final String defaultPattern = "dd/MM/yyyy";
            final String dateTransformed = toAnotherPattern(strDate, pattern, defaultPattern);
            Pattern p = Pattern.compile(regexPattern);
            Matcher m = p.matcher(dateTransformed);
            if (pattern.equals(defaultPattern) && !m.matches()) {
                log.debug("The date: "
                        + dateTransformed
                        + "is not valid for pattern "
                        + pattern);
                return false;
            } else {
                //SimpleDateFormat df = new SimpleDateFormat(pattern);
                //df.parse(dateTransformed);

                final int dia = Integer.parseInt(dateTransformed.substring(0, 2));
                final int mes = Integer.parseInt(dateTransformed.substring(3, 5));
                final int ano = Integer.parseInt(dateTransformed.substring(6));

                log.debug("The integer representing day is: " + String.valueOf(dia));
                log.debug("The integer representing month is: " + String.valueOf(mes));
                log.debug("The integer representing year is: " + String.valueOf(ano));

                switch (mes - 1) {
                    case Calendar.JANUARY:
                    case Calendar.MARCH:
                    case Calendar.MAY:
                    case Calendar.JULY:
                    case Calendar.AUGUST:
                    case Calendar.OCTOBER:
                    case Calendar.DECEMBER:
                        if (dia <= MAX_DIA_MES_TRINTA_UM) {
                            log.debug("The date in month with 31 days");
                            log.debug("The date is valid");
                            return true;
                        }
                        break;
                    case Calendar.APRIL:
                    case Calendar.JUNE:
                    case Calendar.SEPTEMBER:
                    case Calendar.NOVEMBER:
                        if (dia <= MAX_DIA_MES_TRINTA) {
                            log.debug("The date in month with 30 days");
                            log.debug("The date is valid");
                            return true;
                        }
                        break;
                    case Calendar.FEBRUARY:
                        final int[] modsBissexto = {4, 100, 400};
                        boolean bissexto = false;
                        if ((ano % modsBissexto[0] == 0)
                                || (ano % modsBissexto[1] == 0)
                                || (ano % modsBissexto[2] == 0)) {
                            log.debug("the year is leap year");
                            log.debug("The date is valid");
                            bissexto = true;
                        }
                        if ((bissexto) && (dia <= MAX_DIA_MES_VINTE_NOVE)) {
                            log.debug("The month February in leap year");
                            log.debug("The date is valid");
                            return true;
                        }
                        if ((!bissexto) && (dia <= MAX_DIA_MES_VINTE_OITO)) {
                            log.debug("The month February out of leap year");
                            log.debug("The date is valid");
                            return true;
                        }
                        break;
                    default:
                        if (dia <= MAX_DIA_MES_TRINTA_UM) {
                            log.debug("The date in month with 31 days");
                            log.debug("The date is valid");
                            return true;
                        }
                        break;
                }
                return false;
            }
            //} catch (NumberFormatException | ParseException ex) {
        } catch (NumberFormatException ex) {
            log.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * <p>
     *     Date Range Validator, checking if the End Date is less than the Start Date, taking into account a
     *     formatting pattern.
     * </p>
     *
     * @param initialDate Initial date String
     * @param finalDate Final date String
     * @return Boolean
     */
    public static boolean isValidDateInterval(Date initialDate, Date finalDate) {
        log.debug("###################### Begin method isValidDateInterval");
        try {
            if (finalDate.getTime() >= initialDate.getTime()) {
                log.debug("End date is greater than or equal to start date");
                return true;
            } else {
                log.debug("End date is less than start date");
                return false;
            }
        } catch (Exception ex) {
            log.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * <p>
     *     Date Range Validator, checking if the End Date is less than the Start Date, taking into account a
     *     formatting pattern.
     * </p>
     *
     * @param initialDate Initial date String
     * @param finalDate Final date String
     * @param pattern pattern of dates
     * @return Boolean
     */
    public static boolean isValidDateInterval(String initialDate, String finalDate, String pattern) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date beginDate = dateFormat.parse(initialDate);
            Date endDate = dateFormat.parse(finalDate);
            return isValidDateInterval(beginDate, endDate);
        } catch (ParseException ex) {
            log.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * <p>
     *     Date Range Validator, checking if the End Date is less than the Start Date, taking into account a
     *     formatting pattern.
     * </p>
     *
     * @param initialDate Initial date String
     * @param finalDate Final date String
     * @param pattern pattern of dates
     * @param zone TimeZone
     * @return Boolean
     */
    public static boolean isValidDateInterval(String initialDate, String finalDate, String pattern, TimeZone zone) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            dateFormat.setTimeZone(zone);
            Date beginDate = dateFormat.parse(initialDate);
            Date endDate = dateFormat.parse(finalDate);
            return isValidDateInterval(beginDate, endDate);
        } catch (ParseException ex) {
            log.error(ex.toString(), (Throwable) ex);
            return false;
        }
    }

    /**
     * <p>
     *     Convert date to timestamp.
     * </p>
     *
     * @param date Date
     * @return Timestamp
     */
    public static Timestamp convertToTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }
}

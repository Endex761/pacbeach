package com.pac.pacbeach.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Classe per la formattazione delle date e degli orari
 */
public class DateFormatter
{
    private static final String pattern = "yyyy-MM-dd HH:mm:ss";

    public static Timestamp formatDate(String dateString) throws ParseException
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

        String replaced = dateString.replace('T', ' ');

        long dateLong = dateFormat.parse(replaced).getTime();

        return new Timestamp(dateLong);
    }
}

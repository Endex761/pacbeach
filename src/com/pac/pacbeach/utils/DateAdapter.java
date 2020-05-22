package com.pac.pacbeach.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.util.Date;
import java.text.SimpleDateFormat;

//Classe di utilità per la conversione delle date per XML
public class DateAdapter extends XmlAdapter<String, Date>
{
    private final String pattern = "yyyy-MM-dd HH:mm:ss";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

    @Override
    public String marshal(Date v) throws Exception {
        synchronized (dateFormat)
        {
            return dateFormat.format(v);
        }
    }

    @Override
    public Date unmarshal(String v) throws Exception {
        synchronized (dateFormat)
        {
            return dateFormat.parse(v);
        }
    }
}

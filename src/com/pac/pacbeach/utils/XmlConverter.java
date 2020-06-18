package com.pac.pacbeach.utils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

/**
 * Classe per la conversione degli ogetti in Stringa XML
 */
public class XmlConverter
{
    public static String jaxbObjectToXML(Object obj)
    {
        try
        {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Print XML String to Console
            StringWriter sw = new StringWriter();

            //Write XML to StringWriter
            jaxbMarshaller.marshal(obj, sw);

            //Verify XML Content
            return sw.toString();

        } catch (JAXBException e)
        {
            e.printStackTrace();
            return "Errore conversione XML";
        }
    }
}

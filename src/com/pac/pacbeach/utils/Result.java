package com.pac.pacbeach.utils;

import com.pac.pacbeach.model.*;
import com.pac.pacbeach.model.wrapper.WrapperArrayList;

import javax.xml.bind.annotation.*;

/**
 * Classe per la gestione della comunicazione con il front-end
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlSeeAlso({Utente.class, Prenotazione.class, Ombrellone.class, WrapperArrayList.class, Prodotto.class, Ordine.class, ProdottoOrdine.class})
public class Result
{
    private static final long serialVersionUID = 1L;

    /**
     * success: Indica se la richiesta è stata evasa con successo
     * message: Contiene un messaggio per il front-end
     * content: Contiene eventuali dati che il server invia al front-end
     */
    private Boolean success;
    private String message;
    private Object content;

    public Result()
    {
        success = null;
        message = null;
        content = null;
    }

    public Result(String message)
    {
        this(message, true);
    }

    public Result(String message, Boolean success)
    {
        this(message, success, null);
    }

    public Result(Object content, String message)
    {
        this(message, true, content);
    }
    public Result(Object content)
    {
        this(content, null);
    }

    public Result(String message, Boolean success, Object content)
    {
        this.success = success;
        this.message = message;
        this.content = content;
    }

    public Boolean success()
    {
        return success;
    }

    public String getMessage()
    {
        return message;
    }

    public Object getContent()
    {
        return content;
    }

    public void setContent(Object content)
    {
        this.content = content;
    }

    public String toXmlString()
    {
        return XmlConverter.jaxbObjectToXML(this);
    }


}

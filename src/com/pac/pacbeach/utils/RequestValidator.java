package com.pac.pacbeach.utils;

import com.pac.pacbeach.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Classe per la validazione dei parametri nell'header di richiesta.
 */
public class RequestValidator
{
    Map<String, String[]> parameters;
    HttpServletRequest request;

    public RequestValidator(HttpServletRequest request)
    {
        this.request = request;
        parameters = request.getParameterMap();
    }

    /**
     * Metodo che restituisce un parametro della richiesta http
     * @param fieldName nome del parametro
     * @param required true se è un campo richiesto
     * @return valore del parametro richiesto
     * @throws ValidationException se il parametro è richiesto ma non è presente
     */
    public String getParameter(String fieldName, Boolean required) throws ValidationException
    {
        if(parameters.containsKey(fieldName))
            return request.getParameter(fieldName);
        else if(required)
            throw new ValidationException(fieldName + ": campo obbligatorio");
        else
            return null;
    }

    /**
     * Metodo che restituisce un parametro della richiesta http
     * @param fieldName nome del parametro richiesto
     * @return valore del parametro richiesto
     * @throws ValidationException se il parametro non è presente
     */
    public String getParameter(String fieldName) throws ValidationException
    {
        return getParameter(fieldName, true);
    }

    /**
     * Metodo che restituisce un parametro della richiesta http
     * @param fieldName nome del parametro
     * @param required true se è un campo richiesto
     * @param minLength lunghezza minima del valore
     * @param maxLength lunghezza massima del valore
     * @return valore del parametro
     * @throws ValidationException se il parametro è richiesto ma non è presente
     */
    public String getParameter(String fieldName, Boolean required, int minLength, int maxLength) throws ValidationException
    {
        String parameter = getParameter(fieldName, required);

        if(parameter.length() < minLength)
            throw new ValidationException(fieldName + ": lunghezza minima del campo " + minLength);

        if(parameter.length() > maxLength)
            throw new ValidationException(fieldName + ": lunghezza massima del campo " + maxLength);

        return parameter;
    }

    /**
     * Metodo che restituisce un parametro della richiesta http
     * @param fieldName nome del parametro richiesto
     * @param maxLength lunghezza massima del valore
     * @return valore del parametro
     * @throws ValidationException se il parametro non è presente
     */
    public String getParameter(String fieldName, int maxLength) throws ValidationException
    {
        return getParameter(fieldName, true, 0, maxLength);
    }
}

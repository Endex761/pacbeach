package com.pac.pacbeach.utils;

import com.pac.pacbeach.exceptions.ValidationException;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class RequestValidator
{
    Map parameters;
    HttpServletRequest request;

    public RequestValidator(HttpServletRequest request)
    {
        this.request = request;
        parameters = request.getParameterMap();
    }

    public String getParameter(String fieldName, Boolean required) throws ValidationException
    {
        if(parameters.containsKey(fieldName))
            return request.getParameter(fieldName);
        else if(required)
            throw new ValidationException(fieldName + ": campo obbligatorio");
        else
            return null;
    }

    public String getParameter(String fieldName) throws ValidationException
    {
        return getParameter(fieldName, true);
    }
}

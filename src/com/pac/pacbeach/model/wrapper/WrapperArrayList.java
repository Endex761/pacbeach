package com.pac.pacbeach.model.wrapper;

import java.util.List;

/**
 * Classe Wrapper per Liste di elementi T.
 * Viene utilizzata per semplificare la traduzione di liste in xml
 * @param <T>
 */
public class WrapperArrayList<T>
{
    public List<T> element;

    public WrapperArrayList(List<T> list)
    {
        this.element = list;
    }

    public WrapperArrayList()
    {

    }

}

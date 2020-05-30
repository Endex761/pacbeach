package com.pac.pacbeach.model.wrapper;

import java.util.List;

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

package com.pac.pacbeach.dao;

import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.model.ProdottoOrdine;

public class ProdottoOrdineDao extends Dao
{
    public static ProdottoOrdine creaProdottoOrdine(ProdottoOrdine po) throws DuplicatedEntryException
    {
        return (ProdottoOrdine) create(po);
    }
}

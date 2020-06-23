package com.pac.pacbeach.dao;

import com.pac.pacbeach.model.Prodotto;

import java.util.List;

/**
 * Classe per l'accesso ai dati dei prodotti
 */
public class ProdottoDao extends Dao
{
    public static List<Prodotto> listaProdotti()
    {
        String queryString = "from Prodotto";

        return (List<Prodotto>) get(queryString);
    }

    public static List<Prodotto> listaProdottiDisponibili()
    {
        String queryString = "from Prodotto where disponibile = 1";

        return (List<Prodotto>) get(queryString);
    }

    public static Prodotto prodottoPerId(Integer id)
    {
        String queryString = "from Prodotto where idProdotto = ?0";

        return (Prodotto) getOne(queryString, id);
    }
}

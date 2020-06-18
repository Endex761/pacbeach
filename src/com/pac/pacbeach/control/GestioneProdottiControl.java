package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.ProdottoDao;
import com.pac.pacbeach.model.Prodotto;
import com.pac.pacbeach.model.wrapper.WrapperArrayList;
import com.pac.pacbeach.utils.Result;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * Classe controller per la gestione dei prodotti del bar
 */
public class GestioneProdottiControl
{
    /**
     * Metodo che restituisce una lista di prodotti
     * @param mostraTerminati se true mostra anche i prodotti terminati
     * @return oggetto Result con lista di prodotti
     */
    public static Result listaProdotti(String mostraTerminati)
    {
        try
        {
            boolean mostraTerminatiBool = false;

            if(mostraTerminati != null)
                mostraTerminatiBool = Boolean.parseBoolean(mostraTerminati);

            List<Prodotto> prodotti;

            if(mostraTerminatiBool)
                prodotti = ProdottoDao.listaProdotti();
            else
                prodotti = ProdottoDao.listaProdottiDisponibili();

            WrapperArrayList<Prodotto> prodottiWrapper = new WrapperArrayList<>(prodotti);

            return new Result(prodottiWrapper, "Lista prodotti");
        }
        catch (NoResultException e)
        {
            return new Result("Errore, nessun prodotto trovato.", false);
        }

    }
}

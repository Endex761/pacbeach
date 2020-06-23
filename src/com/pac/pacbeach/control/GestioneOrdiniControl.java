package com.pac.pacbeach.control;

import com.pac.pacbeach.dao.OrdineDao;
import com.pac.pacbeach.dao.PrenotazioneDao;
import com.pac.pacbeach.dao.ProdottoOrdineDao;
import com.pac.pacbeach.exceptions.DuplicatedEntryException;
import com.pac.pacbeach.exceptions.EntityNotUpdatedException;
import com.pac.pacbeach.model.Ordine;
import com.pac.pacbeach.model.Prenotazione;
import com.pac.pacbeach.model.Prodotto;
import com.pac.pacbeach.dao.ProdottoDao;
import com.pac.pacbeach.model.ProdottoOrdine;
import com.pac.pacbeach.utils.Result;

import javax.persistence.NoResultException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestioneOrdiniControl
{
    public static Result nuovoOrdine(String idProdotti, String consegna, String pagata, Integer idUtente)
    {
        try
        {
            boolean consegnaBoolean = Boolean.parseBoolean(consegna);
            boolean pagataBoolean = Boolean.parseBoolean(pagata);

            String[] idProdottiString = idProdotti.split(",");
            int[] idProdottiInt = new int[idProdottiString.length];

            for(int i=0; i<idProdottiString.length; ++i)
                idProdottiInt[i] = Integer.parseInt(idProdottiString[i]);

            Map<Integer, Integer> prodotti = new HashMap<>();
            for(int id : idProdottiInt)
            {
                if (prodotti.containsKey(id))
                {
                    int quantita = prodotti.get(id);

                    quantita++;

                    prodotti.replace(id, quantita);
                }
                else
                {
                    prodotti.put(id, 1);
                }
            }

            float totale = 0.0F;

            Timestamp now = new Timestamp(System.currentTimeMillis());

            List<Prenotazione> prenotazioniUtente = PrenotazioneDao.getPrenotazioniUtente(idUtente, now, now);

            if(prenotazioniUtente.size() == 0)
                throw new NoResultException();

            Prenotazione prenotazione = prenotazioniUtente.get(0);

            Ordine o = new Ordine(pagataBoolean,consegnaBoolean, totale, prenotazione);

            o = OrdineDao.creaOrdine(o);

            for (int idProdotto : prodotti.keySet())
            {
                Prodotto p = ProdottoDao.prodottoPerId(idProdotto);

                int quantity = prodotti.get(idProdotto);

                totale += p.getPrezzo() * quantity;

                ProdottoOrdine po = new ProdottoOrdine(quantity, p.getPrezzo(), p, o);

                ProdottoOrdineDao.creaProdottoOrdine(po);
            }

            o.setCosto(totale);

            OrdineDao.aggiornaOrdine(o);

            return new Result("Ordine effettuato con successo");
        }
        catch (NoResultException e)
        {
            return new Result("Non hai nessuna prenotazione attiva al momento.", false);
        }
        catch (DuplicatedEntryException e)
        {
            return new Result("Errore creazione ordine, id ordine duplicato.", false);
        }
        catch (EntityNotUpdatedException e)
        {
            return new Result("Errore aggiornamento ordine", false);
        }
        catch (NumberFormatException e)
        {
            return new Result("Errore formato id prodotti.", false);
        }
    }

    public static Result listaOrdiniUtente(Integer idUtente)
    {
        return null;
    }
}

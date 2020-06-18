
import com.pac.pacbeach.control.GestioneAccountControl;
import com.pac.pacbeach.control.GestionePrenotazioneControl;
import com.pac.pacbeach.dao.OrdineDao;
import com.pac.pacbeach.dao.PrenotazioneDao;
import com.pac.pacbeach.dao.ProdottoDao;
import com.pac.pacbeach.model.Ordine;
import com.pac.pacbeach.model.Prenotazione;
import com.pac.pacbeach.model.Prodotto;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.model.wrapper.WrapperArrayList;
import com.pac.pacbeach.utils.Result;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;


public class App
{
    public static void main(String[] args)
    {
        //List<Ordine> ordini = OrdineDao.ordiniPrenotazione(1);

        //Ordine o = ordini.get(0);

        //System.out.println(o.getIdOrdine());

        //List<Prodotto> prodotti = ProdottoDao.listaProdottiDisponibili();

        //System.out.println(prodottiList.toXmlString());
        //Timestamp start = new Timestamp(System.currentTimeMillis());
        //Timestamp fine = new Timestamp(System.currentTimeMillis() + 60 * 60 * 1000);

        //Boolean libero = GestionePrenotazioneControl.controllaAltrePrenotazioni(start, fine, 1);

        //System.out.println(libero);
        //Utente u = new Utente("simonpietroromeo2@gmail.com","Simon Pietro", "Romeo", "3278360803");

        //Result r2 =GestionePrenotazioneControl.creaNuovaPrenotazione("Bu","true", "8", "1");

        //System.out.println(r2.toXmlString());
        //Result r = GestioneAccountControl.creaNuovoUtente("simonpietroromeo3@gmail.com","Simon Pietro", "Romeo", "3278360803");

        //System.out.println(u.toXmlString());

        /*Result r1 = GestioneAccountControl.getUtenteByEmail("simonpietroromeo@gmail.com");

        Utente u = (Utente) r1.getContent();

        System.out.println(u.getDataRegistrazione());

        List<Prenotazione> prenotazioni = u.getPrenotazioni();

        for(Iterator<Prenotazione> i = prenotazioni.iterator(); i.hasNext();)
        {
            Prenotazione p = i.next();

            System.out.println(p.toXmlString());
        }*/

        //u.setCognome("Romano");

        //UtenteControl.aggiornaUtente(u);

        //Result r2 = UtenteControl.getUtenteByEmail("simonpietroromeo@gmail.com");

        //System.out.println(r2.toXmlString());


    }


}

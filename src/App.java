
import com.pac.pacbeach.control.GestioneAccountControl;
import com.pac.pacbeach.model.Prenotazione;
import com.pac.pacbeach.model.Utente;
import com.pac.pacbeach.utils.Result;

import java.util.Iterator;
import java.util.List;


public class App
{
    public static void main(String[] args)
    {
        Utente u = new Utente("simonpietroromeo2@gmail.com","Simon Pietro", "Romeo", "3278360803");


        Result r = GestioneAccountControl.creaNuovoUtente("simonpietroromeo3@gmail.com","Simon Pietro", "Romeo", "3278360803");

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

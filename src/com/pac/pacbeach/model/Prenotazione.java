package com.pac.pacbeach.model;

import com.pac.pacbeach.utils.DateAdapter;
import com.pac.pacbeach.utils.XmlConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

@XmlRootElement(name = "prenotazione")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "prenotazione")
public class Prenotazione
{
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPrenotazione")
    private Integer idPrenotazione;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @Column(name = "orarioInizio", nullable = false)
    private Timestamp orarioInizio;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @Column(name = "orarioFine", nullable = false)
    private Timestamp orarioFine;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @Column(name = "effettuata")
    private Timestamp effettuata;

    @Column(name = "pagata", nullable = false)
    private Boolean pagata;

    @Column(name = "costo", nullable = false)
    private Float costo;

    @Column(name = "ospiti")
    private String ospiti;

    @ManyToOne
    @JoinColumn(name = "refUtente", nullable = false)
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "refOmbrellone", nullable = false)
    private Ombrellone ombrellone;


    public Prenotazione(){}

    public Prenotazione(Timestamp orarioInizio, Timestamp orarioFine, Boolean pagata, float costo, String ospiti, Utente utente, Ombrellone ombrellone) //Ombrellone ombrellone
    {
        setOrarioInizio(orarioInizio);
        setOrarioFine(orarioFine);
        setPagata(pagata);
        setCosto(costo);
        setOspiti(ospiti);
        setUtente(utente);
        setOmbrellone(ombrellone);
        effettuata = new Timestamp(System.currentTimeMillis());
    }

    public String toXmlString()
    {
        return XmlConverter.jaxbObjectToXML(this);
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Integer getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setIdPrenotazione(Integer idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
    }

    public Timestamp getOrarioInizio() {
        return orarioInizio;
    }

    public void setOrarioInizio(Timestamp orarioInizio) {
        this.orarioInizio = orarioInizio;
    }

    public Timestamp getOrarioFine() {
        return orarioFine;
    }

    public void setOrarioFine(Timestamp orarioFine) {
        this.orarioFine = orarioFine;
    }

    public Timestamp getEffettuata() {
        return effettuata;
    }

    public void setEffettuata(Timestamp effettuata) {
        this.effettuata = effettuata;
    }

    public Boolean getPagata() {
        return pagata;
    }

    public void setPagata(Boolean pagata) {
        this.pagata = pagata;
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public Ombrellone getOmbrellone() {
        return ombrellone;
    }

    public void setOmbrellone(Ombrellone ombrellone) {
        this.ombrellone = ombrellone;
    }

    public String getOspiti() {
        return ospiti;
    }

    public void setOspiti(String ospiti) {
        this.ospiti = ospiti;
    }
}

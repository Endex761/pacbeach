package com.pac.pacbeach.model;

import com.pac.pacbeach.utils.DateAdapter;
import com.pac.pacbeach.utils.XmlConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
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
    private int idPrenotazione;

    @Column(name = "orarioInizio", nullable = false)
    private Timestamp orarioInizio;

    @Column(name = "orarioFine", nullable = false)
    private Timestamp orarioFine;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @Column(name = "effettuata")
    private Timestamp effettuata;

    @Column(name = "pagata", nullable = false)
    private Boolean pagata;

    @Column(name = "costo", nullable = false)
    private float costo;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "refUtente", nullable = false)
    private Utente utente;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "refOmbrellone", nullable = false)
    private Ombrellone ombrellone;

    public Prenotazione(){}

    public Prenotazione(Timestamp orarioInizio, Timestamp orarioFine, Boolean pagata, float costo, Utente utente, Ombrellone ombrellone) //Ombrellone ombrellone
    {
        setOrarioInizio(orarioInizio);
        setOrarioFine(orarioFine);
        setPagata(pagata);
        setCosto(costo);
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

    public int getIdPrenotazione() {
        return idPrenotazione;
    }

    public void setIdPrenotazione(int idPrenotazione) {
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

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }

    public Ombrellone getOmbrellone() {
        return ombrellone;
    }

    public void setOmbrellone(Ombrellone ombrellone) {
        this.ombrellone = ombrellone;
    }
}

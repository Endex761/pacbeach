package com.pac.pacbeach.model;

import com.pac.pacbeach.utils.DateAdapter;
import com.pac.pacbeach.utils.XmlConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Date;
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

    @Column(name = "orario", nullable = false)
    private Date orario;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @Column(name = "effettuata")
    private Timestamp effettuata;

    @Column(name = "pagata", nullable = false)
    private Boolean pagata;

    @Column(name = "costo", nullable = false)
    private float costo;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name="refUtente", nullable = false)
    private Utente utente;

    public Prenotazione(){}

    public Prenotazione(Date orario, Boolean pagata, float costo)
    {
        setOrario(orario);
        setPagata(pagata);
        setCosto(costo);
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

    public Date getOrario() {
        return orario;
    }

    public void setOrario(Date orario) {
        this.orario = orario;
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
}

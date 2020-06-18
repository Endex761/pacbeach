package com.pac.pacbeach.model;

import com.pac.pacbeach.utils.XmlConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "ordine")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "ordine")
public class Ordine
{
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "idOrdine")
    private Integer idOrdine;

    @Column(name = "pagato", nullable = false)
    private Boolean pagato;

    @Column(name = "consegna", nullable = false)
    private Boolean consegna;

    @Column(name = "costo", nullable = false)
    private Float costo;

    @Column(name = "stato", nullable = false)
    private Character stato;

    @ManyToOne
    @JoinColumn(name = "refPrenotazione", nullable = false)
    private Prenotazione prenotazione;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "prodottoOrdine",
            joinColumns = { @JoinColumn(name = "refOrdine") },
            inverseJoinColumns = { @JoinColumn(name = "refProdotto") }
    )
    private List<Prodotto> prodotti;

    public String toXmlString()
    {
        return XmlConverter.jaxbObjectToXML(this);
    }

    public Ordine() {}

    public Ordine(Boolean pagato, Boolean consegna, Float costo, Prenotazione prenotazione)
    {
        setPagato(pagato);
        setConsegna(consegna);
        setCosto(costo);
        setPrenotazione(prenotazione);
    }

    public Integer getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(Integer idOrdine) {
        this.idOrdine = idOrdine;
    }

    public Boolean getPagato() {
        return pagato;
    }

    public void setPagato(Boolean pagato) {
        this.pagato = pagato;
    }

    public Boolean getConsegna() {
        return consegna;
    }

    public void setConsegna(Boolean consegna) {
        this.consegna = consegna;
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public Character getStato() {
        return stato;
    }

    public void setStato(Character stato) {
        this.stato = stato;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public void setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }
}

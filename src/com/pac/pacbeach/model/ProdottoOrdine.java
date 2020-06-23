package com.pac.pacbeach.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlRootElement(name = "prodottoOrdine")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "prodottoOrdine")
public class ProdottoOrdine implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idProdottoOrdine")
    private Integer idProdottoOrdine;

    @Column(name = "quantita", nullable = false)
    private Integer quantita;


    @Column(name = "costo", nullable = false)
    private Float costo;


    @ManyToOne
    @JoinColumn(name = "refProdotto", nullable = false)
    private Prodotto prodotto;

    @ManyToOne
    @JoinColumn(name = "refOrdine", nullable = false)
    private Ordine ordine;

    public ProdottoOrdine(Integer quantita, Float costo, Prodotto p, Ordine o)
    {
        setQuantita(quantita);
        setCosto(costo);
        setProdotto(p);
        setOrdine(o);
    }

    public ProdottoOrdine(){}

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public Float getCosto() {
        return costo;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public void setProdotto(Prodotto prodotto) {
        this.prodotto = prodotto;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }
}

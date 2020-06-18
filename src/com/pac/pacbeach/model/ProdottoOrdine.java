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


    @Column(name = "quantita", nullable = false)
    private Integer quantita;


    @Column(name = "costo", nullable = false)
    private Float costo;

    @Id
    @ManyToOne
    @JoinColumn(name = "refProdotto", nullable = false)
    private Prodotto prodotto;

    @Id
    @ManyToOne
    @JoinColumn(name = "refOrdine", nullable = false)
    private Ordine ordine;
}

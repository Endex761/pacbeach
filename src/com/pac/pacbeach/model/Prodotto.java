package com.pac.pacbeach.model;

import com.pac.pacbeach.utils.XmlConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "prodotto")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "prodotto")
public class Prodotto
{
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column (name = "idProdotto")
    private Integer idProdotto;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "prezzo", nullable = false)
    private Float prezzo;

    @Column(name = "disponibile", nullable = false)
    private Boolean disponibile;

    @Column(name = "consumabile", nullable = false)
    private Boolean consumabile;

    @Column(name = "quantita", nullable = false)
    private Integer quantita;

    public Prodotto() {}

    public String toXmlString()
    {
        return XmlConverter.jaxbObjectToXML(this);
    }

    public Prodotto(String nome, String descrizione, Float prezzo, Boolean disponibile, Boolean consumabile, Integer quantita)
    {
        setNome(nome);
        setDescrizione(descrizione);
        setPrezzo(prezzo);
        setDisponibile(disponibile);
        setConsumabile(consumabile);
        setQuantita(quantita);
    }

    public Integer getIdProdotto() {
        return idProdotto;
    }

    public void setIdProdotto(Integer idProdotto) {
        this.idProdotto = idProdotto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        if(descrizione == null)
            return "Questo prodotto non ha una descrizone.";
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Float getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Float prezzo) {
        this.prezzo = prezzo;
    }

    public Boolean getDisponibile() {
        return disponibile;
    }

    public void setDisponibile(Boolean disponibile) {
        this.disponibile = disponibile;
    }

    public Boolean getConsumabile() {
        return consumabile;
    }

    public void setConsumabile(Boolean consumabile) {
        this.consumabile = consumabile;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }
}

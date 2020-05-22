package com.pac.pacbeach.model;

import com.pac.pacbeach.utils.DateAdapter;
import com.pac.pacbeach.utils.XmlConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;
import java.util.List;


@XmlRootElement(name = "utente")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "utente")
public class Utente
{
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column(name = "idUtente")
    private int id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column( name = "nome", nullable = false)
    private String nome;

    @Column(name = "cognome", nullable = false)
    private String cognome;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "confermaAccount", nullable = false)
    private Boolean confermaAccount;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @Column(name = "dataRegistrazione", nullable = false)
    private Timestamp dataRegistrazione;

    @XmlTransient
    @OneToMany(mappedBy = "utente")
    private List<Prenotazione> prenotazioni;

    public String toXmlString()
    {
        return XmlConverter.jaxbObjectToXML(this);
    }

    public Utente(String email, String nome, String cognome, String telefono)
    {
        setEmail(email);
        setNome(nome);
        setCognome(cognome);
        setTelefono(telefono);
        setConfermaAccount(false);
        setDataRegistrazione(new Timestamp(System.currentTimeMillis()));
    }

    public Utente(){}

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getConfermaAccount() {
        return confermaAccount;
    }

    public void setConfermaAccount(Boolean confermaAccount) {
        this.confermaAccount = confermaAccount;
    }

    public Timestamp getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(Timestamp dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }
}

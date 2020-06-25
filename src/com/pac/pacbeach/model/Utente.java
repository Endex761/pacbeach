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
    private int idUtente;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column( name = "nome", nullable = false)
    private String nome;

    @Column(name = "cognome", nullable = false)
    private String cognome;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @XmlJavaTypeAdapter(DateAdapter.class)
    @Column(name = "dataRegistrazione", nullable = false)
    private Timestamp dataRegistrazione;

    @Column(name = "ruolo", nullable = false)
    private String ruolo;

    @XmlTransient
    @OneToMany(mappedBy = "utente")
    private List<Prenotazione> prenotazioni;

    public String toXmlString()
    {
        return XmlConverter.jaxbObjectToXML(this);
    }

    public Utente(String email, String password, String nome, String cognome, String telefono, String ruolo)
    {
        setEmail(email);
        setPassword(password);
        setNome(nome);
        setCognome(cognome);
        setTelefono(telefono);
        setRuolo(ruolo);
        setDataRegistrazione(new Timestamp(System.currentTimeMillis()));
    }

    public Utente(){}

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int id) {
        this.idUtente = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Timestamp getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(Timestamp dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public String getRuolo() {
        return ruolo;
    }

    public void setRuolo(String ruolo) {
        this.ruolo = ruolo;
    }


}

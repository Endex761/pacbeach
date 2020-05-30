package com.pac.pacbeach.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

@XmlRootElement(name = "ombrellone")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "ombrellone")
public class Ombrellone
{
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOmbrellone")
    private Integer idOmbrellone;

    @Column(name = "prenotabile")
    private Boolean prenotabile;

    @XmlTransient
    @OneToMany(mappedBy = "ombrellone")
    private List<Prenotazione> prenotazioni;

    public Ombrellone()
    {
        setPrenotabile(true);
    }

    public Ombrellone(Boolean prenotabile)
    {
        setPrenotabile(prenotabile);
    }

    public Integer getIdOmbrellone() {
        return idOmbrellone;
    }

    public void setIdOmbrellone(Integer idOmbrellone) {
        this.idOmbrellone = idOmbrellone;
    }

    public Boolean isPrenotabile() {
        return prenotabile;
    }

    public void setPrenotabile(Boolean prenotabile) {
        this.prenotabile = prenotabile;
    }
}

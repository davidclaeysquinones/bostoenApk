package be.bostoenapk.Model;

import be.bostoenapk.Utilities.CustomDate;

/**
 * Created by david on 2/05/2016.
 */
public class Dossier {
    private Integer id;
    private Integer plaatsId;
    private CustomDate datum;
    private String naam;

    public Dossier()
    {


    }

    public Dossier(Integer id,Integer plaatsId, CustomDate datum,String naam)
    {
        this.setId(id);
        this.setPlaatsId(plaatsId);
        this.setDatum(datum);
        this.setNaam(naam);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlaatsId() {
        return plaatsId;
    }

    public void setPlaatsId(Integer plaatsId) {
        this.plaatsId = plaatsId;
    }

    public CustomDate getDatum() {
        return datum;
    }

    public void setDatum(CustomDate datum) {
        this.datum = datum;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }
}
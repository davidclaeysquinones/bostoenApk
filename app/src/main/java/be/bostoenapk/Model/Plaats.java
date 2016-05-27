package be.bostoenapk.Model;


public class Plaats {
    private Integer id;
    private String straat;
    private String gemeente;
    private Integer nummer;
    private Integer postcode;
    private String voornaam;
    private String naam;
    private boolean isEigenaar;


    public Plaats()
    {

    }

    public Plaats(Integer id,String straat,String gemeente,Integer nummer,Integer postcode,String voornaam,String naam,boolean isEigenaar)
    {
        this.id=id;
        this.straat=straat;
        this.gemeente=gemeente;
        this.nummer=nummer;
        this.postcode=postcode;
        this.voornaam=voornaam;
        this.naam=naam;
        this.isEigenaar=isEigenaar;

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStraat() {
        return straat;
    }

    public void setStraat(String straat) {
        this.straat = straat;
    }

    public String getGemeente() {
        return gemeente;
    }

    public void setGemeente(String gemeente) {
        this.gemeente = gemeente;
    }

    public Integer getNummer() {
        return nummer;
    }

    public void setNummer(Integer nummer) {
        this.nummer = nummer;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public void setVoornaam(String voornaam) {
        this.voornaam = voornaam;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public boolean isEigenaar() {
        return isEigenaar;
    }

    public void setIsEigenaar(boolean isEigenaar) {
        this.isEigenaar = isEigenaar;
    }
}
package be.bostoenapk.Model;


public class VragenDossier {
    private String  vraagTekst;
    private String  antwoordTekst;
    private Integer dossierNr;
    private Integer antwoordOptie;

    public VragenDossier()
    {

    }

    public VragenDossier(String vraagTekst,String antwoordTekst,Integer antwoordOptie)
    {
        this.setVraagTekst(vraagTekst);
        this.setAntwoordTekst(antwoordTekst);
        this.setAntwoordOptie(antwoordOptie);
    }


    public String getVraagTekst() {
        return vraagTekst;
    }

    public void setVraagTekst(String vraagTekst) {
        this.vraagTekst = vraagTekst;
    }

    public String getAntwoordTekst() {
        return antwoordTekst;
    }

    public void setAntwoordTekst(String antwoordTekst) {
        this.antwoordTekst = antwoordTekst;
    }

    public Integer getDossierNr() {
        return dossierNr;
    }

    public void setDossierNr(Integer dossierNr) {
        this.dossierNr = dossierNr;
    }

    public Integer getAntwoordOptie() {
        return antwoordOptie;
    }

    public void setAntwoordOptie(Integer antwoordOptie) {
        this.antwoordOptie = antwoordOptie;
    }
}

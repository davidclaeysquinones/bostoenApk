package be.bostoenapk.Model;

import android.graphics.Bitmap;

import be.bostoenapk.Utilities.CustomDate;


public class Vraag {
    //Eigenschappen van de vraag.
    private Integer id;
    private String tekst;
    private String tip;
    private Bitmap image;
    private CustomDate last_update;
    private int reeks_id;
    private boolean geldig;
    //Volgende vraag opties


    public Vraag() {
    }

    public Vraag(Integer id, String tekst, String tip, Bitmap image, CustomDate last_update, int reeks_id, boolean geldig) {
        this.id = id;
        this.tekst = tekst;
        this.tip = tip;
        this.image = image;
        this.last_update = last_update;
        this.reeks_id = reeks_id;
        this.geldig = geldig;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isGeldig() {
        return geldig;
    }

    public void setGeldig(boolean geldig) {
        this.geldig = geldig;
    }

    public int getReeks_id() {
        return reeks_id;
    }

    public void setReeks_id(int reeks_id) {
        this.reeks_id = reeks_id;
    }

    public CustomDate getLast_update() {
        return last_update;
    }

    public void setLast_update(CustomDate last_update) {
        this.last_update = last_update;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

}
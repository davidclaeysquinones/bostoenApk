package be.bostoenapk.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import be.bostoenapk.R;
import be.bostoenapk.Utilities.CustomDate;

/**
 * Created by david on 2/05/2016.
 */
public class AntwoordOptie {
    private int vraagId;
    private String antwoordTekst;
    private String antwoordOpmerking;
    private Integer volgendeVraag;
    private String oplossing;
    private boolean geldig;
    private CustomDate last_update;
    private boolean isChecked;

    public AntwoordOptie()
    {

    }

    public AntwoordOptie(int vraagId,String antwoordTekst,String antwoordOpmerking,Integer volgendeVraag,String oplossing,boolean geldig,CustomDate last_update)
    {
        this.setVraagId(vraagId);
        this.setAntwoordTekst(antwoordTekst);
        this.setAntwoordOpmerking(antwoordOpmerking);
        if(volgendeVraag!=null){
            this.volgendeVraag=volgendeVraag;
        }

        this.setOplossing(oplossing);
        this.setGeldig(geldig);
        this.setLast_update(last_update);
    }

    @Override
    public String toString() {
        return "AntwoordOptie{" +
                "vraagId=" + vraagId +
                ", last_update=" + last_update +
                ", geldig=" + geldig +
                ", oplossing='" + oplossing + '\'' +
                ", volgendeVraag=" + volgendeVraag +
                ", antwoordOpmerking='" + antwoordOpmerking + '\'' +
                ", antwoordTekst='" + antwoordTekst + '\'' +
                '}';
    }

    public String getAntwoordTekst() {
        return antwoordTekst;
    }

    public void setAntwoordTekst(String antwoordTekst) {
        this.antwoordTekst = antwoordTekst;
    }

    public String getAntwoordOpmerking() {
        return antwoordOpmerking;
    }

    public void setAntwoordOpmerking(String antwoordOpmerking) {
        this.antwoordOpmerking = antwoordOpmerking;
    }

    public Integer getVolgendeVraag() {
        return volgendeVraag;
    }

    public void setVolgendeVraag(int volgendeVraag) {
        this.volgendeVraag = volgendeVraag;
    }

    public String getOplossing() {
        return oplossing;
    }

    public void setOplossing(String oplossing) {
        this.oplossing = oplossing;
    }

    public boolean isGeldig() {
        return geldig;
    }

    public void setGeldig(boolean geldig) {
        this.geldig = geldig;
    }

    public CustomDate getLast_update() {
        return last_update;
    }

    public void setLast_update(CustomDate last_update) {
        this.last_update = last_update;
    }

    public int getVraagId() {
        return vraagId;
    }

    public void setVraagId(int vraagId) {
        this.vraagId = vraagId;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean isChecked)
    {
        this.isChecked=isChecked;
    }

    public static class AntwoordOptieAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<AntwoordOptie> antwoordOpties;
        LayoutInflater mInflater;


        public AntwoordOptieAdapter(Context c,ArrayList<AntwoordOptie> antwoordOpties)
        {
            mContext=c;
            this.antwoordOpties=antwoordOpties;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        @Override
        public int getCount() {
            return antwoordOpties.size();
        }

        @Override
        public Object getItem(int position) {
            return antwoordOpties.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.antwoordoptie_view_item,null);
            TextView antwoordtekst = (TextView)view.findViewById(R.id.Antwoordtekst);
            RadioButton btn = (RadioButton)view.findViewById(R.id.AntwoordoptieCheck);
            btn.setEnabled(false);
            btn.setChecked(antwoordOpties.get(position).isChecked());

            AntwoordOptie currentAntwoord=antwoordOpties.get(position);
            antwoordtekst.setText(currentAntwoord.getAntwoordTekst());


            return view;
        }
    }



}
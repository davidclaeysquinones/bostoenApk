package be.bostoenapk.Model;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import be.bostoenapk.R;

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

    public static class PlaatsAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<Plaats> plaatsen;
        LayoutInflater mInflater;


        public PlaatsAdapter(Context c,ArrayList<Plaats> plaatsen)
        {
            mContext=c;
            this.plaatsen=plaatsen;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return plaatsen.size();
        }

        @Override
        public Object getItem(int position) {
            return plaatsen.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.plaats_view_item, null);
            TextView klant = (TextView) view.findViewById(R.id.klantPlaats);
            TextView adres = (TextView) view.findViewById(R.id.adresPlaats);

            Plaats huidig = plaatsen.get(position);

            StringBuilder klantnaam = new StringBuilder();
            klantnaam.append(huidig.getVoornaam()).append(" ").append(huidig.getNaam());

            StringBuilder klantplaats = new StringBuilder();
            klantplaats.append(huidig.getStraat()).append(" ").append(huidig.getNummer()).append("\n");
            klantplaats.append(huidig.getPostcode()).append(" ").append(huidig.getGemeente());

            klant.setText(klantnaam.toString());
            adres.setText(klantplaats.toString());


            return view;
        }





    }
}
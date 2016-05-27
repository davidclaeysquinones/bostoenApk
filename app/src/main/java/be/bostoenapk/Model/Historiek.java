package be.bostoenapk.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import be.bostoenapk.R;


public class Historiek {
    private AntwoordOptie antwoordOptie;
    private Vraag vraag;

    public Historiek(AntwoordOptie antwoordOptie,Vraag vraag)
    {
        this.setAntwoordOptie(antwoordOptie);
        this.vraag=vraag;
    }

    public AntwoordOptie getAntwoordOptie() {
        return antwoordOptie;
    }

    public void setAntwoordOptie(AntwoordOptie antwoordOptie) {
        this.antwoordOptie = antwoordOptie;
    }

    public Vraag getVraag() {
        return vraag;
    }

    public void setVraag(Vraag vraag) {
        this.vraag = vraag;
    }


    public static class HistoriekAdapter extends BaseAdapter {
        Context mContext;
        ArrayList<Historiek> historiek;
        LayoutInflater mInflater;


        public HistoriekAdapter(Context c,ArrayList<Historiek> historiek)
        {
            mContext=c;
            this.historiek=historiek;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return historiek.size();
        }

        @Override
        public Object getItem(int position) {
            return historiek.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mInflater.inflate(R.layout.historiek_view_item, null);

            TextView vraag = (TextView) view.findViewById(R.id.historiekItemVraag);
            TextView antwoord = (TextView) view.findViewById(R.id.historiekItemAntwoord);

            Historiek huidig = historiek.get(position);

            vraag.setText(huidig.getVraag().getTekst());
            antwoord.setText(huidig.getAntwoordOptie().getAntwoordTekst());



            return view;
        }





    }
}
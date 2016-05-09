package be.bostoenapk.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.bostoenapk.Model.AntwoordOptie;
import be.bostoenapk.Model.Vraag;
import be.bostoenapk.Model.VragenDossier;
import be.bostoenapk.R;
import be.bostoenapk.Utilities.Utility;

public class VragenFragment extends Fragment {
    private View view;
    private OnFragmentInteractionListener mListener;

    private int vraagid;
    private boolean answered=false;
    private AntwoordOptie huidig;
    private SharedPreferences sharedpreferences;
    private TextView vraagtekst;
    private TextView tip;
    private ListView antwoorden;
    private ImageView info;
    private Button ok;
    private ImageView image;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.vragenscherm_layout, container, false);
        vraagtekst=(TextView)view.findViewById(R.id.txtVraag);
        tip = (TextView)view.findViewById(R.id.txtTip);
        antwoorden = (ListView)view.findViewById(R.id.AntwoordenList);
        info = (ImageView)view.findViewById(R.id.imgInfo);
        ok = (Button) view.findViewById(R.id.btnOK);
        image = (ImageView) view.findViewById(R.id.imageView);

        Utility.setListViewHeightBasedOnChildren(antwoorden);
        final Vraag vraag = mListener.getVraag(vraagid);
        if(vraag!=null)
        {
            final ArrayList<AntwoordOptie> antwoordOpties=mListener.getAntwoorden(vraagid);
            antwoorden.setAdapter(new AntwoordOptie.AntwoordOptieAdapter(getActivity().getApplicationContext(), antwoordOpties));
            //  Utility.setListViewHeightBasedOnChildren(antwoorden);


            //kijken of de huidige vraag een afbeelding heeft


            tip.setVisibility(View.INVISIBLE);
            tip.setVisibility(View.GONE);
            info.setVisibility(View.INVISIBLE);
            if(mListener.getTips())
            {
                if(vraag.getTip()!=null && !vraag.getTip().equals(""))
                {
                    tip.setVisibility(View.VISIBLE);
                    tip.setText("Tip : " + vraag.getTip());
                    info.setVisibility(View.VISIBLE);
                }
                else{
                    info.setVisibility(View.INVISIBLE);
                }

            }

            image.setVisibility(View.INVISIBLE);
            image.setVisibility(View.GONE);
            if(mListener.getAfbeeldingen())
            {
                if(vraag.getImage()!=null)
                {
                    image.setVisibility(View.VISIBLE);
                    image.setImageBitmap(vraag.getImage());
                }
            }


            vraagtekst.setText(vraag.getTekst());






            ArrayList<VragenDossier> vragenDossiers=mListener.getVragenDossiers(mListener.getLastDossier());
            if(vragenDossiers!=null)
            {
                String antwoord="";
                for(int i=0;i<vragenDossiers.size();i++)
                {
                    VragenDossier vragenDossier = vragenDossiers.get(i);
                    if(vragenDossier.getVraagTekst().equals(vraag.getTekst()))
                    {
                        antwoord=vragenDossier.getAntwoordTekst();
                    }
                }
                for (int i =0;i<antwoordOpties.size();i++)
                {
                    if(antwoordOpties.get(i).getAntwoordTekst().equals(antwoord))
                    {
                        huidig=antwoordOpties.get(i);
                        antwoordOpties.get(i).setChecked(true);
                        answered=true;
                        Log.d("Vraag", "Match");
                    }
                }
            }






            antwoorden.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    huidig = (AntwoordOptie) parent.getItemAtPosition(position);
                    for (int i = 0; i < antwoordOpties.size(); i++) {
                        AntwoordOptie antwoordOptie = antwoordOpties.get(i);

                        if (antwoordOptie == huidig) {
                            antwoordOpties.get(i).setChecked(true);
                        } else {
                            antwoordOpties.get(i).setChecked(false);
                        }

                    }

                    antwoorden.setAdapter(new AntwoordOptie.AntwoordOptieAdapter(getActivity().getApplicationContext(), antwoordOpties));


                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (huidig != null) {

                        VragenDossier vragenDossier = new VragenDossier();
                        vragenDossier.setDossierNr(mListener.getLastDossier());
                        vragenDossier.setAntwoordTekst(huidig.getAntwoordTekst());
                        vragenDossier.setVraagTekst(vraag.getTekst());
                        vragenDossier.setAntwoordOptie(huidig.getVraagId());

                        if(answered)
                        {
                            Log.d("Vraag","answered");
                            mListener.updateVragenDossier(mListener.getLastDossier(),vraag.getTekst(),vragenDossier);
                        }
                        else
                        {
                            Log.d("Vraag","not answered");
                            mListener.addVragenDossier(vragenDossier);
                        }

                        if (huidig.getVolgendeVraag() != null) {

                            mListener.setLastVraag(vraag.getId());
                            mListener.goToVragenFragment(huidig.getVolgendeVraag());
                            Log.d("volgenge vraag", huidig.getVolgendeVraag().toString());
                            Log.d("volgende vraag","bestaat");
                            Log.d("boolean bestaat", new Boolean(huidig.getVolgendeVraag()!=null).toString());
                        }
                        else {
                            Log.d("Volgende vraag is null", "null");
                            mListener.goToEindScherm();
                        }

                    } else {
                        Log.d("Huidig is null", "null");
                    }
                }
            });
        }




        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);


        if(activity instanceof OnFragmentInteractionListener)
        {

            mListener = (OnFragmentInteractionListener) activity;
        }
        else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    public void setVraagid(int vraagid)
    {
        this.vraagid=vraagid;
    }
    public int getVraagid()
    {
        return vraagid;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        Vraag getVraag(int id);
        Integer getLastDossier();
        ArrayList<VragenDossier> getVragenDossiers(int vraagid);
        ArrayList<AntwoordOptie> getAntwoorden(int vraagid);
        void addVragenDossier(VragenDossier vragenDossier);
        void updateVragenDossier(int dossiernr,String vraagtekst,VragenDossier vragenDossier);
        void goToEindScherm();
        void goToVragenFragment(int id);
        void setLastVraag(Integer lastVraag);
        boolean getAfbeeldingen();
        boolean getTips();


    }


}
package be.bostoenapk.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import be.bostoenapk.Model.AntwoordOptie;
import be.bostoenapk.Model.Historiek;
import be.bostoenapk.Model.Vraag;
import be.bostoenapk.Model.VragenDossier;
import be.bostoenapk.R;

/**
 * Created by david on 2/05/2016.
 */

public class HistoriekFragment extends Fragment {

    private View view;
    private OnFragmentInteractionListener mListener;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.historiek_layout, container, false);

        ListView lijst = (ListView)view.findViewById(R.id.historiekLijst);

        Integer lastDossier = mListener.getLastDossier();

        //Inladen vragen in de historiek
        if(lastDossier!=null)
        {
            ArrayList<VragenDossier> vragenDossiers = mListener.getVragenDossiers(mListener.getLastDossier());

            if(vragenDossiers!=null)
            {
                final ArrayList<Historiek> historieken = new ArrayList<Historiek>();
                for(VragenDossier vragenDossier : vragenDossiers)
                {
                    Vraag vraag = mListener.getVraag(vragenDossier.getAntwoordOptie());
                    if(vraag!=null)
                    {
                        ArrayList<AntwoordOptie> antwoordOpties = mListener.getAntwoorden(vraag.getId());
                        AntwoordOptie antwoordOptie = null;

                        for(AntwoordOptie antwoord : antwoordOpties)
                        {
                            if(antwoord.getAntwoordTekst().equals(vragenDossier.getAntwoordTekst()))
                            {
                                antwoordOptie = antwoord;
                            }
                        }

                        if (antwoordOptie != null) {
                            historieken.add(new Historiek(antwoordOptie,vraag));
                        }
                    }
                    else {
                        Log.d("Vraag", "Null");
                    }
                    lijst.setAdapter(new Historiek.HistoriekAdapter(getActivity().getApplicationContext(),historieken));

                    lijst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int i = position+1;
                            while(i<historieken.size())
                            {
                                Historiek historiek = historieken.get(i);

                                VragenDossier vragendossier = mListener.getVragenDossier(mListener.getLastDossier(),historiek.getVraag().getId());
                                mListener.deleteVragenDossier(vragendossier);
                                i++;
                            }
                            mListener.goToVragenFragment(historieken.get(position).getVraag().getId());

                        }

                    });
                }




            }
        }



        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);


        if(activity instanceof OnFragmentInteractionListener)
        {

            mListener = (OnFragmentInteractionListener)activity;

        }
        else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
    public interface OnFragmentInteractionListener {
        Integer getLastDossier();
        ArrayList<VragenDossier> getVragenDossiers(int dossiernr);
        ArrayList<AntwoordOptie> getAntwoorden(int vraagid);
        Vraag getVraag(int id);
        void goToVragenFragment(int id);
        VragenDossier getVragenDossier(int dossiernr,int vraagid);
        void deleteVragenDossier(VragenDossier vragenDossier);
    }
}
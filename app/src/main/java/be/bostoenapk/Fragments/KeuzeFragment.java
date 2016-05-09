package be.bostoenapk.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import be.bostoenapk.Model.Dossier;
import be.bostoenapk.Model.Reeks;
import be.bostoenapk.R;
import be.bostoenapk.Utilities.CustomDate;
import be.bostoenapk.Utilities.Validatie;

public class KeuzeFragment extends Fragment {

    private View view;
    private Reeks huidig;
    private OnFragmentInteractionListener mListener;
    boolean eindFragment=false;
    boolean error = false;
    private Validatie val;

    public void setEindFragment(boolean eindFragment)
    {
        this.eindFragment=eindFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.keuze_layout, container, false);

        val : new Validatie();
        Button volgende = (Button) view.findViewById(R.id.btnFragVragen);
        final EditText dossiernaam = (EditText) view.findViewById(R.id.txtIdentificatieKeuze);
        error = false;

        if (mListener.getLastDossier() != null) {
            if(mListener.getLastPlaats()!=null)
            {
                Dossier dossier = mListener.getDossier(mListener.getLastDossier());
                if (dossier != null) {
                    dossiernaam.setText(dossier.getNaam());
                }
            }
            else {
                Log.d("Plaats", "null");
            }

        }
        volgende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("volgende", "keuzefragment");

                if (!val.valString(dossiernaam.getText().toString(), 51, -1))
                {
                    dossiernaam.setError("Geef een correcte naam op");
                    error = true;
                }
                else
                {
                    dossiernaam.setError(null);
                    error=false;
                }

                if (huidig != null) {
                    Dossier dossier = new Dossier();
                    dossier.setPlaatsId(mListener.getLastPlaats());
                    dossier.setNaam(dossiernaam.getText().toString());


                    if (mListener.getLastDossier() == null || eindFragment) {
                        //voorkomen dat bij een update de aanmaakdatum gewijzigd wordt
                        dossier.setDatum(new CustomDate());
                        mListener.addDosier(dossier);
                    } else {
                        if (dossier != null) {
                            mListener.updateDossier(mListener.getLastDossier(), dossier);
                        }
                    }
                    if (!error)
                        mListener.goToVragenFragment(huidig.getEersteVraag());
                }

            }
        });

        final ListView reekslijst = (ListView) view.findViewById(R.id.reeksLijst);


        final ArrayList<Reeks> reeksen = mListener.getReeksen();

        if (reeksen != null) {
            if (mListener.getLastReeks() != null) {
                reeksen.get(mListener.getLastReeks()).setChecked(true);
                huidig = reeksen.get(mListener.getLastReeks());
            }
            Log.d("Reeksen", "not null");
            reekslijst.setAdapter(new Reeks.ReeksAdapter(getActivity().getApplicationContext(), reeksen));
        } else {
            Log.d("reeksen", "null");
        }


        reekslijst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                huidig = (Reeks) parent.getItemAtPosition(position);
                mListener.setLastReeks(position);
                for (int i = 0; i < reeksen.size(); i++) {
                    Reeks reeks = reeksen.get(i);

                    if (reeks == huidig) {
                        reeksen.get(i).setChecked(true);
                    } else {
                        reeksen.get(i).setChecked(false);
                    }

                }


                reekslijst.setAdapter(new Reeks.ReeksAdapter(getActivity().getApplicationContext(), reeksen));
                Log.d("Listview", "onclick");
            }
        });


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        if (activity instanceof OnFragmentInteractionListener) {

            mListener = (OnFragmentInteractionListener) activity;

        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }


    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {

        ArrayList<Reeks> getReeksen();

        Integer getLastPlaats();

        Integer getLastDossier();

        Dossier getDossier(int id);

        Integer getLastReeks();

        void setLastReeks(Integer id);

        void addDosier(Dossier dossier);

        void updateDossier(int id, Dossier dossier);

        void goToVragenFragment(int id);

    }
}
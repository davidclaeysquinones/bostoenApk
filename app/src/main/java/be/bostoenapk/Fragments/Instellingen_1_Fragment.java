package be.bostoenapk.Fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import be.bostoenapk.R;
import be.bostoenapk.Utilities.Validatie;



public class Instellingen_1_Fragment extends Fragment {

    private static final String TAG = "BOSTOEN";
    private Validatie val;
    private EditText naam;
    private EditText voornaam;
    private EditText email;
    private Button bevestig;
    private SharedPreferences sharedpreferences;

    OnFragmentInteractionListener mListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.instellingen_1_layout, container, false);

        naam = (EditText) view.findViewById(R.id.txtNaamAdviseur);
        voornaam = (EditText) view.findViewById(R.id.txtVoornaamAdviseur);
        email = (EditText) view.findViewById(R.id.txtEmailAdviseur);
        bevestig = (Button) view.findViewById(R.id.btnFragKlant);


        naam.setText(mListener.getNaam());
        voornaam.setText(mListener.getVoornaam());
        email.setText(mListener.getEmail());

        bevestig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                bevestig();
            }
        });
        val = new Validatie();
        return view;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    //Valideer input
    public void bevestig() {
        if(controleerVelden()) {
            mListener.setVoornaam(voornaam.getText().toString().trim());
            mListener.setNaam(naam.getText().toString().trim());
            mListener.setEmail(email.getText().toString().trim());
            Toast.makeText(getActivity(), "Instellingen opgeslagen", Toast.LENGTH_LONG).show();
        }
    }

    public boolean controleerVelden() {
        boolean voorn = true;
        boolean nm = true;
        boolean eml = true;

        if(!val.valString(voornaam.getText().toString().trim(), 51, -1)) {
            voornaam.setError("Gelieve een correcte voornaam in te geven (max. 50 karakters)");
            voorn = false;
        } else voornaam.setError(null);
        if (!val.valString(naam.getText().toString().trim(), 51, -1)) {
            naam.setError("Gelieve een correcte naam in te geven (max. 50 karakters)");
            nm = false;
        } else naam.setError(null);
        if (!val.valEmail(email.getText().toString().trim(), 101)) {
            email.setError("Gelieve een correct e-mailadres in te geven");
            eml=  false;
        } else email.setError(null);
        if(voorn && nm && eml) return true;
        else return false;
    }

    public interface OnFragmentInteractionListener {
        String getVoornaam();
        void setVoornaam(String voornaam);
        String getNaam();
        void setNaam(String naam);
        void setEmail(String email);
        String getEmail();



    }
}
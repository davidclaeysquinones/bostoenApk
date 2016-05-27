package be.bostoenapk.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import be.bostoenapk.R;
import be.bostoenapk.Utilities.Validatie;


public class LoginAdviseurFragment extends Fragment {
    private View view;
    private OnFragmentInteractionListener mListener;
    private Validatie val;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.loginadviseur_layout, container, false);
        Button volgende=(Button)view.findViewById(R.id.btnFragKlant);
        final EditText voornaam= (EditText) view.findViewById(R.id.txtVoornaamAdviseur);
        final EditText naam = (EditText) view.findViewById(R.id.txtNaamAdviseur);
        final EditText email = (EditText) view.findViewById(R.id.txtEmailAdviseur);
        val = new Validatie();
        if(mListener.getVoornaam()!=null)
        {
            voornaam.setText(mListener.getVoornaam());
        }

        if(mListener.getNaam()!=null)
        {
            naam.setText(mListener.getNaam());
        }

        if(mListener.getEmail()!=null)
        {
            naam.setText(mListener.getEmail());
        }

        volgende.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("click","yeaah");
                boolean blnVoornaam=false;
                boolean blnNaam=false;
                boolean blnEmail=false;
                if(val.valString(voornaam.getText().toString(), 51, -1))
                {
                    voornaam.setError(null);
                    blnVoornaam = true;
                }
                else voornaam.setError("Dit veld mag niet leeg zijn (max. 50 karakters)");

                if(val.valString(naam.getText().toString(), 51, -1))
                {
                    naam.setError(null);
                    blnNaam = true;
                }
                else naam.setError("Dit veld mag niet leeg zijn (max. 50 karakters)");

                if(val.valEmail(email.getText().toString(), 101))
                {
                    email.setError(null);
                    blnEmail=true;
                }
                else email.setError("Geef een correct email adres in (max. 100 karakters).");

                if(blnVoornaam && blnNaam && blnEmail)
                {
                    mListener.setVoornaam(voornaam.getText().toString());
                    mListener.setNaam(naam.getText().toString());
                    mListener.setEmail(email.getText().toString());
                    mListener.goToHomeFragment();
                }
            }
        });

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
        else
        {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        String getVoornaam();
        void setVoornaam(String voornaam);
        String getNaam();
        void setNaam(String naam);
        String getEmail();
        void setEmail(String email);
        void goToHomeFragment();

    }
}

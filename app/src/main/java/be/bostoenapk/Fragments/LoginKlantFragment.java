package be.bostoenapk.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import be.bostoenapk.Model.Plaats;
import be.bostoenapk.R;
import be.bostoenapk.Utilities.Validatie;

public class LoginKlantFragment extends Fragment {
    private View view;
    private OnFragmentInteractionListener mListener;
    private final String TAG = LoginKlantFragment.class.getSimpleName();
    private Validatie val;

    EditText naam;
    EditText voornaam;
    EditText straat;
    EditText gemeente;
    EditText nummer;
    EditText postcode;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.instellingen_2_layout, container, false);
        Button volgende = (Button)view.findViewById(R.id.btnFragKeuze);
        final CheckBox isEigenaar = (CheckBox)view.findViewById(R.id.klantIsEigenaar);
        naam = (EditText)view.findViewById(R.id.txtNaamKlant);
        voornaam = (EditText)view.findViewById(R.id.txtVoornaamKlant);
        straat = (EditText)view.findViewById(R.id.txtStraatKlant);
        gemeente = (EditText)view.findViewById(R.id.txtGemeenteKlant);
        nummer = (EditText)view.findViewById(R.id.txtNrKlant);
        postcode = (EditText)view.findViewById(R.id.txtPostcodeKlant);
        val = new Validatie();
        //kijken of er tijdens de uitvoering van de app al een plaats werd toegevoegd
        if(mListener.getLastPlaats()!=null)
        {
            Plaats plaats = mListener.getPlaats(mListener.getLastPlaats());
            if(plaats!=null)
            {
                isEigenaar.setChecked(plaats.isEigenaar());
                naam.setText(plaats.getNaam());
                voornaam.setText(plaats.getVoornaam());
                straat.setText(plaats.getStraat());
                gemeente.setText(plaats.getGemeente());
                if(plaats.getNummer()!=null)
                {
                    nummer.setText(plaats.getNummer().toString());
                }
                if(plaats.getPostcode()!=null)
                {
                    postcode.setText(plaats.getPostcode().toString());
                }


            } else {
                Log.d("Loginklant","plaats is null");
            }

        }
        volgende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controleerVelden()) {
                    Plaats plaats =new Plaats();
                    plaats.setIsEigenaar(isEigenaar.isChecked());
                    plaats.setNaam(naam.getText().toString());
                    plaats.setVoornaam(voornaam.getText().toString());
                    plaats.setStraat(straat.getText().toString());
                    plaats.setGemeente(gemeente.getText().toString());
                    plaats.setNummer(new Integer(nummer.getText().toString()));
                    plaats.setPostcode(new Integer(postcode.getText().toString()));
                    if(mListener.getLastPlaats()==null)
                    {
                        mListener.addPlaats(plaats);
                    }
                    //de plaats updaten
                    else {
                        mListener.updatePlaats(mListener.getLastPlaats(),plaats);
                    }
                    mListener.goToKeuzeFragment();
                }
            }
        });


        return view;
    }

    public boolean controleerVelden() {
        if(!val.valString(naam.getText().toString(), 51, -1)) {
            naam.setError("Gelieve een correcte naam in te geven (max. 50 karakters)");
            return false;
        } else {
            naam.setError(null);
        }
        if (!val.valString(voornaam.getText().toString(), 51, -1)) {
            voornaam.setError("Gelieve een correcte voornaam in te geven (max. 50 karakters)");
            return false;
        } else voornaam.setError(null);

        if (!val.valString(straat.getText().toString(), 101, -1)) {
            straat.setError("Gelieve een correcte straat in te geven (max. 100 karakters)");
            return false;
        } else straat.setError(null);

        if (!val.valString(gemeente.getText().toString(), 51, -1)) {
            gemeente.setError("Gelieve een correcte gemeente in te geven (max. 50 karakters)");
            return false;
        } else gemeente.setError(null);

        if (!val.valNumber(nummer.getText().toString(), 5, -1)) {
            nummer.setError("Gelieve een correct nummer (enkel cijfers) in te geven (max. 4 karkters)");
            return false;
        } else nummer.setError(null);

        if (!val.valNumberExactLength(postcode.getText().toString(), 4)) {
            postcode.setError("Gelieve een correcte postcode in te geven (exact 4 cijfers)");
            return false;
        } else postcode.setError(null);

        return true;
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
                    + " must implement OnFragmentInteractionListenere");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface OnFragmentInteractionListener {
        void addPlaats(Plaats plaats);
        Integer getLastPlaats();
        Plaats getPlaats(int id);
        void updatePlaats(int id,Plaats plaats);
        void goToKeuzeFragment();
    }
}
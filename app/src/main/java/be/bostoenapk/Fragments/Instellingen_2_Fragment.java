package be.bostoenapk.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import be.bostoenapk.Model.Plaats;
import be.bostoenapk.R;
import be.bostoenapk.Utilities.Validatie;

/**
 * Created by david on 2/05/2016.
 */

public class Instellingen_2_Fragment extends Fragment {

    private static final String TAG = "BOSTOEN";
    private EditText naam;
    private  EditText voornaam;
    private  EditText straat;
    private  EditText gemeente;
    private  EditText nummer;
    private  EditText postcode;
    private CheckBox eigenaar;
    private Button bevestig;

    private Validatie val;
    OnFragmentInteractionListener mListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instellingen_2_layout, container, false);

        naam = (EditText) view.findViewById(R.id.txtNaamKlant);
        voornaam = (EditText) view.findViewById(R.id.txtVoornaamKlant);
        straat = (EditText) view.findViewById(R.id.txtStraatKlant);
        gemeente = (EditText) view.findViewById(R.id.txtGemeenteKlant);
        nummer = (EditText) view.findViewById(R.id.txtNrKlant);
        postcode = (EditText) view.findViewById(R.id.txtPostcodeKlant);
        eigenaar = (CheckBox) view.findViewById(R.id.klantIsEigenaar);

        bevestig = (Button) view.findViewById(R.id.btnFragKeuze);
        eigenaar.setChecked(true);
        val = new Validatie();

        //Gegevens ophalen als deze er zijn
        if(mListener.getLastPlaats()!=null) {
            Plaats plaats = mListener.getPlaats(mListener.getLastPlaats());
            if (plaats != null) {

                eigenaar.setChecked(plaats.isEigenaar());
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
                Log.d("Loginklant", "plaats is null");
            }
        }


        bevestig.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Instellingen opgeslagen", Toast.LENGTH_LONG).show();
                if(controleerVelden()) bevestig();
            }
        });

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

    //Validatie input
    public void bevestig() {
        Plaats plaats =new Plaats();
        plaats.setIsEigenaar(eigenaar.isChecked());
        plaats.setNaam(naam.getText().toString());
        plaats.setVoornaam(voornaam.getText().toString());
        plaats.setStraat(straat.getText().toString());
        plaats.setGemeente(gemeente.getText().toString());

        if(mListener.getLastPlaats()!=null && controleerVelden())
        {
            mListener.updatePlaats(mListener.getLastPlaats(), plaats);
        }
        else
        {
            naam.setError("Geen actief klantendossier");
            voornaam.setError("Geen actief klantendossier");
            straat.setError("Geen actief klantendossier");
            gemeente.setError("Geen actief klantendossier");
            nummer.setError("Geen actief klantendossier");
            postcode.setError("Geen actief klantendossier");
        }

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

        if (!val.valString(nummer.getText().toString(), 5, -1)) {
            nummer.setError("Gelieve een correct nummer in te geven (max. 4 cijfers en/of letters)");
            return false;
        } else nummer.setError(null);

        if (val.valNumberExactLength(postcode.getText().toString(), 4)) {
            postcode.setError("Gelieve een correcte postcode in te geven (max. 4 cijfers)");
            return false;
        } else postcode.setError(null);

        return true;
    }

    public interface OnFragmentInteractionListener {
        Integer getLastPlaats();
        Plaats getPlaats(int id);
        void updatePlaats(int id,Plaats plaats);
    }
}
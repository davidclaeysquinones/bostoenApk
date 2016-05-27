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

    private EditText naam;
    private EditText voornaam;
    private EditText straat;
    private EditText gemeente;
    private EditText nummer;
    private EditText postcode;
    private CheckBox isEigenaar;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.instellingen_2_layout, container, false);
        Button volgende = (Button)view.findViewById(R.id.btnFragKeuze);
        isEigenaar = (CheckBox)view.findViewById(R.id.klantIsEigenaar);
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
                    plaats.setNaam(naam.getText().toString().trim());
                    plaats.setVoornaam(voornaam.getText().toString().trim());
                    plaats.setStraat(straat.getText().toString().trim());
                    plaats.setGemeente(gemeente.getText().toString().trim());
                    plaats.setNummer(Integer.valueOf(nummer.getText().toString().trim()));
                    plaats.setPostcode(Integer.valueOf(postcode.getText().toString().trim()));
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
        boolean nm = true;
        boolean vnm = true;
        boolean str = true;
        boolean gem = true;
        boolean nmr = true;
        boolean pst = true;

        if(!val.valString(naam.getText().toString().trim(), 51, -1)) {
            naam.setError("Gelieve een correcte naam in te geven (max. 50 karakters)");
            nm = false;
        } else {
            naam.setError(null);
        }
        if (!val.valString(voornaam.getText().toString().trim(), 51, -1)) {
            voornaam.setError("Gelieve een correcte voornaam in te geven (max. 50 karakters)");
            vnm = false;
        } else voornaam.setError(null);

        if (!val.valString(straat.getText().toString().trim(), 101, -1)) {
            straat.setError("Gelieve een correcte straat in te geven (max. 100 karakters)");
            str = false;
        } else straat.setError(null);

        if (!val.valString(gemeente.getText().toString().trim(), 51, -1)) {
            gemeente.setError("Gelieve een correcte gemeente in te geven (max. 50 karakters)");
            gem = false;
        } else gemeente.setError(null);

        if (!val.valNumber(nummer.getText().toString().trim(), 5, -1)) {
            nummer.setError("Gelieve een correct nummer in te geven (max. 4 cijfers)");
            nmr = false;
        } else nummer.setError(null);

        if (!val.valNumberExactLength(postcode.getText().toString().trim(), 4)) {
            postcode.setError("Gelieve een correcte postcode in te geven (4 cijfers)");
            pst = false;
        } else postcode.setError(null);

        if(nm && vnm && str && gem && nmr && pst) return true;
        else return false;
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
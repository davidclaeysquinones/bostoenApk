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

public class LoginKlantFragment extends Fragment {
    private View view;
    private OnFragmentInteractionListener mListener;
    private final String TAG = LoginKlantFragment.class.getSimpleName();

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
        boolean valid = true;


        if(!isValidText(50, naam.getText().toString())) {
            naam.setError("Gelieve een correcte naam in te geven (max. 50 karakters)");
            valid = false;
        } else {
            naam.setError(null);
            valid = valid ==true;
        }
        if (!isValidText(50, voornaam.getText().toString())) {
            voornaam.setError("Gelieve een correcte voornaam in te geven (max. 50 karakters)");
            valid = false;
        } else {
            voornaam.setError(null);
            valid = valid ==true;
        }

        if (!isValidText(100, straat.getText().toString())) {
            straat.setError("Gelieve een correcte straat in te geven (max. 100 karakters)");
            valid = false;
        } else {
            straat.setError(null);
            valid = valid ==true;
        }

        if (!isValidText(50, gemeente.getText().toString())) {
            gemeente.setError("Gelieve een correcte gemeente in te geven (max. 50 karakters)");
            return false;
        } else {
            gemeente.setError(null);
            valid = valid == true;
        }


        if (nummer.getText().toString().equals("") ||
                nummer.getText().toString().equals(null) ||
                nummer.getText().toString().length() > 5 ||
                !isNummer(nummer.getText().toString())) {
            nummer.setError("Gelieve een correct nummer in te geven (max. 4 cijfers en/of letters)");
            return false;
        } else nummer.setError(null);


        if (!isValidText(5, postcode.getText().toString()) ||
                !isNummer(postcode.getText().toString())) {
            postcode.setError("Gelieve een correcte postcode in te geven (max. 4 cijfers)");
            valid = false;
        } else {
            postcode.setError(null);
            valid = valid ==true;
        }

        return valid;
    }

    protected boolean isNummer(String nummer) {
        try {
            Integer.parseInt(nummer);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected boolean isValidText(int maxLength, String woord) {
        if(woord==null || woord.equals("") || woord.length() > maxLength ) {
            return false;
        } else return true;
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
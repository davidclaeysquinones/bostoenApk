package be.bostoenapk.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import be.bostoenapk.R;


public class HomeFragment extends Fragment {

    private View view;
    private OnFragmentInteractionListener mListener;
    private Button startEnquete;
    private Button settings;
    private Button about;
    private Button plaatsen;
    private TextView welkom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.home_layout, container, false);
        startEnquete =(Button)view.findViewById(R.id.btnStart);
        settings = (Button) view.findViewById(R.id.btnSettings);
        plaatsen = (Button) view.findViewById(R.id.plaatsButton);
        about = (Button) view.findViewById(R.id.btnAbout);
        welkom = (TextView) view.findViewById(R.id.txtWelkom);

        welkom.setText("Welkom " + mListener.getVoornaam());

        startEnquete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener.getLastVraag() == null) {
                    mListener.goToKlantFragment();
                } else {
                    mListener.goToVraag(mListener.getLastVraag());
                }


            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.goToInstellingen();

            }
        });

        plaatsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToPlaatsHistoriek();
                Log.d("homefragment","plaatsen");
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mListener.goToAboutFragment();

            }
        });


        return view;
    }

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

        void goToAboutFragment();
        void goToInstellingen();
        void goToKlantFragment();
        String getVoornaam();
        Integer getLastVraag();
        void goToVraag(int id);
        void goToPlaatsHistoriek();


    }
}
package be.bostoenapk.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import be.bostoenapk.R;


public class Instellingen_3_Fragment extends Fragment {

    private CheckBox tips;
    private CheckBox afbeeldingen;


    OnFragmentInteractionListener mListener;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.instellingen_3_layout, container, false);

        tips = (CheckBox) view.findViewById(R.id.tipsWeergeven);
        afbeeldingen = (CheckBox) view.findViewById(R.id.afbeeldingenWeergeven);

        afbeeldingen.setChecked(mListener.getAfbeeldingen());
        tips.setChecked(mListener.getTips());

        afbeeldingen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bevestig(tips.isChecked(), afbeeldingen.isChecked());
            }
        });

        tips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bevestig(tips.isChecked(), afbeeldingen.isChecked());
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        return view;
    }

    public void bevestig(boolean tips, boolean afbeeldingen){
        Toast.makeText(getActivity(), "Instellingen opgeslagen", Toast.LENGTH_LONG).show();
        mListener.setInstellingenOverig(tips, afbeeldingen);

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

    public void checked(View view) {
        mListener.setInstellingenOverig(tips.isChecked(), afbeeldingen.isChecked());
    }

    public interface OnFragmentInteractionListener {
        void setInstellingenOverig(boolean tips, boolean afbeeldingen);
        boolean getTips();
        boolean getAfbeeldingen();


    }
}
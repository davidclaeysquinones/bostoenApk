package be.bostoenapk.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import be.bostoenapk.Model.Plaats;
import be.bostoenapk.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaatsHistoriekFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaatsHistoriekFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaatsHistoriekFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private View view;

    public PlaatsHistoriekFragment() {
        // Required empty public constructor
    }


    public static PlaatsHistoriekFragment newInstance(String param1, String param2) {
        PlaatsHistoriekFragment fragment = new PlaatsHistoriekFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_plaats_historiek, container, false);
        ListView plaatsenlijst = (ListView) view.findViewById(R.id.plaatsen);
        TextView empty = (TextView) view.findViewById(R.id.plaatsHisotiekEmpty);


        ArrayList<Plaats> plaatsen = mListener.getPlaatsen();
        if(plaatsen!=null)
        {
            empty.setVisibility(View.GONE);
            plaatsenlijst.setAdapter(new Plaats.PlaatsAdapter(getActivity().getApplicationContext(),plaatsen));
            plaatsenlijst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Plaats huidig = (Plaats) parent.getItemAtPosition(position);
                    mListener.setLastPlaats(huidig.getId());
                    mListener.goToKeuzeFragment();
                }
            });
        }
        else {
            Log.d("plaatshistoriek","geen plaatsen");
            plaatsenlijst.setVisibility(View.GONE);
        }



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        ArrayList<Plaats> getPlaatsen();
        void goToKeuzeFragment();
        void setLastPlaats(Integer lastPlaats);
    }
}

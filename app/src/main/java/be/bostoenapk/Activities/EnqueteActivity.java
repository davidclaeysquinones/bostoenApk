package be.bostoenapk.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;

import be.bostoenapk.Fragments.EindFragment;
import be.bostoenapk.Fragments.HistoriekFragment;
import be.bostoenapk.Fragments.KeuzeFragment;
import be.bostoenapk.Fragments.VragenFragment;
import be.bostoenapk.Model.AntwoordOptie;
import be.bostoenapk.Model.DataDBAdapter;
import be.bostoenapk.Model.Dossier;
import be.bostoenapk.Model.Plaats;
import be.bostoenapk.Model.Reeks;
import be.bostoenapk.Model.Vraag;
import be.bostoenapk.Model.VragenDossier;
import be.bostoenapk.R;

public class EnqueteActivity extends Activity implements KeuzeFragment.OnFragmentInteractionListener, VragenFragment.OnFragmentInteractionListener, EindFragment.OnFragmentInteractionListener, HistoriekFragment.OnFragmentInteractionListener{

    private DataDBAdapter dataDBAdapter;
    private static final String PREFS_NAME = "COM.BOSTOEN.BE";

    private SharedPreferences sharedpreferences;
    private Integer lastReeks;

    //Declaratie voor de DrawerLayout

    private String[] mVragen = {"Home", "Historiek"}; //Namen van de gemaakte vragen in deze array
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquete_layout);
        dataDBAdapter = new DataDBAdapter(getApplicationContext());

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        boolean previousInstellingen = false;

        //Nieuwe enquete kiezen
        if (savedInstanceState == null && !previousInstellingen ) {
            //Als alle enquetes zijn ingevuld naar eindscherm
            if(getIntent().getStringExtra("last_fragment")!=null)
            {
                getFragmentManager().beginTransaction().add(R.id.content_frame, new EindFragment(), "EindFragment")
                        .commit();
                previousInstellingen=true;
                Log.d("Previous","instellingen");
            } else {
                Log.d("Previous","null");
                //als een vraag reeds ingevuld is
                if(getIntent().hasExtra("lastVraag"))
                {
                    int lastVraag = getIntent().getIntExtra("lastVraag",-1);
                    VragenFragment vraag=new VragenFragment();
                    vraag.setVraagid(lastVraag);
                    getFragmentManager().beginTransaction().add(R.id.content_frame, vraag, "VragenFragment")
                            .commit();
                }
                else {
                    getFragmentManager().beginTransaction().add(R.id.content_frame, new KeuzeFragment(), "KeuzeFragment")

                            .commit();
                    Log.d("Previous instellingen", "false");
                }


            }



        }










        //DrawerLayout instellingen
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.icon, R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                invalidateOptionsMenu();
            }


            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Listview aan drawerlayout linken

        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mVragen));

        //Clicklistener (verwijst naar DrawerItemClickListener)
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    //Navigatie in de DrawerLayout
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {

            switch (position){
                case 0: Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    mDrawerLayout.closeDrawers();

                    break;
                case 1: getFragmentManager().beginTransaction().replace(R.id.content_frame, new HistoriekFragment(), "HistoriekFragment")
                        .addToBackStack("HistoriekFragment")
                        .commit();
                    mDrawerLayout.closeDrawers();


                    break;
              /*  case 2: Intent i = new Intent(getApplicationContext(), InstellingenActivity.class);
                       startActivity(i);
                        mDrawerLayout.closeDrawers();

                    break;

              */

            }

        }
    }

    private void selectItem(int position) {
        Toast.makeText(this, "Laad vraag in op positie " + position, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public void goToVragenFragment(int id) {
        VragenFragment vraag=new VragenFragment();
        vraag.setVraagid(id);
        getFragmentManager().beginTransaction().replace(R.id.content_frame, vraag, "VragenFragment")
                .addToBackStack("VragenFragment")
                .commit();
    }

    @Override
    public void setLastVraag(Integer lastVraag) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (lastVraag != null) {
            editor.putInt("LastVraag", lastVraag);
            editor.commit();
        } else {
            editor.putInt("LastVraag", -1);

            editor.commit();
        }

    }

    public Integer getLastVraag()
    {
        if(sharedpreferences.contains("LastVraag"))
        {
            Integer lastVraag = sharedpreferences.getInt("LastVraag", -1);
            if(lastVraag == -1)
            {
                return null;
            }
            else {
                return lastVraag;
            }
        }
        else return null;
    }

    @Override
    public VragenDossier getVragenDossier(int dossiernr, int vraagid) {
        VragenDossier vragenDossier = null;
        dataDBAdapter.open();
        vragenDossier =dataDBAdapter.getVragenDossierFromCursor(dataDBAdapter.getVragenDossier(dossiernr,vraagid));
        dataDBAdapter.close();
        return vragenDossier;
    }

    @Override
    public void deleteVragenDossier(VragenDossier vragenDossier) {
        dataDBAdapter.open();
        dataDBAdapter.deleteVragenDossier(vragenDossier);
        dataDBAdapter.close();
    }


    @Override
    public void goToKeuzeFragment() {
        KeuzeFragment keuzeFragment = new KeuzeFragment();
        keuzeFragment.setEindFragment(true);
        getFragmentManager().beginTransaction().replace(R.id.content_frame,keuzeFragment , "KeuzeFragment")
                .addToBackStack("KeuzeFragment")
                .commit();
    }

    @Override
    public void goToEindScherm() {
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new EindFragment(), "EindFragment")
                .addToBackStack("EindFragment")
                .commit();
    }



    public void goToInstellingen(Integer lastDossier){
        Intent intent = new Intent(getApplicationContext(), InstellingenActivity.class);
        intent.putExtra("last_fragment","eind");
        intent.putExtra("last_dossier", lastDossier);
        startActivity(intent);
    }



    @Override
    public ArrayList<Reeks> getReeksen() {
        dataDBAdapter.open();
        ArrayList<Reeks> reeksen = null;
        try {
            reeksen = dataDBAdapter.getReeksenFromCursor(dataDBAdapter.getReeksen());
        } catch (ParseException e) {
            Log.d("Activity getReeksen", e.getMessage());
            dataDBAdapter.close();
        }
        dataDBAdapter.close();
        return reeksen;
    }



    @Override
    public Vraag getVraag(int id) {
        dataDBAdapter.open();
        Vraag vraag=new Vraag();
        try {
            Cursor cursor = dataDBAdapter.getVraag(id);
            if(cursor!=null)
            {
                vraag =dataDBAdapter.getVraagFromCursor(cursor);
            }
            else {
                return null;
            }

        } catch (ParseException e) {
            Log.d("Verkeerd formaat datum",e.getMessage());
            dataDBAdapter.close();
        }
        dataDBAdapter.close();
        return vraag;
    }

    @Override
    public ArrayList<AntwoordOptie> getAntwoorden(int vraagid) {
        dataDBAdapter.open();
        ArrayList<AntwoordOptie> antwoordOpties = new ArrayList<>();
        try{
            antwoordOpties=dataDBAdapter.getAntwoordOptiesFromCursor(dataDBAdapter.getAntwoordOptiesVraag(vraagid));
        } catch (ParseException e) {
            Log.d("Verkeerd formaat datum", e.getMessage());
            dataDBAdapter.close();
        }
        dataDBAdapter.close();
        return antwoordOpties;
    }

    @Override
    public void addVragenDossier(VragenDossier vragenDossier) {
        dataDBAdapter.open();
        dataDBAdapter.addVragenDossier(vragenDossier);
        dataDBAdapter.close();
    }

    @Override
    public void updateVragenDossier(int dossiernr, String vraagtekst, VragenDossier vragenDossier) {
        dataDBAdapter.open();
        Log.d("Vraagtekst", vraagtekst);
        Log.d("Antwoordtekst", vragenDossier.getAntwoordTekst());
        dataDBAdapter.updateVragenDossier(dossiernr, vraagtekst, vragenDossier);
        dataDBAdapter.close();
    }

    public void setLastDossier(Integer lastDossier)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (lastDossier != null) {
            editor.putInt("LastDossier", lastDossier);
            editor.commit();
        } else {
            editor.putInt("LastDossier", -1);

            editor.commit();
            Log.d("Activity", "Last Dossier is set");
        }

    }


    @Override
    public Integer getLastDossier() {
        if(sharedpreferences.contains("LastDossier"))
        {
            Integer lastDossier = sharedpreferences.getInt("LastDossier", -1);
            if(lastDossier == -1)
            {
                return null;
            }
            else {
                return lastDossier;
            }
        }
        else return null;
    }

    @Override
    public ArrayList<VragenDossier> getVragenDossiers(int dossiernr) {
        dataDBAdapter.open();
        ArrayList<VragenDossier> vragenDossiers;
        vragenDossiers=dataDBAdapter.getVragenDossiersFromCursor(dataDBAdapter.getVragenDossiers(dossiernr));

        dataDBAdapter.close();
        return vragenDossiers;
    }

    @Override
    public Dossier getDossier(int id) {
        dataDBAdapter.open();
        Dossier dossier = null;
        try {
            dossier = dataDBAdapter.getDossierFromCursor(dataDBAdapter.getDossier(id));
        } catch (ParseException e) {
            Log.d("Activity parse error",e.getMessage());
        }
        dataDBAdapter.close();
        return dossier;
    }

    @Override
    public Integer getLastReeks() {
        return lastReeks;
    }

    @Override
    public void setLastReeks(Integer lastReeks) {
        this.lastReeks = lastReeks;
    }

    @Override
    public void addDosier(Dossier dossier) {
        dataDBAdapter.open();
        setLastDossier((int) dataDBAdapter.addDossier(dossier));
        dataDBAdapter.close();
    }

    @Override
    public void updateDossier(int id, Dossier dossier) {
        dataDBAdapter.open();
        dataDBAdapter.updateDossier(id, dossier);
        dataDBAdapter.close();
    }


    public void setLastPlaats(Integer lastPlaats) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (lastPlaats != null) {
            editor.putInt("LastPlaats", lastPlaats);
            editor.commit();
        } else {
            editor.putInt("LastPlaats", -1);

            editor.commit();

        }
    }

    @Override
    public Integer getLastPlaats() {
        if(sharedpreferences.contains("LastPlaats"))
        {
            Integer lastPlaats = sharedpreferences.getInt("LastPlaats", -1);
            if(lastPlaats == -1)
            {
                Log.d("get lastplaats","null");
                return null;
            }
            else {
                return lastPlaats;
            }
        }
        else {
            Log.d("get lastplaats", "null key not found");
            return null;
        }
    }
    @Override
    public boolean getTips(){
        if(sharedpreferences.contains("Tips"))
        {
            return sharedpreferences.getBoolean("Tips", true);
        }
        else return true;
    }

    @Override
    public boolean getAfbeeldingen(){
        if(sharedpreferences.contains("Afbeeldingen"))
        {
            return sharedpreferences.getBoolean("Afbeeldingen", true);
        }
        else return true;
    }


    @Override
    public Plaats getPlaats(int id) {
        dataDBAdapter.open();
        Plaats plaats = dataDBAdapter.getPlaatsFromCursor(dataDBAdapter.getPlaats(id));
        dataDBAdapter.close();
        return plaats;
    }

    /**
     * deze methode voegt een nieuwe oplossing toe aan een bestaande oplossings
     * wanneer de oplossing op null wordt gezet, wordt deze gereset
     * @param oplossing
     */
    public  void setOplossing ( String oplossing)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(oplossing!=null)
        {

            String vorig = "";
            if (sharedpreferences.contains("Oplossing"))
            {
                vorig = sharedpreferences.getString("Oplossing",null);
            }

            editor.putString("Oplossing",vorig+"\n"+oplossing);

        }
        else {
            Log.d("Oplossing","null");
            editor.putString("Oplossing",null);
        }
        editor.commit();
    }

    public String getOplossing(){
        return sharedpreferences.getString("Oplossing",null);
    }

    @Override
    public String getEmail() {
        if(sharedpreferences.contains("Email"))
        {
            return sharedpreferences.getString("Email","");
        }
        else return null;
    }
}
package be.bostoenapk.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import be.bostoenapk.Fragments.AboutFragment;
import be.bostoenapk.Fragments.HomeFragment;
import be.bostoenapk.Fragments.LoginAdviseurFragment;
import be.bostoenapk.Fragments.LoginKlantFragment;
import be.bostoenapk.Model.AntwoordOptie;
import be.bostoenapk.Model.DataDBAdapter;
import be.bostoenapk.Model.Plaats;
import be.bostoenapk.Model.Reeks;
import be.bostoenapk.Model.Vraag;
import be.bostoenapk.R;
import be.bostoenapk.Services.Service;
import be.bostoenapk.Utilities.CustomDate;


public class LoginActivity extends Activity implements LoginAdviseurFragment.OnFragmentInteractionListener,LoginKlantFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener{

    private final String PREFS_NAME = "COM.BOSTOEN.BE";
    private SharedPreferences sharedpreferences;
    private DataDBAdapter dataDBAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);


        if (savedInstanceState == null) {
            //Controleren of de adviseur zijn gegevens al heeft ingevuld
            if(!sharedpreferences.contains("Voornaam"))
            {
                Log.d("no shared preferences","");
                //inladen LoginAdviseurFragment
                getFragmentManager().beginTransaction().add(R.id.container, new LoginAdviseurFragment(), "AdviseurFragment")
                        .addToBackStack("AdviseurFragment")
                        .commit();
            }
            else
            {
                //inladen van HomeFragment
                getFragmentManager().beginTransaction().add(R.id.container, new HomeFragment(), "HomeFragment")
                        .addToBackStack("HomeFragment")
                        .commit();
                Log.d("Shared preferences", "");
            }

        }

        dataDBAdapter = new DataDBAdapter(getApplicationContext());

        //controleren dat er geen enquete bezig is
        if(getLastVraag()==null && getLastPlaats()==null)
        {
            //ophalen van alle gegevens
            /** if(getLastUpdate()==null)
             {
             sync();
             }
             else {
             //ophalen van nieuwe gegevens sinds laatste update
             sync(getLastUpdate());
             }*/
        }
        else {
            Log.d("Login","bezig met enquete");
        }

        //sync();
        Log.d("oncreate","end");
        //methode om te testen
        addSampleData();


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
    public void goToKlantFragment() {
        getFragmentManager().beginTransaction().replace(R.id.container, new LoginKlantFragment(), "KlantFragment")
                .addToBackStack("KlantFragment")
                .commit();
    }



    @Override
    public void goToAboutFragment() {
        getFragmentManager().beginTransaction().replace(R.id.container, new AboutFragment(), "AboutFragment")
                .addToBackStack("AboutFragment")
                .commit();
    }

    @Override
    public void goToHomeFragment(){
        getFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment(), "HomeFragment")
                .addToBackStack("HomeFragment")
                .commit();
    }

    @Override
    public void goToKeuzeFragment() {
        goEnqueteActivity();
    }


    //fix voor marnix
    //marnix //

    protected void goEnqueteActivity(){
        Intent intent = new Intent(getApplicationContext(), EnqueteActivity.class);
        startActivity(intent);
    }

    public void goToInstellingen(){
        Intent intent = new Intent(getApplicationContext(), InstellingenActivity.class);
        // deze string wordt meegegeven zodat InstellingenActivity weet vanuit welk fragment werd genavigeerd
        intent.putExtra("last_fragment","home");

        startActivity(intent);
    }



    @Override
    public String getVoornaam() {
        if(sharedpreferences.contains("Voornaam"))
        {
            return sharedpreferences.getString("Voornaam","");
        }
        else return null;
    }

    @Override
    public void setVoornaam(String voornaam) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Voornaam", voornaam);
        editor.commit();
        Log.d("Activity", "Voornaam is set");
    }

    @Override
    public String getNaam() {
        if(sharedpreferences.contains("Naam"))
        {
            return sharedpreferences.getString("Naam","");
        }
        else return null;
    }

    @Override
    public void setNaam(String naam) {
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putString("Naam",naam);
        editor.commit();
        Log.d("Activity", "Naam is set");
    }

    @Override
    public String getEmail() {
        if(sharedpreferences.contains("Email"))
        {
            return sharedpreferences.getString("Email","");
        }
        else return null;
    }

    @Override
    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Email",email);
        editor.commit();
        Log.d("Activity", "Email is set");
    }

    @Override
    public Plaats getPlaats(int id) {
        dataDBAdapter.open();
        Plaats plaats = dataDBAdapter.getPlaatsFromCursor(dataDBAdapter.getPlaats(id));
        dataDBAdapter.close();
        return plaats;
    }

    @Override
    public void updatePlaats(int id, Plaats plaats) {
        dataDBAdapter.open();
        dataDBAdapter.updatePlaats(id, plaats);
        dataDBAdapter.close();
    }

    @Override
    public void addPlaats(Plaats plaats) {
        dataDBAdapter.open();
        Integer lastPlaats = new Integer((int) dataDBAdapter.addPlaats(plaats));
        if(lastPlaats!=null)
        {
            setLastPlaats(lastPlaats);
            Log.d("last plaats added",lastPlaats.toString());
        }

        StringBuilder plaatsen= new StringBuilder();
        ArrayList<Plaats> plaatsArrayList = dataDBAdapter.getPlaatsenFromCursor(dataDBAdapter.getPlaatsen());
        for(int i=0;i<plaatsArrayList.size();i++)
        {
            plaatsen.append(i + "\n");
        }
        Log.d("added plaatsen", plaatsen.toString());
        dataDBAdapter.close();
    }

    public void setLastPlaats(Integer lastPlaats) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if (lastPlaats != null) {
            editor.putInt("LastPlaats", lastPlaats);
            editor.commit();
            Log.d("set last plaats",lastPlaats.toString());
        } else {
            editor.putInt("LastPlaats", -1);

            editor.commit();
            Log.d("Activity", "Last plaats is set");
        }
    }

    @Override
    public Integer getLastPlaats() {
        if(sharedpreferences.contains("LastPlaats"))
        {
            Integer lastPlaats = sharedpreferences.getInt("LastPlaats", -1);
            if(lastPlaats == -1)
            {
                return null;
            }
            else {
                return lastPlaats;
            }
        }
        else return null;
    }

    public CustomDate getLastUpdate()
    {
        if(sharedpreferences.contains("LastUpdate"))
        {
            String date = sharedpreferences.getString("LastUpdate", "");
            if(date.equals("") || date!=null)
            {
                try {
                    return new CustomDate(sharedpreferences.getString("LastUpdate",""));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("Getlastupdate","Parse Exception");
                }

                return null;
            }
            else {
                return null;
            }
        }
        else return null;
    }

    public void setLastUpdate(CustomDate date)
    {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(date!=null)
        {
            editor.putString("LastUpdate",date.toString());
        }
        else{
            editor.putString("LastUpdate","");
        }

        editor.commit();
        Log.d("Activity", "Last update is set");
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
    public void goToVraag(int id) {
        Intent intent = new Intent(getApplicationContext(), EnqueteActivity.class);
        //laatste vraag aan inten meegeven
        intent.putExtra("lastVraag",id);
        startActivity(intent);
    }


    public void addSampleData()  {


        dataDBAdapter.open();
        dataDBAdapter.clearAppdata();
        dataDBAdapter.addReeks(new Reeks(null, "Muur", 1, new CustomDate()));
        dataDBAdapter.addReeks(new Reeks(null, "Tuin", 2, new CustomDate()));
        dataDBAdapter.addReeks(new Reeks(null, "Dak", 3, new CustomDate()));

        Bitmap bitone = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.test1);
        Bitmap bittwo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.test2);

        dataDBAdapter.addVraag(new Vraag(null, "Hoe dik is de muur", "meet in cm", bitone, new CustomDate(), 1, true));
        dataDBAdapter.addVraag(new Vraag(null, "Hoe groot is de tuin", "meet in m", bittwo, new CustomDate(), 2, true));
        dataDBAdapter.addVraag(new Vraag(null, "Wat is de helling van het dak", "", null, new CustomDate(), 3, true));


        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(1, "< 10 cm", "", 4, "spouwisolatie", true, new CustomDate()));
        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(1, ">10cm < 30cm", "", 5, "recticel", true, new CustomDate()));
        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(2, "< 30m2", "", null, "onderhoud zelf je tuin", true, new CustomDate()));
        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(2, "> 50m2", "", null, "bel de tuinman", true, new CustomDate()));
        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(3, "<10°", "", null, "het water zal zich opstapelen", true, new CustomDate()));
        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(3, ">15°", "", null, "er is een goede afvoer", true, new CustomDate()));

        dataDBAdapter.addVraag(new Vraag(null, "Welk type baksteen wordt gebruikt ?", "", null, new CustomDate(), 1, true));
        dataDBAdapter.addVraag(new Vraag(null, "Zijn er vochtproblemen ?", null, null, new CustomDate(), 1, true));

        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(4, "thermoblok", "", null, "", true, new CustomDate()));
        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(4, "cyproc", "", null, "U moet het huis beter isoleren", true, new CustomDate()));
        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(5, "ja", "", null, "U moet de hoek afwerken", true, new CustomDate()));
        dataDBAdapter.addAntwoordOptie(new AntwoordOptie(5, "nee", "", null, "", true, new CustomDate()));


        dataDBAdapter.close();

        Log.d("Activity","Sample data");

        /**dataDBAdapter.open();
         Service service = new Service(this);
         try {
         ArrayList<Reeks> reeksen = service.getReeksen();
         if(reeksen!=null)
         {

         for(Reeks reeks : reeksen)
         {
         dataDBAdapter.addReeks(reeks);
         ArrayList<ArrayList> vraagOpties=service.getVragen(reeks.getId());
         if(vraagOpties!=null)
         {
         ArrayList<Vraag> vragen = vraagOpties.get(0);
         ArrayList<AntwoordOptie> antwoordOpties = vraagOpties.get(1);

         if(vragen!=null)
         {
         for(Vraag vraag : vragen)
         {
         dataDBAdapter.addVraag(vraag);
         }
         }

         if(antwoordOpties!=null)
         {
         for(AntwoordOptie antwoordOptie : antwoordOpties)
         {
         Log.d("Antwoordoptie",antwoordOptie.toString());
         dataDBAdapter.addAntwoordOptie(antwoordOptie);
         }
         }
         }

         }

         }

         } catch (ExecutionException e) {
         e.printStackTrace();
         } catch (InterruptedException e) {
         e.printStackTrace();
         } catch (ParseException e) {
         e.printStackTrace();
         }finally {
         dataDBAdapter.close();
         }*/


    }


    public void ClearData() {
        /**
         dataDBAdapter.open();
         dataDBAdapter.clearAll();
         dataDBAdapter.create();
         dataDBAdapter.close();
         */
    }

    public boolean sync()
    {
        Service service = new Service(this);

        if(service.isOnline())
        {
            ArrayList<Reeks> reeksen = null;
            ArrayList<ArrayList> vraagOpties = null;
            ArrayList<Vraag> vragen = null;
            ArrayList<AntwoordOptie> antwoordOpties = null;

            try {
                reeksen = service.getReeksen();

                if(reeksen!=null) {

                    for (Reeks reeks : reeksen) {

                        vraagOpties = service.getVragen(reeks.getId());

                        if (vraagOpties != null) {
                            vragen = vraagOpties.get(0);
                            antwoordOpties = vraagOpties.get(1);

                            if (vragen != null) {
                                for (Vraag vraag : vragen) {

                                }
                            }

                            if (antwoordOpties != null) {
                                for (AntwoordOptie antwoordOptie : antwoordOpties) {
                                    Log.d("Antwoordoptie", antwoordOptie.toString());
                                }
                            }
                        }

                    }
                }
            } catch (ExecutionException|InterruptedException|ParseException e) {
                e.printStackTrace();
                Log.d("Ophalen webservices mislukt",e.getMessage());
                return false;
            }



            if(reeksen!=null && vraagOpties!=null && vragen!=null && antwoordOpties!=null)
            {
                dataDBAdapter.open();

                dataDBAdapter.clearAppdata();

                dataDBAdapter.addReeksen(reeksen);
                dataDBAdapter.addVragen(vragen);
                dataDBAdapter.addAntwoordOpties(antwoordOpties);

                dataDBAdapter.close();

                setLastUpdate(new CustomDate());
            }
            else {
                Log.d("sync","error");
            }


        }

        Log.d("Sync","()");

        return true;
    }

    public boolean sync(CustomDate date)
    {
        Service service = new Service(this);

        if(service.isOnline())
        {
            ArrayList<Reeks> reeksen = null;
            ArrayList<ArrayList> vraagOpties = null;
            ArrayList<Vraag> vragen = null;
            ArrayList<AntwoordOptie> antwoordOpties = null;

            try {
                reeksen = service.getReeksen(date);

                if(reeksen!=null) {

                    for (Reeks reeks : reeksen) {

                        vraagOpties = service.getVragen(reeks.getId());

                        if (vraagOpties != null) {
                            vragen = vraagOpties.get(0);
                            antwoordOpties = vraagOpties.get(1);

                            if (vragen != null) {
                                for (Vraag vraag : vragen) {

                                }
                            }

                            if (antwoordOpties != null) {
                                for (AntwoordOptie antwoordOptie : antwoordOpties) {
                                    Log.d("Antwoordoptie", antwoordOptie.toString());
                                }
                            }
                        }

                    }
                }
            } catch (ExecutionException|InterruptedException|ParseException e) {
                e.printStackTrace();
                Log.d("Ophalen webservices milukt",e.getMessage());
                return false;
            }



            if(reeksen!=null && vraagOpties!=null && vragen!=null && antwoordOpties!=null)
            {
                dataDBAdapter.open();

                dataDBAdapter.clearAppdata();

                dataDBAdapter.addReeksen(reeksen);
                dataDBAdapter.addVragen(vragen);
                dataDBAdapter.addAntwoordOpties(antwoordOpties);

                dataDBAdapter.close();

                setLastUpdate(new CustomDate());
            }
            else {
                Log.d("sync", "error");
            }


        }

        Log.d("Sync","(date)");
        return true;
    }

}



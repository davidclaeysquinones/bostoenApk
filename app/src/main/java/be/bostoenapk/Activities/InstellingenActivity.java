package be.bostoenapk.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import be.bostoenapk.Fragments.Instellingen_1_Fragment;
import be.bostoenapk.Fragments.Instellingen_2_Fragment;
import be.bostoenapk.Fragments.Instellingen_3_Fragment;
import be.bostoenapk.Model.DataDBAdapter;
import be.bostoenapk.Model.Plaats;
import be.bostoenapk.R;
import be.bostoenapk.Utilities.PagerAdapter;


public class InstellingenActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener,
        Instellingen_1_Fragment.OnFragmentInteractionListener, Instellingen_2_Fragment.OnFragmentInteractionListener, Instellingen_3_Fragment.OnFragmentInteractionListener  {



    private TabHost mTabHost;
    private ViewPager mViewPager;
    private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, InstellingenActivity.TabInfo>();
    private PagerAdapter mPagerAdapter;
    private Button gereed;
    private SharedPreferences sharedpreferences;
    private DataDBAdapter dataDBAdapter;
    private static final String PREFS_NAME = "COM.BOSTOEN.BE";


    @Override
    public void setInstellingenOverig(boolean tips, boolean afbeeldingen) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("Tips", tips);
        editor.putBoolean("Afbeeldingen", afbeeldingen);

        editor.commit();
    }

    @Override
    public String getNaam(){
        if(sharedpreferences.contains("Naam"))
        {
            return sharedpreferences.getString("Naam","");
        }
        else return null;
    }

    @Override
    public void setNaam(String naam) {
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putString("Naam", naam);
        editor.commit();
        Log.d("Activity", "Naam is set");
    }

    @Override
    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Email",email);
        editor.commit();
        Log.d("Activity", "Email is set");
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
        editor.putString("Voornaam",voornaam);
        editor.commit();
        Log.d("Activity", "Voornaam is set");
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



    // deze methode navigeert naar het vorig Fragment

    public void goToPrevious(){
        Intent intent = getIntent();
        //string met het laatste fragment
        String last_fragment=intent.getStringExtra("last_fragment");

        switch (last_fragment)
        {
            case "home" :   Intent home = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(home);
                Log.d("Previous","home");
                break;

            case "eind" :   Intent eind = new Intent(getApplicationContext(),EnqueteActivity.class);
                //wordt in EnqueteActivity opgevangen
                eind.putExtra("last_fragment","instellingen");
                startActivity(eind);
                Log.d("previous","eind");
                break;


            default: Log.d("previous","error");
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


    public class TabInfo {
        private String tag;
        private Class<?> clss;
        private Bundle args;
        private Fragment fragment;
        TabInfo(String tag, Class<?> clazz, Bundle args) {
            this.tag = tag;
            this.clss = clazz;
            this.args = args;
        }

    }
    /**
     * A simple factory that returns dummy views to the Tabhost
     * @author mwho
     */
    class TabFactory implements TabHost.TabContentFactory {

        private final Context mContext;

        /**
         * @param context
         */
        public TabFactory(Context context) {
            mContext = context;
        }

        /** (non-Javadoc)
         * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
         */
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_viewpager_layout);

        gereed = (Button) findViewById(R.id.btnGereed1);
        sharedpreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.initialiseTabHost(savedInstanceState);
        if (savedInstanceState != null) {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); //set the tab as per the saved state
        }


        dataDBAdapter = new DataDBAdapter(getApplicationContext());



        gereed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                goToPrevious();
            }
        });

        // Intialise ViewPager
        this.intialiseViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.instellingenmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String fragmentName = item.getIntent().getStringExtra("fragmentName");
        switch (fragmentName) {
            case "instellingen_1":





        }
        return true;
    }
    /** (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
     */
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("tab", mTabHost.getCurrentTabTag()); //save the tab selected
        super.onSaveInstanceState(outState);
    }
//
    /**
     * Initialise ViewPager
     */
    private void intialiseViewPager() {

        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this, Instellingen_1_Fragment.class.getName()));

        if(getLastPlaats() != null){
            fragments.add(Fragment.instantiate(this, Instellingen_2_Fragment.class.getName()));
        }

        fragments.add(Fragment.instantiate(this, Instellingen_3_Fragment.class.getName()));
        this.mPagerAdapter  = new PagerAdapter(super.getSupportFragmentManager(), fragments);
        //
        this.mViewPager = (ViewPager)super.findViewById(R.id.viewpager);
        this.mViewPager.setAdapter(this.mPagerAdapter);
        this.mViewPager.setOnPageChangeListener(this);
    }

    /**
     * Initialise the Tab Host
     */
    private void initialiseTabHost(Bundle args) {
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();
        TabInfo tabInfo = null;
        InstellingenActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("Renovatie adviseur"), (tabInfo = new TabInfo("Tab1", Instellingen_1_Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);
        if(getLastPlaats() !=null) {
            InstellingenActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("Klant"), (tabInfo = new TabInfo("Tab2", Instellingen_2_Fragment.class, args)));
            this.mapTabInfo.put(tabInfo.tag, tabInfo);
        }
        InstellingenActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("Overige"), ( tabInfo = new TabInfo("Tab3", Instellingen_3_Fragment.class, args)));
        this.mapTabInfo.put(tabInfo.tag, tabInfo);

        // Default to first tab
        //this.onTabChanged("Tab1");
        //
        mTabHost.setOnTabChangedListener(this);
    }


    private static void AddTab(InstellingenActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
        // Attach a Tab view factory to the spec
        tabSpec.setContent(activity.new TabFactory(activity));
        tabHost.addTab(tabSpec);
    }

    /** (non-Javadoc)
     * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
     */
    public void onTabChanged(String tag) {
        //TabInfo newTab = this.mapTabInfo.get(tag);
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
     */
    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
     */
    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub
        this.mTabHost.setCurrentTab(position);
    }

    /* (non-Javadoc)
     * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // TODO Auto-generated method stub

    }

}
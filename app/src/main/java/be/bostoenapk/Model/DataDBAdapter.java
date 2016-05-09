package be.bostoenapk.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.util.ArrayList;

import be.bostoenapk.Utilities.CustomDate;

/**
 * Created by david on 2/05/2016.
 */
public class DataDBAdapter {
    /**naam van de database*/
    private static final String DATABASE_NAME = "BOSTOEN_DATABASE.db";

    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;
    public static String TAG = DataDBAdapter.class.getSimpleName();

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**declaratie van alle tabelnamen*/

    private static final String VRAAG_TABLE = "VRAAG";
    private static final String ANTWOORDOPTIE_TABLE = "ANTWOORDOPTIE";
    private static final String VRAGENDOSSIER_TABLE="VRAGENDOSSIER";
    private static final String PLAATS_TABLE="PLAATS";
    private static final String DOSSIER_TABLE="DOSSIER";

    //declaratie van de tabelnaam
    private static final String REEKS_TABLE = "REEKS";

    //declaratie van de kolomnamen
    public static final String REEKS_ID = "id";
    public static final String REEKS_NAAM = "naam";
    public static final String REEKS_LAST_UPDATE = "last_update";
    public static final String REEKS_EERSTE_VRAAG = "eerste_vraag";

    private static final String[] REEKS_FIELDS = new String[] {
            REEKS_ID,
            REEKS_NAAM,
            REEKS_LAST_UPDATE,
            REEKS_EERSTE_VRAAG
    };
    //CREATE script opbouwen volgens correcte SQLite syntax
    private static final String CREATE_TABLE_REEKS =
            "create table " + REEKS_TABLE + "("
                    + REEKS_ID + " INTEGER PRIMARY KEY,"
                    + REEKS_NAAM + " TEXT NOT NULL UNIQUE,"
                    + REEKS_LAST_UPDATE +" TEXT NOT NULL,"
                    + REEKS_EERSTE_VRAAG + " INTEGER"
                    + ");";



    //VRAAG TABLE
    public static final String VRAAG_ID = "id";
    public static final String VRAAG_TEKST = "naam";
    public static final String VRAAG_TIP = "tip";
    public static final String VRAAG_AFBEELDING = "afbeelding";
    public static final String VRAAG_LAST_UPDATE = "last_update";
    public static final String VRAAG_REEKS_ID = "reeks_id";
    public static final String VRAAG_GELDIG = "geldig";

    private static final String[] VRAAG_FIELDS = new String[] {
            VRAAG_ID,
            VRAAG_TEKST,
            VRAAG_TIP,
            VRAAG_AFBEELDING,
            VRAAG_LAST_UPDATE,
            VRAAG_REEKS_ID,
            VRAAG_GELDIG
    };


    //ANTWOORDOPTIE TABLE
    public static final String ANTWOORDOPTIE_ID = "vraag_id";
    public static final String ANTWOORDOPTIE_ANTWOORD_TEKST = "antwoord_tekst";
    public static final String ANTWOORDOPTIE_ANTWOORD_OPMERKING = "antwoord_opmerking";
    public static final String ANTWOORDOPTIE_VOLGENDEVRAAG_ID = "volgendeVraag_id";
    public static final String ANTWOORDOPTIE_OPLOSSING_TEKST = "oplossing_tekst";
    public static final String ANTWOORDOPTIE_GELDIG = "geldig";
    public static final String ANTWOORDOPTIE_LAST_UPDATE = "last_update";

    private static final String[] ANTWOORDOPTIE_FIELDS = new String[] {
            ANTWOORDOPTIE_ID,
            ANTWOORDOPTIE_ANTWOORD_TEKST,
            ANTWOORDOPTIE_ANTWOORD_OPMERKING,
            ANTWOORDOPTIE_VOLGENDEVRAAG_ID,
            ANTWOORDOPTIE_OPLOSSING_TEKST,
            ANTWOORDOPTIE_GELDIG,
            ANTWOORDOPTIE_LAST_UPDATE
    };
    //PLAATS TABLE
    private static final String PLAATS_ID="id";
    private static final String PLAATS_STRAAT="straat";
    private static final String PLAATS_GEMEENTE="gemeente";
    private static final String PLAATS_NR="nummer";
    private static final String PLAATS_CODE="postcode";
    private static final String PLAATS_VOORNAAM="voornaam";
    private static final String PLAATS_NAAM="naam";
    private static final String PLAATS_ISEIGENAAR="isEigenaar";


    private static final String[] PLAATS_FIELDS = new String[] {
            PLAATS_ID,
            PLAATS_STRAAT,
            PLAATS_GEMEENTE,
            PLAATS_NR,
            PLAATS_CODE,
            PLAATS_VOORNAAM,
            PLAATS_NAAM,
            PLAATS_ISEIGENAAR
    };
    //DOSSIER TABLE
    private static final String DOSSIER_ID="id";
    private static final String DOSSIER_PLAATS_ID="plaats_id";
    private static final String DOSSIER_DATUM="datum";
    private static final String DOSSIER_NAAM="naam";

    private static final String[] DOSSIER_FIELDS = new String[] {
            DOSSIER_ID,
            DOSSIER_PLAATS_ID,
            DOSSIER_DATUM,
            DOSSIER_NAAM
    };
    //VRAGENDOSSIER TABLE
    private static final String VRAGENDOSSIER_ID="id";
    private static final String VRAGENDOSSIER_DOSSIER_NR="dossiernr";
    private static final String VRAGENDOSSIER_VRAAG_TEKST="vraag_tekst";
    private static final String VRAGENDOSSIER_ANTWOORD_TEKST="antwoord_tekst";
    private static final String VRAGENDOSSIER_ANTWOORD_OPTIE="antwoord_optie";

    private static final String[] VRAGENDOSSIER_FIELDS = new String[] {
            VRAGENDOSSIER_ID,
            VRAGENDOSSIER_DOSSIER_NR,
            VRAGENDOSSIER_VRAAG_TEKST,
            VRAGENDOSSIER_ANTWOORD_TEKST,
            VRAGENDOSSIER_ANTWOORD_OPTIE
    };

    /**strings met sql statement om tabellen aan te maken*/


    private static final String CREATE_TABLE_VRAAG =
            "create table " + VRAAG_TABLE + "("
                    + VRAAG_ID + " INTEGER PRIMARY KEY,"
                    + VRAAG_TEKST + " TEXT NOT NULL,"
                    + VRAAG_TIP +" TEXT,"
                    + VRAAG_AFBEELDING + " BLOB,"
                    + VRAAG_LAST_UPDATE + " TEXT NOT NULL,"
                    + VRAAG_REEKS_ID + " INTEGER NOT NULL,"
                    + VRAAG_GELDIG + " INTEGER NOT NULL"
                    + ");";


    private static final String CREATE_TABLE_ANTWOORDOPTIE =
            "create table " + ANTWOORDOPTIE_TABLE + "("
                    + ANTWOORDOPTIE_ID+" INTEGER,"
                    + ANTWOORDOPTIE_ANTWOORD_TEKST+" TEXT,"
                    + ANTWOORDOPTIE_ANTWOORD_OPMERKING + " TEXT,"
                    + ANTWOORDOPTIE_VOLGENDEVRAAG_ID + " INTEGER,"
                    + ANTWOORDOPTIE_OPLOSSING_TEKST + " TEXT,"
                    + ANTWOORDOPTIE_GELDIG +" TEXT NOT NULL,"
                    + ANTWOORDOPTIE_LAST_UPDATE + " TEXT,"
                    +"PRIMARY KEY ("+ANTWOORDOPTIE_ID+","+ANTWOORDOPTIE_ANTWOORD_TEKST+")"
                    + ");";

    private static final String CREATE_TABLE_PLAATS=
            "create table " + PLAATS_TABLE +"("
                    + PLAATS_ID +" INTEGER PRIMARY KEY,"
                    + PLAATS_STRAAT +" TEXT,"
                    + PLAATS_NR +" INTEGER,"
                    + PLAATS_GEMEENTE +" TEXT,"
                    + PLAATS_CODE +" INTEGER,"
                    + PLAATS_NAAM + " TEXT,"
                    + PLAATS_VOORNAAM +" TEXT,"
                    + PLAATS_ISEIGENAAR+" INTEGER"+");";
    private static final String CREATE_TABLE_DOSSIER=
            "create table "+ DOSSIER_TABLE+"("
                    + DOSSIER_ID +" INTEGER PRIMARY KEY,"
                    + DOSSIER_PLAATS_ID +" INTEGER,"
                    + DOSSIER_DATUM +" text NOT NULL,"
                    + DOSSIER_NAAM +" text"+");";
    private static final String CREATE_TABLE_VRAGENDOSSIER=
            "create table "+ VRAGENDOSSIER_TABLE+"("
                    + VRAGENDOSSIER_ID+" INTEGER PRIMARY KEY,"
                    + VRAGENDOSSIER_DOSSIER_NR + " INTEGER,"
                    + VRAGENDOSSIER_VRAAG_TEKST + " text,"
                    + VRAGENDOSSIER_ANTWOORD_OPTIE + " INTEGER,"
                    + VRAGENDOSSIER_ANTWOORD_TEKST+" text"+")";


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_REEKS);
            db.execSQL(CREATE_TABLE_ANTWOORDOPTIE);
            db.execSQL(CREATE_TABLE_DOSSIER);
            db.execSQL(CREATE_TABLE_PLAATS);
            db.execSQL(CREATE_TABLE_VRAGENDOSSIER);
            db.execSQL(CREATE_TABLE_VRAAG);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + REEKS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ANTWOORDOPTIE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DOSSIER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PLAATS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + VRAGENDOSSIER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + VRAAG_TABLE);
            onCreate(db);
        }
    }
    public void create()
    {
        mDb.execSQL(CREATE_TABLE_REEKS);
        mDb.execSQL(CREATE_TABLE_ANTWOORDOPTIE);
        Log.d("create", CREATE_TABLE_ANTWOORDOPTIE);
        mDb.execSQL(CREATE_TABLE_DOSSIER);
        mDb.execSQL(CREATE_TABLE_PLAATS);
        mDb.execSQL(CREATE_TABLE_VRAGENDOSSIER);
        mDb.execSQL(CREATE_TABLE_VRAAG);
    }

    public void clearAll()
    {
        mDb.execSQL("DROP TABLE IF EXISTS " + REEKS_TABLE);
        mDb.execSQL("DROP TABLE IF EXISTS " + ANTWOORDOPTIE_TABLE);
        mDb.execSQL("DROP TABLE IF EXISTS " + DOSSIER_TABLE);
        mDb.execSQL("DROP TABLE IF EXISTS " + PLAATS_TABLE);
        mDb.execSQL("DROP TABLE IF EXISTS " + VRAGENDOSSIER_TABLE);
        mDb.execSQL("DROP TABLE IF EXISTS " + VRAAG_TABLE);
    }

    public void clearAppdata() {
        mDb.delete(REEKS_TABLE, null, null);
        mDb.delete(VRAAG_TABLE, null, null);
        mDb.delete(ANTWOORDOPTIE_TABLE, null, null);
    }

    public void clearUserData()
    {
        mDb.delete(PLAATS_TABLE, null, null);
        mDb.delete(VRAGENDOSSIER_TABLE, null, null);
        mDb.delete(VRAAG_TABLE,null,null);
    }

    public DataDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    //PieDbAdapter Open, Close, Upgrade
    public DataDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     *
     */
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    /**
     *
     * @throws SQLException
     */
    public void upgrade() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx); //open
        mDb = mDbHelper.getWritableDatabase();
        mDbHelper.onUpgrade(mDb, 1, 0);
    }


    /**reeks toevoegen aan database
     *
     * @param reeks
     * het id in het opgegeven Reeks object heeft geen belang
     */
    public void addReeks(Reeks reeks)
    {
        ContentValues initialValues=new ContentValues();
        initialValues.put(REEKS_ID,reeks.getId());
        initialValues.put(REEKS_NAAM,reeks.getNaam());
        initialValues.put(REEKS_EERSTE_VRAAG,reeks.getEersteVraag());
        initialValues.put(REEKS_LAST_UPDATE, reeks.getLast_update().toString());

        mDb.insert(REEKS_TABLE, null, initialValues);
    }

    /**
     * een ArrayList met reeksen toevoegen aan de database
     * @param reeksen
     * de opgegeven id's zijn niet van belang
     */
    public void addReeksen(ArrayList<Reeks> reeksen)
    {
        for(Reeks reeks:reeksen)
        {
            addReeks(reeks);

        }
    }

    /**
     *Methode om de gewenste reeks te verwijderen
     * @param reeks
     */
    public void deleteReeks(Reeks reeks)
    {
        mDb.delete(REEKS_TABLE, REEKS_ID + "=" + reeks.getId(), null);
    }

    /**
     *
     * @param reeksen
     */
    public void deleteReeksen(ArrayList<Reeks> reeksen)
    {
        for(Reeks reeks : reeksen)
        {
            deleteReeks(reeks);
        }
    }
    /**
     * @return een Cursor met alle reeksen
     */
    public Cursor getReeksen() {
        //eerste null is de 'where', dan 'selectionArgs' 'GroupBy' 'Having' 'orderBy' en 'limit'
        return mDb.query(REEKS_TABLE, REEKS_FIELDS, null, null, null, null, null);
    }

    /**
     *
     *@param cursor wordt verkregen door getReeksen() op te roepen
     *@return een ArrayList met alle reeksen (null indien de tabel leeg is)
     * @throws ParseException indien het formaat van het veld last_update verkeerd is
     */
    public static ArrayList<Reeks> getReeksenFromCursor(Cursor cursor) throws ParseException {
        if(cursor.getCount()>0)
        {
            ArrayList<Reeks> output = new ArrayList<>();
            while (cursor.moveToNext()) {
                Reeks reeks = new Reeks();
                reeks.setId(cursor.getInt(cursor.getColumnIndex(REEKS_ID)));
                reeks.setEersteVraag(cursor.getInt(cursor.getColumnIndex(REEKS_EERSTE_VRAAG)));
                reeks.setNaam(cursor.getString(cursor.getColumnIndex(REEKS_NAAM)));
                reeks.setLast_update(new CustomDate(cursor.getString(cursor.getColumnIndex(REEKS_LAST_UPDATE))));
                output.add(reeks);
            }

            return output;
        }
        else
        {
            return null;
        }

    }

    /**
     *
     * @param id het id van het gewenste Reeks object
     * @return  een Cursor met de opgegeven reeks
     */
    public Cursor getReeks(int id)
    {
        String[] selectionArgs = {String.valueOf(id)};
        //eerste null is de 'where', dan 'selectionArgs' 'GroupBy' 'Having' 'orderBy' en 'limit'
        return mDb.query(REEKS_TABLE, REEKS_FIELDS,REEKS_ID+"=?",selectionArgs, null, null, null, null);
    }

    /**
     * @param cursor wordt verkregen door getReeks() op te roepen
     * @return het gevraagde Reeks object (indien null werd het niet gevonden)
     * @throws ParseException indien het formaat van het veld last_update verkeerd is
     */
    public Reeks getReeksFromCursor(Cursor cursor) throws ParseException {
        if(cursor.getCount()>0 &&  cursor!=null && cursor.moveToFirst())
        {
            Reeks reeks = new Reeks();
            reeks.setId(cursor.getInt(cursor.getColumnIndex(REEKS_ID)));
            reeks.setEersteVraag(cursor.getInt(cursor.getColumnIndex(REEKS_EERSTE_VRAAG)));
            reeks.setNaam(cursor.getString(cursor.getColumnIndex(REEKS_NAAM)));
            reeks.setLast_update(new CustomDate(cursor.getString(cursor.getColumnIndex(REEKS_LAST_UPDATE))));

            return reeks;
        }
        else
        {
            return null;
        }

    }

    /**
     * Methode om een vraag toe te voegen aan de database
     * @param vraag
     * het opgegeven id is niet van belang
     */
    public void  addVraag(Vraag vraag)
    {
        ContentValues initialValues=new ContentValues();
        initialValues.put(VRAAG_ID,vraag.getId());
        initialValues.put(VRAAG_TEKST,vraag.getTekst());
        initialValues.put(VRAAG_TIP,vraag.getTip());
        initialValues.put(VRAAG_REEKS_ID,vraag.getReeks_id());
        /**boolean naar int*/
        if(vraag.isGeldig())
        {
            initialValues.put(VRAAG_GELDIG, 1);
        }
        else
        {
            initialValues.put(VRAAG_GELDIG, 0);
        }
        initialValues.put(VRAAG_LAST_UPDATE,vraag.getLast_update().toString());

        if(vraag.getImage()!=null)
        {
            /**Bitmap naar BLOB converteren*/
            Bitmap bmp=vraag.getImage();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            initialValues.put(VRAAG_AFBEELDING, byteArray);
        }


        mDb.insert(VRAAG_TABLE, null, initialValues);


    }

    /**
     * @param vragen ArrayList met alle vragen die je wilt toevoegen
     * de opgegeven id's zijn niet van belang
     */
    public void addVragen(ArrayList<Vraag> vragen)
    {
        for(Vraag vraag:vragen)
        {
            addVraag(vraag);
        }
    }

    /**
     * Methode om de gewenste vraag te verwijderen
     * @param vraag
     */
    public void deleteVraag(Vraag vraag)
    {

        mDb.delete(REEKS_TABLE, REEKS_ID + "=" + vraag.getId(), null);

    }

    /**
     * Methode om de opgegeven vragen in een ArrayList te verwijderen
     * @param vragen
     */
    public void deleteVragen(ArrayList<Vraag> vragen)
    {
        for(Vraag vraag : vragen)
        {
            deleteVraag(vraag);
        }
    }

    /**
     * @return een Cursor met alle vragen
     */
    public Cursor getVragen() {
        return mDb.query(VRAAG_TABLE, VRAAG_FIELDS, null, null, null, null, null);
    }

    /**
     * @param cursor wordt verkregen door getVragen op te roepen
     * @return een ArrayList met alle vragen (null indien de tabel leeg is)
     * @throws ParseException indien het formaat van het veld last_update verkeerd is
     */
    public ArrayList<Vraag> getVragenFromCursor(Cursor cursor) throws ParseException {
        if(cursor.getCount()>0)
        {
            ArrayList<Vraag> output = new ArrayList<>();
            while (cursor.moveToNext()) {
                Vraag vraag=new Vraag();
                vraag.setId(cursor.getInt(cursor.getColumnIndex(VRAAG_ID)));
                vraag.setTekst(cursor.getString(cursor.getColumnIndex(VRAAG_TEKST)));
                vraag.setLast_update(new CustomDate(cursor.getString(cursor.getColumnIndex(VRAAG_LAST_UPDATE))));
                vraag.setReeks_id(cursor.getInt(cursor.getColumnIndex(VRAAG_REEKS_ID)));
                vraag.setGeldig(cursor.getInt(cursor.getColumnIndex(VRAAG_GELDIG)) == 1);

                byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(VRAAG_AFBEELDING));
                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                vraag.setImage(image);

                output.add(vraag);
            }
            return output;
        }
        else {
            return null;
        }

    }

    /**
     *
     * @param id het id van het gewenste Vraag object
     * @return  een Cursor met de opgegeven vraag
     */
    public Cursor getVraag(int id)
    {
        String[] selectionArgs = {String.valueOf(id)};
        //eerste null is de 'where', dan 'selectionArgs' 'GroupBy' 'Having' 'orderBy' en 'limit'
        return mDb.query(VRAAG_TABLE, VRAAG_FIELDS,VRAAG_ID+"=?",selectionArgs, null, null, null, null);
    }

    /**
     *
     * @param cursor wordt verkregen door getVraag() op te roepen
     * @return het gevraagde Vraag object (indien null werd het niet gevonden)
     * @throws ParseException indien het formaat van het veld last_update verkeerd is
     */
    public Vraag getVraagFromCursor(Cursor cursor) throws ParseException {
        if(cursor.getCount()>0 &&  cursor!=null && cursor.moveToFirst())
        {
            Vraag vraag=new Vraag();
            vraag.setId(cursor.getInt(cursor.getColumnIndex(VRAAG_ID)));
            vraag.setTekst(cursor.getString(cursor.getColumnIndex(VRAAG_TEKST)));
            vraag.setLast_update(new CustomDate(cursor.getString(cursor.getColumnIndex(VRAAG_LAST_UPDATE))));
            vraag.setReeks_id(cursor.getInt(cursor.getColumnIndex(VRAAG_REEKS_ID)));
            vraag.setGeldig(cursor.getInt(cursor.getColumnIndex(VRAAG_GELDIG)) == 1);
            vraag.setTip(cursor.getString(cursor.getColumnIndex(VRAAG_TIP)));
            byte[] byteArray = cursor.getBlob(cursor.getColumnIndex(VRAAG_AFBEELDING));
            if(byteArray!=null)
            {
                Bitmap image = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                vraag.setImage(image);
            }

            return vraag;
        }
        else
        {
            return null;
        }

    }

    /**
     *methode om een antwoordoptie toe te voegen aan de database
     * @param antwoordOptie
     * het opgegeven id is niet van belang
     */
    public void addAntwoordOptie(AntwoordOptie antwoordOptie)
    {
        ContentValues initialValues=new ContentValues();
        initialValues.put(ANTWOORDOPTIE_ID, antwoordOptie.getVraagId());
        initialValues.put(ANTWOORDOPTIE_ANTWOORD_TEKST, antwoordOptie.getAntwoordTekst());
        initialValues.put(ANTWOORDOPTIE_ANTWOORD_OPMERKING, antwoordOptie.getAntwoordOpmerking());
        initialValues.put(ANTWOORDOPTIE_OPLOSSING_TEKST, antwoordOptie.getOplossing());
        if(antwoordOptie.getVolgendeVraag()!=null)
        {
            initialValues.put(ANTWOORDOPTIE_VOLGENDEVRAAG_ID, antwoordOptie.getVolgendeVraag());
        }
        else {
            initialValues.put(ANTWOORDOPTIE_VOLGENDEVRAAG_ID, -1);
        }

        initialValues.put(ANTWOORDOPTIE_LAST_UPDATE, antwoordOptie.getLast_update().toString());
        if (antwoordOptie.isGeldig())
        {
            initialValues.put(ANTWOORDOPTIE_GELDIG, 1);
        }
        else
        {
            initialValues.put(ANTWOORDOPTIE_GELDIG,0);
        }
        mDb.insert(ANTWOORDOPTIE_TABLE, null, initialValues);
    }

    /**
     *
     * methode om antwoordopties toe te voegen aan de database
     * @param antwoordOpties
     * de opgegeven id's zijn niet van belang
     */
    public void addAntwoordOpties(ArrayList<AntwoordOptie> antwoordOpties)
    {
        for(AntwoordOptie antwoordOptie : antwoordOpties)
        {
            addAntwoordOptie(antwoordOptie);
        }
    }

    /**
     * Methode om alle antwoordopties de bij de opgegeven vraag horen te verwijderen
     * @param vraag
     */
    public void deleteAntwoordOpties(Vraag vraag)
    {
        mDb.delete(ANTWOORDOPTIE_TABLE, ANTWOORDOPTIE_ID + "=" + vraag.getId(), null);
    }

    /**
     * @return een Cursor met alle antwoordopties
     */
    public Cursor getAntwoordOpties()
    {
        return mDb.query(ANTWOORDOPTIE_TABLE, ANTWOORDOPTIE_FIELDS, null, null, null, null, null);
    }

    /**
     *
     * @param cursor wordt verkregen door getAntwoordOpties() op te roepen
     * @return een ArrayList met alle AntwoordOpties (null indien de tabel leeg is)
     * @throws ParseException indien het formaat van het veld last_update verkeerd is
     */
    public ArrayList<AntwoordOptie> getAntwoordOptiesFromCursor(Cursor cursor) throws ParseException {
        if(cursor.getCount()>0)
        {
            ArrayList<AntwoordOptie> output = new ArrayList<>();

            while (cursor.moveToNext())
            {
                AntwoordOptie antwoordOptie = new AntwoordOptie();

                antwoordOptie.setVraagId(cursor.getInt(cursor.getColumnIndex(ANTWOORDOPTIE_ID)));
                antwoordOptie.setAntwoordTekst(cursor.getString(cursor.getColumnIndex(ANTWOORDOPTIE_ANTWOORD_TEKST)));
                antwoordOptie.setAntwoordOpmerking(cursor.getString(cursor.getColumnIndex(ANTWOORDOPTIE_ANTWOORD_OPMERKING)));
                antwoordOptie.setOplossing(cursor.getString(cursor.getColumnIndex(ANTWOORDOPTIE_OPLOSSING_TEKST)));
                if(cursor.getInt(cursor.getColumnIndex(ANTWOORDOPTIE_VOLGENDEVRAAG_ID))!=-1)
                {
                    antwoordOptie.setVolgendeVraag(cursor.getInt(cursor.getColumnIndex(ANTWOORDOPTIE_VOLGENDEVRAAG_ID)));
                }

                antwoordOptie.setGeldig(cursor.getInt(cursor.getColumnIndex(ANTWOORDOPTIE_GELDIG))==1);
                antwoordOptie.setLast_update(new CustomDate(cursor.getString(cursor.getColumnIndex(ANTWOORDOPTIE_LAST_UPDATE))));

                output.add(antwoordOptie);
            }
            return output;
        }
        else {
            return null;
        }

    }
    /**
     *
     * @param vraagid
     * @return een Cursor met de Antwoordopties van de opgegeven vraag
     */
    public Cursor getAntwoordOptiesVraag(int vraagid)
    {
        String[] selectionArgs = {String.valueOf(vraagid)};
        //eerste null is de 'where', dan 'selectionArgs' 'GroupBy' 'Having' 'orderBy' en 'limit'
        return mDb.query(ANTWOORDOPTIE_TABLE, ANTWOORDOPTIE_FIELDS,ANTWOORDOPTIE_ID+"=?",selectionArgs, null, null, null, null);
    }

    /**
     *
     * @param cursor wordt verkregen door  getAntwoordOptiesVraag(int vraagid) op te roepen (indien null werd het niet gevonden)
     * @return de gevraagde AntwoordOpties van de vraag (null indien het niet werd gevonden)
     * @throws ParseException indien het formaat van het veld last_update verkeerd is
     */
    public ArrayList<AntwoordOptie> getAntwoordOptiesVraagFromCursor(Cursor cursor) throws ParseException {
        Log.d("getcount", new Integer(cursor.getCount()).toString());
        if (cursor.getCount() > 0) {
            ArrayList<AntwoordOptie> output = new ArrayList<>();

            while (cursor.moveToNext()) {
                AntwoordOptie antwoordOptie = new AntwoordOptie();

                antwoordOptie.setVraagId(cursor.getInt(cursor.getColumnIndex(ANTWOORDOPTIE_ID)));
                antwoordOptie.setAntwoordTekst(cursor.getString(cursor.getColumnIndex(ANTWOORDOPTIE_ANTWOORD_TEKST)));
                antwoordOptie.setAntwoordOpmerking(cursor.getString(cursor.getColumnIndex(ANTWOORDOPTIE_ANTWOORD_OPMERKING)));
                antwoordOptie.setOplossing(cursor.getString(cursor.getColumnIndex(ANTWOORDOPTIE_OPLOSSING_TEKST)));

                if(cursor.getInt(cursor.getColumnIndex(ANTWOORDOPTIE_VOLGENDEVRAAG_ID))!=-1)
                {
                    antwoordOptie.setVolgendeVraag(cursor.getInt(cursor.getColumnIndex(ANTWOORDOPTIE_VOLGENDEVRAAG_ID)));
                }

                antwoordOptie.setGeldig(cursor.getInt(cursor.getColumnIndex(ANTWOORDOPTIE_GELDIG)) == 1);
                antwoordOptie.setLast_update(new CustomDate(cursor.getString(cursor.getColumnIndex(ANTWOORDOPTIE_LAST_UPDATE))));

                output.add(antwoordOptie);
            }
            return output;
        } else {
            return null;
        }
    }



    /**
     * Voegt de gewenste plaats toe aan de database
     * @param plaats
     * @return id van toegevoegde plaats in de database
     */
    public long addPlaats(Plaats plaats)
    {
        ContentValues initialValues=new ContentValues();
        initialValues.put(PLAATS_ID,plaats.getId());
        initialValues.put(PLAATS_STRAAT,plaats.getStraat());
        if(plaats.getNummer()!=null)
        {
            initialValues.put(PLAATS_NR,plaats.getNummer().toString());
        }
        else {
            initialValues.put(PLAATS_NR,-1);
        }

        initialValues.put(PLAATS_GEMEENTE,plaats.getGemeente());

        if(plaats.getPostcode()!=null)
        {
            initialValues.put(PLAATS_CODE,plaats.getPostcode());
        }
        else {
            initialValues.put(PLAATS_CODE,-1);
        }

        initialValues.put(PLAATS_VOORNAAM, plaats.getVoornaam());
        initialValues.put(PLAATS_NAAM, plaats.getNaam());
        if(plaats.isEigenaar())
        {
            initialValues.put(PLAATS_ISEIGENAAR,1);
        }
        else
        {
            initialValues.put(PLAATS_ISEIGENAAR,0);
        }

        return mDb.insert(PLAATS_TABLE, null, initialValues);
    }

    public void updatePlaats(int id,Plaats plaats)
    {
        ContentValues initialValues=new ContentValues();
        initialValues.put(PLAATS_STRAAT,plaats.getStraat());
        if(plaats.getNummer()!=null)
        {
            initialValues.put(PLAATS_NR,plaats.getNummer().toString());
        }
        else {
            initialValues.put(PLAATS_NR,-1);
        }

        initialValues.put(PLAATS_GEMEENTE,plaats.getGemeente());

        if(plaats.getPostcode()!=null)
        {
            initialValues.put(PLAATS_CODE,plaats.getPostcode());
        }
        else {
            initialValues.put(PLAATS_CODE,-1);
        }

        initialValues.put(PLAATS_VOORNAAM, plaats.getVoornaam());
        initialValues.put(PLAATS_NAAM, plaats.getNaam());
        if(plaats.isEigenaar())
        {
            initialValues.put(PLAATS_ISEIGENAAR,1);
        }
        else
        {
            initialValues.put(PLAATS_ISEIGENAAR,0);
        }

        String[] selectionArgs = {new Integer(id).toString()};

        mDb.update(PLAATS_TABLE, initialValues, "id = ?", selectionArgs);

    }

    /**
     * methode om de gewenste plaats te verwijderen
     * @param plaats
     */
    public void deletePlaats(Plaats plaats) {
        mDb.delete(PLAATS_TABLE, PLAATS_ID + "=" + plaats.getId(), null);
    }

    /**
     * methode om alle plaatsen in de opgegeven ArrayLIst te verwijderen
     * @param plaatsen
     */
    public void deletePlaatsen(ArrayList<Plaats> plaatsen)
    {
        for(Plaats plaats : plaatsen)
        {
            deletePlaats(plaats);
        }
    }

    /**
     *
     * @param id het id van de gewenste plaats
     * @return een Cursor met het gevraagd Plaats object
     */
    public Cursor getPlaats(int id)
    {
        String[] selectionArgs = {String.valueOf(id)};
        //eerste null is de 'where', dan 'selectionArgs' 'GroupBy' 'Having' 'orderBy' en 'limit'
        return mDb.query(PLAATS_TABLE, PLAATS_FIELDS,PLAATS_ID+"=?",selectionArgs, null, null, null, null);
    }


    /**
     *
     * @return
     */
    public Cursor getPlaatsen()
    {
        //eerste null is de 'where', dan 'selectionArgs' 'GroupBy' 'Having' 'orderBy' en 'limit'
        return mDb.query(PLAATS_TABLE, PLAATS_FIELDS,null,null, null, null, null, null);
    }
    /**
     *
     *  @param cursor wordt verkregen door getPlaats() op te roepen
     * @return het gevraagde Plaats object (null indien het object niet werd gevonden)
     */
    public Plaats getPlaatsFromCursor(Cursor cursor)
    {
        if(cursor.getCount()>0 &&  cursor!=null && cursor.moveToFirst()) {
            Plaats plaats = new Plaats();
            plaats.setId(cursor.getInt(cursor.getColumnIndex(PLAATS_ID)));
            plaats.setStraat(cursor.getString(cursor.getColumnIndex(PLAATS_STRAAT)));
            if(cursor.getInt(cursor.getColumnIndex(PLAATS_NR))!=-1)
            {
                plaats.setNummer(cursor.getInt(cursor.getColumnIndex(PLAATS_NR)));
            }
            else {
                plaats.setNummer(null);
            }

            plaats.setGemeente(cursor.getString(cursor.getColumnIndex(PLAATS_GEMEENTE)));

            if(cursor.getInt(cursor.getColumnIndex(PLAATS_CODE))!=-1)
            {

                plaats.setPostcode(cursor.getInt(cursor.getColumnIndex(PLAATS_CODE)));
            }
            else {
                plaats.setPostcode(null);
            }

            plaats.setVoornaam(cursor.getString(cursor.getColumnIndex(PLAATS_VOORNAAM)));
            plaats.setNaam(cursor.getString(cursor.getColumnIndex(PLAATS_NAAM)));
            plaats.setIsEigenaar(cursor.getInt(cursor.getColumnIndex(PLAATS_ISEIGENAAR))==1);

            return plaats;
        }
        else
        {
            return null;
        }

    }

    public ArrayList<Plaats> getPlaatsenFromCursor(Cursor cursor)
    {
        if (cursor.getCount() > 0) {
            ArrayList<Plaats> output = new ArrayList<>();

            while (cursor.moveToNext()) {
                Plaats plaats = new Plaats();

                plaats.setId(cursor.getInt(cursor.getColumnIndex(PLAATS_ID)));
                plaats.setStraat(cursor.getString(cursor.getColumnIndex(PLAATS_STRAAT)));
                if(cursor.getInt(cursor.getColumnIndex(PLAATS_NR))!=-1)
                {
                    plaats.setNummer(cursor.getInt(cursor.getColumnIndex(PLAATS_NR)));
                }
                else {
                    plaats.setNummer(null);
                }

                plaats.setGemeente(cursor.getString(cursor.getColumnIndex(PLAATS_GEMEENTE)));

                if(cursor.getInt(cursor.getColumnIndex(PLAATS_CODE))!=-1)
                {

                    plaats.setPostcode(cursor.getInt(cursor.getColumnIndex(PLAATS_CODE)));
                }
                else {
                    plaats.setPostcode(null);
                }

                plaats.setVoornaam(cursor.getString(cursor.getColumnIndex(PLAATS_VOORNAAM)));
                plaats.setNaam(cursor.getString(cursor.getColumnIndex(PLAATS_NAAM)));
                plaats.setIsEigenaar(cursor.getInt(cursor.getColumnIndex(PLAATS_ISEIGENAAR))==1);

                output.add(plaats);
            }
            return output;
        } else {
            return null;
        }
    }


    /**
     * Voegt het gewenste dossier toe aan de database
     * @param dossier
     */
    public long addDossier(Dossier dossier)
    {
        ContentValues initialValues=new ContentValues();
        initialValues.put(DOSSIER_ID,dossier.getId());
        initialValues.put(DOSSIER_NAAM,dossier.getNaam());
        initialValues.put(DOSSIER_PLAATS_ID,dossier.getPlaatsId());
        initialValues.put(DOSSIER_DATUM, dossier.getDatum().toString());

        return mDb.insert(DOSSIER_TABLE, null, initialValues);
    }

    /**
     * Verwijdert het gewenste dossier object
     * @param dossier
     */
    public void deleteDossier(Dossier dossier) {
        mDb.delete(DOSSIER_TABLE, DOSSIER_FIELDS + "=" + dossier.getId(), null);
    }

    public void updateDossier(int id,Dossier dossier)
    {
        ContentValues initialValues=new ContentValues();

        initialValues.put(DOSSIER_NAAM,dossier.getNaam());
        initialValues.put(DOSSIER_PLAATS_ID,dossier.getPlaatsId());
        if(dossier.getDatum()!=null)
        {
            initialValues.put(DOSSIER_DATUM,dossier.getDatum().toString());
        }
        String[] selectionArgs = {new Integer(id).toString()};

        mDb.update(DOSSIER_TABLE, initialValues, "id = ?", selectionArgs);
    }

    /**
     * Verwijdert de dossiers in de opgegeven ArrayList
     * @param dossiers
     */
    public void deleteDossiers(ArrayList<Dossier> dossiers)
    {
        for(Dossier dossier : dossiers)
        {
            deleteDossier(dossier);
        }
    }

    /**
     *
     * @param id het id van het gewenste Dossier
     * @return een Cursor met het gewenste Dossier object
     */
    public Cursor getDossier(int id)
    {
        String[] selectionArgs = {String.valueOf(id)};
        //eerste null is de 'where', dan 'selectionArgs' 'GroupBy' 'Having' 'orderBy' en 'limit'
        return mDb.query(DOSSIER_TABLE, DOSSIER_FIELDS,DOSSIER_ID+"=?",selectionArgs, null, null, null, null);
    }

    /**
     *
     * @param cursor wordt verkregen door getDossier() op te roepen
     * @return het gevraagde Dossier object (null indien het object niet werd gevonden)
     * @throws ParseException indien het formaat van het veld last_update verkeerd is
     */
    public Dossier getDossierFromCursor(Cursor cursor) throws ParseException {

        if(cursor.getCount()>0 &&  cursor!=null && cursor.moveToFirst())
        {
            Dossier dossier = new Dossier();
            dossier.setId(cursor.getInt(cursor.getColumnIndex(DOSSIER_ID)));
            dossier.setNaam(cursor.getString(cursor.getColumnIndex(DOSSIER_NAAM)));
            dossier.setPlaatsId(cursor.getInt(cursor.getColumnIndex(DOSSIER_PLAATS_ID)));
            dossier.setDatum(new CustomDate(cursor.getString(cursor.getColumnIndex(DOSSIER_DATUM))));

            return dossier;
        }
        else
        {
            return null;
        }

    }

    /**
     * Methode om een vragenDossier aan de database toe te voegen
     * @param vragenDossier
     */
    public void addVragenDossier(VragenDossier vragenDossier )
    {
        ContentValues initialValues=new ContentValues();
        initialValues.put(VRAGENDOSSIER_DOSSIER_NR,vragenDossier.getDossierNr());
        initialValues.put(VRAGENDOSSIER_VRAAG_TEKST,vragenDossier.getVraagTekst());
        initialValues.put(VRAGENDOSSIER_ANTWOORD_TEKST,vragenDossier.getAntwoordTekst());
        initialValues.put(VRAGENDOSSIER_ANTWOORD_OPTIE,vragenDossier.getAntwoordOptie());
        mDb.insert(VRAGENDOSSIER_TABLE, null, initialValues);
    }

    /**
     * Methode die een ArrayList met vragenDossiers aan de database toevoegt
     * @param vragenDossiers
     */
    public void addVragenDossiers(ArrayList<VragenDossier> vragenDossiers)
    {
        for(VragenDossier vragenDossier : vragenDossiers)
        {
            addVragenDossier(vragenDossier);
        }
    }

    public void updateVragenDossier(int dossiernr,String vraagtekst,VragenDossier vragenDossier)
    {
        ContentValues initialValues=new ContentValues();

        initialValues.put(VRAGENDOSSIER_ANTWOORD_TEKST,vragenDossier.getAntwoordTekst());
        String selectionArgs [] ={new Integer(vragenDossier.getDossierNr()).toString(),vragenDossier.getVraagTekst()};
        mDb.update(VRAGENDOSSIER_TABLE,initialValues, VRAGENDOSSIER_DOSSIER_NR+"= ? AND "+VRAGENDOSSIER_VRAAG_TEKST+"= ?",selectionArgs);
    }

    /**
     *
     * @param dossiernr het id van het dossier waarvan je alle VragenDossiers wilt
     * @return
     */
    public Cursor getVragenDossiers(int dossiernr)
    {
        String[] selectionArgs = {String.valueOf(dossiernr)};
        //eerste null is de 'where', dan 'selectionArgs' 'GroupBy' 'Having' 'orderBy' en 'limit'
        return mDb.query(VRAGENDOSSIER_TABLE, VRAGENDOSSIER_FIELDS,VRAGENDOSSIER_DOSSIER_NR+"=?",selectionArgs, null, null, null, null);
    }

    public Cursor getVragenDossier(int dossiernr,int vraagdId)
    {
        String[] selectionArgs = {String.valueOf(dossiernr),String.valueOf(vraagdId)};
        //eerste null is de 'where', dan 'selectionArgs' 'GroupBy' 'Having' 'orderBy' en 'limit'
        return mDb.query(VRAGENDOSSIER_TABLE, VRAGENDOSSIER_FIELDS,VRAGENDOSSIER_DOSSIER_NR+"=? AND "+VRAGENDOSSIER_ANTWOORD_OPTIE+"=?",selectionArgs, null, null, null, null);
    }

    public VragenDossier getVragenDossierFromCursor(Cursor cursor)
    {
        if(cursor.getCount()>0 &&  cursor!=null && cursor.moveToFirst()){
            VragenDossier vragenDossier = new VragenDossier();

            vragenDossier.setDossierNr(cursor.getInt(cursor.getColumnIndex(VRAGENDOSSIER_DOSSIER_NR)));
            vragenDossier.setAntwoordTekst(cursor.getString(cursor.getColumnIndex(VRAGENDOSSIER_ANTWOORD_TEKST)));
            vragenDossier.setVraagTekst(cursor.getString(cursor.getColumnIndex(VRAGENDOSSIER_VRAAG_TEKST)));
            vragenDossier.setAntwoordOptie(cursor.getInt(cursor.getColumnIndex(VRAGENDOSSIER_ANTWOORD_OPTIE)));

            return vragenDossier;
        }else {
            return null;
        }


    }

    /**
     *
     * @param cursor wordt verkregen door getVragenDossiers() op te roepen
     * @return de gevraagd VragenDossiers in een ArrayList (null indien het niet werd gevonden)
     */
    public ArrayList<VragenDossier> getVragenDossiersFromCursor(Cursor cursor)
    {
        if (cursor.getCount() > 0) {
            ArrayList<VragenDossier> output = new ArrayList<>();

            while (cursor.moveToNext()) {
                VragenDossier vragenDossier = new VragenDossier();

                vragenDossier.setDossierNr(cursor.getInt(cursor.getColumnIndex(VRAGENDOSSIER_DOSSIER_NR)));
                vragenDossier.setAntwoordTekst(cursor.getString(cursor.getColumnIndex(VRAGENDOSSIER_ANTWOORD_TEKST)));
                vragenDossier.setVraagTekst(cursor.getString(cursor.getColumnIndex(VRAGENDOSSIER_VRAAG_TEKST)));
                vragenDossier.setAntwoordOptie(cursor.getInt(cursor.getColumnIndex(VRAGENDOSSIER_ANTWOORD_OPTIE)));

                output.add(vragenDossier);
            }
            return output;
        }
        else
        {
            return null;
        }
    }

    public void deleteVragenDossier(VragenDossier vragenDossier)
    {
        String[]whereArgs = {String.valueOf(vragenDossier.getDossierNr()),String.valueOf(vragenDossier.getAntwoordOptie())};
        mDb.delete(VRAGENDOSSIER_TABLE, VRAGENDOSSIER_DOSSIER_NR+"= ? AND "+VRAGENDOSSIER_ANTWOORD_OPTIE+"=?", whereArgs);
    }
}
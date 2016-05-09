package be.bostoenapk.Services;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import be.bostoenapk.Model.AntwoordOptie;
import be.bostoenapk.Model.Reeks;
import be.bostoenapk.Model.Vraag;
import be.bostoenapk.R;
import be.bostoenapk.Utilities.CustomDate;

/**
 * Created by david on 2/05/2016.
 */
public class Service {
    private Activity activity;
    private static final String PREFS_NAME = "COM.BOSTOEN.BE";
    private SharedPreferences sharedpreferences;

    public Service(Activity activity) {
        this.activity = activity;
    }


    public boolean userExist() throws ExecutionException, InterruptedException {
        String email = getUseremail();
        if(isOnline())
        {
            if(email!=null)
            {
                Call call = new Call();
                JSONObject response =call.execute("http://bostoen.info/api/scorecard/user/read","email" ,email).get();
                if(response!=null){
                    Log.d("response", response.toString());
                    return  response.optInt("id")!=0;
                }
                else {
                    Log.d("response","null");
                }
                return false;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }

    }

    public ArrayList<Reeks> getReeksen() throws ExecutionException, InterruptedException, ParseException {
        ArrayList<Reeks> reeksen = null;
        if(isOnline())
        {
            Call call = new Call();
            JSONObject response = call.execute("http://bostoen.info/api/scorecard/serie/read","timestamp","").get();
            if(response!=null)
            {
                JSONArray responseArray = response.optJSONArray("reeks");

                if(responseArray!=null)
                {
                    reeksen = new ArrayList<>();

                    for(int i =0;i<responseArray.length();i++)
                    {
                        JSONObject huidig = responseArray.optJSONObject(i);
                        Reeks reeks = new Reeks();
                        reeks.setId(huidig.optInt("id"));
                        reeks.setNaam(huidig.optString("naam"));
                        reeks.setEersteVraag(huidig.optInt("eerste_vraag"));
                        reeks.setLast_update(new CustomDate(huidig.optString("last_update").replace(" ","-")));
                        reeksen.add(reeks);;
                    }
                }

            }
            else {
                Log.d("response","null");
            }
        }


        return reeksen;
    }

    public ArrayList<Reeks> getReeksen(CustomDate date) throws ParseException, ExecutionException, InterruptedException {
        ArrayList<Reeks> reeksen = null;

        if(isOnline())
        {
            Call call = new Call();
            JSONObject response = call.execute("http://bostoen.info/api/scorecard/serie/read","timestamp",date.toString()).get();
            if(response!=null)
            {
                JSONArray responseArray = response.optJSONArray("reeks");
                if(responseArray!=null)
                {
                    reeksen = new ArrayList<>();

                    for(int i =0;i<responseArray.length();i++)
                    {
                        JSONObject huidig = responseArray.optJSONObject(i);
                        Reeks reeks = new Reeks();
                        reeks.setId(huidig.optInt("id"));
                        reeks.setNaam(huidig.optString("naam"));
                        reeks.setEersteVraag(huidig.optInt("eerste_vraag"));
                        reeks.setLast_update(new CustomDate(huidig.optString("last_update").replace(" ", "-")));
                        reeksen.add(reeks);
                    }
                }

            }
            else {
                Log.d("response","null");
            }
        }


        return reeksen;
    }
    public boolean isOnline()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(activity.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return ((networkInfo != null) && networkInfo.isConnected());
    }

    /**
     *
     * @param reeksid
     * @return ArrayList met twee ArrayList, het eerste element is een ArrayList met alle vragen, het tweede elemente is een ArrayList met alle antwoordopties
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws ParseException
     */
    public ArrayList<ArrayList>getVragen(int reeksid) throws ExecutionException, InterruptedException, ParseException {
        ArrayList output = null;


        if(isOnline())
        {
            Call call = new Call();
            JSONObject response = call.execute("http://bostoen.info/api/scorecard/question/read", "reeks_id", new Integer(reeksid).toString()).get();

            if(response!=null)
            {
                output = new ArrayList();
                JSONArray responseReeksArray = response.optJSONArray("reeks");
                if(responseReeksArray!=null)
                {
                    Log.d("array length",responseReeksArray.length()+"");
                    ArrayList<Vraag> vragen  = new ArrayList<>();
                    for(int i = 0;i<responseReeksArray.length();i++)
                    {
                        JSONObject current = responseReeksArray.optJSONObject(i);
                        Log.d("current",current.toString());

                        Log.d("geldig", new Integer(current.optInt("geldig")).toString() + " id :" + i);
                        Vraag vraag = new Vraag();
                        vraag.setId(current.optInt("id"));
                        vraag.setTekst(current.optString("tekst"));
                        vraag.setTip(current.optString("tip"));
                        if(current.optString("afbeelding")==null ||current.optString("afbeelding").equals("null"))
                        {
                            vraag.setImage(null);
                        }
                        vraag.setLast_update(new CustomDate(current.optString("last_update").replace(" ","-")));
                        vraag.setReeks_id(reeksid);
                        vraag.setGeldig(current.optInt("geldig") == 1);

                        vragen.add(vraag);

                    }
                    output.add(vragen);
                }
                JSONArray responseAntwoordOptieArray = response.optJSONArray("antwoordoptie");

                if(responseAntwoordOptieArray!=null)
                {
                    ArrayList<AntwoordOptie> antwoordOpties = new ArrayList<>();
                    for(int i=0;i<responseAntwoordOptieArray.length();i++)
                    {
                        JSONObject current = responseAntwoordOptieArray.optJSONObject(i);
                        AntwoordOptie antwoordOptie = new AntwoordOptie();
                        antwoordOptie.setVraagId(current.optInt("vraag_id"));
                        antwoordOptie.setAntwoordTekst(current.optString("antwoord_tekst"));
                        antwoordOptie.setAntwoordOpmerking(current.optString("antwoord_opmerking"));
                        antwoordOptie.setOplossing(current.optString("oplossings_tekst"));
                        Log.d("volgende vraag service",new Integer(current.optInt("volgendevraag_id")).toString());
                        antwoordOptie.setVolgendeVraag(current.optInt("volgendevraag_id"));
                        antwoordOptie.setGeldig(current.optInt("geldig") == 1);
                        antwoordOptie.setLast_update(new CustomDate(current.optString("last_update").replace(" ","-")));
                        Log.d("antwoordoptie",antwoordOptie.toString());
                        antwoordOpties.add(antwoordOptie);

                    }
                    output.add(antwoordOpties);
                }


            }

        }

        return output;
    }

    private String getSecret(Date date) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String plaintext = date.toString() + activity.getString(R.string.private_key);
        messageDigest.update(plaintext.getBytes());

        String output =  new BigInteger(1, messageDigest.digest()).toString(16);
        Log.d("datestring",date.toString());
        Log.d("private key",activity.getString(R.string.private_key));
        Log.d("encoded", output);
        return output;
    }

    private String getUseremail()
    {
        if(sharedpreferences == null)
        {
            sharedpreferences = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        if(sharedpreferences.contains("Email"))
        {
            return sharedpreferences.getString("Email","");
        }
        else return null;
    }

    private class Call extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject = null;
            if(isOnline())
            {
                HttpClient httpclient = new DefaultHttpClient();

                String url = params[0];
                Log.d("url",url);
                String parameterName = params[1];
                String data = params[2];
                Log.d("data",data);
                HttpPost httppost = new HttpPost(url);


                try {
                    // Add your data
                    Date date = new CustomDate();
                    List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                    nameValuePairs.add(new BasicNameValuePair("auth", activity.getString(R.string.public_key)));
                    nameValuePairs.add(new BasicNameValuePair("secret", getSecret(date)));
                    nameValuePairs.add(new BasicNameValuePair("time", date.toString()));

                    if(parameterName!=null)
                    {
                        nameValuePairs.add(new BasicNameValuePair("data["+parameterName+"]", data));
                    }
                    else {
                        nameValuePairs.add(new BasicNameValuePair("data",data));
                    }

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    Log.d("encoded entity", new UrlEncodedFormEntity(nameValuePairs).toString());
                    Log.d("httpclient",httpclient.getParams().toString());

                    ResponseHandler<String> responseHandler=new BasicResponseHandler();
                    // Execute HTTP Post Request
                    String responseString = httpclient.execute(httppost,responseHandler);

                    Log.d("responseString",responseString);
                    jsonObject = new JSONObject(responseString);

                } catch (ClientProtocolException e) {
                    Log.d("ClientProtocolException",e.getMessage());
                } catch (IOException e) {
                    Log.d("IOException",e.getMessage());

                } catch (JSONException e) {
                    Log.d("JSONException", e.getMessage());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }

            return jsonObject;
        }
    }


}
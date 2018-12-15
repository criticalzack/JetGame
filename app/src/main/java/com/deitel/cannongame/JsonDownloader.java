package com.deitel.cannongame;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//This code was taken from this Stack Overflow posting:
//https://stackoverflow.com/questions/33229869/get-json-data-from-url-using-android
public class JsonDownloader extends AsyncTask<String, String, String> {

    private Context context;
    //if you change where this class gets created, you'll need to change this variable to be of the same type of the class you are located when JsonDownloader is created
    private MainActivity activity;
    private ProgressDialog pd;

    //also need to change the second parameter in the constructor to be of the same type as above
    public JsonDownloader(Context con, MainActivity main)
    {
        this.context = con;
        this.activity = main;

    }
    protected void onPreExecute() {
        super.onPreExecute();

        pd = new ProgressDialog(context);
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();
    }

    protected String doInBackground(String... params) {


        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();


            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                Log.d("Response: ", "> " + line);   //here u ll get whole response...... :-)

            }

            //delete the square brackets because they won't convert to Json objects
            buffer.deleteCharAt(0);
            buffer.deleteCharAt(buffer.length() - 1);

            return buffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (pd.isShowing()){
            pd.dismiss();
        }

        //because the result is an array of Json objects, we need to split them up into separate Json strings
        //a plain comma can't be used as a delimiter because there are commas between the elements within the Json strings
        //so as a workaround, delimit by a comma followed by a forward curly brace
        String[] jsonStrings = result.split(",\\{");

        //and then, you need to add the curly brace back in:
        if(jsonStrings.length > 1) {
            for (int j = 1; j < jsonStrings.length; j++)
                jsonStrings[j] = "{" + jsonStrings[j];
        }
        JSONObject[] jsonObjects = new JSONObject[jsonStrings.length];
        int i = 0;
        for(String json : jsonStrings) {
            try {
                jsonObjects[i] = new JSONObject(json);
                i++;
            } catch (JSONException e) {

            }
        }

        activity.setJsonObjects(jsonObjects);
    }
}



package com.smartnote.rishabh_pc.smartnote;

import android.os.AsyncTask;;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Objects;

/**
 * Created by rishabh-pc on 3/21/2015.
 */
public class getCloudData extends AsyncTask<MainActivity,Integer,MainActivity>{
    String result="";
    HttpURLConnection conn;

    protected MainActivity doInBackground(MainActivity... ma) {
            //Log.d("hi", "" + R.id.recom1);
            URL url = null;
            String data="";

            try {
                url = new URL("http://54.152.214.89:1337");

                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                result=conn.getResponseMessage();
         //       ma.note.setText(ma.note.getText()+" "+conn.getHeaderFields());
                Log.d("Request",""+conn.getHeaderFields());
                Log.d("Response ::",""+conn.getResponseMessage());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return ma[0];

    }


    protected void onPostExecute(MainActivity ma) {
        super.onPostExecute(ma);

        try {
            Log.d("Response post execute::",""+conn.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ma.note.setText(ma.note.getText()+" "+conn.getHeaderFields());
    }
}

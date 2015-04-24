package com.smartnote.rishabh_pc.smartnote;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.smartnote.rishabh_pc.smartnote.R.layout.activity_main;

/**
 * Created by rishabh-pc on 3/25/2015.
 */
public class postCloudData extends AsyncTask<MainActivity , MainActivity, MainActivity> {


    String result = "";
    HttpURLConnection conn;

    MainActivity m = null;
 //   JSONObject returnObject;


    protected MainActivity doInBackground(MainActivity... mainActivities) {
        URL url = null;
        String data = "";
            try {

                url = new URL("http://52.1.219.41:1337");

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoInput(true);
                conn.connect();
                conn.getOutputStream().write(mainActivities[0].notes.toString().getBytes());

                result = conn.getResponseMessage();
              //  Log.d("Response",""+ conn.getResponseCode());

                InputStream inputStream = new BufferedInputStream(conn.getInputStream());
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String recommendation;
                while ((recommendation = bufferedReader.readLine()) != null) {
                    sb.append(recommendation);
                }
                try {
                    mainActivities[0].returnObject = new JSONObject(sb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.d("Response", mainActivities[0].returnObject.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        return mainActivities[0];
    }

    protected void onPostExecute(final MainActivity mainActivity) {
        super.onPostExecute(mainActivity);

        String options = null;
        String name;
        mainActivity.buttonlayout.removeAllViews();
        m = mainActivity;

        for (int i = 1; i <= mainActivity.returnObject.length(); i++) {
            name = "recom" + i;
            mainActivity.recom = new Button(mainActivity);
            mainActivity.buttonlayout.addView(mainActivity.recom);
            try {
                options = mainActivity.returnObject.getString(name);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            mainActivity.recom.setId(i);
            mainActivity.recom.setText(options);
            mainActivity.recom.setVisibility(View.VISIBLE);
            mainActivity.recom.setOnClickListener(addTag);
            Log.d("Response post execute::", "" + options);
        }
    }


    View.OnClickListener addTag = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Event:", "Adding tag");
            Button b = (Button) v;
            m.buttnCount++;
            m.Tags.add(b);
            b.setId(m.buttnCount);
            b.setText(b.getText());

            // mainActivity.search.setText(mainActivity.search.getText()+" "+b.getText());
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

            b.setLayoutParams(layoutParams);
            m.buttonlayout.removeView(b);
            m.textbuttonrelative.addView(b);
            b.setOnClickListener(m.delTag);
            //Delete the clicked button
            Log.d("Button id", b.toString());
        }
    };
}

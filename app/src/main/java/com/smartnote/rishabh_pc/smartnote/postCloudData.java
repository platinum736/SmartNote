package com.smartnote.rishabh_pc.smartnote;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Iterator;

import static com.smartnote.rishabh_pc.smartnote.R.layout.activity_main;

/**
 * Created by rishabh-pc on 3/25/2015.
 */
public class postCloudData extends AsyncTask<MainActivity , MainActivity, MainActivity> {


    String result = "";
    HttpURLConnection conn;

    JSONObject notes = new JSONObject();
    JSONObject returnObject = null;
    MainActivity m = null;


    protected MainActivity doInBackground(MainActivity... mainActivities) {
        URL url = null;
        String data = "";
        String tag;

        for (Button b : mainActivities[0].Tags) {
            try {
                tag = b.getText().toString();
                notes.accumulate("name", b.getText());
                Log.d("Tags:", b.getText().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            notes.accumulate("name", mainActivities[0].search.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
       /* int start = 0, end = 0;

        for (int i = 0; i < mainActivities[0].search.getText().length(); i++) {
            if (mainActivities[0].search.getText().charAt(i) != ' ') {
                //   notes.accumulate("name",note.getText().subSequence(start,i));
            } else if (mainActivities[0].search.getText().charAt(i) == ' ') {
                end = i;
                try {
                    notes.accumulate("name", mainActivities[0].search.getText().subSequence(start, end));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ArrayIndexOutOfBoundsException e1) {
                    e1.printStackTrace();
                }
                start = i + 1;
            }
            //  Log.d("current char", String.valueOf(note.getText().charAt(i)));
        }
       */
        try {

            url = new URL("http://54.152.214.89:1337");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.connect();
            conn.getOutputStream().write(notes.toString().getBytes());

            result = conn.getResponseMessage();
            //       ma.note.setText(ma.note.getText()+" "+conn.getHeaderFields());

            InputStream inputStream = new BufferedInputStream(conn.getInputStream());
            StringBuilder sb = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String recommendation;
            while ((recommendation = bufferedReader.readLine()) != null) {
                sb.append(recommendation);
            }
            try {
                returnObject = new JSONObject(sb.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("Response", returnObject.toString());

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

        for (int i = 1; i <= returnObject.length(); i++) {
            name = "recom" + i;
            mainActivity.recom = new Button(mainActivity);
            mainActivity.buttonlayout.addView(mainActivity.recom);
            try {
                options = returnObject.getString(name);
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

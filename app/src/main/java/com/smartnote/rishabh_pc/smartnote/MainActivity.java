package com.smartnote.rishabh_pc.smartnote;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    EditText note;
    Button recom;
    LinearLayout buttonlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        note = (EditText) findViewById(R.id.note);
        buttonlayout = (LinearLayout) findViewById(R.id.button_layout);
        /*
        recom[0] = (Button) findViewById(R.id.recom1);
        recom[1]=(Button)findViewById(R.id.recom2);
        recom[2]=(Button)findViewById(R.id.recom3);


        recom[0].setOnClickListener(new View.OnClickListener() {
            URL url;

            public void onClick(View v) {
                try {
                    URL url = new URL("http://54.152.214.89:1337");
                    Log.d("Event:", "OnClickListener");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                //      getCloudData cd = new getCloudData();
                //      cd.execute(MainActivity.this);

            }
        });
*/
        note.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (note.getText().charAt(note.getText().length() - 1) == ' ') {
                    Log.d("Space entered", " ");
                    try {
                        Log.d("Event:","TextChanged");
                        createJson();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void createJson() throws JSONException, IOException {
        postCloudData pc=new postCloudData();
        pc.execute(this);
    }
}

package com.smartnote.rishabh_pc.smartnote;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends ActionBarActivity {

    EditText note;
    Button recom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        note = (EditText) findViewById(R.id.note);
        recom = (Button)findViewById(R.id.recom1);


        recom.setOnClickListener(new View.OnClickListener() {
            URL url;
            public void onClick(View v) {
                try{
                    URL url = new URL("http://54.152.214.89:1337");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                getCloudData cd=new getCloudData();
                cd.execute(MainActivity.this);

            }
        });
    }
}
       /* private void getCloudData() throws IOException{
        Log.d("hi", "" + R.id.recom1);
        URL url = null;
        String data="";
        try {
            url = new URL("http://54.152.214.89:1337");
            HttpURLConnection conn;
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            note.setText(note.getText()+" "+conn.getHeaderFields());
            Log.d("Request",""+conn.getHeaderFields());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }*/


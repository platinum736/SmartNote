package com.smartnote.rishabh_pc.smartnote;

import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    EditText search;
    Button recom,submit;
    LinearLayout buttonlayout;
    LinearLayout textbuttonrelative;
    List<Button> Tags = new ArrayList<Button>();
    static int buttnCount;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (EditText) findViewById(R.id.searchfield);
        buttonlayout = (LinearLayout) findViewById(R.id.button_layout);
        textbuttonrelative = (LinearLayout) findViewById(R.id.noteArea);
        submit=(Button)findViewById(R.id.sub);
        context = this;

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTag();
            }
        });

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(search.getText().length()!=0) {
                   if(search.getText().charAt(search.getText().length()-1)==' '){
                       try {
                           createJson();
                       } catch (JSONException e) {
                           e.printStackTrace();
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
                }
             }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void addTag() {
        Log.d("Event:","Submit");
        buttnCount++;
        Button b=new Button(context);
        Tags.add(b);
        b.setId(buttnCount);
        b.setText(search.getText());
        b.setOnClickListener(delTag);
        //  note.setText("");
        LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        //  LayoutParams TextLayout = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

       // if(buttnCount>1){
         //   layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
           // layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            b.setLayoutParams(layoutParams);

       // }
       // else{
         //   layoutParams.addRule(RelativeLayout.BELOW,buttnCount-1);
           // layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

           // b.setLayoutParams(layoutParams);
        //}
        //TextLayout.addRule(RelativeLayout.BELOW,buttnCount);
        //note.setLayoutParams(TextLayout);
        textbuttonrelative.addView(b);
        search.setText("");

        Log.d("Button id",b.toString());


    }

    private void createJson() throws JSONException, IOException {
        postCloudData pc=new postCloudData();
        pc.execute(this);
    }

    View.OnClickListener delTag=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b=(Button)v;
            buttnCount--;
            Tags.remove(b);
            textbuttonrelative.removeView(b);
        }
    };
}

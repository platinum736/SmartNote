package com.smartnote.rishabh_pc.smartnote;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    JSONObject notes = new JSONObject();
    JSONObject returnObject = null;
    EditText search;
    Button recom;
    ImageButton submit;
    LinearLayout buttonlayout;
    LinearLayout textbuttonrelative;
    List<Button> Tags = new ArrayList<Button>();
    static int buttnCount;
    Context context;
    Button action_save;
    postCloudData pc=new postCloudData();
    private PopupWindow pwindo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (EditText) findViewById(R.id.searchfield);
        buttonlayout = (LinearLayout) findViewById(R.id.button_layout);
        textbuttonrelative = (LinearLayout) findViewById(R.id.noteArea);
        submit=(ImageButton)findViewById(R.id.sub);
        context = this;
        Bundle extras=getIntent().getExtras();

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search.setText("");
                return false;
            }
        });
        if(extras!=null)
        {
            String noteData=extras.getString("filecontent", null);
            buttnCount=0;
            try {
                createJson();
                Log.d("Data Read",notes.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            populateScreen(noteData);
        }
       // setContentView(R.layout.popup);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("save", String.valueOf(action_save));
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
                    try {
                        createJson();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(search.getText().charAt(search.getText().length()-1)==' ') {
                        Log.d("Send","sending request");
                        pc.execute(MainActivity.this);
                    }
                   }
                }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void populateScreen(String noteData) {
        String delimiter=",";
        String[] tokens=noteData.split(delimiter);
        String text;
        for(int i=0;i<tokens.length;i++)
        {
            Button b=new Button(context);
            buttnCount++;
            Tags.add(b);
            text=tokens[i].split(":")[1];

            if(i==tokens.length-1)
            {
                text=text.substring(0,text.length()-2);
            }
            text=text.substring(1,text.length()-1);
            //The button ids start with 1 but in the array List they are stored starting with index 0.

            b.setId(buttnCount);
            Tags.get(buttnCount-1).setText(text);
            Tags.get(buttnCount-1).setOnClickListener(delTag);
            LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams linearparams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            textbuttonrelative.setLayoutParams(linearparams);
            Tags.get(buttnCount-1).setLayoutParams(layoutParams);
            textbuttonrelative.addView(Tags.get(buttnCount-1));
        }

    }

    private void createPopup() {
        Button savebtn;

        try {
// We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) MainActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 500, 500, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            final EditText filename= (EditText) layout.findViewById(R.id.filename);

            savebtn= (Button) layout.findViewById(R.id.save);

            savebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Filename",filename.toString());
                    saveFile(filename.getText().toString());
                }
            });

            Button Cancel;
            Cancel= (Button) layout.findViewById(R.id.cancel);

            Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pwindo.dismiss();
                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }
   }

    private void saveFile(String filename) {
        try {
            String filepath="/sdcard/NotesDir/"+filename;
            File file=new File(filepath);
            try {
                createJson();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(file.createNewFile())
                Log.d("Success","File Created");
            FileOutputStream fout=new FileOutputStream(file);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fout);
            outputStreamWriter.write(notes.toString());
            outputStreamWriter.close();
            Log.e("File saved", notes.toString());
            Toast toast=new Toast(context);
            toast.makeText(context,"File Saved",Toast.LENGTH_LONG).show();
            pwindo.dismiss();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void onWindowFocusChanged (boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            System.out.println("onWindowFocusChanged");
            if(buttnCount>1)
               System.out.println("Button Right" + Tags.get(buttnCount-1) .getRight());
        }
    }

    private void addTag() {
      //  Log.d("Event:","Submit");
        buttnCount++;
        Button b=new Button(context);

        Tags.add(b);
        b.setId(buttnCount);
        b.setText(search.getText().toString());
        b.setOnClickListener(delTag);
        LayoutParams layoutParams=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams linearparams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        textbuttonrelative.setLayoutParams(linearparams);
        b.setLayoutParams(layoutParams);
        textbuttonrelative.addView(b);
        search.setText("");
        System.out.println("Button Right" + Tags.get(buttnCount-1) .getRight());
      }

    private void createJson() throws JSONException, IOException {
        String tag;
        String key;
        int i=0;
        notes=new JSONObject();
        for (Button b : Tags) {
            try {
                tag = b.getText().toString();
                key="name"+i;
                notes.accumulate(key, b.getText());
                Log.d("Tags:", b.getText().toString());
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            if(search.getText().toString()!="search"&&search.getText().toString().trim().length()>0){
                key="name"+i;
                notes.accumulate(key, search.getText().toString());
                Log.d("notes",notes.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
   //     postCloudData pc=new postCloudData();
   //     pc.execute(this);
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

    public void onBackPressed() {
        Intent intent=new Intent(MainActivity.this, fileActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filesave:
                createPopup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

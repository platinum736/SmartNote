package com.smartnote.rishabh_pc.smartnote;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static android.view.Gravity.TOP;


public class MainActivity extends ActionBarActivity {

    JSONObject notes = new JSONObject();
    JSONObject returnObject = null;
    EditText search;
    Button recom;
    ImageButton submit;
    LinearLayout buttonlayout;
    LinearLayout textbuttonrelative;
    LinearLayout parentLinearLayout;
    List<Button> Tags = new ArrayList<Button>();
    List<LinearLayout> rows=new ArrayList<LinearLayout>();
    static int buttnCount;
    static int rowCount=1;
    Context context;
    Button action_save;
   // postCloudData pc=new postCloudData();
    private PopupWindow pwindo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = (EditText) findViewById(R.id.searchfield);
        textbuttonrelative = (LinearLayout) findViewById(R.id.noteArea);
        buttonlayout = (LinearLayout) findViewById(R.id.button_layout);

        parentLinearLayout= (LinearLayout) findViewById(R.id.button_layout);
        LinearLayout notesrow= (LinearLayout) findViewById(R.id.notesrow);
        rows.add(notesrow);
        rowCount=1;

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
                }
                if(isOnline())
                   new postCloudData().execute(MainActivity.this);
                else
                {
                   Toast toast=new Toast(context);
                   Log.d("Net error","Net not present");
                   toast.makeText(context,"Not Connected to internet",Toast.LENGTH_LONG).show();
                   toast.setGravity(Gravity.TOP,0,0);
                   pwindo.dismiss();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isOnline() {
        boolean status=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                status= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED)
                    status= true;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return status;
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

            //Setting the layout parameters for new button
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams linearparams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            rows.get(rowCount-1).setLayoutParams(linearparams);
            b.setLayoutParams(layoutParams);
            // b.setText(search.getText());

            //Find measure of new button and current layout
            rows.get(rowCount-1).measure(0, 0);
            b.measure(0,0);

            if(rows.get(rowCount-1).getMeasuredWidth()+b.getMeasuredWidth()>600)//rows.get(rowCount-1).getRight())
            {
                Log.e("Button Right",""+b.getMeasuredWidth()+" "+rows.get(rowCount-1).getMeasuredWidth()+" "+rows.get(rowCount-1).getRight());
                LinearLayout newrow=new LinearLayout(context);
                LinearLayout.LayoutParams rowparams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                newrow.setLayoutParams(rowparams);
                newrow.setOrientation(LinearLayout.HORIZONTAL);

                //add this new row to array list and give it a parent view
                textbuttonrelative.addView(newrow);
                rows.add(newrow);
                rowCount++;
            }


            rows.get(rowCount-1).addView(b);

            Tags.get(buttnCount-1).setOnClickListener(delTag);

            //textbuttonrelative.setLayoutParams(linearparams);
            Tags.get(buttnCount-1).setLayoutParams(layoutParams);
            //rows.get(rowCount-1).addView(Tags.get(buttnCount-1));
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

        //Add new button to the array list
        Tags.add(b);
        b.setId(buttnCount);


        //Setting the layout parameters for new button
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams linearparams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        rows.get(rowCount-1).setLayoutParams(linearparams);
        b.setLayoutParams(layoutParams);
        b.setText(search.getText());
        //Find measure of new button and current layout
        rows.get(rowCount-1).measure(0, 0);
        b.measure(0,0);

        if(rows.get(rowCount-1).getMeasuredWidth()+b.getMeasuredWidth()>rows.get(rowCount-1).getRight())
        {
            Log.e("Button Right",""+b.getMeasuredWidth()+" "+rows.get(rowCount-1).getMeasuredWidth()+" "+rows.get(rowCount-1).getRight());
            LinearLayout newrow=new LinearLayout(context);
            LinearLayout.LayoutParams rowparams= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            newrow.setLayoutParams(rowparams);
            newrow.setOrientation(LinearLayout.HORIZONTAL);

           //add this new row to array list and give it a parent view
            textbuttonrelative.addView(newrow);
            rows.add(newrow);
            rowCount++;
        }


        rows.get(rowCount-1).addView(b);
        b.setOnClickListener(delTag);
        search.setText("");
        System.out.println("Button Right" + Tags.get(buttnCount-1) .getRight());
      }

    private void createJson() throws JSONException, IOException {
        String tag;
        String key;
        int i=0;
        notes=new JSONObject();

        try {
            if(search.getText().toString()!="search"&&search.getText().toString().trim().length()>0){
                key="name"+i;
                notes.accumulate(key, search.getText().toString());
                Log.d("notes",notes.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        i=1;
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

   //     postCloudData pc=new postCloudData();
   //     pc.execute(this);
    }

    View.OnClickListener delTag=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b=(Button)v;
            buttnCount--;
            Tags.remove(b);
            LinearLayout parent= (LinearLayout) b.getParent();
            parent.measure(0,0);
            parent.removeView(b);
            if(parent.getMeasuredWidth()==0)
            {
                LinearLayout superparent= (LinearLayout) parent.getParent();
                superparent.removeView(parent);
                rowCount--;
                rows.remove(parent);
            }


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
            case R.id.action_Tag:
                callTagScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callTagScreen() {
        Intent intent=new Intent( MainActivity.this,tagScreen.class);
        startActivity(intent);
    }
}

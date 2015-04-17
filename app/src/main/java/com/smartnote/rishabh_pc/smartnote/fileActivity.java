package com.smartnote.rishabh_pc.smartnote;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.smartnote.rishabh_pc.smartnote.MainActivity;
import com.smartnote.rishabh_pc.smartnote.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static java.lang.System.exit;

/**
 * Created by rishabh-pc on 4/6/2015.
 */
public class fileActivity extends ActionBarActivity {
    Button newfile;
    Context context;
    Boolean clickFinder = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_menu);
        newfile = (Button) findViewById(R.id.NewFile);

        File direct = new File("/sdcard/NotesDir/");
        context = this;
        if (!direct.exists()) {
            if (direct.mkdir()) //directory is created;
            {
                Log.d("File", "Dir Created");
            } else {
                Log.d("File", "Dir already present");
            }

        } else
            Log.d("File", "Dir already present");


        loadFileslist();
        registerClickCallback();
        registerlongClickcallback();

        newfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed() {
        moveTaskToBack(true);
    }


    private void loadFileslist() {
        String path = "/sdcard/NotesDir/";
        ArrayList<String> Myfiles = new ArrayList<String>();
        File file = new File(path);

        File[] files = file.listFiles();

        if (files.length == 0)
            Log.e("FILES", "No files present");
        else {
            for (int i = 0; i < files.length; ++i) {
                Myfiles.add(files[i].getName());
                Log.e("FILE:", path + files[i].getName());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.listviewcontent, Myfiles);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }


    private void registerClickCallback() {
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textview = (TextView) viewClicked;
                StringBuffer buffer;

                buffer = readFile(((TextView) viewClicked).getText().toString());
                clickFinder = true;
                if (clickFinder = true) {
                    Intent intent = new Intent(fileActivity.this, MainActivity.class);
                    intent.putExtra("filecontent", buffer.subSequence(0, buffer.length()));
                    startActivity(intent);
                }

            }
        });
    }

    //Listeners for long click and implement delete on a file and displaying a popup box
    private void registerlongClickcallback() {
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setLongClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick (AdapterView < ? > parent, View view,int position, long id){
                Log.e("Long Click", ""+view.getId());
                clickFinder = false;
                showDeletePopup(view);
                return false;
            }
        });

    }


   PopupWindow pwindo;
   private void showDeletePopup(final View view){

           Button yesbtn,nobtn;

           try {
// We need to get the instance of the LayoutInflater
               LayoutInflater inflater = (LayoutInflater) fileActivity.this
                       .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View layout = inflater.inflate(R.layout.deletepopup,
                       (ViewGroup) findViewById(R.id.deletpopup_element));
               pwindo = new PopupWindow(layout, 500, 500, true);
               pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

               yesbtn= (Button) layout.findViewById(R.id.yes);

               yesbtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Log.d("Filename", view.toString());
                       //deletefile(String filename);
                   }
               });

               nobtn= (Button) layout.findViewById(R.id.no);

               nobtn.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       pwindo.dismiss();
                   }
               });




           } catch (Exception e) {
               e.printStackTrace();
           }
       }

    private StringBuffer readFile(String filename) {
        String filepath="/sdcard/NotesDir/"+filename;
        File file=new File(filepath);
        String content = null,str;
        JSONObject notes=null;
        StringBuffer buf = new StringBuffer();

        if(file.exists()){
            try {
                FileInputStream fin=new FileInputStream(file);
                InputStreamReader inputStreamReader = new InputStreamReader(fin);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                while((str=reader.readLine())!=null)
                {
                    buf.append(str+"\n");
                }
                inputStreamReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("File saved",buf.toString());
        return buf;
    }
}

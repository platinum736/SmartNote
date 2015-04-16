package com.smartnote.rishabh_pc.smartnote;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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

/**
 * Created by rishabh-pc on 4/6/2015.
 */
public class fileActivity extends ActionBarActivity{
    Button newfile;
    Context context;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_menu);
        newfile = (Button) findViewById(R.id.NewFile);

        File direct = new File("/sdcard/NotesDir/");
        context=this;
        if(!direct.exists())
        {
            if(direct.mkdir()) //directory is created;
            {
                Log.d("File", "Dir Created");
            }
            else
            {
                Log.d("File", "Dir already present");
            }

        }
        else
            Log.d("File", "Dir already present");


        loadFileslist();
        registerClickCallback();

        newfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(fileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loadFileslist() {
        String path = "/sdcard/NotesDir/";
        ArrayList<String> Myfiles=new ArrayList<String>();
        File file=new File(path);

        File[] files= file.listFiles();

        if(files.length==0)
            Log.e("FILES", "No files present");
        else {
            for (int i = 0; i < files.length; ++i) {
                Myfiles.add(files[i].getName());
                Log.e("FILE:", path + files[i].getName());
            }
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context,R.layout.listviewcontent,Myfiles);

        ListView listView= (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }


    private void registerClickCallback() {
        ListView listView=(ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textview= (TextView) viewClicked;
                StringBuffer buffer;

                buffer=readFile(((TextView) viewClicked).getText().toString());
                Intent intent=new Intent(fileActivity.this, MainActivity.class);
                intent.putExtra("filecontent",buffer.subSequence(0,buffer.length()));
                startActivity(intent);

            }
        });
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

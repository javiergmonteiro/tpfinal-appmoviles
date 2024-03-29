package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.RangeValueIterator;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import com.example.myapplication.databases.DatabaseHelper;

import com.example.myapplication.dummy.DummyContent;


public class MainActivity extends Activity  {
    Button b1,b2;
    EditText ed1, ed2;
    TextView tx1;
    DatabaseHelper helper;

    private static final String SHARED_PREF_NAME = "username";
    private static final String KEY_NAME = "key_username";
    private static final String KEY_SEARCH = "key_search";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DatabaseHelper(this);

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        editor.putString(KEY_SEARCH,"null");
        editor.apply();

        //dummyContent.addItem(new DummyContent.DummyItem("1","Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen","source1","javier"));
        //dummyContent.addItem(new DummyContent.DummyItem("2","Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen","source2", "javier"));
        //dummyContent.addItem(new DummyContent.DummyItem("3","Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen Una descripción para esta imagen","source3", "javier"));
        
        b1 = (Button)findViewById(R.id.button);
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);
        b2 = (Button)findViewById(R.id.button2);



        if (sp.getString(KEY_NAME,null) != null){
            Intent home = new Intent(MainActivity.this, ItemListActivity.class);
            startActivity(home);
            finish();
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = ed1.getText().toString();
                String password = ed2.getText().toString();
                SQLiteDatabase db = helper.open();
                String[] args = new String[]{user,password};
                Cursor c = db.rawQuery("SELECT * FROM USERS where ( user = ? and password= ?)",args);
                if (c.moveToFirst()){
                    editor.putString(KEY_NAME, user);
                    editor.apply();
                    Intent home = new Intent(MainActivity.this, ItemListActivity.class);
                    startActivity(home);
                    finish();
                }
                else{
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("Incorrect Login");
                    alertDialog.show();
                }
                db.close();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });
    }
}

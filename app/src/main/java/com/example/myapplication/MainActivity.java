package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import com.example.myapplication.databases.DatabaseHelper;


public class MainActivity extends Activity  {
    Button b1,b2;
    EditText ed1, ed2;
    TextView tx1;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button)findViewById(R.id.button);
        ed1 = (EditText)findViewById(R.id.editText);
        ed2 = (EditText)findViewById(R.id.editText2);
        b2 = (Button)findViewById(R.id.button2);
        tx1 = (TextView)findViewById(R.id.button2);
        tx1.setVisibility(View.GONE);
        helper = new DatabaseHelper(this);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = ed1.getText().toString();
                String password = ed2.getText().toString();
                SQLiteDatabase db = helper.open();
                String[] args = new String[]{user,password};
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                Cursor c = db.rawQuery("SELECT * FROM USERS where ( user = ? and password= ?)",args);
                if (c.moveToFirst()){
                    Intent home = new Intent(MainActivity.this, ItemListActivity.class);
                    startActivity(home);
                }
                else{
                    alertDialog.setMessage("incorrect login");
                    alertDialog.show();
                }

            }
        });
    }
}

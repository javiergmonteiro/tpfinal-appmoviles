package com.example.myapplication;

import android.app.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;
import com.example.myapplication.dummy.DummyContent;
import com.example.myapplication.databases.DatabaseHelper;



public class ItemEdit extends Activity {
    DatabaseHelper helper;
    private DummyContent.DummyItem mItem;
    private static final String SHARED_PREF_NAME = "username";
    private static final String KEY_MOMENT = "key_moment";
    private SharedPreferences  mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        Button bt1 = findViewById(R.id.button_save);
        Button bt2 = findViewById(R.id.button_edit_cancel);
        helper = new DatabaseHelper(this);
        final SQLiteDatabase db = helper.open();

        mySharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        final String moment = mySharedPreferences.getString(KEY_MOMENT,null);
        mItem = DummyContent.ITEM_MAP.get(moment);
        final EditText edt = (EditText)findViewById(R.id.edit_description);
        edt.setText(mItem.description);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String query = "UPDATE moments SET description='" + edt.getText().toString() + "' where id=" + moment;
                    db.execSQL(query);
                    Toast toast = Toast.makeText(getApplicationContext(),"Datos guardados", Toast.LENGTH_SHORT);
                    toast.show();
                }
                catch (Exception e){
                    Toast toast = Toast.makeText(getApplicationContext(),"Ocurrio un error", Toast.LENGTH_SHORT);
                    toast.show();
                }
                finally {
                    Intent itemlist = new Intent(ItemEdit.this,ItemListActivity.class);
                    startActivity(itemlist);
                    finish();
                }
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemlist = new Intent(ItemEdit.this,ItemListActivity.class);
                finish();
                startActivity(itemlist);
            }
        });
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent itemlist = new Intent(ItemEdit.this,ItemListActivity.class);
        finish();
        startActivity(itemlist);
    }
}

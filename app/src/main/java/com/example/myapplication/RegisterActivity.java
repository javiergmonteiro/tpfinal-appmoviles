package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.databases.DatabaseHelper;

public class RegisterActivity extends Activity {

    DatabaseHelper helper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Usuario creado exitosamente")
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        Button register = findViewById(R.id.button3);
        final EditText user = findViewById(R.id.editText3);
        final EditText password = findViewById(R.id.editText6);
        final EditText confirmPassword = findViewById(R.id.editText7);
        helper = new DatabaseHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
                SQLiteDatabase db = helper.open();
                String[] args;
                String suser = user.getText().toString();
                String spassword = password.getText().toString();
                String sconfirmpassword = confirmPassword.getText().toString();



                if (!spassword.equals(sconfirmpassword)){
                    alertDialog.setMessage("Las contraseñas no coinciden" + spassword + " " + sconfirmpassword);
                    alertDialog.show();
                    return;
                }
                args = new String[]{suser};
                Cursor c = db.rawQuery("SELECT * FROM USERS where ( user = ?)",args);

                if (c.moveToFirst()){
                    alertDialog.setMessage("El usuario ya existe");
                    alertDialog.show();
                    return;
                }
                args = new String[]{suser,spassword};
                try{
                    String query = "INSERT INTO USERS (user,password) values ('"+suser+"','"+spassword+"')";
                    db.execSQL(query);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                catch (Exception e){
                    alertDialog.setMessage("Hubo un error al crear un usuario");
                    alertDialog.show();
                    return;
                }
            }
        });
    }
}

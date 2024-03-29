package com.example.myapplication.databases;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BD";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_USERS = "CREATE TABLE users (_id integer primary key autoincrement, " + "user text not null, password text not null)";
    private static final String DATABASE_CREATE_MOMENTS = "CREATE TABLE moments (id integer primary key autoincrement, user text not null, description text not null, tags text, image text not null, timestamp date, altitud text, latitud text)";
    private static final String DATABASE_CREATE_ADMIN = "INSERT INTO users VALUES (1,'Javier','123')";

    private SQLiteDatabase bd;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db){
        try{
            db.execSQL(DATABASE_CREATE_USERS);
            db.execSQL(DATABASE_CREATE_MOMENTS);
            db.execSQL(DATABASE_CREATE_ADMIN);
            int count = 1;
            for (int i=0;i<4;i++){
                String image = "source"+Integer.toString(count);
                db.execSQL("INSERT INTO moments VALUES ("+Integer.toString(count)+",'Javier','Este es un texto creado desde la base de datos que sirve solamente para simuular una descripcion de un momento','#landscape','"+image+"','2018-11-01','-34.774249','-58.267798')");
                count++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS moments");
        onCreate(db);
    }

    public SQLiteDatabase open(){
        bd = this.getWritableDatabase();
        return bd;
    }
    public void close(){
        bd.close();
    }
}
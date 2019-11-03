package com.example.myapplication.databases;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BD";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE users (_id integer primary key autoincrement, " + "user text not null, password text not null)";
    private static final String DATABASE_CREATE_ADMIN = "INSERT INTO users VALUES (1,'javier','123')";

    private SQLiteDatabase bd;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate (SQLiteDatabase db){
        try{
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE_ADMIN);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
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
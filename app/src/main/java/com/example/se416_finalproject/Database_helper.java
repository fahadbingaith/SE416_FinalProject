package com.example.se416_finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;


public class Database_helper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME= "User";
    public static  final String TABLE_NAME="client";
    public static final String COLUMN_ID="ID";
    public static final String COLUMN_Phone ="PhoneNumber";
    public static final String COLUMN_FirstNAME="FirstName";
    public static final String COLUMN_LastNAME="LastName";
    public static final String COLUMN_email="email";


    SQLiteDatabase db;
    public Database_helper(@Nullable Context context ) {
        super(context, DATABASE_NAME, null, 1);
        db=getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                        + "("+ COLUMN_ID + " INTEGER PRIMARY KEY ,"
                        + COLUMN_FirstNAME +  " TEXT NOT NULL,"
                        + COLUMN_LastNAME + " TEXT NOT NULL,"
                        + COLUMN_Phone + " TEXT NOT NULL,"
                        + COLUMN_email +" TEXT NOT NULL)");
    }

    public void insert(int ID,String phone,String Fname,String Lname,String email){
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, ID);
        values.put(COLUMN_Phone, phone);
        values.put(COLUMN_FirstNAME, Fname);
        values.put(COLUMN_LastNAME, Lname);
        values.put(COLUMN_email, email);
        db.insert(TABLE_NAME, null, values);

    }

    public void insert(User u){
    insert(u.userId,u.phoneNumber,u.firstName,u.lastName,u.emailAddress);
    }

    public Cursor selectAll(){
        Cursor c= db.rawQuery("SELECT * FROM "+ TABLE_NAME, null);

        if (c.moveToNext()){
            return c;
        }else{
            return null;
        }
    }

    public void delete(int id){
        String [] args={id+""};
        db.delete(TABLE_NAME, "ID = ?", args);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getSpecificResult(int ID){
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME+" Where ID="+ID,null);
        if (data.moveToNext()){
            return data;
        }else{
            return null;
        }
    }

    public void update(int ID,String phone,String Fname,String Lname,String email){
        String [] args={ID+""};
        ContentValues values = new ContentValues();
        values.put(COLUMN_Phone, phone);
        values.put(COLUMN_ID, ID);
        values.put(COLUMN_FirstNAME, Fname);
        values.put(COLUMN_LastNAME, Lname);
        values.put(COLUMN_email, email);

        db.update(TABLE_NAME,values,COLUMN_ID+"=?",args);
        return;
    }

}

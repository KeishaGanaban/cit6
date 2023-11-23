package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper2 extends SQLiteOpenHelper {
    public DBHelper2(Context context) {
        super(context, "Profiledata.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create Table Profiledata(name TEXT primary key, occupation TEXT, number TEXT, email TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int i1) {
        DB.execSQL("drop Table if exists Profiledata");

    }

    public Boolean addprofiledata(String name, String occupation, String number, String email) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("occupation", occupation);
        contentValues.put("number", number);
        contentValues.put("email", email);
        long result=DB.insert("Profiledata", null, contentValues);
        if(result==-1) {
            return false;
        }else{
            return true;
        }
    }

    public Boolean updateprofiledata(String name, String occupation, String number, String email) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("occupation", occupation);
        contentValues.put("number", number);
        contentValues.put("email", email);
        Cursor cursor = DB.rawQuery("Select * from Profiledata where name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.update("Profiledata", contentValues, "name=?", new String[]{name});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }

    }

    public Boolean deletedata(String name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Profiledata where name = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            long result = DB.delete("Profiledata", "name=?", new String[]{name});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else {
            return false;
        }

    }

    public Cursor getdata() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Profiledata", null);
        return cursor;

    }

}

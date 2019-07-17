package com.example.utsav.bhojpuri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ScannerDB extends SQLiteOpenHelper {
    private static final String DATABASE = "scanner_db";
    private static final String TABLE_NAME = "mul_qrcode";
    private static final String TABLE_NAMEsplit = "mul_qrcodesplit";
    private static final String TABLE_NAMEdummy = "dummy";
    public static final String CODE1 = "code1";
    public ScannerDB(Context context) {

        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME + "(" + "ques" +" TEXT," + CODE1 +" TEXT" + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAMEsplit + "(" + "ques" +" TEXT," + "stid" +" text," +"optn" +" text" + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAMEdummy + "(" + "ques" +" TEXT," + "stid" +" text," +"optn" +" text" + ")");


    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEsplit);
       sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEdummy);
        onCreate(sqLiteDatabase);
    }

    public void insert(String ques,String s1) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ques",ques );
        contentValues.put(CODE1, s1);
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }
    public void insert1(String ques,String stid,String optn) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ques",ques );
        contentValues.put("stid",stid);
        contentValues.put("optn",optn);
        sqLiteDatabase.insert(TABLE_NAMEsplit, null, contentValues);
    }
  public void dummy(String ques,String stid,String optn) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ques",ques );
        contentValues.put("stid",stid);
        contentValues.put("optn",optn);
        sqLiteDatabase.insert(TABLE_NAMEdummy, null, contentValues);
    }

    public ArrayList<String> getData() {
        ArrayList arr1 = new ArrayList();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAMEdummy, null);
        if (cursor != null && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                arr1.add(cursor.getString(0)+"."+cursor.getString(1)+"-"+cursor.getString(2));
                cursor.moveToNext();
            }
        }
        return arr1;
    }
    public Boolean codeqr(String s1,String s2){
        SQLiteDatabase sqLiteDatabase =getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from mul_qrcodesplit where ques=? and stid=?",new String[]{s1,s2});
        if (cursor.getCount()!=0)
            return false;
        else
            return true;
    }
    public Cursor scanned_data() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAMEsplit ,null);
        return res;
    }
    public Cursor scanned_single_data() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAMEdummy ,null);
        return res;
    }
    public Integer deleteData (String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }


}
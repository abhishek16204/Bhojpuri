package com.example.utsav.bhojpuri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.List;

public class SubmitScanDB extends SQLiteOpenHelper {
    public static final String DATABASE = "submitscan";
    private static final String TABLE1= "submit_table1";
    private static final String TABLE2= "submit_table2";
    private static  String submit_1 =
            "create table  submit_table1(assesment_id integer primary key autoincrement, "
                   +"submit_by integer ,"+"submit_on text,"+"flag integer);";

  private static  String submit_2 =
            "create table  submit_table2(assesment_id integer ," + "ques_id integer ,"+ "student_id integer ,"+"response text);";


    public SubmitScanDB( Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(submit_1);
        db.execSQL(submit_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS submit_table1" );
        db.execSQL("DROP TABLE IF EXISTS submit_table2" );
    }
    public long insert_table1(int submit_by, String submit_on,int flag){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("submit_by",submit_by);
        contentValues.put("submit_on", submit_on);
         contentValues.put("flag",flag);
        long id=sqLiteDatabase.insert(TABLE1, null, contentValues);
        sqLiteDatabase.close();
        return id;

    }
    public long insert_table2(int assesment_id,int ques_id,int student_id,String response){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("assesment_id", assesment_id);
        contentValues.put("ques_id", ques_id);
        contentValues.put("student_id",student_id);
        contentValues.put("response",response);
        long id=sqLiteDatabase.insert(TABLE2, null, contentValues);
        sqLiteDatabase.close();
        return id;

    }
    public Cursor getAllData3() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE1,null);
        return res;
    }
    public Cursor getAllData4() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE2,null);
        return res;
    }
    public Cursor getAllData11(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM submit_table1 WHERE assesment_id = "+ id,null);
        return res;
    }
    public Cursor getAllData2(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM submit_table2 WHERE assesment_id = "+ id,null);
        return res;
    }
    public void update(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("flag",1);
        String strSQL = "UPDATE submit_table1 SET flag = 1 WHERE assesment_id = "+ id;
        db.execSQL(strSQL);
    }

}

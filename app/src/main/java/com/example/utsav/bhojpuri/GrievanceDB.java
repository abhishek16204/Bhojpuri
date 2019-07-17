package com.example.utsav.bhojpuri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class GrievanceDB extends SQLiteOpenHelper {
    public static final String DATABASE = "grievance";
    private static final String TABLE_NAME_grievance= "grievance_details";
    private static  String submit_1 =
            "create table  grievance_details(grievanceid integer primary key autoincrement, "
                    + "school_id integer ,"+"grievancecategory_id integer ,"+"grievance_name text,"+"image text,"+"submit_by integer,"+"submit_on text,"+"grievance_remark text,"+"flag int);";

    public GrievanceDB( Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(submit_1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS grievance_details" );
    }
    public long insertdata(int school_id,int grievancecategory_id,String grievance_name,String image,int submit_by,String submit_on,String grievance_remark,int flag){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("school_id", school_id);
        contentValues.put("grievancecategory_id",grievancecategory_id);
        contentValues.put("grievance_name", grievance_name);
        contentValues.put("image",image);
        contentValues.put("submit_by", submit_by);
        contentValues.put( "submit_on",submit_on);
        contentValues.put( "grievance_remark",grievance_remark);
        contentValues.put( "flag",flag);
        long id=sqLiteDatabase.insert(TABLE_NAME_grievance, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }
    public Cursor getAllData11(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM grievance_details WHERE grievanceid = "+ id,null);
        return res;
    }
    public void update(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("flag",1);
        String strSQL = "UPDATE grievance_details SET flag = 1 WHERE grievanceid = "+ id;
        db.execSQL(strSQL);
    }
    public Cursor getAllData3() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME_grievance,null);
        return res;
    }
}

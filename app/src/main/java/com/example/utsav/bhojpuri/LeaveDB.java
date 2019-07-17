package com.example.utsav.bhojpuri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LeaveDB extends SQLiteOpenHelper {
    public static final String DATABASE = "leave";
    private static final String TABLE_NAME_leave= "leave_details";
    private static  String submit_1 =
            "create table  leave_details(leaveid integer primary key autoincrement, "
                    + "school_id integer ,"+"leave_type_id integer ,"+"leave_name text,"+"from_date text,"+"to_date text,"+"image text,"+"submit_by integer,"+"submit_on text,"+"reason text,"+"flag integer);";



    public LeaveDB( Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(submit_1);
    }@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS leave_details" );
    }

    public long insertdata(int school_id,int leave_type_id,String leave_name,String from_date,String to_date,String image,int submit_by,String submit_on,String reason,int flag){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("school_id", school_id);
        contentValues.put("leave_type_id",leave_type_id);
        contentValues.put("leave_name", leave_name);
        contentValues.put("from_date",from_date);
        contentValues.put("to_date",to_date);
        contentValues.put("image",image);
        contentValues.put("submit_by", submit_by);
        contentValues.put( "submit_on",submit_on);
        contentValues.put( "reason",reason);
        contentValues.put( "flag",flag);
        long id=sqLiteDatabase.insert(TABLE_NAME_leave, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }
    public Cursor getAllData11(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM leave_details WHERE grievanceid = "+ id,null);
        return res;
    }
    public void update(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("flag",0);
        String strSQL = "UPDATE leave_details SET flag = 0 WHERE leaveid = "+ id;
        db.execSQL(strSQL);
    }
    public Cursor getAllData3() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME_leave,null);
        return res;
    }


}

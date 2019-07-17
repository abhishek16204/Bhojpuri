package com.example.utsav.bhojpuri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TeacherDB extends SQLiteOpenHelper {
    public static final String DATABASE = "hm_details";
    public static final String TABLE_NAME1 = "lst";
    public static final String TABLE_NAMEU = "user_details";
    public static final String TABLE_NAMET = "teacher_details";
    private static final String TABLE_NAME_slot_list = "slot_list";
    private static final String TABLE_NAME_status = "status";
    private static final String TABLE_NAME_message = "message";
    private static final String TABLE_NAME_school_details= "school_details";
    private static final String TABLE_NAME_class_detail= "class_detail";
    private static final String TABLE_NAME_student_details= "student_details";
    private static final String TABLE_NAME_category= "category_details";
    private static final String TABLE_NAME_leave= "leave";
 private static final String TABLE_NAME_Question= "question_details";
    private static final String TABLE_NAME_Options= "option_details";
 private static final String TABLE_NAME_module= "module_details";

    public TeacherDB(Context context) {

        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table " + TABLE_NAMET + "(" + "designation" + " TEXT," + "teacher_id" + " TEXT," + "tcontact" + " TEXT,"+ "trole" + " TEXT,"+ "tgender" + " TEXT,"+ "teacher_name" + " TEXT" + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME1 + "(" + "role" + " TEXT," + "level" + " TEXT," + "name" + " TEXT," + "user_id" + " TEXT" + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAMEU + "(" + "designation" + " TEXT," + "email" + " TEXT," + "name" + " TEXT," + "block" + " TEXT," + "degree" + " TEXT," + "cluster" + " TEXT," + "contact" + " TEXT,"
                + "school" + " TEXT," + "doj" + " TEXT," + "gender" + " TEXT," + "dob" + " TEXT," + "noa" + " TEXT," + "district" + " TEXT" + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_slot_list + "("
                + "slot_id" + " TEXT,"
                + "from_time" + " TEXT,"
                + "to_time" + " TEXT,"
                + "bool" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_class_detail + "("
                + "class_id" + " TEXT,"
                + "class_name" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_student_details + "("
                + "student_code" + " TEXT,"
                + "student_id" + " TEXT,"
                + "class_id" + " TEXT,"
                + "student_name" + " TEXT"
                + ")");
  sqLiteDatabase.execSQL("Create table " + TABLE_NAME_category + "("
                + "qc_id" + " TEXT,"
                + "category" + " TEXT"
                + ")");

        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_leave + "("
                + "leave_type_id" + " TEXT,"
                + "leave_type" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_Question + "("
                + "question_id" + " TEXT,"
                + "question_order" + " TEXT,"
                + "question_name" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_module + "("
                + "orderr" + " TEXT,"
                + "icon" + " TEXT,"
                + "name" + " TEXT"
                + ")");
       sqLiteDatabase.execSQL("Create table " + TABLE_NAME_Options + "("
                + "question_id" + " TEXT,"
                + "option_id" + " TEXT,"
                + "option_name" + " TEXT,"
                + "option_order" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_school_details + "("
                + "block" + " TEXT,"
                + "dise_code" + " TEXT,"
                + "school_category" + " TEXT,"
                + "cluster" + " TEXT,"
                + "school_name" + " TEXT,"
                + "is_coed" + " TEXT,"
                + "control_department" + " TEXT,"
                + "school_id" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_status + "(" + "status" + " TEXT"+")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_message + "(" + "message" + " TEXT"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMET);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAMEU);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_slot_list);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_status);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_message);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_school_details);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_class_detail);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_student_details);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_category);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_leave);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Question);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Options);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_module);

        onCreate(sqLiteDatabase);
    }
    public long insert2(String designation,String teacher_id,String contact,String trole,String tgender,String teacher_name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designation", designation);
        contentValues.put("teacher_id",teacher_id);
        contentValues.put("tcontact", contact);
        contentValues.put("trole",trole);
        contentValues.put("tgender", tgender);
        contentValues.put( "teacher_name",teacher_name);
        long id=sqLiteDatabase.insert(TABLE_NAMET, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }


    public long insert(String role,String level,String name,String user_id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("role", role);
        contentValues.put("level",level);
        contentValues.put("name", name);
        contentValues.put("user_id",user_id);
        long id= sqLiteDatabase.insert(TABLE_NAME1, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }
         public long category_detail(String qc_id,String category) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("qc_id", qc_id);
        contentValues.put("category",category);
        long id= sqLiteDatabase.insert(TABLE_NAME_category, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long leave(String leave_type_id,String leave_type) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("leave_type_id", leave_type_id);
        contentValues.put("leave_type",leave_type);
        long id= sqLiteDatabase.insert(TABLE_NAME_leave, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long question_detail(String question_id,String question_order,String question_name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question_id", question_id);
        contentValues.put("question_order", question_order);
        contentValues.put("question_name",question_name);
        long id= sqLiteDatabase.insert(TABLE_NAME_Question, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }
     public long module(String order,String icon,String name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("orderr", order);
        contentValues.put("icon", icon);
        contentValues.put("name",name);
        long id= sqLiteDatabase.insert(TABLE_NAME_module, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }
     public long option_detail(String question_id,String option_id,String option_name,String option_order) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("option_id", option_id);
        contentValues.put("question_id", question_id);
        contentValues.put("option_name",option_name);
         contentValues.put("option_order",option_order);
        long id= sqLiteDatabase.insert(TABLE_NAME_Options, null, contentValues);
        sqLiteDatabase.close();
        return id;
     }
  public long class_detail(String class_id,String class_name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("class_id", class_id);
        contentValues.put("class_name",class_name);
        long id= sqLiteDatabase.insert(TABLE_NAME_class_detail, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long student_details(String student_code,String student_id,String class_id,String student_name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("student_code",student_code);
        contentValues.put("student_id",student_id);
        contentValues.put("class_id", class_id);
        contentValues.put("student_name", student_name);

        long id= sqLiteDatabase.insert(TABLE_NAME_student_details, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long user_details(String designation,String email,String name,String block,String degree,String cluster,String contact,
                             String school,String doj,String gender,String dob,String noa,String district) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("designation", designation);
        contentValues.put("email",email);
        contentValues.put("name", name);
        contentValues.put("block",block);
        contentValues.put("degree", degree);
        contentValues.put("cluster",cluster);
        contentValues.put("contact", contact);
        contentValues.put("school",school);
        contentValues.put("doj",doj);
        contentValues.put("gender", gender);
        contentValues.put("dob",dob);
        contentValues.put("noa", noa);
        contentValues.put("district",district);
        long id=  sqLiteDatabase.insert(TABLE_NAMEU, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }
    public long school_details(String block,String dise_code,String school_category,String is_coed,String school_name,String cluster,int control_department,
                               int school_id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("block", block);
        contentValues.put("dise_code",dise_code);
        contentValues.put("school_category", school_category);
        contentValues.put("cluster",cluster);
        contentValues.put("school_name", school_name);
        contentValues.put("is_coed",is_coed);
        contentValues.put("control_department", control_department);
        contentValues.put("school_id",school_id);
        long id=  sqLiteDatabase.insert(TABLE_NAME_school_details, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }
    public void slot_lists(String slot_id, String from_time, String to_time,String bool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("slot_id",slot_id);
        values.put("from_time",from_time);
        values.put("to_time",to_time);
        values.put("bool",bool);
        db.insert(TABLE_NAME_slot_list, null, values);
    }

    public void status(String status) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);

        sqLiteDatabase.insert(TABLE_NAME_status, null, contentValues);
        sqLiteDatabase.close();
    }
    public void message(String message) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("message", message);
        sqLiteDatabase.insert(TABLE_NAME_message, null, contentValues);
        sqLiteDatabase.close();
    }
    public Cursor getTeacherdata() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAMET,null);
        return res;
    }
    public Cursor getAllData1() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME_slot_list,null);
        return res;
    }
    public Cursor getAllData2() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_status ,null);
        return res;
    }
    public Cursor getAllData3( ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME1 ,null);
        return res;
    }
    public Cursor getAllDataa(String r) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME1+" WHERE role = "+ r,null);
        return res;
    }
    public Cursor getAllData4() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_message ,null);
        return res;
    }
    public Cursor getUserData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAMEU ,null);
        return res;
    }
    /* public ArrayList<ModuleList> getModuleData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_module ,null);
        return res;
    }*/
    public ArrayList<ModuleList> getModuleData() {

        String query = "SELECT * FROM module_details ";
        ArrayList<ModuleList> students = new ArrayList<ModuleList>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor c = database.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                String icon = c.getString(c.getColumnIndex("icon"));
                String name = c.getString(c.getColumnIndex("name"));
                ModuleList st = new ModuleList();
                Bitmap bitmap=null;
                try {
                    InputStream input = new URL(icon ).openStream();
                    bitmap = BitmapFactory.decodeStream( input );
                    input.close();
                    File storagePath = Environment.getExternalStorageDirectory();
                    OutputStream bytes = new FileOutputStream( new File( storagePath, "name.jpg" ) );

                    bitmap.compress( Bitmap.CompressFormat.JPEG, 100, bytes );
                    bytes.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                st.setModuleImage(bitmap);
                st.setModulename(name);
                students.add(st);
            }
        }
        return students;
    }
    public Cursor getSchool_details() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_school_details,null);
        return res;
    }
    public Cursor getCl_details() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_class_detail ,null);
        return res;
    }
    public Cursor getSt_details(int c_id ) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM student_details WHERE class_id= "+c_id, null);
        return res;
    }
    public Cursor getQuestion() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_Question ,null);
        return res;
    } public Cursor getOption() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME_Options ,null);
        return res;
    }
    public List<String> getClass_detail(){
        List<String> labels = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_class_detail;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }
    public List<Integer> getClass_id(){
        List<Integer> labels = new ArrayList<Integer>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_class_detail;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getInt(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return labels;
    }
    public Integer deleteData (String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }
    public Cursor category_data() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME_category,null);
        return res;
    }

    public Cursor leave_data() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TABLE_NAME_leave ,null);
        return res;
    }


}

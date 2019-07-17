package com.example.utsav.bhojpuri;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.security.keystore.StrongBoxUnavailableException;
import android.util.Log;
import android.widget.Toast;

public class ClusterDB extends SQLiteOpenHelper {
    public static final String DATABASE = "crp_details";
    public static final String TABLE_NAME1 = "lst";
    public static final String TABLE_NAMEU = "user_details";
    public static final String TABLE_NAMET = "teacher_details";
    private static final String TABLE_NAME_slot_list = "slot_lists";
    private static final String TABLE_NAME_status = "status";
    private static final String TABLE_NAME_message = "message";
    private static final String TABLE_NAME_school_details = "school_details";
    private static final String TABLE_NAME_cluster_details = "cluster_details";
    private static final String TABLE_NAME_class_detail = "class_detail";
    private static final String TABLE_NAME_student_details = "student_details";
    private static final String TABLE_NAME_category = "category_details";
    private static final String TABLE_NAME_monitot_list = "monitot_list";
    private static final String TABLE_NAME_leave = "leave";
    private static final String TABLE_NAME_Question = "survey_question_list";
    private static final String TABLE_NAME_Options = "survey_option_list";
    private static final String TABLE_NAME_module = "module_details";
    private static final String TABLE_NAME_submit = "submit";

    public ClusterDB(Context context) {
        super(context, DATABASE, null, 1);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_class_detail + "("
                + "school_id" + " TEXT,"
                + "class_name" + " TEXT,"
                + "class_id" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME1 + "(" + "name" + " TEXT," + "user_id" + " TEXT," + "level" + " TEXT," + "role" + " TEXT" + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_student_details + "("
                + "student_code" + " TEXT,"
                + "student_id" + " TEXT,"
                + "class_id" + " TEXT,"
                + "student_name" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_cluster_details + "("
                + "cluster_name" + " TEXT,"
                + "block_id" + " TEXT,"
                + "cluster_id" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAMEU + "("
                + "email" + " TEXT,"
                + "cluster" + " TEXT,"
                + "dob" + " TEXT,"
                + "block" + " TEXT,"
                + "name" + " TEXT,"
                + "gender" + " TEXT,"
                + "contact" + " TEXT,"
                + "blood_group" + " TEXT,"
                + "designation" + " TEXT,"
                + "district" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_school_details + "("
                + "block" + " TEXT,"
                + "school_category" + " TEXT,"
                + "cluster_id" + " TEXT,"
                + "cluster" + " TEXT,"
                + "school_name" + " TEXT,"
                + "is_coed" + " TEXT,"
                + "control_department" + " TEXT,"
                + "school_id" + " TEXT,"
                + "dise_code" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_slot_list + "("
                + "slot_id" + " TEXT,"
                + "from_time" + " TEXT,"
                + "to_time" + " TEXT,"
                + "bool" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_monitot_list + "("
                + "input_type_id" + " TEXT,"
                + "q_no" + " TEXT,"
                + "is_image" + " TEXT,"
                + "q_name" + " TEXT,"
                + "q_id" + " TEXT,"
                + "name_regional" + " TEXT,"
                + "opg_id" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_Options + "("
                + "input_type_id" + " TEXT,"
                + "opc_id" + " TEXT,"
                + "opg_id" + " TEXT,"
                + "option_choices" + " TEXT,"
                + "ques_id" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table " + TABLE_NAME_Question + "("
                + "input_type_id" + " TEXT,"
                + "opg_id" + " TEXT,"
                + "is_image" + " TEXT,"
                + "question" + " TEXT,"
                + "ques_no" + " TEXT,"
                + "in_regional_language" + " TEXT,"
                + "ques_id" + " TEXT"
                + ")");
        sqLiteDatabase.execSQL("Create table if not EXISTS " + TABLE_NAME_submit + "("
                + "question_id" + " TEXT,"
                + "answer" + " TEXT,"
                + "issynced" + " TEXT,"
                + "imagestr" + " TEXT,"
                + "timestamp" + " TEXT,"
                + "school_id" + " TEXT,"
                + "cluster_id" + " TEXT,"
                + "remark" + " TEXT,"
                + "date" + " TEXT"
                + ")");
    }

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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_monitot_list);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_cluster_details);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_submit);

        onCreate(sqLiteDatabase);
    }

    public long lst(String role, String level, String name, String user_id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("user_id", user_id);
        contentValues.put("level", level);
        contentValues.put("role", role);
        long id = sqLiteDatabase.insert(TABLE_NAME1, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }





    public long class_detail(String school_id, String class_name, String class_id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("school_id", school_id);
        contentValues.put("class_name", class_name);
        contentValues.put("class_id", class_id);
        long id = sqLiteDatabase.insert(TABLE_NAME_class_detail, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long student_detail(String student_code, String student_id, String class_id, String student_name) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("student_code", student_code);
        contentValues.put("school_id", student_id);
        contentValues.put("class_id", class_id);
        contentValues.put("student_name", student_name);
        long id = sqLiteDatabase.insert(TABLE_NAME_class_detail, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long cluster_detail(String cluster_name, String block_id, String cluster_id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("cluster_name", cluster_name);
        contentValues.put("block_id", block_id);
        contentValues.put("cluster_id", cluster_id);
        long id = sqLiteDatabase.insert(TABLE_NAME_cluster_details, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long user(String email, String cluster, String dob, String block, String name, String gender, String contact, String blood_group, String designation, String district) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("cluster", cluster);
        contentValues.put("dob", dob);
        contentValues.put("block", block);
        contentValues.put("name", name);
        contentValues.put("gender", gender);
        contentValues.put("contact", contact);
        contentValues.put("blood_group", blood_group);
        contentValues.put("designation", designation);
        contentValues.put("district", district);
        long id = sqLiteDatabase.insert(TABLE_NAMEU, null, contentValues);
        sqLiteDatabase.close();
        return id;


    }

    public long school_details(String block, String school_category, String cluster_id, String cluster, String school_name, String is_coed, String school_id, String dise_code) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("block", block);
        contentValues.put("dise_code", dise_code);
        contentValues.put("school_category", school_category);
        contentValues.put("cluster", cluster);
        contentValues.put("cluster_id", cluster_id);
        contentValues.put("school_name", school_name);
        contentValues.put("is_coed", is_coed);
        contentValues.put("school_id", school_id);
        long id = sqLiteDatabase.insert(TABLE_NAME_school_details, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long slot_lists(String slot_id, String from_time, String to_time, String bool) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("slot_id", slot_id);
        values.put("from_time", from_time);
        values.put("to_time", to_time);
        values.put("bool", bool);
        long id = db.insert(TABLE_NAME_slot_list, null, values);

        return id;
    }

    public long submit(int question_id, String answer, int isSynced, String timeStamp, int school_id, int cluster_id, String remark, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("question_id", question_id);
        values.put("answer", answer);
        values.put("issynced", isSynced);
        values.put("timestamp", timeStamp);
        values.put("school_id", school_id);
        values.put("cluster_id", cluster_id);
        values.put("remark", remark);
        values.put("date", date);
        long id = db.insert(TABLE_NAME_submit, null, values);
        return id;
    }

    public long submitimage(int question_id, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.e("inside image", "submit");
        values.put("question_id", question_id);
        values.put("image", image);
        long id = db.insert(TABLE_NAME_submit, null, values);
        return id;
    }

    public long submitduration(int question_id, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.e("inside image", "submit");
        values.put("question_id", question_id);
        values.put("duration", duration);
        long id = db.insert(TABLE_NAME_submit, null, values);
        return id;
    }

    public boolean updateduration(int question_id, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.e("inside image", "submit");
        values.put("question_id", question_id);
        values.put("duration", duration);
        return db.update(TABLE_NAME_submit, values, "question_id  =" + question_id, null) > 0;

    }

    public long monitot_list(int input_type_id, int q_no, int is_image, String q_name, int q_id, String name_regional, int opg_id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("input_type_id", input_type_id);
        contentValues.put("q_no", q_no);
        contentValues.put("is_image", is_image);
        contentValues.put("q_name", q_name);
        contentValues.put("q_id", q_id);
        contentValues.put("name_regional", name_regional);
        contentValues.put("opg_id", opg_id);
        long id = sqLiteDatabase.insert(TABLE_NAME_monitot_list, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long survey_option_list(String input_type_id, String opc_id, String opg_id, String option_choices, String ques_id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("input_type_id", input_type_id);
        contentValues.put("opc_id", opc_id);
        contentValues.put("opg_id", opg_id);
        contentValues.put("option_choices", option_choices);
        contentValues.put("ques_id", ques_id);
        long id = sqLiteDatabase.insert(TABLE_NAME_Options, null, contentValues);
        sqLiteDatabase.close();
        return id;
    }

    public long survey_question_list(String input_type_id, String opg_id, String is_image, String question, String ques_no, String in_regional_language, String ques_id) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("input_type_id", input_type_id);
        contentValues.put("opg_id", opg_id);
        contentValues.put("is_image", is_image);
        contentValues.put("question", question);
        contentValues.put("ques_no", ques_no);
        contentValues.put("in_regional_language", in_regional_language);
        contentValues.put("ques_id", ques_id);
        long id = sqLiteDatabase.insert(TABLE_NAME_Question , null, contentValues);
        sqLiteDatabase.close();
        return id;

    }

    public Cursor getUserData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAMEU, null);
        return res;
    }
    public Cursor getLST() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME1, null);
        return res;
    }

    public Cursor getQuestion() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_Question, null);
        return res;
    }

    public Cursor getMonitor() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_monitot_list, null);
        return res;
    }

    public Cursor getCluster() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_cluster_details, null);
        return res;
    }

    public Cursor getSchools(int cluster_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_school_details + " where cluster_id = " + cluster_id, null);
        return res;
    }

    public Cursor getOption() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_Options, null);
        return res;
    }

    public Integer deleteData(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }

    public boolean id_already_exists(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_NAME_submit + " WHERE  question_id = " + id;
        Cursor cursor = db.rawQuery(selectString, null);

        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
        }

        cursor.close();
        return hasObject;
    }


    public boolean id_sid_cid_already_exists(int id, int school_id, int cluster_id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_NAME_submit + " WHERE  question_id = " + id + " AND school_id = " + school_id + " AND cluster_id = " + cluster_id;
        Cursor cursor = db.rawQuery(selectString, null);
        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
        }
        cursor.close();
        return hasObject;
    }


    public boolean idwiths_already_exists(int id, int school_id, int cluster_id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT * FROM " + TABLE_NAME_submit + " WHERE  question_id = " + id + " AND  school_id = " + school_id + " AND cluster_id = " + cluster_id;
        Cursor cursor = db.rawQuery(selectString, null);

        boolean hasObject = false;
        if (cursor.moveToFirst()) {
            hasObject = true;
            int count = 0;
            while (cursor.moveToNext()) {
                count++;
            }
        }

        cursor.close();
        return hasObject;
    }

    public boolean image_already_exists(int question_id) {
        SQLiteDatabase db = getWritableDatabase();
        String selectString = "SELECT image FROM " + TABLE_NAME_submit + " WHERE  question_id = " + question_id;
        Cursor cursor = db.rawQuery(selectString, null);

        boolean hasObject = false;
        if (cursor.getCount() > 0) {
            hasObject = true;

        }

        cursor.close();
        return hasObject;
    }

    public boolean updatedetails(int question_id, String answer, int isSynced, String timeStamp, int school_id, int cluster_id,String remark,String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("question_id", question_id);
        values.put("answer", answer);
        values.put("issynced", isSynced);
        values.put("timestamp", timeStamp);
        values.put("school_id", school_id);
        values.put("cluster_id", cluster_id);
        values.put("remark", remark);
        values.put("date", date);

        return db.update(TABLE_NAME_submit, values, "question_id  = " + question_id + " AND school_id = " + school_id + " AND cluster_id = " + cluster_id, null) > 0;
    }


    public boolean updateimage(int question_id, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("question_id", question_id);
        contentValues.put("imagestr", image);
        return db.update(TABLE_NAME_submit, contentValues, "question_id  =" + question_id, null) > 0;


    }


    public boolean updateIsSync(int school_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("issynced", 1);
        return db.update(TABLE_NAME_submit, contentValues, "school_id  =" + school_id, null) > 0;

    }






    /* public long submitSidCid(int school_id , int cluster_id){
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         Log.e("inside image", "submit");
         values.put("school_id", school_id);
         values.put("cluster_id", cluster_id);
         long id=  db.insert(TABLE_NAME_submit, null, values);
         return id;
     }*/
    public boolean updateSidCid(int question_id, int school_id, int cluster_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.e("inside image", "submit");
        values.put("school_id", school_id);
        values.put("cluster_id", cluster_id);
        return db.update(TABLE_NAME_submit, values, "question_id  =" + question_id, null) > 0;

    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_submit , null);
        return res;

    }


    public Cursor getIsImage() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_Question , null);
        return res;
    }

    public Cursor getDataForSchool(int school_id,int cluster_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_submit + " Where school_id = " + school_id + " AND cluster_id = " + cluster_id, null);
        return res;
    }



    public Cursor getSchoolName(int school_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_school_details+ " Where school_id = " + school_id , null);
        return res;
    }

}


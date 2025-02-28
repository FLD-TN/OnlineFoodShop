//package com.sinhvien.onlinefoodshop;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import androidx.annotation.Nullable;
//
//import com.sinhvien.onlinefoodshop.Model.UserModel;
//
//public class DatabaseHelper extends SQLiteOpenHelper{
//
//    public static final String databasename = "Register.db";
//    public DatabaseHelper(@Nullable Context context) {
//        super(context, "Register.db", null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase MyDatabase) {
//    MyDatabase.execSQL("create Table allusers(email TEXT primary key, password TEXT,fullname TEXT , phonenumber TEXT)");
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase MyDatabase, int i, int i1) {
//    MyDatabase.execSQL("drop Table if exists allusers");
//    onCreate(MyDatabase);
//    }
//    public Boolean insertData(String email, String password, String fullname, String phonenumber){
//        SQLiteDatabase MyDatabase = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("email", email);
//        contentValues.put("password", password);
//        contentValues.put("fullname", fullname);
//        contentValues.put("phonenumber", phonenumber);
//        long result = MyDatabase.insert("allusers",null,contentValues);
//
//        if(result==-1){
//            return false;
//        }else{
//            return true;
//        }
//
//        }
//        public boolean checkEmail(String email){
//        SQLiteDatabase MyDatabase = this.getWritableDatabase();
//        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ?", new String[]{email});
//        if(cursor.getCount()>0){
//            return true;
//        }else{
//            return false;
//        }
//        }
//        public boolean checkEmailPassword(String email,String password){
//        SQLiteDatabase MyDatabase = this.getWritableDatabase();
//        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where email = ? and password = ?", new String[]{email,password});
//
//        if(cursor.getCount()>0){
//            return true;
//        }else{
//            return false;
//        }
//        }
//    public UserModel getUserByEmail(String email) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        UserModel user = null;
//
//        Cursor cursor = db.rawQuery("SELECT * FROM allusers WHERE email = ?", new String[]{email});
//        if (cursor.moveToFirst()) {
//            String fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
//            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("phonenumber"));
//            String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
//
//            user = new UserModel(email, fullName, phoneNumber, password);
//        }
//        cursor.close();
//        db.close();
//        return user;
//    }
//
//
//}
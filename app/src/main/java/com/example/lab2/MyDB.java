package com.example.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDB extends SQLiteOpenHelper {
    public static final String TableName = "ContactTable";
    public static final String id = "Id";

    public static final String name = "Name";
    public static final String phone = "PhoneNumber";
    public static final String image = "Image";

    public MyDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "Create table if not exists " + TableName + "("
                + id + " Interger Primary key, "
                + name + " Text, "
                + phone + " Text, "
                + image + " Text)";
        //chạy câu truy vấn SQL để tạo bảng
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //xóa bảng TableContact đã có
        db.execSQL("Drop table if exists " + TableName);
        //Tạo lại
        onCreate(db);
    }
    public void addcontact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(id,contact.getId());
        values.put(name,contact.getName());
        values.put(phone,contact.getPhoneNumber());
        db.insert(TableName,null,values);
        db.close();
    }
    public ArrayList<Contact> getAllContact(){
        ArrayList<Contact> list = new ArrayList<>();
        //cau truy van
        String sql = "Select * from  " + TableName;
        //lay doi tuong csdl sqlite
        SQLiteDatabase db = this.getReadableDatabase();
        //chay cau truy van tra ve dang cursor
        Cursor cursor = db.rawQuery(sql,null);
        //tao arraylist<contact> de tra ve;
        if(cursor!=null)
            while (cursor.moveToNext()){
                Contact contact = new Contact(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2),
                        cursor.getString(3));
                list.add(contact);
            }
        return list;
    }
    public void updateContact(int id, Contact contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(String.valueOf(id), contact.getId());
        values.put(image,contact.getImage());
        values.put(name, contact.getName());
        values.put(phone, contact.getPhoneNumber());
        db.update(TableName,values,id + "=?",
                new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteContact(int id){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "Delete from " + TableName + " Where ID = " + id;
        db.execSQL(sql);
        db.close();
    }
}

package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String AUTH_TABLE = "users";
    private static final String POST_TABLE = "posts";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table when database is created
        db.execSQL("CREATE TABLE "+ AUTH_TABLE +" (id INTEGER PRIMARY KEY, name TEXT,email TEXT UNIQUE,password TEXT)");
        db.execSQL("CREATE TABLE "+ POST_TABLE +" (id INTEGER PRIMARY KEY, content TEXT,authId INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades here
        if (oldVersion < newVersion) {
            // For simplicity, we'll drop and recreate the tables
            db.execSQL("DROP TABLE IF EXISTS "+ AUTH_TABLE);
            db.execSQL("DROP TABLE IF EXISTS "+ POST_TABLE);
            onCreate(db);
        }
    }

    //create
    public long registerUser(String name,String email,String password) {
        //check email
        if(isEmailExists(email)){
            return -1;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password",password);
        long userId = db.insert(AUTH_TABLE, null, values);
        db.close();
        return userId;
    }

    // Check if a email already exists
    private boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(AUTH_TABLE, null,
                "email" + "=?", new String[]{email}, null, null, null);

        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    public Integer totoalUsers() {
        ArrayList<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+AUTH_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                dataList.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList.size();
    }

    // Check if a user with the given credentials exists
    public long loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(AUTH_TABLE, new String[]{"id"},
                "email" + "=? AND " + "password" + "=?",
                new String[]{email, password}, null, null, null);
        long userId = -1;
        if (cursor.moveToFirst()) {
            // Retrieve the user ID from the cursor
            int column_index = cursor.getColumnIndex("id");
           userId =  cursor.getLong(column_index);
        }
        cursor.close();
        db.close();
        return userId;
    }

    public void addPost(String content, long authId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", content);
        values.put("authId", authId);
        db.insert(POST_TABLE, null, values);
        db.close();
    }

    public Integer totoalPosts() {
        ArrayList<String> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+POST_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                dataList.add(name);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList.size();
    }

    public ArrayList<PostModel> getAllPost(long userId) {
        ArrayList<PostModel> dataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

//        Cursor cursor = db.rawQuery("SELECT * FROM "+POST_TABLE, null);

        Cursor cursor = db.query(POST_TABLE, null,
                "authId" + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String content = cursor.getString(1);
                Integer authId = cursor.getInt(2);
                dataList.add(new PostModel(id,content,authId));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dataList;
    }

    public void deletePost(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(POST_TABLE, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updatePost(int id, String content, int authId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", content);
        values.put("authId", authId);
        db.update(POST_TABLE, values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }


}
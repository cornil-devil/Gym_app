package com.example.androidproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "members_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(members_bean.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + members_bean.TABLE_NAME);

        onCreate(db);

    }

    public long insertMember(members_bean member){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(member.COLUMN_NAME,member.getName());
        values.put(member.COLUMN_ADDRESS,member.getAddress());
        values.put(member.COLUMN_CITY,member.getCity());
        values.put(member.COLUMN_DOB,member.getDob());
        values.put(member.COLUMN_CODE,member.getCode());
        values.put(member.COLUMN_VISITS,member.getVisits());
        values.put(member.COLUMN_PHOTO,member.getPhoto());
        values.put(member.COLUMN_STATUS,member.getStatus());

        Long id = db.insert(members_bean.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public members_bean getMember(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(members_bean.TABLE_NAME,
                new String[]{members_bean.COLUMN_ID, members_bean.COLUMN_NAME, members_bean.COLUMN_ADDRESS, members_bean.COLUMN_CITY, members_bean.COLUMN_CODE,
                        members_bean.COLUMN_DOB, members_bean.COLUMN_PHOTO, members_bean.COLUMN_VISITS,members_bean.COLUMN_STATUS},
                members_bean.COLUMN_ID + "= ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        members_bean member = new members_bean(
                cursor.getInt(cursor.getColumnIndex(members_bean.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_CITY)),
                cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_DOB)),
                cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_CODE)),
                cursor.getInt(cursor.getColumnIndex(members_bean.COLUMN_VISITS)),
                cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_PHOTO)),
                cursor.getInt(cursor.getColumnIndex(members_bean.COLUMN_STATUS))
        );

        // close the db connection
        cursor.close();

        return member;
    }

    public ArrayList<members_bean> getAllMembers() {
        ArrayList<members_bean> member_list = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + members_bean.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                members_bean member = new members_bean(
                        cursor.getInt(cursor.getColumnIndex(members_bean.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_CITY)),
                        cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_DOB)),
                        cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_CODE)),
                        cursor.getInt(cursor.getColumnIndex(members_bean.COLUMN_VISITS)),
                        cursor.getString(cursor.getColumnIndex(members_bean.COLUMN_PHOTO)),
                        cursor.getInt(cursor.getColumnIndex(members_bean.COLUMN_STATUS)));

                        member_list.add(member);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return member_list;
    }

    public int getStudentCount() {
        String countQuery = "SELECT  * FROM " + members_bean.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateStudent(members_bean member) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(members_bean.COLUMN_STATUS, member.getStatus());
        values.put(members_bean.COLUMN_PHOTO,member.getPhoto());
        values.put(members_bean.COLUMN_VISITS,member.getVisits());
        // update row
        int id = db.update(members_bean.TABLE_NAME, values, members_bean.COLUMN_ID + "= ?",
                new String[]{String.valueOf(member.getId())});

        // close db connection
        db.close();

        // return updated row id
        return id;
    }

    public void hardDeleteMember(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(members_bean.TABLE_NAME, members_bean.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

//    public void softDeleteStudent(int id){
//        // get writable database as we want to write data
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        // `id` and `timestamp` will be inserted automatically.
//        // no need to add them
//        values.put(members_bean.COLUMN_STATUS, 0);
//
//        // update row
//        db.update(members_bean.TABLE_NAME, values, members_bean.COLUMN_ID + "= ?",
//                new String[]{String.valueOf(id)});
//
//        // close db connection
//        db.close();
//    }
}

package com.cqupt.handspringflower.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cqupt.handspringflower.MyApplication;

import java.util.List;

/**
 * Created by hhx on 2018/4/8.
 */

public class DBUtils {

    public static long insert(String[] args) {
        SQLiteDatabase db = MyApplication.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("createTime", System.currentTimeMillis());
        values.put("title", args[0]);
        values.put("time", args[1]);
        values.put("institute", args[2]);
        values.put("location", args[3]);
        values.put("content", args[4]);
        values.put("author", args[5]);
        values.put("isCreate", args[6]);
        values.put("isJoin", args[7]);
        values.put("isColl", args[8]);
        values.put("page", args[9]);
        values.put("imageId", args[10]);
        values.put("authorId", args[11]);
        return db.insert("Info", null, values);
    }

    public static void insert(List<String[]> list) {
        for(int i=0; i<list.size(); i++) {
            long res = insert(list.get(i));
            Log.e("hhx", "db insert (batch): " + res);
        }
    }

    public static Cursor query(String sql, String[] args) {
        SQLiteDatabase db = MyApplication.getWritableDatabase();
        return db.rawQuery(sql, args);
    }

    public static void update(String sql, String[] args) {
        SQLiteDatabase db = MyApplication.getWritableDatabase();
        db.execSQL(sql, args);
    }
}

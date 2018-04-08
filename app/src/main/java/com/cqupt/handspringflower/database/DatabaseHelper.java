package com.cqupt.handspringflower.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by hhx on 2018/4/8.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_INFO =
            "create table Info ("
                    + "createTime long primary key,"
                    + "title text,"
                    + "time text,"
                    + "institute text,"
                    + "location text,"
                    + "content text,"
                    + "author text,"
                    + "isCreate integer,"
                    + "isJoin integer,"
                    + "isColl integer,"
                    + "page integer,"
                    + "imageId integer,"
                    + "authorId integer)";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int
            version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_INFO);
        Log.e("hhx", "Create Succeed!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

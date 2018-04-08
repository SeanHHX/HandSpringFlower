package com.cqupt.handspringflower;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.cqupt.handspringflower.database.DatabaseHelper;

public class MyApplication extends Application{
    private static Context context;
    private static DatabaseHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        context.deleteDatabase("InfoStore.db");
        dbHelper = new DatabaseHelper(context, "InfoStore.db", null, 1);
    }

    public static Context getContext(){
        return context;
    }

    public static void showSoftInput(View view) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        manager.showSoftInput(view, 0);
    }

    public static SQLiteDatabase getWritableDatabase() {
        return dbHelper.getWritableDatabase();
    }

    public static SQLiteDatabase getReadableDatabase() {
        return dbHelper.getReadableDatabase();
    }
}

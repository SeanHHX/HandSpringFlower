package com.cqupt.handspringflower.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;

import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.search.SearchAdapter;

import java.util.List;

public class DialogUtils {

    public static void showDialog(Context context , String title , String message,
                                  String negative, String positive) {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negative, null);
        builder.setPositiveButton(positive, null);
        builder.setCancelable(false);
        builder.show();
    }

    public static void showSearchDialog(Context context , final List list, final SearchAdapter adapter,
                                        int drawableId, String title , String message,
                                        String negative, String positive) {
        android.support.v7.app.AlertDialog.Builder builder
                = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setIcon(drawableId);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setNegativeButton(negative, null);
        builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                list.clear();
                adapter.notifyDataSetChanged();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
}

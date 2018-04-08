package com.cqupt.handspringflower.utils;

import android.database.Cursor;
import android.util.Log;

import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.database.DBUtils;
import com.cqupt.handspringflower.main.ActivityItem;
import com.cqupt.handspringflower.search.SearchItem;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerUtils {

    //Later: Get from Server.
    // 主界面
    public static void getItems(List list) {
//        Log.e("RecycelerUtils", "==getItems");
        for (int i = 0; i < 10; i++) {
            ActivityItem item = new ActivityItem(
                    R.drawable.a_img_0, "活动名称" + i,
                    "活动时间", "学院", "运动场", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }

    public static void getItemsDB(List list, int page) {
        Cursor cursor = DBUtils.query("select * from Info where page = ?", new String[]{page+""});
        ActivityItem item;
        if (cursor.moveToFirst()) {
            do {
                int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String institute = cursor.getString(cursor.getColumnIndex("institute"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                int authorId = cursor.getInt(cursor.getColumnIndex("authorId"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                item = new ActivityItem(imageId, title, time,
                        institute, location, content, authorId, author);
                if(page == 0) {
                    list.add(0, item);
                } else {
                    list.add(item);
                }
            } while (cursor.moveToNext());
        }
    }

    // 创建
    public static void getCreateItems(List list) {
//        Log.e("RecycelerUtils", "==getCreateItems");
        for (int i = 0; i < 10; i++) {
            ActivityItem item = new ActivityItem(
                    R.drawable.a_img_1, "活动名称" + i,
                    "活动时间", "学院", "运动场", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }

    public static void getCreateItemsDB(List list) {
        Cursor cursor = DBUtils.query("select * from Info where isCreate = ?", new String[]{"1"});
        ActivityItem item;
        if (cursor.moveToFirst()) {
            do {
                int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String institute = cursor.getString(cursor.getColumnIndex("institute"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                int authorId = cursor.getInt(cursor.getColumnIndex("authorId"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                Log.e("hhx", "args[10]-args[11]: " + imageId + "-" + authorId);
                item = new ActivityItem(imageId, title, time,
                        institute, location, content, authorId, author);
                list.add(0, item);
            } while (cursor.moveToNext());
        }
    }

    // 加入
    public static void getJoinItems(List list) {
//        Log.e("RecycelerUtils", "==getJoinItems");
        for (int i = 0; i < 10; i++) {
            ActivityItem item = new ActivityItem(
                    R.drawable.a_img_2, "活动名称" + i,
                    "活动时间", "学院", "运动场", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }

    public static void getJoinItemsDB(List list) {
        Cursor cursor = DBUtils.query("select * from Info where isJoin = ?", new String[]{"1"});
        ActivityItem item;
        if (cursor.moveToFirst()) {
            do {
                int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String institute = cursor.getString(cursor.getColumnIndex("institute"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                int authorId = cursor.getInt(cursor.getColumnIndex("authorId"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                item = new ActivityItem(imageId, title, time,
                        institute, location, content, authorId, author);
                list.add(0, item);
            } while (cursor.moveToNext());
        }
    }

    // 收藏
    public static void getCollItems(List list) {
//        Log.e("RecycelerUtils", "==getCollItems");
        for (int i = 0; i < 10; i++) {
            ActivityItem item = new ActivityItem(
                    R.drawable.a_img_3, "活动名称" + i,
                    "活动时间", "学院", "运动场", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }

    public static void getCollItemsDB(List list) {
        Cursor cursor = DBUtils.query("select * from Info where isColl = ?", new String[]{"1"});
        ActivityItem item;
        if (cursor.moveToFirst()) {
            do {
                int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String institute = cursor.getString(cursor.getColumnIndex("institute"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                int authorId = cursor.getInt(cursor.getColumnIndex("authorId"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                item = new ActivityItem(imageId, title, time,
                        institute, location, content, authorId, author);
                list.add(0, item);
            } while (cursor.moveToNext());
        }
    }

    // 搜索历史
    public static void getHisItems(List list) {
        Log.e("RecycelerUtils", "==getHisItems");
        /*for (int i = 0; i < 5; i++) {
            SearchItem item = new SearchItem("搜索历史" + i);
            list.add(item);
        }*/
    }

    // 搜索建议
    public static void getSugItems(List list) {
        Log.e("RecycelerUtils", "==getSugItems");
        for (int i = 0; i < 5; i++) {
            SearchItem item = new SearchItem("搜索建议" + i);
            list.add(item);
        }
    }

    public static void getSugItemsDB(List list, String input) {
        List<String> list0 = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        // 按title检索
        Cursor cursor = DBUtils.query("select title from Info where title like ?", new String[]{"%" + input + "%"});
        SearchItem item;
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex("title"));
                list0.add(0, title);
            } while (cursor.moveToNext());
        }
        // 按author检索
        Cursor cursorAuthor = DBUtils.query("select author from Info where author like ?", new String[]{"%" + input + "%"});
        if (cursorAuthor.moveToFirst()) {
            do {
                String author = cursorAuthor.getString(cursorAuthor.getColumnIndex("author"));
                list0.add(0, author);
            } while (cursorAuthor.moveToNext());
        }

        int size = 10;
        for(int i=0; i< (list0.size() <= size ? list0.size() : size); i++) {
            if(!list1.contains(list0.get(i))) {
                list1.add(list0.get(i));
            }
        }

        for(int i=0; i< (list1.size() <= size ? list1.size() : size); i++) {
            item = new SearchItem(list1.get(i));
            list.add(item);
        }
    }

    // 搜索结果
    public static void getResultItems(List list) {
        Log.e("RecycelerUtils", "==getResultItems");
        for (int i = 0; i < 10; i++) {
            ActivityItem item = new ActivityItem(
                    R.drawable.a_img_0, "活动名称" + i,
                    "活动时间", "学院", "运动场", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }

    public static void getResultItemsDB(List list, String input) {
        Cursor cursor = DBUtils.query("select * from Info where title = ? or author = ?", new String[]{input, input});
        ActivityItem item;
        if (cursor.moveToFirst()) {
            do {
                int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String institute = cursor.getString(cursor.getColumnIndex("institute"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                int authorId = cursor.getInt(cursor.getColumnIndex("authorId"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                item = new ActivityItem(imageId, title, time,
                        institute, location, content, authorId, author);
                list.add(0, item);
            } while (cursor.moveToNext());
        }
    }

    public static List getDefaultItems() {
        List<String[]> list = new ArrayList<>();
        String[] args = new String[12];
        args[0] = "重邮梦";
        args[1] = "2017-05-16";
        args[2] = "计算机科学与技术";
        args[3] = "太极操场";
        args[4] = "校运动会表演节目";
        args[5] = "童真";
        args[6] = "0";
        args[7] = "1";
        args[8] = "0";
        args[9] = "0";
        args[10] = R.drawable.img_0 + "";
        args[11] = R.drawable.author_0 + "";
        list.add(args);
        String[] args1 = new String[12];
        args1[0] = "运动激情";
        args1[1] = "2017-9-20";
        args1[2] = "通信工程学院";
        args1[3] = "风雨操场";
        args1[4] = "院表演节目，展现我院风采";
        args1[5] = "折花几暮";
        args1[6] = "0";
        args1[7] = "1";
        args1[8] = "0";
        args1[9] = "0";
        args1[10] = R.drawable.img_1 + "";
        args1[11] = R.drawable.author_1 + "";
        list.add(args1);
        String[] args2 = new String[12];
        args2[0] = "我们一起来";
        args2[1] = "2017-6-13";
        args2[2] = "计算机科学与技术";
        args2[3] = "太极操场";
        args2[4] = "院表演节目，舞动吧青春";
        args2[5] = "huanghx";
        args2[6] = "1";
        args2[7] = "0";
        args2[8] = "0";
        args2[9] = "0";
        args2[10] = R.drawable.img_2 + "";
//        args2[11] = R.drawable.author_2 + "";
        args2[11] = R.drawable.author_5 + "";
        list.add(args2);
        String[] args3 = new String[12];
        args3[0] = "魅力无限";
        args3[1] = "2017-7-26";
        args3[2] = "外国语学院";
        args3[3] = "老运动场";
        args3[4] = "院运动会表演节目，全员参加";
        args3[5] = "独言";
        args3[6] = "0";
        args3[7] = "1";
        args3[8] = "0";
        args3[9] = "0";
        args3[10] = R.drawable.img_3 + "";
        args3[11] = R.drawable.author_3 + "";
        list.add(args3);
        String[] args4 = new String[12];
        args4[0] = "致青春";
        args4[1] = "2017-4-22";
        args4[2] = "通信工程学院";
        args4[3] = "老运动场";
        args4[4] = "校运动会表演节目";
        args4[5] = "浅月流歌";
        args4[6] = "0";
        args4[7] = "1";
        args4[8] = "0";
        args4[9] = "0";
        args4[10] = R.drawable.img_4 + "";
        args4[11] = R.drawable.author_4 + "";
        list.add(args4);
        String[] args5 = new String[12];
        args5[0] = "追梦赤子心";
        args5[1] = "2017-4-22";
        args5[2] = "计算机科学与技术";
        args5[3] = "太极操场";
        args5[4] = "校运动会表演节目，加入改节目的同学请自觉加群";
        args5[5] = "huanghx";
        args5[6] = "1";
        args5[7] = "0";
        args5[8] = "0";
        args5[9] = "0";
        args5[10] = R.drawable.img_5 + "";
        args5[11] = R.drawable.author_5 + "";
        list.add(args5);
        String[] args6 = new String[12];
        args6[0] = "南湖秋月";
        args6[1] = "2017-5-8";
        args6[2] = "软件工程学院";
        args6[3] = "太极操场";
        args6[4] = "院运动会表演节目";
        args6[5] = "颜如舜华";
        args6[6] = "0";
        args6[7] = "1";
        args6[8] = "0";
        args6[9] = "0";
        args6[10] = R.drawable.img_6 + "";
        args6[11] = R.drawable.author_6 + "";
        list.add(args6);
        String[] args7 = new String[12];
        args7[0] = "追梦";
        args7[1] = "2018-3-14";
        args7[2] = "计算机科学与技术";
        args7[3] = "太极操场";
        args7[4] = "校运动会表演节目";
        args7[5] = "纸风铃つ";
        args7[6] = "0";
        args7[7] = "1";
        args7[8] = "0";
        args7[9] = "1";
        args7[10] = R.drawable.img_7 + "";
        args7[11] = R.drawable.author_7 + "";
        list.add(args7);
        String[] args8 = new String[12];
        args8[0] = "青春宣言";
        args8[1] = "2018-3-14";
        args8[2] = "光电学院";
        args8[3] = "老运动场";
        args8[4] = "院运动会表演节目";
        args8[5] = "挽歌渐起";
        args8[6] = "0";
        args8[7] = "0";
        args8[8] = "1";
        args8[9] = "1";
        args8[10] = R.drawable.img_8 + "";
        args8[11] = R.drawable.author_8 + "";
        list.add(args8);
        String[] args9 = new String[12];
        args9[0] = "舞起来";
        args9[1] = "2017-12-14";
        args9[2] = "通信工程学院";
        args9[3] = "太极操场";
        args9[4] = "院运动会表演节目";
        args9[5] = "入云栖";
        args9[6] = "0";
        args9[7] = "1";
        args9[8] = "1";
        args9[9] = "1";
        args9[10] = R.drawable.img_9 + "";
        args9[11] = R.drawable.author_9 + "";
        list.add(args9);
        String[] args10 = new String[12];
        args10[0] = "点亮重邮";
        args10[1] = "2017-12-14";
        args10[2] = "通信工程学院";
        args10[3] = "老运动场";
        args10[4] = "院运动会表演节目";
        args10[5] = "花落叶冷";
        args10[6] = "0";
        args10[7] = "1";
        args10[8] = "1";
        args10[9] = "1";
        args10[10] = R.drawable.img_10 + "";
        args10[11] = R.drawable.author_10 + "";
        list.add(args10);
        String[] args11 = new String[12];
        args11[0] = "策马奔腾";
        args11[1] = "2017-12-14";
        args11[2] = "光电工程学院";
        args11[3] = "老运动场";
        args11[4] = "校运动会表演节目";
        args11[5] = "梦醉为红颜丶";
        args11[6] = "0";
        args11[7] = "0";
        args11[8] = "1";
        args11[9] = "1";
        args11[10] = R.drawable.img_11 + "";
        args11[11] = R.drawable.author_11 + "";
        list.add(args11);
        return list;
    }
}

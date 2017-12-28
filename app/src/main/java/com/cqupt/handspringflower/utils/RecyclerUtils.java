package com.cqupt.handspringflower.utils;

import android.util.Log;

import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.main.ActivityItem;
import com.cqupt.handspringflower.search.SearchItem;

import org.json.JSONObject;

import java.util.List;

public class RecyclerUtils {

    //Later: Get from Server.
    // 主界面
    public static void getItems(List list) {
//        Log.e("RecycelerUtils", "==getItems");
        for(int i=0; i<10; i++) {
            ActivityItem item =  new ActivityItem(R.drawable.a_img_0, "活动名称"+i, "活动时间", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }

    // 创建
    public static void getCreateItems(List list) {
//        Log.e("RecycelerUtils", "==getCreateItems");
        for(int i=0; i<10; i++) {
            ActivityItem item =  new ActivityItem(R.drawable.a_img_1, "活动名称"+i, "活动时间", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }

    // 加入
    public static void getJoinItems(List list) {
//        Log.e("RecycelerUtils", "==getJoinItems");
        for(int i=0; i<10; i++) {
            ActivityItem item =  new ActivityItem(R.drawable.a_img_2, "活动名称"+i, "活动时间", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }

    // 收藏
    public static void getCollItems(List list) {
//        Log.e("RecycelerUtils", "==getCollItems");
        for(int i=0; i<10; i++) {
            ActivityItem item =  new ActivityItem(R.drawable.a_img_3, "活动名称"+i, "活动时间", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }

    // 搜索历史
    public static void getHisItems(List list) {
        Log.e("RecycelerUtils", "==getHisItems");
        for(int i=0; i<5; i++) {
            SearchItem item = new SearchItem("搜索历史"+i);
            list.add(item);
        }
    }

    // 搜索建议
    public static void getSugItems(List list) {
        Log.e("RecycelerUtils", "==getSugItems");
        for(int i=0; i<5; i++) {
            SearchItem item = new SearchItem("搜索建议"+i);
            list.add(item);
        }
    }

    // 搜索结果
    public static void getResultItems(List list) {
        Log.e("RecycelerUtils", "==getResultItems");
        for(int i=0; i<10; i++) {
            ActivityItem item =  new ActivityItem(R.drawable.a_img_0, "活动名称"+i, "活动时间", "活动内容",
                    R.drawable.ic_author_m, "创建者");
            list.add(item);
        }
    }
}

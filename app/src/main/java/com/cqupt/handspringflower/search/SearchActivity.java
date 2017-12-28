package com.cqupt.handspringflower.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;

import com.cqupt.handspringflower.BaseActivity;
import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.main.ActivityItem;
import com.cqupt.handspringflower.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

    private static SearchActivity instance;

    private List<SearchItem> mListHis = new ArrayList<>();
    private List<SearchItem> mListSug = new ArrayList<>();
    private List<ActivityItem> mListRes = new ArrayList<>();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        instance = this;

        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        // 获取SearchView 中的EditView
        int editId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText editText = (EditText) searchView.findViewById(editId);

        final RecyclerView recyclerView = (RecyclerView)
                findViewById(R.id.recycler_search_main_history);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        final SearchResAdapter searchResAdapter = new SearchResAdapter(mListRes);
        final SearchAdapter searchAdapter = new SearchAdapter(mListHis, editText,
                recyclerView, searchResAdapter, mListRes);
        final SearchSugAdapter searchSugAdapter = new SearchSugAdapter(mListSug, editText,
                recyclerView, searchResAdapter, mListRes, mListHis);
        recyclerView.setLayoutManager(layoutManager);

        // At first history is empty.
        RecyclerUtils.getHisItems(mListHis);
        recyclerView.setAdapter(searchAdapter);

        // 设置搜索框中搜索图标的位置
        searchView.onActionViewExpanded();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Later: Send to server.
                SearchItem item = new SearchItem(query);
                mListHis.add(0, item);
                mListRes.clear();
                RecyclerUtils.getResultItems(mListRes);
                recyclerView.setAdapter(searchResAdapter);
                hideKeyBoard();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)) {
                    recyclerView.setAdapter(searchAdapter);
                } else {
                    if(mListSug.size() != 0) mListSug.clear();
                    RecyclerUtils.getSugItems(mListSug);
                    recyclerView.setAdapter(searchSugAdapter);
                }
                return true;
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.search_main_return);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static SearchActivity getInstance() {
        return instance;
    }

    // 显示或隐藏键盘(在adapter中使用)，<SearchActivity.getInstance>
    public static void hideKeyBoard() {
        InputMethodManager inputMethodManager = (InputMethodManager)
                SearchActivity.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}

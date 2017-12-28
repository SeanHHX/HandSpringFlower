package com.cqupt.handspringflower.message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cqupt.handspringflower.BaseActivity;
import com.cqupt.handspringflower.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageActivity extends BaseActivity {

    private List<MessageItem> mList = new ArrayList<>();

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_message);
        toolbar.setTitle(getString(R.string.message));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerMessage = (RecyclerView) findViewById(R.id.recycler_list_message);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerMessage.setLayoutManager(manager);
        getItem();
        MessageAdapter adapter = new MessageAdapter(mList);
        recyclerMessage.setAdapter(adapter);
    }

    private void getItem() {
        MessageItem item;
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy年MM月dd日 HH:m", Locale.getDefault());
        for(int i=0; i<10; i++) {
            Date curDate = new Date(System.currentTimeMillis());
            String currentTime = formatter.format(curDate);
            item = new MessageItem(currentTime, "这是第"+i+"条通知，你可以在这里查看情况");
            mList.add(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}

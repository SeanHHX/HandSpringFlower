package com.cqupt.handspringflower.main;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cqupt.handspringflower.BaseActivity;
import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.utils.LogUtil;

public class InfoActivity extends BaseActivity
        implements View.OnClickListener{

    public static final String ACTIVITY_IMAGE_ID = "activity_image_id";
    public static final String ACTIVITY_TITLE = "activity_title";

    public static void actionStart(Context context, int id, String title) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra(ACTIVITY_IMAGE_ID, id);
        intent.putExtra(ACTIVITY_TITLE, title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_info);
        CollapsingToolbarLayout collapsingToobar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toobar);
        collapsingToobar.setTitle(getString(R.string.activity_info_label));

        ImageView imageToolbar = (ImageView) findViewById(R.id.image_info_toolbar);
        TextView textTitleInfo = (TextView) findViewById(R.id.text_title_info);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Get data from intent.
        Intent intent = getIntent();
        int activityImageId = intent.getIntExtra(ACTIVITY_IMAGE_ID, 0);
        String activityTitle = intent.getStringExtra(ACTIVITY_TITLE);
        Glide.with(this).load(activityImageId).into(imageToolbar);
        textTitleInfo.setText(activityTitle);

        // 活动相关操作
        FloatingActionButton fabComment = (FloatingActionButton) findViewById(R.id.fab_comment);
        fabComment.setOnClickListener(this);
        AppCompatButton buttonLook = (AppCompatButton) findViewById(R.id.info_button_look);
        buttonLook.setOnClickListener(this);
        AppCompatButton buttonCollection = (AppCompatButton) findViewById(R.id.info_button_collection);
        buttonCollection.setOnClickListener(this);
        AppCompatButton buttonJoin = (AppCompatButton) findViewById(R.id.info_button_join);
        buttonJoin.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_comment:
                Toast.makeText(this, "评论功能待完善~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_button_look:
                Toast.makeText(this, "查看详情，功能待完善~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_button_collection:
                Toast.makeText(this, "收藏，功能待完善~", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info_button_join:
                Toast.makeText(this, "加入，功能待完善~", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}

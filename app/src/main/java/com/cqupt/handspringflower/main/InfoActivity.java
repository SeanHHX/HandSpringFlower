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

import de.hdodenhof.circleimageview.CircleImageView;

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

    public static void actionStart(Context context, Bundle bundle) {
        Intent intent = new Intent(context, InfoActivity.class);
        intent.putExtra("info", bundle);
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
        CircleImageView imageAuthor = (CircleImageView) findViewById(R.id.image_author_info);
        TextView textAuthor = (TextView) findViewById(R.id.text_author_info);
        TextView textTime = (TextView) findViewById(R.id.text_time_info);
        TextView textInstitute = (TextView) findViewById(R.id.text_owner_info);
        TextView textLocation = (TextView) findViewById(R.id.text_location_info);
        TextView textContent = (TextView) findViewById(R.id.text_content_info);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Get data from intent.
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("info");
        int activityImageId = bundle.getInt("image_id");
        String activityTitle = bundle.getString("title");
        String activityTime = bundle.getString("time");
        String institute = bundle.getString("institute");
        String location = bundle.getString("location");
        String content = bundle.getString("content");
        int authorAvatarId = bundle.getInt("author_id");
        String author = bundle.getString("author");

        Glide.with(this).load(activityImageId).into(imageToolbar);
        textTitleInfo.setText(activityTitle);
        Glide.with(this).load(authorAvatarId).into(imageAuthor);
        textAuthor.setText(author);
        textTime.setText(activityTime);
        textInstitute.setText(institute);
        textLocation.setText(location);
        textContent.setText(content);

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

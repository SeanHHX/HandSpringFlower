package com.cqupt.handspringflower.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cqupt.handspringflower.ActivityCollector;
import com.cqupt.handspringflower.BaseActivity;
import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.create.CreateActivity;
import com.cqupt.handspringflower.login_register.LoginActivity;
import com.cqupt.handspringflower.login_register.LoginRegister;
import com.cqupt.handspringflower.message.MessageActivity;
import com.cqupt.handspringflower.personal.PersonalActivity;
import com.cqupt.handspringflower.profile.ProfileActivity;
import com.cqupt.handspringflower.search.SearchActivity;
import com.cqupt.handspringflower.utils.HttpUtils;
import com.cqupt.handspringflower.utils.LogUtil;
import com.cqupt.handspringflower.utils.RecyclerUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener{

    public static final String TAG = "MainActivity";
    public static final String ACTIVITY_LOAD_URL
            = "http://118.89.29.87:8080/InternetPlus/activity/loadActivities";
    public static final int MSG_LOAD_SUCCESS = 0;
    public static final int MSG_LOAD_FAILURE = 1;

    private DrawerLayout mDrawerLayout;
    private List<ActivityItem> mList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private AppCompatButton mChangeUser;
    private AppCompatButton mQuitUser;

    private CircleImageView mCircleImageView;
    private TextView mTextViewName;
    private TextView mTextViewEmail;

    private SharedPreferences sp;

    private boolean hasMore = true;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        // 登录之后清空任务栈，并新建任务栈
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void actionStart(Context context, String email) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("user_email", email);
        // 登录之后清空任务栈，并新建任务栈
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar.setTitle(getString(R.string.activity_main_label));
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_nav_m);
        }

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        // NavigationView顶部按钮
        View headView = navView.getHeaderView(0);
        mCircleImageView = (CircleImageView) headView.findViewById(R.id.image_user_avatar);
        mTextViewEmail = (TextView) headView.findViewById(R.id.text_user_email);
        mTextViewName = (TextView) headView.findViewById(R.id.text_user_name);
        mCircleImageView.setOnClickListener(this);
        mTextViewEmail.setOnClickListener(this);
        mTextViewName.setOnClickListener(this);
        Intent intent = getIntent();
        mTextViewEmail.setText(intent.getStringExtra("user_email"));

        // NavigationView底部按钮
        AppCompatButton change = (AppCompatButton) navView.findViewById(R.id.change_user);
        change.setOnClickListener(this);
        AppCompatButton quit = (AppCompatButton) navView.findViewById(R.id.quit_user);
        quit.setOnClickListener(this);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(this);

//        RecyclerUtils.getItems(mList);
        RecyclerUtils.getItemsDB(mList, 0);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_list_main);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ActivityAdapter(mList);
        recyclerView.setAdapter(mAdapter);

        final SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refesh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
         // 下拉刷新
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mList.clear();
//                                RecyclerUtils.getItems(mList);
                                RecyclerUtils.getItemsDB(mList, 0);
                                /*HttpUtils.sendOkHttpRequest(ACTIVITY_LOAD_URL, createJsonRefresh(mList), new Callback() {
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        if(response.isSuccessful()) {
                                            String responseString = response.body().string();
                                            LogUtil.e("main_response:", responseString);
                                            List<InteractionActivity> list = parseJson(responseString);

                                            if(list.size()>0) {
                                                Message message = new Message();
                                                message.what = MSG_LOAD_SUCCESS;
                                                // list展示数据
                                                message.obj = list;
                                                mHandler.sendMessage(message);
                                            } else {
                                                Message message = new Message();
                                                message.what = MSG_LOAD_FAILURE;
                                                mHandler.sendMessage(message);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        call.cancel();
                                        Message message = new Message();
                                        message.what = MSG_LOAD_FAILURE;
                                        mHandler.sendMessage(message);
                                    }
                                });*/
                                mAdapter.notifyDataSetChanged();
                                swipeRefresh.setRefreshing(false);
                                hasMore = true;
                            }
                        });
                    }
                }).start();
            }
        });

        // 上拉更多
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if ((lastVisibleItemPosition+1) == mAdapter.getItemCount()) {
                    if(hasMore) {
                        hasMore = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int before = mAdapter.getItemCount();
                                        Log.e("hhx", "before: " + before);
                                        RecyclerUtils.getItemsDB(mList, 1);
                                        mAdapter.notifyDataSetChanged();
//                                        mAdapter.notifyItemRemoved(before-1);
//                                        recyclerView.smoothScrollToPosition(before-1);
                                    }
                                });
                            }
                        }).start();
                    } else {
                        Log.e("hhx", "底部更新");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAdapter.notifyItemChanged(mList.size(), 0);
                                    }
                                });
                            }
                        }).start();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // 切换账号及退出程序
        mChangeUser = (AppCompatButton) findViewById(R.id.change_user);
        mChangeUser.setOnClickListener(this);
        mQuitUser = (AppCompatButton) findViewById(R.id.quit_user);
        mQuitUser.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sp = getSharedPreferences("profile", MODE_PRIVATE);
        String petname = sp.getString("petname", "");
        String uriStr = sp.getString("image_uri", "");
        mTextViewName.setText(petname);
        if (uriStr != null && !uriStr.equals("")) {
            Uri uri = Uri.parse(uriStr);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(uri));
                mCircleImageView.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("hhx", Log.getStackTraceString(e));
                mCircleImageView.setImageResource(R.drawable.ic_avatar_m);
            }
        } else {
            mCircleImageView.setImageResource(R.drawable.ic_avatar_m);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what) {
               case MSG_LOAD_SUCCESS:
                   /*List<InteractionActivity> list =
                           ( List<InteractionActivity>) msg.obj;

                   LogUtil.e("list.size", "===="+list.size());
                   for(int i=0; i<list.size(); i++) {

                       InteractionActivity activity = list.get(i);
                       Integer aid = activity.getAid();
                       String title = activity.getTitle();
                       String time = activity.getTime().toString();
                       String content = activity.getIntroduce();
                       // 用aid将InteractionActivity和ActivityItem连接起来
                       ActivityItem item = new ActivityItem(R.drawable.sky0, title, time, content,
                               R.drawable.ic_author_m, "匿名创建");
                       mList.add(item);

                       // ================================
                   }*/
                   break;
               case MSG_LOAD_FAILURE:

                   break;
               default:
                   break;
           }
        }
    };

    private String createJsonRefresh(List list) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("currentPageNum", list.size() % 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonData = jsonObject.toString();
        return jsonData;
    }

    private String createJsonLoadMore(List list) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("currentPageNum", list.size() % 8 + 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonData = jsonObject.toString();
        return jsonData;
    }

    private List<InteractionActivity> parseJson(String jsonData) {
        List<InteractionActivity> activityList = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            activityList = (List<InteractionActivity>) jsonObject.get("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activityList;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.image_user_avatar:
            case R.id.text_user_email:
            case R.id.text_user_name:
                mDrawerLayout.closeDrawers();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(300);
                        } catch(Exception e) {
                        }
                        // 在子线程中跳转页面(但不能更新UI)
//                        ProfileActivity.actionStart(MainActivity.this);
                        // 在ProfileActivity不能调用startActivityForResult
                        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                        startActivityForResult(intent, 1);
                    }
                }).start();
                break;
            case R.id.change_user:
                // 切换账号
                Intent[] intents = new Intent[2];
                intents[0] = new Intent(MainActivity.this, LoginRegister.class);
                intents[1] = new Intent(MainActivity.this, LoginActivity.class);
                //回到登录界面，清空任务栈并新建任务栈
                intents[0].setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivities(intents);
                break;
            case R.id.quit_user:
                // 退出程序
                ActivityCollector.finishAll();
                break;
            case R.id.fab_add:
                /*Snackbar.make(view, getString(R.string.fab_add), Toast.LENGTH_SHORT)
                        .setAction(getString(R.string.fab_undo), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, getString(R.string.fab_cancel),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).show();*/
                CreateActivity.actionStart(MainActivity.this);
                break;
            default:
                break;
        }
    }

    /**
     * 没有用到返回值，用sp替代.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                    /*String uriStr = data.getStringExtra("image_uri");
                    Log.e("hhx", "Image_Uri: " + uriStr);
                    if (uriStr != null && !uriStr.equals("")) {
                        Uri uri = Uri.parse(uriStr);
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(
                                    getContentResolver().openInputStream(uri));
                            mCircleImageView.setImageBitmap(bitmap);
//                            Glide.with(MainActivity.this).load(uri).into(mCircleImageView);
                        } catch (Exception e) {
                            Log.e("hhx", Log.getStackTraceString(e));
                            mCircleImageView.setImageResource(R.drawable.ic_avatar_m);
                        }
                    } else {
                        mCircleImageView.setImageResource(R.drawable.ic_avatar_m);
                    }
                    String userName = data.getStringExtra("user_name");
                    mTextViewName.setText(userName);*/
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        mDrawerLayout.closeDrawers();
        //先关闭drawer再页面跳转
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(300);
                }catch (Exception e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        switch (item.getItemId()) {
                            case R.id.create:
                                PersonalActivity.actionStart(MainActivity.this, 0);
                                break;
                            case R.id.join:
                                PersonalActivity.actionStart(MainActivity.this, 1);
                                break;
                            case R.id.collection:
                                PersonalActivity.actionStart(MainActivity.this, 2);
                                break;
                            case R.id.message:
                                Toast.makeText(MainActivity.this, "消息中心，该功能待完善~", Toast.LENGTH_SHORT).show();
//                                MessageActivity.actionStart(MainActivity.this);
                                break;
                        }
                    }
                });
            }
        }).start();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.search_main:
                SearchActivity.actionStart(MainActivity.this);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 监听返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 如果drawer存在，按返回键就先关闭它
             if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawers();
            } else {
                 // 否则就退到后台
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}


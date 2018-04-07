package com.cqupt.handspringflower.personal;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.cqupt.handspringflower.BaseActivity;
import com.cqupt.handspringflower.R;

public class PersonalActivity extends BaseActivity {

    public static String CurrentPage = "current_page";

    public static void actionStart(Context context, int page) {
        actionStart(context, page, null);
    }

    public static void actionStart(Context context, int page, Bundle bundle) {
        Intent intent = new Intent(context, PersonalActivity.class);
        intent.putExtra(CurrentPage, page);
        intent.putExtra("new_create", bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        // Set toolbar_menu_main.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_personal);
        toolbar.setTitle(getString(R.string.personal));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        int page = intent.getIntExtra(CurrentPage, 0);
        Bundle newCreate = intent.getBundleExtra("new_create");
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager_personal);
        PersonalFragmentAdapter fragmentAdapter = new PersonalFragmentAdapter(
                PersonalActivity.this, getSupportFragmentManager(), newCreate);
        viewPager.setAdapter(fragmentAdapter);
        // Set preload num.
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(page, true);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_personal);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

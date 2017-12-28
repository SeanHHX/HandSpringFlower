package com.cqupt.handspringflower.create;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.create.fragment.FirstCreateFragment;
import com.cqupt.handspringflower.create.fragment.MainCreateFragment;

public class CreateActivity extends AppCompatActivity {
    public static Fragment mFragment;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, CreateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame,mFragment=new FirstCreateFragment()).commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(mFragment instanceof MainCreateFragment&&keyCode==KeyEvent.KEYCODE_BACK)
        {
            return ((MainCreateFragment) mFragment).onKeyDown(keyCode);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mFragment instanceof MainCreateFragment)
            getMenuInflater().inflate(R.menu.menu_tool,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}

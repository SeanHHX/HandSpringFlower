package com.cqupt.handspringflower;

import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cqupt.handspringflower.login_register.LoginRegister;
import com.cqupt.handspringflower.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    public static final String FILE_NAME = "data";
    public static final String APP_FIRST_RUN = "first_run";
    // 切换账号和退出账号要重新回到First Run.
    //  1.Static 2.Server.
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Later: Server keep a num to check if it is first run.

        mSharedPreferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
//        boolean first_run = mSharedPreferences.getBoolean(APP_FIRST_RUN, true);
        // Enter to MainActivity. Delete Later.
        boolean first_run = mSharedPreferences.getBoolean(APP_FIRST_RUN, true);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        // Test: First Run(Delete later).
        editor.putBoolean(APP_FIRST_RUN, true);
        editor.apply();

        if(first_run == true) {
            // Test: true->LoginRegister, false->MainActivty.
//            editor.putBoolean(APP_FIRST_RUN, false);
//            editor.apply();

            new Handler().postDelayed(new Runnable() {
                public void run() {
                    LoginRegister.actionStart(SplashActivity.this);
                    finish();
                }
            }, 2000);
            // Enter to MainActivity.
        }  else {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MainActivity.actionStart(SplashActivity.this);
                    finish();
                }
            }, 2000);
        }
    }
}

package com.cqupt.handspringflower.login_register;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.cqupt.handspringflower.BaseActivity;
import com.cqupt.handspringflower.R;

public class LoginRegister extends BaseActivity implements View.OnClickListener{

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginRegister.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_register);
        Button buttonLogin = (Button) findViewById(R.id.button_switch_login);
        Button buttonRegister = (Button) findViewById(R.id.button_switch_register);
        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_switch_login:
                LoginActivity.actionStart(LoginRegister.this);
                break;
            case R.id.button_switch_register:
                RegisterActivity.actionStart(LoginRegister.this);
                break;
            default:
                break;
        }
    }

    // Press back key to set background instead of destroy application.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

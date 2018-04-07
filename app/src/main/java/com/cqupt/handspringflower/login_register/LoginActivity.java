package com.cqupt.handspringflower.login_register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cqupt.handspringflower.BaseActivity;
import com.cqupt.handspringflower.main.MainActivity;
import com.cqupt.handspringflower.utils.HttpUtils;
import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.utils.LogUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    ProgressDialog mProgressDialog;

    public static final String LOGIN_URL
            = "http://118.89.29.87:8080/InternetPlus/authorization/login";
    public static final int MESSAGE_OK_REQUEST = 0;
    public static final int MESSAGE_CANCEL_REQUEST = 1;
    public static final String TAG = "LoginActivity";

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.setTitle(getString(R.string.activity_login_label));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mEmailView = (EditText) findViewById(R.id.email_login);
        mPasswordView = (EditText) findViewById(R.id.password_login);
        //Set 'Enter' behaviour.
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.ime_login || id == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button signInButton = (Button) findViewById(R.id.button_sign_in);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
                SharedPreferences preferences = getSharedPreferences("Cookies_Prefs", 0);
                String s = preferences.getString("118.89.29.87", "");
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    LogUtil.e("Cookie_path", jsonObject.getString("path"));
                }catch (Exception e) {
                }
            }
        });

        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setMessage(getString(R.string.progress_load));
        mProgressDialog.setCancelable(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            //Email and password are valid.
            mProgressDialog.show();

            // Non-server login: default email and password.
            if (getString(R.string.user_email).equals(email)
                    && getString(R.string.user_pass).equals(password)) {
                Message message = new Message();
                message.what = MESSAGE_OK_REQUEST;
                mHandler.sendMessage(message);
            } else {
                Message message = new Message();
                message.what = MESSAGE_CANCEL_REQUEST;
                mHandler.sendMessage(message);
            }

            /*String jsonData = createJSON(email, password);
            HttpUtils.sendOkHttpRequest(LOGIN_URL, jsonData, new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()) {
                        String responseString = response.body().string();
                        LogUtil.e("LoginresponseString", responseString);
                        List<String> list = parseJSON(responseString);

                        if(list.size()>0 && list.get(0).equals("登录成功")) {
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message message = new Message();
                            message.what = MESSAGE_OK_REQUEST;
                            mHandler.sendMessage(message);
                        } else {
                            // 登录失败
                            Message message_cancel = new Message();
                            message_cancel.what = MESSAGE_CANCEL_REQUEST;
                            mHandler.sendMessage(message_cancel);
                        }
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();
                    try{
                        Thread.sleep(1000);
                    } catch(Exception e1) {
                        e1.printStackTrace();
                    }
                    Message message_cancel = new Message();
                    message_cancel.what = MESSAGE_CANCEL_REQUEST;
                    mHandler.sendMessage(message_cancel);
                }
            });*/
        }
    }

    private String createJSON(String value1, String value2) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", value1);
            jsonObject.put("password", value2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonData = jsonObject.toString();
        return jsonData;
    }

    private List<String> parseJSON(String jsonData) {
        List<String> list = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
//            list.add(jsonObject.getString("success"));
            if(!jsonObject.isNull("success")) {
                list.add("登录成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_OK_REQUEST:
                    mProgressDialog.dismiss();
                    MainActivity.actionStart(LoginActivity.this, mEmailView.getText().toString());
                    break;
                case MESSAGE_CANCEL_REQUEST:
                    mProgressDialog.dismiss();
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.internet_no_connecton), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private boolean isEmailValid(String email) {
        // Regular Expressions.
        String regex_email = "^(\\w+(-|\\.)?)+\\w@(\\w+(-\\w+)?\\.)+[\\w&&\\D]{2,}$";
        Pattern pattern = Pattern.compile(regex_email);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        //Regular Expressions.
        String regex_pass = "^(\\w|\\.){6,16}$";
        Pattern pattern = Pattern.compile(regex_pass);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    //Click all areas except EditText or input method area to hide softinput.
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            // How to void clicking parent view to lead to hide first then show ?
            if(isShouldHideInput(v, event)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean isShouldHideInput(View v, MotionEvent event) {
        if(v!=null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int right = left + v.getWidth();
            int bottom = top + v.getHeight();
            //If click EditText， shouldn't hide.
            return !(event.getX()>left && event.getX()<right
                    && event.getY()>top && event.getY()<bottom);
        }
        return false;
    }

    // Hide input method.
    private void hideSoftInput(IBinder token) {
        if(token != null) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}


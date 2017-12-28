package com.cqupt.handspringflower.login_register;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cqupt.handspringflower.BaseActivity;
import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.main.MainActivity;
import com.cqupt.handspringflower.utils.DialogUtils;
import com.cqupt.handspringflower.utils.HttpUtils;
import com.cqupt.handspringflower.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPassComfirmView;
    ProgressDialog mProgressDialog;

    public static final String TAG = "RegisterActivity";
    public static final int MESSAGE_OK_REQUEST_REG = 0;
    public static final int MESSAGE_CANCEL_REQUEST_REG = 1;
    public static final int MESSAGE_SUCCESS_RES = 2;
    public static final String REGISTER_URL = "http://118.89.29.87:8080/InternetPlus/register";

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        toolbar.setTitle(getString(R.string.activity_register_label));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mEmailView = (EditText) findViewById(R.id.email_register);
        mPasswordView = (EditText) findViewById(R.id.password_register);
        mPassComfirmView = (EditText) findViewById(R.id.pass_confirm_register);
        //Set 'Enter' behaviour.
        mPassComfirmView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == R.id.ime_register || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button registerButton = (Button) findViewById(R.id.button_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();
            }
        });

        mProgressDialog = new ProgressDialog(RegisterActivity.this);
        mProgressDialog.setMessage(getString(R.string.progress_register));
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

    private void attemptRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPassComfirmView.setError(null);

        // Store values at the time of the register attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String passConfirm = mPassComfirmView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if(TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if(!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password.
        if(TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(password)) {
            mPassComfirmView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for password equals confirm password.
        if(!passConfirm.equals(password)) {
            mPassComfirmView.setError(getString(R.string.error_unequals_password));
            focusView = mPassComfirmView;
            cancel = true;
        }

        if(cancel) {
            focusView.requestFocus();
        } else {
            mProgressDialog.show();
            String jsonData = createJSON(email, password);
            HttpUtils.sendOkHttpRequest(REGISTER_URL, jsonData, new okhttp3.Callback() {

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful()) {
                        String responseString = response.body().string();
                        LogUtil.e("responseString=======", responseString);
                        List<String> list = parseJSON(responseString);

                        if(list.size()>0 && list.get(0).equals("注册成功！")) {
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Message message = new Message();
                            message.what = MESSAGE_OK_REQUEST_REG;
                            mHandler.sendMessage(message);
                        } else {
                            // 注册失败
                            Message message_cancel = new Message();
                            message_cancel.what = MESSAGE_CANCEL_REQUEST_REG;
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
                    message_cancel.what = MESSAGE_CANCEL_REQUEST_REG;
                    mHandler.sendMessage(message_cancel);
                }
            });
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
            list.add(jsonObject.getString("success"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_OK_REQUEST_REG:
                    mProgressDialog.setMessage(
                            getString(R.string.dialog_register_success));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                Thread.sleep(1000);
                                Message message = new Message();
                                message.what = MESSAGE_SUCCESS_RES;
                                mHandler.sendMessage(message);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case MESSAGE_CANCEL_REQUEST_REG:
                    mProgressDialog.dismiss();
                    DialogUtils.showDialog(RegisterActivity.this ,null, getString(R.string.dialog_register_failure),
                            getString(R.string.dialog_cancel), getString(R.string.dialog_confirm));
                    break;
                case MESSAGE_SUCCESS_RES:
                    mProgressDialog.dismiss();
                    MainActivity.actionStart(RegisterActivity.this, mEmailView.getText().toString());
                    break;
                default:
                    break;
            }
        }
    };

    private boolean isEmailValid(String email) {
        //Regular Expressions.
        String regex_email = "^(\\w+(-|\\.)?)+\\w@(\\w+(-\\w+)?\\.)+[\\w&&\\D]{2,}$";
        Pattern pattern = Pattern.compile(regex_email);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        // Regular Expressions.
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

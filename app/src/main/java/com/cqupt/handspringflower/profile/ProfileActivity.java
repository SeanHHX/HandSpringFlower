package com.cqupt.handspringflower.profile;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cqupt.handspringflower.BaseActivity;
import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.login_register.LoginActivity;
import com.cqupt.handspringflower.main.MainActivity;
import com.cqupt.handspringflower.utils.DialogUtils;
import com.cqupt.handspringflower.utils.HttpUtils;
import com.cqupt.handspringflower.utils.LogUtil;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProfileActivity extends BaseActivity
        implements View.OnClickListener {

    public static final int TAKE_PHOTO = 0;
    public static final int CHOOSE_PHOTO = 1;
    public static final int CROP_PHOTO_CAPTURE = 2;
    public static final int CROP_PHOTO_ALBUM = 3;
    public static final int PERMISSION_SD_AVATAR = 10;
    public static final int PERMISSION_SD_BACKGROUND = 20;
    public static final int PERMISSION_CAMERA_AVATAR = 30;
    public static final int PERMISSION_CAMERA_BACK = 40;

    public static final int AVATAR_CHOOSE_ID = 11;
    public static final int BACK_CHOOSE_ID = 12;
    // mType: 标记是头像or侧边栏背景, 取值AVATAR_CHOOSE_ID or BACK_CHOOSE_ID
    private int mType;
    private ImageView mImageAvatar;
    private ImageView mImageBackground;
    private Uri imageUri; //拍照得到的uri及截图后的uri
    private Uri albumImageUri;    //相册中选择的图片的uri
    private Uri albumCropUri;    //裁剪选择的图片并获取uri
    private String albumImagePath;
    private File outputImage;    //拍照后存储路径
    private File cropOutput;    //裁剪后存储路径

    public static final int PETNAME_INPUT_ID = 13;
    public static final int REALNAME_INPUT_ID = 14;
    public static final int SIGN_INPUT_ID = 15;

    public static final int WORDS_TOTAL_SHORT = 16;
    public static final int WORDS_TOTAL_LONG = 42;

    public static final int MESSAGE_SAVE_SUCCESS = 0;
    public static final int MESSAGE_SAVE_FAILURE = 1;
    public static final int NOTIFICATION_HANGUP = 0;
    private ProgressDialog mProgressDialog;

    // 输入对话框
    private TextInputLayout mLayoutInput;
    private EditText mEditInput;
    private TextView mWordsCount;
    private TextView mTextPetname;
    private TextView mTextRealName;
    private TextView mTextSign;

    // 单选对话框
    private TextView mTextSex;

    private AlertDialog mAlertDialog;

    // 日期选择对话框
    private TextView mTextBirthday;
    private int mYear;
    private int mMonth;
    private int mDay;

    // 学历选择对话框
    private TextView mTextDegree;
    private ArrayList<String> mDegreeList;
    private String strNew;
    private int initPos;

    // 学校选择对话框
    private TextView mTextCollege;
    private AppCompatSpinner spinnerCollege;

    private SharedPreferences sp;
    private boolean isModified = false;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        toolbar.setTitle(getString(R.string.activity_profile_label));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.save_changes));
        mProgressDialog.setCancelable(false);

        // 头像|侧边栏
        RelativeLayout layoutAvatar = (RelativeLayout) findViewById(R.id.layout_avatar);
        layoutAvatar.setOnClickListener(this);
        RelativeLayout layoutBackground = (RelativeLayout) findViewById(R.id.layout_background);
        layoutBackground.setOnClickListener(this);

        mImageAvatar = (ImageView) findViewById(R.id.image_avatar);
        mImageBackground = (ImageView) findViewById(R.id.image_background);

        // 昵称|姓名|个性签名
        RelativeLayout layoutPetname = (RelativeLayout) findViewById(R.id.layout_petname);
        layoutPetname.setOnClickListener(this);
        RelativeLayout layoutRealName = (RelativeLayout) findViewById(R.id.layout_realname);
        layoutRealName.setOnClickListener(this);
        RelativeLayout layoutSign = (RelativeLayout) findViewById(R.id.layout_sign);
        layoutSign.setOnClickListener(this);
        // 显示对话框输入内容
        mTextPetname = (TextView) findViewById(R.id.text_petname);
        mTextRealName = (TextView) findViewById(R.id.text_realname);
        mTextSign = (TextView) findViewById(R.id.text_sign);

        // 性别选择
        RelativeLayout layoutSex = (RelativeLayout) findViewById(R.id.layout_sex);
        layoutSex.setOnClickListener(this);
        mTextSex = (TextView) findViewById(R.id.text_sex);

        // 生日选择
        RelativeLayout layoutBirthday = (RelativeLayout) findViewById(R.id.layout_birthday);
        layoutBirthday.setOnClickListener(this);
        mTextBirthday = (TextView) findViewById(R.id.text_birthday);

        // 学历选择
        RelativeLayout layoutDegree = (RelativeLayout) findViewById(R.id.layout_degree);
        layoutDegree.setOnClickListener(this);
        mTextDegree = (TextView) findViewById(R.id.text_degree);
        // 设置学历的数据源
        String[] degreeArray = getResources().getStringArray(R.array.degree_list);
        mDegreeList = new ArrayList<>(Arrays.asList(degreeArray));

        // 学校选择
        RelativeLayout layoutCollege = (RelativeLayout) findViewById(R.id.layout_college);
        layoutCollege.setOnClickListener(this);
        mTextCollege = (TextView) findViewById(R.id.text_college);

        if(savedInstanceState != null) {
            mTextPetname.setText(savedInstanceState.getString("petname"));
            mTextRealName.setText(savedInstanceState.getString("real_name"));
            mTextSex.setText(savedInstanceState.getString("sex"));
            mTextBirthday.setText(savedInstanceState.getString("birthday"));
            mTextCollege.setText(savedInstanceState.getString("college"));
            mTextDegree.setText(savedInstanceState.getString("degree"));
            mTextSign.setText(savedInstanceState.getString("sign"));
            String uriStr = savedInstanceState.getString("image_uri");
            if (uriStr != null && !uriStr.equals("")) {
                Uri uri = Uri.parse(uriStr);
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(
                            getContentResolver().openInputStream(uri));
                    mImageAvatar.setImageBitmap(bitmap);
                } catch (Exception e) {
                    Log.e("hhx", Log.getStackTraceString(e));
                    mImageAvatar.setImageResource(R.drawable.ic_avatar_m);
                }
            } else {
                mImageAvatar.setImageResource(R.drawable.ic_avatar_m);
            }
        }

        sp = getSharedPreferences("profile", MODE_PRIVATE);
        String petname = sp.getString("petname", "");
        String realName = sp.getString("real_name", "");
        String sex = sp.getString("sex", "");
        String birthday = sp.getString("birthday", "");;
        String college = sp.getString("college", "");
        String degree = sp.getString("degree", "");
        String sign = sp.getString("sign", "");
        String uriStr = sp.getString("image_uri", "");
        Log.e("hhx", "uriStr: " + uriStr);
        mTextPetname.setText(petname);
        mTextRealName.setText(realName);
        mTextSex.setText(sex);
        mTextBirthday.setText(birthday);
        mTextCollege.setText(college);
        mTextDegree.setText(degree);
        mTextSign.setText(sign);
        if (uriStr != null && !uriStr.equals("")) {
            Uri uri = Uri.parse(uriStr);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(
                        getContentResolver().openInputStream(uri));
                mImageAvatar.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("hhx", Log.getStackTraceString(e));
                mImageAvatar.setImageResource(R.drawable.ic_avatar_m);
            }
        } else {
            mImageAvatar.setImageResource(R.drawable.ic_avatar_m);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isModified = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("hhx", "onDestroy");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String petname = "", realName = "", sex = "",
                birthday = "", college = "", degree = "", sign = "";
        if(mTextPetname.getText() != null) {
            petname = mTextPetname.getText().toString();
        }
        if(mTextRealName.getText() != null) {
            realName = mTextRealName.getText().toString();
        }
        if(mTextSex.getText() != null) {
            sex = mTextSex.getText().toString();
        }
        if(mTextBirthday.getText() != null) {
            birthday = mTextBirthday.getText().toString();
        }
        if(mTextCollege.getText() != null) {
            college = mTextCollege.getText().toString();
        }
        if(mTextDegree.getText() != null) {
            degree = mTextDegree.getText().toString();
        }
        if(mTextSign.getText() != null) {
            sign = mTextSign.getText().toString();
        }
        outState.putString("petname", petname);
        outState.putString("real_name", realName);
        outState.putString("sex", sex);
        outState.putString("birthday", birthday);
        outState.putString("college", college);
        outState.putString("degree", degree);
        outState.putString("sign", sign);
        if(imageUri != null) {
           outState.putString("image_uri", imageUri.toString());
        }
    }

    private void saveProfile() {
        SharedPreferences.Editor editor = sp.edit();
        if(mTextPetname.getText() != null
                && !mTextPetname.getText().equals(sp.getString("petname", ""))) {
            editor.putString("petname", mTextPetname.getText().toString());
            isModified = true;
        }
        if(mTextRealName.getText() != null
                && !mTextRealName.getText().equals(sp.getString("real_name", ""))) {
            editor.putString("real_name", mTextRealName.getText().toString());
            isModified = true;
        }
        if(mTextSex.getText() != null
                && !mTextSex.getText().equals(sp.getString("sex", ""))) {
            editor.putString("sex", mTextSex.getText().toString());
            isModified = true;
        }
        if(mTextBirthday.getText() != null
                && !mTextBirthday.getText().equals(sp.getString("birthday", ""))) {
            editor.putString("birthday", mTextBirthday.getText().toString());
            isModified = true;
        }
        if(mTextCollege.getText() != null
                && !mTextCollege.getText().equals(sp.getString("college", ""))) {
            editor.putString("college", mTextCollege.getText().toString());
        }
        if(mTextDegree.getText() != null
                && !mTextDegree.getText().equals(sp.getString("degree", ""))) {
            editor.putString("degree", mTextDegree.getText().toString());
            isModified = true;
        }
        if(mTextSign.getText() != null
                && !mTextSign.getText().equals(sp.getString("sign", ""))) {
            editor.putString("sign", mTextSign.getText().toString());
            isModified = true;
        }
        // Only support avator.
        if(imageUri != null
                && !imageUri.toString().equals(sp.getString("image_uri", ""))) {
            editor.putString("image_uri", imageUri.toString());
            isModified = true;
        }
        if(isModified) {
            boolean commit = editor.commit();
            Log.e("hhx", "commit: " + commit);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                requestSave();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            requestSave();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void requestSave() {
        saveProfile();
        if(isModified) {
            mProgressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = MESSAGE_SAVE_SUCCESS;
                    mHandler.sendMessage(message);
                }
            }).start();
        } else {
            finish();
        }

        /*HttpUtils.sendOkHttpRequest("http://www.baidu.com", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                try{
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Message message = new Message();
                message.what = MESSAGE_SAVE_FAILURE;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    // 测试网络请求，实际要检验返回值
                    try{
                        Thread.sleep(1000);
                    } catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = MESSAGE_SAVE_SUCCESS;
                    mHandler.sendMessage(message);
                }
            }
        });*/
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SAVE_SUCCESS:
                    mProgressDialog.dismiss();
                    Toast toastSuccess = Toast.makeText(ProfileActivity.this,
                            getString(R.string.save_success), Toast.LENGTH_SHORT);
                    toastSuccess.setGravity(Gravity.TOP, 0, 40);
                    toastSuccess.show();

                    Intent intent = new Intent();
                    if (imageUri != null) {
                        intent.putExtra("image_uri", imageUri.toString());
                    } else if (albumImageUri != null) {
                        intent.putExtra("image_uri", albumImageUri.toString());
                    } else if (albumCropUri != null) {
                        intent.putExtra("image_uri", albumCropUri.toString());
                    }
                    intent.putExtra("user_name", mTextPetname.getText().toString());
                    setResult(RESULT_OK, intent);

                    finish();
                    break;
                case MESSAGE_SAVE_FAILURE:
                    mProgressDialog.dismiss();
                    finish();

                    // Later: 自定义Toast
                    Toast toast = Toast.makeText(ProfileActivity.this,
                            getString(R.string.save_failure), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 40);
                    toast.show();
                    break;
                default:
                    break;
            }
        }
    };



    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.layout_avatar:
                String[] avatarItems = {getString(R.string.take_photo),
                        getString(R.string.choose_album)};
                showImageDialog(AVATAR_CHOOSE_ID ,getString(R.string.change_avator), avatarItems);
                break;
            case R.id.layout_background:
                String[] backgroundItems = {getString(R.string.take_photo),
                        getString(R.string.choose_album)};
                showImageDialog(BACK_CHOOSE_ID ,getString(R.string.change_avator), backgroundItems);
                break;
            case R.id.layout_petname:
                showInputDialog(PETNAME_INPUT_ID, WORDS_TOTAL_SHORT,
                        getString(R.string.petname_input), getString(R.string.dialog_confirm),
                        getString(R.string.dialog_cancel)
                        );
                break;
            case R.id.layout_realname:
                showInputDialog(REALNAME_INPUT_ID, WORDS_TOTAL_SHORT,
                        getString(R.string.realname_input), getString(R.string.dialog_confirm),
                        getString(R.string.dialog_cancel));
                break;
            case R.id.layout_sign:
                showInputDialog(SIGN_INPUT_ID, WORDS_TOTAL_LONG,
                        getString(R.string.sign_input), getString(R.string.dialog_confirm),
                        getString(R.string.dialog_cancel));
                break;
            case R.id.layout_sex:
                showSexDialog();
                break;
            case R.id.layout_sex_male:
                mAlertDialog.dismiss();
                mTextSex.setText(getString(R.string.sex_male));
                break;
            case R.id.layout_sex_female:
                mAlertDialog.dismiss();
                mTextSex.setText(getString(R.string.sex_female));
                break;
            case R.id.layout_birthday:
                showDateDialog();
                break;
            case R.id.layout_degree:
                showDegreeDialog();
                break;
            case R.id.layout_college:
                showCollegeDialog();
                break;
            default:
                break;
        }
    }

    // 显示头像和侧边栏背景的选择对话框
    private void showImageDialog(final int type, String title, String[] items) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);

        if(type == AVATAR_CHOOSE_ID) {
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //动态申请相机权限
//                            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
//                                    Manifest.permission.CAMERA)
//                                    != PackageManager.PERMISSION_GRANTED) {
//                                ActivityCompat.requestPermissions(ProfileActivity.this,
//                                        new String[]{ Manifest.permission.CAMERA }, PERMISSION_CAMERA_AVATAR);
//                            } else {
                                openCapture(type);
//                            }
                            break;
                        case 1:
                            //动态申请权限
                            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ProfileActivity.this,
                                        new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                                        PERMISSION_SD_AVATAR);
                            } else {
                                openAlbum(type);
                            }
                            break;
                    }
                }
            });
        } else if(type == BACK_CHOOSE_ID) {
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            //动态申请相机权限
//                            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
//                                    Manifest.permission.CAMERA)
//                                    != PackageManager.PERMISSION_GRANTED) {
//                                ActivityCompat.requestPermissions(ProfileActivity.this,
//                                        new String[]{ Manifest.permission.CAMERA }, PERMISSION_CAMERA_BACK);
//                            } else {
                                openCapture(type);
//                            }
                            break;
                        case 1:
                            //动态申请权限
                            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(ProfileActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSION_SD_BACKGROUND);
                            } else {
                                openAlbum(type);
                            }
                            break;
                    }
                }
            });
        }
        dialog.show();
    }

    // 显示昵称、真实姓名的输入对话框
    private void showInputDialog(final int type, final int wordsNum, String title,
                                 String positive, String negative) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        // 自定义dialog
        View dialogLayout = getLayoutInflater().inflate(R.layout.dialog_input,
                (ViewGroup) findViewById(R.id.layout_dialog_input));
        mLayoutInput = (TextInputLayout) dialogLayout.findViewById(R.id.layout_input);
        mEditInput = (EditText) dialogLayout.findViewById(R.id.edit_input);
        mWordsCount = (TextView) dialogLayout.findViewById(R.id.text_words_count);
        TextView wordsTotal = (TextView) dialogLayout.findViewById(R.id.text_words_total);
        if(type == SIGN_INPUT_ID) {
            // 个性签名字数限制
            wordsTotal.setText(getString(R.string.words_limit_long));
        } else {
            // 昵称和名字字数限制
            wordsTotal.setText(getString(R.string.words_limit_short));
        }

        // 监听文本框变化，实时统计输入字数
        mEditInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() <= wordsNum) {
                    mWordsCount.setText(String.valueOf(s.length()));
                    mLayoutInput.setErrorEnabled(false);
                } else {
                    mWordsCount.setText(String.valueOf(s.length()));
                    if(type == SIGN_INPUT_ID) {
                        // 个性签名提醒
                        mLayoutInput.setError(getString(R.string.words_out_of_limit_long));
                    } else {
                        // 昵称和名字提醒
                        mLayoutInput.setError(getString(R.string.words_out_of_limit_short));
                    }
                    mLayoutInput.setErrorEnabled(true);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.setTitle(title);
        dialog.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(type == PETNAME_INPUT_ID) {
                    mTextPetname.setText(mEditInput.getText());
                } else if(type == REALNAME_INPUT_ID) {
                    mTextRealName.setText(mEditInput.getText());
                } else{
                    mTextSign.setText(mEditInput.getText());
                }
            }
        });
        dialog.setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setView(dialogLayout);

        // 在对话框中自动弹出键盘(Dialog)
        AlertDialog alertDialog = dialog.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                InputMethodManager inputMethodManager = (InputMethodManager)
                        mEditInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(mEditInput, 0);
            }
        });
        alertDialog.show();
    }

    // 显示性别的单选对话框
    private void showSexDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_sex_select,
                (ViewGroup) findViewById(R.id.layout_dialog_sex));
        RelativeLayout layoutMale = (RelativeLayout) view.findViewById(R.id.layout_sex_male);
        layoutMale.setOnClickListener(this);
        RelativeLayout layoutFemale = (RelativeLayout) view.findViewById(R.id.layout_sex_female);
        layoutFemale.setOnClickListener(this);
        ImageView mImageMale = (ImageView) view.findViewById(R.id.image_sex_male);
        ImageView mImageFemale = (ImageView) view.findViewById(R.id.image_sex_female);
        // 判断选中哪一栏
        if(mTextSex.getText().toString().equals(getString(R.string.sex_male))) {
            mImageMale.setVisibility(View.VISIBLE);
            mImageFemale.setVisibility(View.INVISIBLE);
        } else if(mTextSex.getText().toString().equals(getString(R.string.sex_female))) {
            mImageMale.setVisibility(View.INVISIBLE);
            mImageFemale.setVisibility(View.VISIBLE);
        } else {
            mImageMale.setVisibility(View.INVISIBLE);
            mImageFemale.setVisibility(View.INVISIBLE);
        }
        dialog.setView(view);
        mAlertDialog = dialog.create();
        mAlertDialog.show();
    }

    // 显示日期对话框
    private void showDateDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_date_select,
                (ViewGroup) findViewById(R.id.layout_date_birthday));
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.picker_birthday);
        datePicker.init(mYear, mMonth, mDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
            }
        });
        dialog.setPositiveButton(getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTextBirthday.setText(mYear + getString(R.string.hyphen)
                        + (mMonth+1) + getString(R.string.hyphen) + mDay);
            }
        });
        dialog.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    // 显示学历对话框
    private void showDegreeDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        // 设置对话框布局
        final String strOld = mTextDegree.getText().toString();
        strNew = "";
        LoopView loopView = new LoopView(this);
        loopView.setItems(mDegreeList);
        if(strOld.equals("")) {
            initPos = mDegreeList.size() / 2;
        }
        loopView.setInitPosition(initPos);
        loopView.setNotLoop();
        loopView.setTextSize(14);
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                strNew = mDegreeList.get(index);
                initPos = index;
            }
        });

        dialog.setTitle(getString(R.string.degree_dialog_title));
        dialog.setView(loopView);
        dialog.setPositiveButton(getString(R.string.dialog_confirm),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(strNew.equals("")) {
                    mTextDegree.setText(mDegreeList.get(initPos));
                } else {
                    mTextDegree.setText(strNew);
                }
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(getString(R.string.dialog_cancel),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTextDegree.setText(strOld);
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
        // 获取屏幕尺寸
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // 获取对话框当前参数
        WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
        // 更新参数
        params.height = (int) (metrics.heightPixels * 0.6);
        alertDialog.getWindow().setAttributes(params);
    }

    // 显示学校对话框
    private void showCollegeDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_college_select,
                (ViewGroup) findViewById(R.id.layout_dialog_college));

        spinnerCollege = (AppCompatSpinner) view.findViewById(R.id.spinner_college);
        AppCompatSpinner spinnerProvince = (AppCompatSpinner) view.findViewById(R.id.spinner_province);
        ArrayAdapter<CharSequence> adapterPro = ArrayAdapter.createFromResource(this,
                R.array.province_list, android.R.layout.simple_spinner_item);
        adapterPro.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // 绑定数据源
        spinnerProvince.setAdapter(adapterPro);

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                String pro = (String) spinner.getItemAtPosition(position);
                ArrayAdapter<CharSequence> adapterColl = selectProvince(pro);
                spinnerCollege.setAdapter(adapterColl);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        dialog.setTitle(getString(R.string.college_dialog_title));
        dialog.setPositiveButton(getString(R.string.dialog_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTextCollege.setText(spinnerCollege.getSelectedItem().toString());
            }
        });
        dialog.setNegativeButton(getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private ArrayAdapter<CharSequence> selectProvince(String pro) {
        ArrayAdapter<CharSequence> ada = null;
        if(pro.equals("北京市")) {
            ada = ArrayAdapter.createFromResource(ProfileActivity.this,
                    R.array.beijing_list, android.R.layout.simple_spinner_item);
            ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } else if (pro.equals("天津市")) {
            ada = ArrayAdapter.createFromResource(ProfileActivity.this,
                    R.array.tianjin_list, android.R.layout.simple_spinner_item);
            ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } else if (pro.equals("上海市")) {
            ada = ArrayAdapter.createFromResource(ProfileActivity.this,
                    R.array.shanghai_list, android.R.layout.simple_spinner_item);
            ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } else if (pro.equals("重庆市")) {
            ada = ArrayAdapter.createFromResource(ProfileActivity.this,
                    R.array.chongqing_list, android.R.layout.simple_spinner_item);
            ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } else if (pro.equals("四川省")) {
            ada = ArrayAdapter.createFromResource(ProfileActivity.this,
                    R.array.sichuan_list, android.R.layout.simple_spinner_item);
            ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } else if (pro.equals("江苏省")) {
            ada = ArrayAdapter.createFromResource(ProfileActivity.this,
                    R.array.jiangsu_list, android.R.layout.simple_spinner_item);
            ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } else if (pro.equals("浙江省")) {
            ada = ArrayAdapter.createFromResource(ProfileActivity.this,
                    R.array.zhejiang_list, android.R.layout.simple_spinner_item);
            ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        } else if (pro.equals("湖北省")) {
            ada = ArrayAdapter.createFromResource(ProfileActivity.this,
                    R.array.hubei_list, android.R.layout.simple_spinner_item);
            ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        }
        return ada;
    }

    // 拍照：新建文件存储照片，并且将File对象转为Uri对象
    private void setImageUri() {
        //以时间命名图片
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        outputImage = new File(getExternalCacheDir(), "IMG"+str+".jpg");
        try{
            if(outputImage.exists()) {
                boolean b = outputImage.delete();
                LogUtil.e("outputImage", "delete:"+String.valueOf(b));
            }
            // public boolean createNewFile().
            // 如果指定的文件不存在，并已成功创建,返回true.如果该文件存在，该方法返回false
            boolean b = outputImage.createNewFile();
            LogUtil.e("outputImage", "createNewFile:"+String.valueOf(b));
        }catch(IOException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(ProfileActivity.this,
                    "com.cqupt.photoupload.fileprovider", outputImage);
        } else{
            imageUri = Uri.fromFile(outputImage);
        }
    }

    // 拍照：调用摄像头
    private void openCapture(int type) {
        setImageUri();
        Intent intentCapture = new Intent("android.media.action.IMAGE_CAPTURE");
        intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        mType = type;
        startActivityForResult(intentCapture, TAKE_PHOTO);
    }

    //调用相册
    private void openAlbum(int type) {
        Intent intentAlbum = new Intent("android.intent.action.GET_CONTENT");
        intentAlbum.setType("image/*");
        mType = type;
        startActivityForResult(intentAlbum, CHOOSE_PHOTO);
    }

    // 权限请求结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_SD_AVATAR:
                if(grantResults.length>0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    LogUtil.e("Permission", "==allow");
                    openAlbum(AVATAR_CHOOSE_ID);
                } else {
                    Toast.makeText(ProfileActivity.this, R.string.toast_deny_permission,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_SD_BACKGROUND:
                if(grantResults.length>0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    LogUtil.e("Permission", "==allow");
                    openAlbum(BACK_CHOOSE_ID);
                } else {
                    Toast.makeText(ProfileActivity.this, R.string.toast_deny_permission,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_CAMERA_AVATAR:
                if(grantResults.length>0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    LogUtil.e("Permission", "==allow");
                    openCapture(AVATAR_CHOOSE_ID);
                } else {
                    Toast.makeText(ProfileActivity.this, R.string.toast_deny_permission,
                            Toast.LENGTH_SHORT).show();
                }
            case PERMISSION_CAMERA_BACK:
                if(grantResults.length>0 && grantResults[0] == PackageManager.
                        PERMISSION_GRANTED) {
                    LogUtil.e("Permission", "==allow");
                    openCapture(BACK_CHOOSE_ID);
                } else {
                    Toast.makeText(ProfileActivity.this, R.string.toast_deny_permission,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if(resultCode == RESULT_OK) {
                    // 截取拍照图片
                    cropPhoto(TAKE_PHOTO);
                } else if(outputImage.length() == 0) {
                    //若没有拍照(resultCode == RESULT_OK)，删除创建的文件
                    boolean b = outputImage.delete();
                    LogUtil.e("outputImage", "delete:"+String.valueOf(b));
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK) {
                    if(Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                    // 截取相册图片
                    cropPhoto(CHOOSE_PHOTO);
                }
                break;
            case CROP_PHOTO_CAPTURE:
                if(resultCode == RESULT_OK) {
                    try {
                        Log.e("hhx", "头像: " + imageUri.toString());
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(imageUri));
                        if (mType == AVATAR_CHOOSE_ID) {
                            mImageAvatar.setImageBitmap(bitmap);
                        } else if (mType == BACK_CHOOSE_ID) {
                            mImageBackground.setImageBitmap(bitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_PHOTO_ALBUM:
                if(resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(albumCropUri));
                        if (mType == AVATAR_CHOOSE_ID) {
                            mImageAvatar.setImageBitmap(bitmap);
                        } else if (mType == BACK_CHOOSE_ID) {
                            mImageBackground.setImageBitmap(bitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else if(cropOutput.length() == 0) {
                    boolean b = cropOutput.delete();
                    LogUtil.e("cropOutput", "delete:"+String.valueOf(b));
                }
                break;
            default:
                break;
        }
    }

    // 解析封装的Uri, 得到图片真实路径
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        albumImagePath = null;
        LogUtil.e("URI_STRING", "=="+uri);

        if(DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            LogUtil.e("DocumentsId", "=="+docId);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                albumImagePath = getImagePath(MediaStore.Images.Media.
                        EXTERNAL_CONTENT_URI, selection);
                LogUtil.e("media.documents", "=="+MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                albumImagePath = getImagePath(contentUri, null);
                LogUtil.e("downloads.documents", "=="+contentUri);
            }
        } else if("content".equalsIgnoreCase(uri.getScheme())) {
            albumImagePath = getImagePath(uri, null);
            LogUtil.e("content", "==");
        } else if("file".equalsIgnoreCase(uri.getScheme())) {
            albumImagePath = uri.getPath();
            LogUtil.e("file", "=="+uri.getPath());
        }
    }
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        albumImagePath = getImagePath(uri, null);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if(cursor != null) {
            if(cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                LogUtil.e("path", "=="+path);
            }
            cursor.close();
        }
        return path;
    }

    // 根据图片真实路径获取Uri
    private void getAlbumUri(String imagePath) {
        if(imagePath != null) {
            albumImageUri = Uri.fromFile(new File(albumImagePath));
        } else{
            Toast.makeText(ProfileActivity.this, R.string.toast_fail_path,
                    Toast.LENGTH_SHORT).show();
        }
    }

    // 裁剪后存放
    private void cropPhoto(int action) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if(action == TAKE_PHOTO) {
            // 拍照后截取图片
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("scale", true);
            if(AVATAR_CHOOSE_ID == mType) {
                // 设置圆形裁剪区域(invalid)
                intent.putExtra("circleCrop", true);
            }
            // 裁剪后覆盖原来的图片(imageUri)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CROP_PHOTO_CAPTURE);
        } else if(action == CHOOSE_PHOTO) {
            // 选取相册照片截取图片
            getAlbumUri(albumImagePath);
            intent.setDataAndType(albumImageUri, "image/*");
            intent.putExtra("scale", true);
            albumCropUri = getCropUri();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, albumCropUri);
            startActivityForResult(intent, CROP_PHOTO_ALBUM);
        }
    }

    private Uri getCropUri() {
        // 以时间命名图片
        Uri uri;
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        cropOutput = new File(getExternalCacheDir(), "Crop"+str+".jpg");
        try{
            if(cropOutput.exists()) {
                boolean b = cropOutput.delete();
                LogUtil.e("cropOutput", "delete:"+String.valueOf(b));
            }
            // public boolean createNewFile().
            // 如果指定的文件不存在，并已成功创建,返回true.如果该文件存在，该方法返回false
            boolean b = cropOutput.createNewFile();
            LogUtil.e("cropOutput", "createNewFile:"+String.valueOf(b));
        }catch(IOException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(ProfileActivity.this,
                    "com.cqupt.photoupload.fileprovider", cropOutput);
        } else{
            uri = Uri.fromFile(cropOutput);
        }
        return uri;
    }
}

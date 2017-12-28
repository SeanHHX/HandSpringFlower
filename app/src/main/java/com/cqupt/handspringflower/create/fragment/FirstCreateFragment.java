package com.cqupt.handspringflower.create.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.create.CreateActivity;
import com.cqupt.handspringflower.create.utilDialog.DateTimePickDialogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FirstCreateFragment extends Fragment implements View.OnClickListener{
    private ImageButton img_btn_ChooseTime;
    private EditText et_ActivityName,et_ActivityRowNum,et_ActivityLineNum,et_ActivityAcademy,
            et_ActivityTime,et_ActivityDes,et_ActivityGround;
    private Button btn_next;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_create,container,false);
        init(view);
        return view;
    }
    public void init(View view)
    {
        img_btn_ChooseTime= (ImageButton) view.findViewById(R.id.img_btn_chooseTime);
        et_ActivityName= (EditText) view.findViewById(R.id.et_activityName);
        et_ActivityRowNum= (EditText) view.findViewById(R.id.et_activityRowNum);
        et_ActivityLineNum= (EditText) view.findViewById(R.id.et_activityLineNum);
        et_ActivityAcademy= (EditText) view.findViewById(R.id.et_activityAcademy);
        et_ActivityTime= (EditText) view.findViewById(R.id.et_activityTime);
        et_ActivityDes= (EditText) view.findViewById(R.id.et_activityDes);
        et_ActivityGround= (EditText) view.findViewById(R.id.et_activityGround);
        btn_next= (Button) view.findViewById(R.id.btn_next);
        Toolbar toolbar= (Toolbar) view.findViewById(R.id.toolbar_create1);

        AppCompatActivity superActivity= (AppCompatActivity) getActivity();
        toolbar.setTitle("创建活动");
//        toolbar.setNavigationIcon(R.drawable.img_backspace);

        img_btn_ChooseTime.setOnClickListener(this);
        btn_next.setOnClickListener(this);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        superActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = superActivity.getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.img_btn_chooseTime:
                SimpleDateFormat formatter=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                Date date=new Date(System.currentTimeMillis());
                String nowTime=formatter.format(date);
                DateTimePickDialogUtil dateTimePickDialog=new DateTimePickDialogUtil(getActivity(),nowTime);
                dateTimePickDialog.dateTimePicKDialog(et_ActivityTime);
                break;
            case R.id.btn_next:
                if(isRright()) {
                    MainCreateFragment fragment = new MainCreateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("name",et_ActivityName.getText().toString());
                    bundle.putString("row", et_ActivityRowNum.getText().toString());
                    bundle.putString("line", et_ActivityLineNum.getText().toString());
                    bundle.putString("academy",et_ActivityAcademy.getText().toString());
                    bundle.putString("time",et_ActivityTime.getText().toString());
                    bundle.putString("ground",et_ActivityGround.getText().toString());
                    if(!et_ActivityDes.getText().toString().isEmpty()){
                        bundle.putString("des",et_ActivityDes.getText().toString());
                    }
                    //未添加描述
                    else{
                        bundle.putString("des","暂无描述");
                    }
                    fragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.frame, fragment).addToBackStack(null).commit();
                    CreateActivity.mFragment=fragment;
                }
                else{
                    Toast.makeText(getActivity(),"请完善信息后再继续!",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public boolean isRright() {
        String name=et_ActivityName.getText().toString();
        String row=et_ActivityRowNum.getText().toString();
        String line=et_ActivityLineNum.getText().toString();
        String academy=et_ActivityAcademy.getText().toString();
        String time=et_ActivityTime.getText().toString();
        String ground=et_ActivityGround.getText().toString();
        return !name.isEmpty()&&!row.isEmpty()&&!line.isEmpty()&&!academy.isEmpty()&&!time.isEmpty()&&!ground.isEmpty();
    }
}

package com.cqupt.handspringflower.create.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.create.CreateActivity;
import com.cqupt.handspringflower.create.utilDialog.MyColorAdapter;
import com.cqupt.handspringflower.create.utilView.CannotScrollViewPager;
import com.cqupt.handspringflower.create.utilView.SeatTable;
import com.cqupt.handspringflower.create.utilView.SuspendButtonLayout;
import com.cqupt.handspringflower.database.DBUtils;
import com.cqupt.handspringflower.personal.PersonalActivity;
import com.cqupt.handspringflower.utils.HttpUtils;
import com.cqupt.handspringflower.utils.LogUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainCreateFragment extends Fragment  {

    public static final String Creat_URL = "http://118.89.29.87:8080/InternetPlus/activity/addNewActivity";

    private CannotScrollViewPager viewPager;
    private SuspendButtonLayout suspendButtonLayout;
    private LayoutInflater mInflater;
    private MyPagerAdapter mPagerAdapter;
    private TextView mPageTextView;
    private PopupWindow popupWindow;
    private Toolbar toolbar;


    private List<View> mList;
    private int position=0;
    private int pageCount=0;
    private int rowNum=1,lineNum=1;
    private String title=null;
    private Bundle mBundle=null;
    private boolean isPopupwindowOpened=false;
    private boolean isUsed=false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_create1,container,false);
        this.mInflater=inflater;
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
            this.title=bundle.getString("name");
            this.rowNum=Integer.valueOf(bundle.getString("row"));
            this.lineNum=Integer.valueOf(bundle.getString("line"));
            this.mBundle=bundle;
        }
        initView(view);
        return view;
    }

    public void initView(View view)
    {
        viewPager= (CannotScrollViewPager) view.findViewById(R.id.create_viewPager);
        suspendButtonLayout= (SuspendButtonLayout) view.findViewById(R.id.layout);
        mPageTextView= (TextView) view.findViewById(R.id.text_page);

        toolbar= (Toolbar) view.findViewById(R.id.toolbar_create2);
        final AppCompatActivity superActivity= (AppCompatActivity) getActivity();
        toolbar.setTitle("绘制图案");
        superActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.img_back_m);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage("您正编辑的活动尚未保存，直接退出将丢失数据，是否直接退出？");
                builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(isPopupwindowOpened) {
                            popupWindow.dismiss();
                        }
                        getFragmentManager().popBackStack();
                        CreateActivity.mFragment=new FirstCreateFragment();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.menu)
                {
                    String postJson = getJsonString();
                    LogUtil.e("createJsonString", postJson);
                   /* HttpUtils.sendOkHttpRequest(Creat_URL , postJson, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if(response.isSuccessful()) {
                                LogUtil.e("createResponse", response.body().string());
                            }
                        }
                    });*/
                    // Later: 删除对话框，进一步完善
                    AlertDialog.Builder builder = new AlertDialog.Builder(superActivity);
                    builder.setTitle("创建成功");
                    builder.setMessage("已成功创建活动");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // 在数据库中添加记录
                            SharedPreferences sp = getContext()
                                    .getSharedPreferences("profile", Context.MODE_PRIVATE);
                            String[] args = new String[12];
                            args[0] = mBundle.getString("name");
                            args[1] = mBundle.getString("time");
                            args[2] = mBundle.getString("academy");
                            args[3] = mBundle.getString("ground");
                            args[4] = mBundle.getString("des");
                            args[5] = sp.getString("petname", "author");
                            args[6] = "1";
                            args[7] = "0";
                            args[8] = "0";
                            args[9] = "0";
                            args[10] = R.drawable.img_default + "";
                            args[11] = R.drawable.avatar_default + "";
                            long res = DBUtils.insert(args);
                            Log.e("hhx", "db insert (single): " + res + "args[10]-args[11]: " + args[10] + "-" + args[11]);
                            // 跳转到创建列表
                            PersonalActivity.actionStart(getContext(), 0, mBundle);
                            ((Activity) getContext()).finish();
                        }
                    });
                    builder.show();
//                    LogUtil.e("nnnn","Click Menu");
                }
                return false;
            }
        });

        //添加浮动按钮监听
        suspendButtonLayout.setOnSuspendListener(new SuspendButtonLayout.OnSuspendListener() {
            @Override
            public void onButtonStatusChanged(int status) {

            }

            @Override
            public void onChildButtonClick(int index) {

                switch (index)
                {
                    case 1:
                        RelativeLayout layout= (RelativeLayout) mInflater.inflate(R.layout.item_page,null);
                        final SeatTable mSeatTable= (SeatTable) layout.findViewById(R.id.page_seattable);
                        mSeatTable.setData(rowNum,lineNum);
                        mSeatTable.setSeatChecker(new SeatTable.SeatChecker() {
                            @Override
                            public boolean isValidSeat(int row, int column) {
                                return true;
                            }

                            @Override
                            public boolean isSold(int row, int column) {
                                return false;
                            }

                            @Override
                            public void checked(int row, int column) {

                            }

                            @Override
                            public void unCheck(int row, int column) {

                            }

                            @Override
                            public String[] checkedSeatTxt(int row, int column) {
                                return null;
                            }
                        });
                        mList.add(layout);
                        mPagerAdapter.setList(mList);
                        mPagerAdapter.notifyDataSetChanged();
                        pageCount++;
                        viewPager.setCurrentItem(pageCount-1);
                        mPagerAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        RelativeLayout layout1= (RelativeLayout) mList.get(position);
                        final SeatTable mSeatTable1= (SeatTable) layout1.findViewById(R.id.page_seattable);
                        if(mSeatTable1.getIsDrawOverView())
                        {
                            mSeatTable1.setIsDrawOverView(false);
                            suspendButtonLayout.setChildImageResource(2,R.drawable.img_closeoverview);
                        }
                        else {
                            mSeatTable1.setIsDrawOverView(true);
                            suspendButtonLayout.setChildImageResource(2,R.drawable.img_openoverview);
                        }
                        mSeatTable1.invalidate();
                        break;
                    case 4:
                        if(position>0)
                            viewPager.setCurrentItem(position-1);
                        else
                            Toast.makeText(getActivity(),"已经是第一页了哦~",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        if(isPopupwindowOpened==false) {
                            RelativeLayout layout2 = (RelativeLayout) mList.get(position);
                            final SeatTable mSeatTable2 = (SeatTable) layout2.findViewById(R.id.page_seattable);
                            /*final ColorChooserDialog dialog=new ColorChooserDialog(getContext());
                            dialog.setData(getData());
                            dialog.setListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    mSeatTable2.setBitMapColor(i);
                                    Log.e("gridview",i+"");
                                    dialog.dismiss();

                                }
                            });
                            dialog.show();
                            Window dialogWindow=dialog.getWindow();
                            WindowManager wm=getActivity().getWindowManager();
                            Display d=wm.getDefaultDisplay();
                            WindowManager.LayoutParams p=dialogWindow.getAttributes();
                            p.height= (int) (d.getHeight()*0.6);
                            p.width= (int) (d.getWidth()*0.8);
                            dialogWindow.setAttributes(p);*/
                            View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.my_dialog_colorchooser, null);
                            WindowManager wm = (WindowManager) getContext()
                                    .getSystemService(Context.WINDOW_SERVICE);
                            popupWindow = new PopupWindow(contentView, (int) (wm.getDefaultDisplay().getWidth() * 0.2),  ViewGroup.LayoutParams.WRAP_CONTENT);
                            popupWindow.setAnimationStyle(R.style.contextMenuAnim);
                            GridView gridView = (GridView) contentView.findViewById(R.id.dialog_gridview);
                            gridView.setAdapter(new MyColorAdapter(getActivity(), getData(), R.layout.item_colorgridview, new String[]{"color"}, new int[]{R.id.img_color}));
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    mSeatTable2.setBitMapColor(position);
                                    popupWindow.dismiss();
                                    isPopupwindowOpened = false;
                                }
                            });
                            popupWindow.showAsDropDown(toolbar, 0, 0);
                            isPopupwindowOpened = true;
                        }
                        else
                        {
                            popupWindow.dismiss();
                            isPopupwindowOpened=false;
                        }
                        break;
                    case 5:
                        if(position+1<pageCount)
                            viewPager.setCurrentItem(position+1);
                        else
                            Toast.makeText(getActivity(),"已经是最后一页了哦~",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        //添加viewPager监听
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainCreateFragment.this.position=position;
                mPageTextView.setText((position+1)+"/"+pageCount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //添加第一页view
        mList=new ArrayList<>();
        RelativeLayout layout= (RelativeLayout) mInflater.inflate(R.layout.item_page,null);
        SeatTable mSeatTable= (SeatTable) layout.findViewById(R.id.page_seattable);
        mSeatTable.setData(this.rowNum,this.lineNum);
        mSeatTable.setSeatChecker(new SeatTable.SeatChecker() {
            @Override
            public boolean isValidSeat(int row, int column) {
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                return false;
            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }
        });
        mList.add(layout);
        mPagerAdapter=new MyPagerAdapter(mList);
        pageCount++;

        viewPager.setAdapter(mPagerAdapter);
        viewPager.setCurrentItem(0);
    }

    private String getJsonString()
    {
        JSONObject jsonObject = new JSONObject();
        try {
//            String time = mBundle.getString("time");
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            Date date = dateFormat.parse(time);
            jsonObject.put("queue", getColorImfo());
            jsonObject.put("ActivityContent",this.mBundle.getString("des"));
            jsonObject.put("ActivityTitle",this.mBundle.getString("name"));
            jsonObject.put("ActivityTime", this.mBundle.getString("time"));
            LogUtil.e("ActivityTime", mBundle.getString("time"));
            jsonObject.put("ActivityAcademy",this.mBundle.getString("academy"));
            jsonObject.put("ActivityLocation",this.mBundle.getString("ground"));
            jsonObject.put("ActivityTotal",String.valueOf(this.rowNum*this.lineNum));
            //默认收藏人数为0
//            jsonObject.put("ActivityCollection","0");

        }catch(Exception e) {
            LogUtil.e("Error", e.getClass().getSimpleName());
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;
        public int size;
        private View currentView;

        public void setList(List<View> mlist) {
            this.mListViews=mlist;
            size=mListViews==null?0:mListViews.size();
        }
        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
            size=mListViews==null?0:mListViews.size();
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ViewGroup v = (ViewGroup) mListViews.get(arg1).getParent();
            if (v != null)
                v.removeView(mListViews.get(arg1));
            ((ViewPager) arg0).addView(mListViews.get(arg1));
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

    }
    public List<Map<String,Object>> getData(){
        List<Map<String,Object>> list=new ArrayList<>();
        Map map;
        map=new HashMap<>();
        map.put("color",R.drawable.img_color1);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color2);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color3);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color4);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color5);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color6);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color7);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color8);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color9);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color10);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color11);
        list.add(map);
        map=new HashMap<>();
        map.put("color",R.drawable.img_color12);
        list.add(map);
        return list;
    }
    public String getColorImfo()
    {
        String color="";
        for(int i=0;i<mList.size();i++)
        {
            RelativeLayout t= (RelativeLayout) mList.get(i);
            SeatTable table= (SeatTable) t.findViewById(R.id.page_seattable);
            int mColor[][]=table.getmColorMartix();
            for(int j=0;j<this.rowNum;j++)
            {
                for(int k=0;k<this.lineNum;k++)
                {
                    if(k!=this.lineNum-1)
                        color=color+mColor[j][k]+",";
                    else if(j!=this.rowNum-1)
                        color=color+mColor[j][k]+";";
                    else if(i!=mList.size()-1)
                        color=color+mColor[j][k]+"-";
                    else
                        color=color+mColor[j][k];
                }

            }
        }
        Log.e("666",color);
        return color;
    }

    public boolean onKeyDown(int keyCode)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("提示");
            builder.setMessage("您正编辑的活动尚未保存，直接退出将丢失数据，是否直接退出？");
            builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    if (isPopupwindowOpened) {
                        popupWindow.dismiss();
                    }
                    getFragmentManager().popBackStack();
                    CreateActivity.mFragment = new FirstCreateFragment();
                    isUsed = true;
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    isUsed = false;
                }
            });
            builder.show();
            return isUsed;
        }
        else return false;
    }
}

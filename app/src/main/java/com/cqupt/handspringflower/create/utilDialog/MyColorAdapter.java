package com.cqupt.handspringflower.create.utilDialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.cqupt.handspringflower.R;

import java.util.List;
import java.util.Map;

public class MyColorAdapter extends SimpleAdapter {
    private Context mContext;
    private List<Map<String,Object>> mList;
    private int mResourceID;

    public MyColorAdapter(Context context, List< Map<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext=context;
        mList=data;
        mResourceID=resource;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        view= View.inflate(mContext,mResourceID,null);
        ImageView imageView= (ImageView) view.findViewById(R.id.img_color);
        imageView.setImageResource((Integer) mList.get(position).get("color"));
        return view;
    }
}

package com.cqupt.handspringflower.create.utilView;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class CannotScrollViewPager extends ViewPager {
    public CannotScrollViewPager(Context context) {
        super(context);
    }
    public CannotScrollViewPager(Context context, AttributeSet attr){
        super(context,attr);
    }
    public boolean onTouchEvent(MotionEvent arg0) {
        Log.e("OnTouchEvent","Called");
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return false;
    }
}

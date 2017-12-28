package com.cqupt.handspringflower.personal;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cqupt.handspringflower.R;

public class PersonalFragmentAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;
    private Context mContext;
    private static final String[] PAGE_TITLE = new String[PAGE_COUNT];

    public PersonalFragmentAdapter(Context context, FragmentManager manager) {
        super(manager);
        this.mContext = context;
        PAGE_TITLE[0] = mContext.getString(R.string.create);
        PAGE_TITLE[1] = mContext.getString(R.string.join);
        PAGE_TITLE[2] = mContext.getString(R.string.collection);
    }

    @Override
    public Fragment getItem(int position) {
        // Current item.
        return PersonalFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return PAGE_TITLE[position];
    }
}

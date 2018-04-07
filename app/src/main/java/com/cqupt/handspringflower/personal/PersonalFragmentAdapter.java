package com.cqupt.handspringflower.personal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cqupt.handspringflower.R;

public class PersonalFragmentAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;
    private Context mContext;
    private static final String[] PAGE_TITLE = new String[PAGE_COUNT];
    private Bundle newCreate;

    public PersonalFragmentAdapter(Context context, FragmentManager manager, Bundle bundle) {
        super(manager);
        this.mContext = context;
        this.newCreate = bundle;
        PAGE_TITLE[0] = mContext.getString(R.string.create);
        PAGE_TITLE[1] = mContext.getString(R.string.join);
        PAGE_TITLE[2] = mContext.getString(R.string.collection);
    }

    @Override
    public Fragment getItem(int position) {
        // Current item.
        return PersonalFragment.newInstance(position, newCreate);
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

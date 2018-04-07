package com.cqupt.handspringflower.personal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.main.ActivityAdapter;
import com.cqupt.handspringflower.main.ActivityItem;
import com.cqupt.handspringflower.search.SearchResAdapter;
import com.cqupt.handspringflower.utils.RecyclerUtils;

import java.util.ArrayList;
import java.util.List;

public class PersonalFragment extends Fragment {

    private static final String ARGS_PAGE = "args_page";
    private int mPage;
    private Bundle newCreate;
    private RecyclerView mRecyclerView;
    private List<ActivityItem> mListCreate = new ArrayList<>();
    private List<ActivityItem> mListJoin = new ArrayList<>();
    private List<ActivityItem> mListColl = new ArrayList<>();

    public static PersonalFragment newInstance(int page, Bundle bundle) {
        Bundle args = new Bundle();
        args.putInt(ARGS_PAGE, page);
        args.putBundle("new_create", bundle);
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARGS_PAGE);
        newCreate = getArguments().getBundle("new_create");
//        Log.e("PersonalFragment", "onCreate"+" "+mPage);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_list_personal);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        switch (mPage) {
            case 0:
                if (newCreate != null) {
                    SharedPreferences sp = getContext()
                            .getSharedPreferences("profile", Context.MODE_PRIVATE);
                    String author = sp.getString("petname", "author");
                    ActivityItem item = new ActivityItem(
                            R.drawable.a_img_1,
                            newCreate.getString("name"),
                            newCreate.getString("time"),
                            newCreate.getString("des"),
                            R.drawable.ic_author_m,
                            author);
                    mListCreate.add(0, item);
                }
                RecyclerUtils.getCreateItems(mListCreate);
                SearchResAdapter adapter0 = new SearchResAdapter(mListCreate);
                mRecyclerView.setAdapter(adapter0);
                break;
            case 1:
                RecyclerUtils.getJoinItems(mListJoin);
                SearchResAdapter adapter1 = new SearchResAdapter(mListJoin);
                mRecyclerView.setAdapter(adapter1);
                break;
            case 2:
                RecyclerUtils.getCollItems(mListColl);
                SearchResAdapter adapter2 = new SearchResAdapter(mListColl);
                mRecyclerView.setAdapter(adapter2);
                break;
            default:
                break;
        }
        return view;
    }
}

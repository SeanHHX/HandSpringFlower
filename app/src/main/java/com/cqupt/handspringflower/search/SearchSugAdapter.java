package com.cqupt.handspringflower.search;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.main.ActivityItem;
import com.cqupt.handspringflower.utils.RecyclerUtils;

import java.util.List;

public class SearchSugAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<SearchItem> mListSug;
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private SearchResAdapter mAdapter;
    private List<ActivityItem> mListRes;
    private List<SearchItem> mListHis;

    public SearchSugAdapter(List<SearchItem> listSug, EditText editText, RecyclerView recyclerView,
                            SearchResAdapter adapter, List<ActivityItem> listRes, List<SearchItem> listHis) {
        mListSug = listSug;
        mEditText = editText;
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mListRes = listRes;
        mListHis = listHis;
    }

    static class SearchSugItemHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mTextSearch;
        ImageView mImageGet;

        public SearchSugItemHolder(View view) {
            super(view);
            mCardView = (CardView) view;
            mTextSearch = (TextView) view.findViewById(R.id.text_search);
            mImageGet = (ImageView) view.findViewById(R.id.image_get);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null)
            mContext = parent.getContext();

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_search, parent, false);
        return new SearchSugItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SearchItem item = mListSug.get(position);
        ( (SearchSugItemHolder) holder).mTextSearch.setText(item.getSearch());

        ((SearchSugItemHolder)holder).mImageGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText(item.getSearch());
                mEditText.setSelection(mEditText.getText().length());
            }
        });

        ((SearchSugItemHolder)holder).mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Click to show result.
                mEditText.setText(item.getSearch());
                mEditText.setSelection(mEditText.getText().length());
                mListHis.add(0, item);
                mListRes.clear();
                RecyclerUtils.getResultItems(mListRes);
                mRecyclerView.setAdapter(mAdapter);
                SearchActivity.hideKeyBoard();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListSug.size();
    }
}

package com.cqupt.handspringflower.search;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.main.ActivityItem;
import com.cqupt.handspringflower.utils.DialogUtils;
import com.cqupt.handspringflower.utils.RecyclerUtils;

import java.util.List;

public class SearchAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int TYPE_ITEM = 0;
    private int TYPE_FOOT = 1;

    private Context mContext;
    private List<SearchItem> mListHis;
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private SearchResAdapter mAdapter;
    private List<ActivityItem> mListRes;

    public SearchAdapter(List<SearchItem> listHis, EditText editText,
                         RecyclerView recyclerView, SearchResAdapter adapter, List<ActivityItem> listRes) {
        mListHis = listHis;
        mEditText = editText;
        mRecyclerView = recyclerView;
        mAdapter = adapter;
        mListRes = listRes;
    }

    static class SearchItemHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView mTextSearch;
        ImageView mImageGet;
        View mPopupView;
        AppCompatButton mPopButton;
        PopupWindow mPopupWindow;

        public SearchItemHolder(View view) {
            super(view);
            mCardView = (CardView) view;
            mTextSearch = (TextView) view.findViewById(R.id.text_search);
            mImageGet = (ImageView) view.findViewById(R.id.image_get);
            mPopupView = SearchActivity.getInstance().getLayoutInflater()
                    .inflate(R.layout.popup_window, null);
            mPopButton = (AppCompatButton) mPopupView.findViewById(R.id.pop_button);
            mPopupWindow = new PopupWindow(mPopupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mPopupWindow.setOutsideTouchable(true);
        }
    }

    static class SearchFootHolder extends RecyclerView.ViewHolder {
        LinearLayout mLayout;
        public SearchFootHolder(View view) {
            super(view);
            mLayout = (LinearLayout) view;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1 == getItemCount())
            return TYPE_FOOT;
        else
            return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null)
            mContext = parent.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_search, parent, false);
            viewHolder = new SearchItemHolder(view);
        } else if(viewType == TYPE_FOOT) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_search_foot, parent, false);
            viewHolder = new SearchFootHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof SearchItemHolder) {
            final SearchItem item = mListHis.get(position);
            ((SearchItemHolder)holder).mTextSearch.setText(item.getSearch());

            ((SearchItemHolder)holder).mImageGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditText.setText(item.getSearch());
                    mEditText.setSelection(mEditText.getText().length());
                }
            });

            //点击搜索历史搜索
            ((SearchItemHolder)holder).mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Click to show result.
                    mEditText.setText(item.getSearch());
                    mEditText.setSelection(mEditText.getText().length());
                    // Click history: set first postion.
                    mListHis.remove(position);
                    mListHis.add(0, item);
                    mListRes.clear();
                    RecyclerUtils.getResultItems(mListRes);
                    mRecyclerView.setAdapter(mAdapter);
                    SearchActivity.hideKeyBoard();
                }
            });

            //长按搜索历史删除
            ((SearchItemHolder)holder).mCardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((SearchItemHolder)holder).mPopupWindow.showAsDropDown(
                            ((SearchItemHolder)holder).mCardView, 120, -72);
//                    SearchActivity.hideKeyBoard();
                    return true;
                }
            });

            ((SearchItemHolder)holder).mPopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListHis.remove(position);
                    notifyDataSetChanged();
                    if(((SearchItemHolder)holder).mPopupWindow != null
                            &&((SearchItemHolder)holder).mPopupWindow.isShowing())
                        ((SearchItemHolder)holder).mPopupWindow.dismiss();
                }
            });
        } else if(holder instanceof SearchFootHolder) {
            //清空搜索历史
            ((SearchFootHolder)holder).mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SearchActivity.hideKeyBoard();
                    DialogUtils.showSearchDialog(mContext, mListHis, SearchAdapter.this,
                            R.drawable.ic_warning_m,
                            mContext.getString(R.string.dialog_title_delete_promote),
                            mContext.getString(R.string.dialog_message_delete_all),
                            mContext.getString(R.string.dialog_cancel),
                            mContext.getString(R.string.dialog_confirm));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mListHis.size()==0 ? 0 : mListHis.size()+1;
    }
}

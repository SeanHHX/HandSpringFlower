package com.cqupt.handspringflower.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cqupt.handspringflower.R;

import java.util.List;

public class ActivityAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int TYPE_ITEM = 0;
    private int TYPE_FOOT = 1;
//    private int TYPE_HEAD = 2;
    private Context mContext;
    private List<ActivityItem> mList;

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView mImage;
        TextView mTextTitle;
        TextView mTextTime;
        TextView mTextContent;
        ImageView mImageAuthor;
        TextView mTextAuthor;

        public ItemViewHolder(View view) {
            super(view);
            mCardView = (CardView) view;
            mImage = (ImageView) view.findViewById(R.id.image_activity);
            mTextTitle = (TextView) view.findViewById(R.id.text_title);
            mTextTime = (TextView) view.findViewById(R.id.text_time);
            mTextContent = (TextView) view.findViewById(R.id.text_content);
            mTextContent = (TextView) view.findViewById(R.id.text_content);
            mImageAuthor = (ImageView) view.findViewById(R.id.image_author);
            mTextAuthor = (TextView) view.findViewById(R.id.text_author);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        ProgressBar mProgressBar;
        TextView mTextView;

        public FootViewHolder(View view) {
            super(view);
            mProgressBar = view.findViewById(R.id.foot_progress);
            mTextView = view.findViewById(R.id.foot_text);
        }
    }

/*    static class HeadViewHolder extends RecyclerView.ViewHolder {

        public HeadViewHolder(View view) {
            super(view);
        }
    }*/

    public ActivityAdapter(List<ActivityItem> list) {
        mList = list;
    }

    @Override
    public int getItemViewType(int position) {
        if(position + 1 == getItemCount()){
            return TYPE_FOOT;
        } /*else if(position == 0) {
            return TYPE_HEAD;
        } */else {
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null) {
            mContext = parent.getContext();
        }
        RecyclerView.ViewHolder viewHolder = null;
        if(viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_activity, parent, false);
            viewHolder = new ItemViewHolder(view);
        } else if(viewType == TYPE_FOOT) {
            View view  = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_main_foot, parent, false);
            viewHolder = new FootViewHolder(view);
        } /*else if(viewType == TYPE_HEAD) {
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_main_head, parent, false);
            viewHolder = new HeadViewHolder(view);
        }*/
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if(holder instanceof ItemViewHolder) {
            final ActivityItem item = mList.get(position);
            Glide.with(mContext).load(item.getImageId())
                    .into(((ItemViewHolder) holder).mImage);
            ((ItemViewHolder) holder).mTextTitle.setText(item.getTitle());
            ((ItemViewHolder) holder).mTextTime.setText(item.getTime());
            ((ItemViewHolder) holder).mTextContent.setText(item.getContent());
            Glide.with(mContext).load(item.getAutorId())
                    .into(((ItemViewHolder) holder).mImageAuthor);
            ((ItemViewHolder) holder).mTextAuthor.setText(item.getAuthor());

            ((ItemViewHolder) holder).mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("image_id", item.getImageId());
                    bundle.putString("title", item.getTitle());
                    bundle.putString("time", item.getTime());
                    bundle.putString("institute", item.getInstitute());
                    bundle.putString("location", item.getLocation());
                    bundle.putString("content", item.getContent());
                    bundle.putInt("author_id", item.getAutorId());
                    bundle.putString("author", item.getAuthor());
                    InfoActivity.actionStart(mContext, bundle);
                }
            });
        } else if(holder instanceof  FootViewHolder) {
            Log.e("hhx", "payloads: " + payloads.isEmpty());
            if(!payloads.isEmpty() && ((int)payloads.get(0)) == 0) {
                Log.e("hhx", "没有更多数据");
                ((FootViewHolder) holder).mProgressBar.setVisibility(View.GONE);
                ((FootViewHolder) holder).mTextView.setText("我是有底线的～");
            } else {
                Log.e("hhx", "正在加载数据");
                ((FootViewHolder) holder).mProgressBar.setVisibility(View.VISIBLE);
                ((FootViewHolder) holder).mTextView.setText("加载中...");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList.size()==0 ? 0 : mList.size() + 1;
    }
}

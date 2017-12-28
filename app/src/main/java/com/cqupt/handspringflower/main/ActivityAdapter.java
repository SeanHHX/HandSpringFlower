package com.cqupt.handspringflower.main;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        public FootViewHolder(View view) {
            super(view);
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
                    InfoActivity.actionStart(mContext, item.getImageId(), item.getTitle());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size()==0 ? 0 : mList.size() + 1;
    }
}

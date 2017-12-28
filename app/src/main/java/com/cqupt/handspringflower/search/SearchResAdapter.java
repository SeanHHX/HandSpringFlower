package com.cqupt.handspringflower.search;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cqupt.handspringflower.R;
import com.cqupt.handspringflower.main.ActivityItem;
import com.cqupt.handspringflower.main.InfoActivity;

import java.util.List;

public class SearchResAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<ActivityItem> mList;

    static class ResItemViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView mImage;
        TextView mTextTitle;
        TextView mTextTime;
        TextView mTextContent;
        ImageView mImageAuthor;
        TextView mTextAuthor;

        public ResItemViewHolder(View view) {
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

    public SearchResAdapter(List<ActivityItem> list) {
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null) {
            mContext = parent.getContext();
        }

            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_activity, parent, false);
            return  new ResItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            final ActivityItem item = mList.get(position);
            Glide.with(mContext).load(item.getImageId())
                    .into(((ResItemViewHolder) holder).mImage);
            ((ResItemViewHolder) holder).mTextTitle.setText(item.getTitle());
            ((ResItemViewHolder) holder).mTextTime.setText(item.getTime());
            ((ResItemViewHolder) holder).mTextContent.setText(item.getContent());
            Glide.with(mContext).load(item.getAutorId())
                    .into(((ResItemViewHolder) holder).mImageAuthor);
            ((ResItemViewHolder) holder).mTextAuthor.setText(item.getAuthor());

            ((ResItemViewHolder) holder).mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InfoActivity.actionStart(mContext, item.getImageId(), item.getTitle());
                }
            });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

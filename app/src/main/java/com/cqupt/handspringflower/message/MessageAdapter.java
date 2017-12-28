package com.cqupt.handspringflower.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqupt.handspringflower.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<MessageItem> mList;

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView msgTime;
        TextView msgContent;
        public ItemViewHolder(View view) {
            super(view);
            msgTime = (TextView) view.findViewById(R.id.text_message_time);
            msgContent = (TextView) view.findViewById(R.id.text_message_content);
        }
    }

    public MessageAdapter(List<MessageItem> list) {
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mContext == null) {
            mContext = parent.getContext();
        }

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_message, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MessageItem item = mList.get(position);
        ((ItemViewHolder)holder).msgTime.setText(item.getMsgTime());
        ((ItemViewHolder)holder).msgContent.setText(item.getMsgContent());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}

package com.xiyoumobile.xiyoumobileim;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<MessageObject> messageObjectList;
    private LayoutInflater inflater;
    private Context context;
    public MessageAdapter(List<MessageObject> messageObjectList,Context context) {
        this.messageObjectList = messageObjectList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == MessageObject.ME_TYPE)
            view = inflater.inflate(R.layout.view_me_message_item,parent,false);
        else
            view = inflater.inflate(R.layout.view_message_item,parent,false);
        return new MessageViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.textdata.setText(messageObjectList.get(position).getTextData());
        holder.userName.setText(messageObjectList.get(position).getName());
    }

    @Override
    public int getItemViewType(int position) {
        if (messageObjectList.get(position).isMe())
            return MessageObject.ME_TYPE;
        else
            return MessageObject.OTHER_PEOPLE_TYPE;

    }

    @Override
    public int getItemCount() {
        return messageObjectList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        ImageView userIcon;
        TextView userName;
        TextView textdata;
        public MessageViewHolder(View itemView,int type) {
            super(itemView);
            if (type == MessageObject.ME_TYPE){
                userIcon = itemView.findViewById(R.id.me_user_icon);
                userName = itemView.findViewById(R.id.me_user_name);
                textdata = itemView.findViewById(R.id.me_text_data);
            }else {
                userIcon = itemView.findViewById(R.id.user_icon);
                userName = itemView.findViewById(R.id.user_name);
                textdata = itemView.findViewById(R.id.text_data);
            }
        }
    }
}

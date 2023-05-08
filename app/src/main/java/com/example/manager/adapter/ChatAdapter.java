package com.example.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.model.ChatMessage;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessagesList;
    private String sendId;
    private static final int TYPE_SEND = 1;
    private static final int TYPE_RECEIVE = 2;

    public ChatAdapter(Context context, List<ChatMessage> chatMessagesList, String sendId) {
        this.context = context;
        this.chatMessagesList = chatMessagesList;
        this.sendId = sendId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_SEND) {
            view = LayoutInflater.from(context).inflate(R.layout.item_send_mess, parent, false);
            return new SendMessViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_received, parent, false);
            return new ReceivedMessViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SEND) {
            ((SendMessViewHolder) holder).txtMess.setText(chatMessagesList.get(position).mess);
            ((SendMessViewHolder) holder).txtTime.setText(chatMessagesList.get(position).datetime);
        } else {
            ((ReceivedMessViewHolder) holder).txtMess.setText(chatMessagesList.get(position).mess);
            ((ReceivedMessViewHolder) holder).txtTime.setText(chatMessagesList.get(position).datetime);
        }
    }

    @Override
    public int getItemCount() {
        return chatMessagesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessagesList.get(position).sendID.equals(sendId)) {
            return TYPE_SEND;
        } else {
            return TYPE_RECEIVE;
        }

    }

    class SendMessViewHolder extends RecyclerView.ViewHolder {
        TextView txtMess, txtTime;
        public SendMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMess = itemView.findViewById(R.id.txt_mess_send);
            txtTime = itemView.findViewById(R.id.txt_time_send);

        }
    }

    class ReceivedMessViewHolder extends RecyclerView.ViewHolder {
        TextView txtMess, txtTime;
        public ReceivedMessViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMess = itemView.findViewById(R.id.txt_mess_receive);
            txtTime = itemView.findViewById(R.id.txt_time_receive);        }

    }

}

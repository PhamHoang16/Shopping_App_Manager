package com.example.manager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.Interface.ItemClickListener;
import com.example.manager.R;
import com.example.manager.activity.ChatActivity;
import com.example.manager.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    List<User> userList;
    Context context;

    public UserAdapter(Context context, List<User> userList) {
        this.userList = userList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtid.setText(user.getId() + " ");
        holder.txtuser.setText(userList.get(position).getUsername());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongCLick) {
                isLongCLick = false;
                if (!isLongCLick) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("id", user.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtid, txtuser;
        ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtid = itemView.findViewById(R.id.iduser);
            txtuser = itemView.findViewById(R.id.username);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
            return false;
        }
    }
}

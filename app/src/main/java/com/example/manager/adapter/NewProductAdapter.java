package com.example.manager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manager.Interface.ItemClickListener;
import com.example.manager.R;
import com.example.manager.activity.ChiTietActivity;
import com.example.manager.model.EventBus.SuaXoaEvent;
import com.example.manager.model.NewProduct;
import com.example.manager.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.MyViewHolder> {
    Context context;
    List<NewProduct> arr;

    public NewProductAdapter(Context context, List<NewProduct> arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_product, parent, false);

        return new MyViewHolder(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NewProduct newProduct = arr.get(position);
        holder.textName.setText(newProduct.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textPrice.setText(decimalFormat.format(Double.parseDouble(newProduct.getPrice())) + "đ");
        if (newProduct.getPicture().contains("http")) {
            Glide.with(context).load(newProduct.getPicture()).into(holder.imgPicture);
        } else {
            String hinh = Utils.BASE_URL + "images/" + newProduct.getPicture();
            Glide.with(context).load(hinh).into(holder.imgPicture);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongCLick) {
                if (!isLongCLick) {
                    Intent intent = new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet", newProduct);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    EventBus.getDefault().postSticky(new SuaXoaEvent(newProduct));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView textPrice, textName;
        ImageView imgPicture;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textPrice = itemView.findViewById(R.id.new_product_price);
            textName = itemView.findViewById(R.id.new_product_name);
            imgPicture = itemView.findViewById(R.id.new_product_image);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            contextMenu.add(0, 0, getAdapterPosition(), "Sửa");
            contextMenu.add(0, 1, getAdapterPosition(), "Xoá");
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return false;
        }
    }
}

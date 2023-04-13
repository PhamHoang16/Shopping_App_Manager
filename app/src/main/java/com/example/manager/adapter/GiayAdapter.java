package com.example.manager.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manager.Interface.ItemClickListener;
import com.example.manager.R;
import com.example.manager.activity.ChiTietActivity;
import com.example.manager.model.NewProduct;

import java.text.DecimalFormat;
import java.util.List;

public class GiayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<NewProduct> arr;
    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;


    public GiayAdapter(Context context, List<NewProduct> arr) {
        this.context = context;
        this.arr = arr;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giay, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            NewProduct newProduct = arr.get(position);
            myViewHolder.Name.setText(newProduct.getName().trim());
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.Price.setText(decimalFormat.format(Double.parseDouble(newProduct.getPrice())) + "đ");
            myViewHolder.Description.setText(newProduct.getDescription());
            Glide.with(context).load(newProduct.getPicture()).into(myViewHolder.Picture);
            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongCLick) {
                    if (!isLongCLick) {
                        Intent intent = new Intent(context, ChiTietActivity.class);
                        intent.putExtra("chitiet", newProduct);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        } else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return arr.get(position) == null ? VIEW_TYPE_LOADING: VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Name, Price, Description, idsp;
        ImageView Picture;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.item_giay_ten);
            Price = itemView.findViewById(R.id.item_giay_gia);
            Description = itemView.findViewById(R.id.item_giay_mota);
            Picture = itemView.findViewById(R.id.item_giay_image);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}

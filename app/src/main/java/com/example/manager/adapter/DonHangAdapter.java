package com.example.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.model.Order;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;

    List<Order> listOrder;

    public DonHangAdapter(Context context, List<Order> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_donhang, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = listOrder.get(position);
        holder.txtdonhang.setText("Đơn hàng: " + order.getId() + " ");
        holder.status.setText(orderStatus(order.getStatus()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
            holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(order.getItemList().size());
        //adaper chi tiet
        ChiTietAdapter chiTietAdapter = new ChiTietAdapter(order.getItemList(), context);
        holder.reChitiet.setLayoutManager(layoutManager);
        holder.reChitiet.setAdapter(chiTietAdapter);
        holder.reChitiet.setRecycledViewPool(viewPool);

    }

    private String orderStatus(int status) {
        String result = "";
        switch (status) {
            case 0:
                result = "Đơn hàng đang được xử lý";
                break;
            case 1:
                result = "Đơn hàng đã được chấp nhận";
                break;
            case 2:
                result = "Đơn hàng đã được giao cho đơn vị vận chuyển";
                break;
            case 3:
                result = "Đơn hàng đã được giao";
                break;
            case 4:
                result = "Đơn hàng đã bị huỷ";
                break;
        }

        return result;
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdonhang, status;
        RecyclerView reChitiet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.iddonhang);
            status = itemView.findViewById(R.id.order_status);
            reChitiet = itemView.findViewById(R.id.recycleview_chitiet);
        }
    }
}

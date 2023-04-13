package com.example.manager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.manager.Interface.ImageClickListenner;
import com.example.manager.R;
import com.example.manager.model.EventBus.TinhTongEvent;
import com.example.manager.model.GioHang;
import com.example.manager.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder> {
    Context context;
    List<GioHang> gioHangList;

    public GioHangAdapter(Context context, List<GioHang> gioHangList) {
        this.context = context;
        this.gioHangList = gioHangList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang = gioHangList.get(position);
        holder.item_cart_name.setText(gioHang.getName());
        holder.item_cart_num.setText(String.valueOf(gioHang.getNum()));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.item_cart_price.setText(decimalFormat.format(gioHang.getPrice()) + "đ");
        long totalprice =  gioHang.getNum() * gioHang.getPrice();
        holder.item_cart_totalprice.setText(decimalFormat.format(totalprice));
        Glide.with(context).load(gioHang.getPicture()).into(holder.item_cart_img);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Utils.arr_muahang.add(gioHang);
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                } else {
                    for (int i = 0; i < Utils.arr_muahang.size(); i++) {
                        if (Utils.arr_muahang.get(i).getIdsp() == gioHang.getIdsp()) {
                            Utils.arr_muahang.remove(i);
                        }
                    }
                }
            }
        });
        holder.setListenner(new ImageClickListenner() {
            @Override
            public void onImageCLick(View view, int pos, int giatri) {
                if (giatri == 2) {
                    if (gioHangList.get(pos).getNum() > 1) {
                        int soluongmoi = gioHangList.get(pos).getNum() - 1;
                        gioHangList.get(pos).setNum(soluongmoi);
                        holder.item_cart_num.setText(String.valueOf(gioHangList.get(pos).getNum() + " "));
                        long gia = gioHangList.get(pos).getNum() * gioHangList.get(pos).getPrice();
                        holder.item_cart_totalprice.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    } else if (gioHangList.get(pos).getNum() == 1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xoá sản phẩm này khỏi giỏ hàng?");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.arr_giohang.remove(pos);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Huỷ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();

                    }
                } else if (giatri == 1) {
                    if (gioHangList.get(pos).getNum() < 11) {
                        int soluongmoi = gioHangList.get(pos).getNum() + 1;
                        gioHangList.get(pos).setNum(soluongmoi);

                    }
                    holder.item_cart_num.setText(String.valueOf(gioHangList.get(pos).getNum() + " "));
                    long gia = gioHangList.get(pos).getNum() * gioHangList.get(pos).getPrice();
                    holder.item_cart_totalprice.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return gioHangList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView item_cart_img, img_remove, img_add;
        TextView item_cart_name, item_cart_price, item_cart_num, item_cart_totalprice;
        ImageClickListenner listenner;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_cart_img = itemView.findViewById(R.id.item_cart_img);
            item_cart_name = itemView.findViewById(R.id.item_cart_name);
            item_cart_price = itemView.findViewById(R.id.item_cart_price);
            item_cart_num = itemView.findViewById(R.id.item_cart_num);
            item_cart_totalprice = itemView.findViewById(R.id.item_cart_totalprice);
            img_remove = itemView.findViewById(R.id.item_cart_remove);
            img_add = itemView.findViewById(R.id.item_cart_add);
            checkBox = itemView.findViewById(R.id.item_cart_check);

            //event click
            img_remove.setOnClickListener(this);
            img_add.setOnClickListener(this);

        }

        public void setListenner(ImageClickListenner listenner) {
            this.listenner = listenner;
        }

        @Override
        public void onClick(View view) {
            if (view == img_add) {
                listenner.onImageCLick(view, getAdapterPosition(), 1);
            } else if (view == img_remove) {
                listenner.onImageCLick(view, getAdapterPosition(), 2);
            }
        }
    }
}

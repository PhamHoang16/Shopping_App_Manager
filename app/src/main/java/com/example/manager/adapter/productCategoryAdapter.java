package com.example.manager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.manager.R;
import com.example.manager.model.ProductCategory;

import java.util.List;

public class productCategoryAdapter extends BaseAdapter {
    List<ProductCategory> arr;
    Context context;

    public productCategoryAdapter(Context context, List<ProductCategory> arr) {
        this.arr = arr;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder{
        TextView textensp;
        ImageView imghinhanh;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewholder = null;
        if (view == null) {
            viewholder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_product, null);
            viewholder.textensp = view.findViewById(R.id.item_product_name);
            viewholder.imghinhanh = view.findViewById(R.id.item_image);
            view.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) view.getTag();
        }
        viewholder.textensp.setText(arr.get(i).getName());
        Glide.with(context).load(arr.get(i).getPicture()).into(viewholder.imghinhanh);
        return view;
    }
}

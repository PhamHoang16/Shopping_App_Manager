package com.example.manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.manager.R;
import com.example.manager.model.GioHang;
import com.example.manager.model.NewProduct;
import com.example.manager.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp, giasp, mota;
    NewProduct newProduct;
    Button btnthem;
    ImageView imghinhanh;
    Spinner spinner;
    Toolbar toolbar;
    NotificationBadge badge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });
    }

    private void addToCart() {
        if (Utils.arr_giohang.size() > 0) {
            boolean flag = false;
            int num = Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.arr_giohang.size(); i++) {
                if (Utils.arr_giohang.get(i).getIdsp() == newProduct.getID()) {
                    Utils.arr_giohang.get(i).setNum(num + Utils.arr_giohang.get(i).getNum());
                    flag = true;
                }
            }
            if (flag == false) {
                long price = Long.parseLong(newProduct.getPrice());
                GioHang gioHang = new GioHang();
                gioHang.setPrice(price);
                gioHang.setNum(num);
                gioHang.setIdsp(newProduct.getID());
                gioHang.setName(newProduct.getName());
                gioHang.setPicture(newProduct.getPicture());
                Utils.arr_giohang.add(gioHang);
            }
        } else {
            int num = Integer.parseInt(spinner.getSelectedItem().toString());
            long price = Long.parseLong(newProduct.getPrice());
            GioHang gioHang = new GioHang();
            gioHang.setPrice(price);
            gioHang.setNum(num);
            gioHang.setIdsp(newProduct.getID());
            gioHang.setName(newProduct.getName());
            gioHang.setPicture(newProduct.getPicture());
            Utils.arr_giohang.add(gioHang);
        }
        int totalItem = 0;
        for (int i = 0; i < Utils.arr_giohang.size(); i++) {
            totalItem = totalItem + Utils.arr_giohang.get(i).getNum();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private void initData() {

        newProduct = (NewProduct) getIntent().getSerializableExtra("chitiet");
        tensp.setText(newProduct.getName());
        mota.setText(newProduct.getDescription());
        Glide.with(getApplicationContext()).load(newProduct.getPicture()).into(imghinhanh);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText(decimalFormat.format(Double.parseDouble(newProduct.getPrice())) + "đ");
        Integer[] num = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, num);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        tensp = findViewById(R.id.txtensp);
        giasp = findViewById(R.id.txtgiasp);
        imghinhanh = findViewById(R.id.imgchitiet);
        spinner = findViewById(R.id.spinner);
        mota = findViewById(R.id.txtmotachitiet);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_sl);
        FrameLayout frameLayoutCart = findViewById(R.id.framecart);
        frameLayoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        if (Utils.arr_giohang != null) {
            int totalItem = 0;
            for (int i = 0; i < Utils.arr_giohang.size(); i++) {
                totalItem = totalItem + Utils.arr_giohang.get(i).getNum();
            }
            badge.setText(String.valueOf(totalItem));
        }
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.arr_giohang != null) {
            int totalItem = 0;
            for (int i = 0; i < Utils.arr_giohang.size(); i++) {
                totalItem = totalItem + Utils.arr_giohang.get(i).getNum();
            }

            badge.setText(String.valueOf(totalItem));
        }

    }
}
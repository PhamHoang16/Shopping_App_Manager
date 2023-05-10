package com.example.manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.adapter.GioHangAdapter;
import com.example.manager.model.EventBus.TinhTongEvent;
import com.example.manager.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {

    TextView empty_cart, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GioHangAdapter gioHangAdapter;
    long tongtiensp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        if (Utils.arr_muahang != null) {
            Utils.arr_muahang.clear();
        }
        tinhTongTien();
    }

    private void tinhTongTien() {
        tongtiensp = 0;
        for (int i = 0; i < Utils.arr_muahang.size(); i++) {
            tongtiensp = tongtiensp + (Utils.arr_muahang.get(i).getPrice() * Utils.arr_muahang.get(i).getNum());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (Utils.arr_giohang.size() == 0) {
            empty_cart.setVisibility(View.VISIBLE);
        } else {
            gioHangAdapter = new GioHangAdapter(getApplicationContext(), Utils.arr_giohang);
            recyclerView.setAdapter(gioHangAdapter);
        }

        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tongtiensp == 0) {
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ThanhToanActivity.class);
                    intent.putExtra("tongtien", tongtiensp);
                    Utils.arr_giohang.clear();
                    startActivity(intent);
                }
            }
        });
    }

    private void initView() {
        empty_cart = findViewById(R.id.empty_cart);
        toolbar = findViewById(R.id.toolbar_cart);
        recyclerView = findViewById(R.id.recycleview_cart);
        tongtien = findViewById(R.id.tongtien);
        btnmuahang = findViewById(R.id.btnmuahang);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent event) {
        if (event != null) {
            tinhTongTien();
        }
    }
}
package com.example.manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.adapter.NewProductAdapter;
import com.example.manager.model.EventBus.SuaXoaEvent;
import com.example.manager.model.NewProduct;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClient;
import com.example.manager.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import soup.neumorphism.NeumorphCardView;

public class ManageActivity extends AppCompatActivity {
    ImageView img_them;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    List<NewProduct> newProductList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    NewProductAdapter newProductAdapter;
    NewProduct productSuaXoa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        initView();
        initControl();
        getNewProduct();
    }

    private void initView() {
        img_them = findViewById(R.id.img_them);
        recyclerView = findViewById(R.id.recycleview_manage);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals("Sửa")) {
            suaSanpham();
        } else if (item.getTitle().equals("Xoá")) {
            xoaSanpham();
        }
        return super.onContextItemSelected(item);
    }

    private void xoaSanpham() {
        compositeDisposable.add(apiBanHang.xoaSanpham(productSuaXoa.getID())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    messageModel -> {
                        if (messageModel.isSuccess()) {
                            Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                            getNewProduct();
                        } else {
                            Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } ,
                        throwable -> {
                        Log.d("log", throwable.getMessage());
                        }

                ));
    }

    private void suaSanpham() {
        Intent intent = new Intent(getApplicationContext(), ThemSPActivity.class);
        intent.putExtra("sua", productSuaXoa);
        startActivity(intent);
    }

    private void initControl() {
        img_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThemSPActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getNewProduct() {
        compositeDisposable.add(apiBanHang.getNewProduct()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newProductModel -> {
                            if (newProductModel.isSuccess()) {
                                newProductList = newProductModel.getResult();
                                newProductAdapter = new NewProductAdapter(getApplicationContext(), newProductList);
                                recyclerView.setAdapter(newProductAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Cannot connect to server" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void eventSuaXoa(SuaXoaEvent event) {
        if (event != null) {
            productSuaXoa = event.getNewProduct();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
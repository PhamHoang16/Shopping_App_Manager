package com.example.manager.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.manager.R;
import com.example.manager.adapter.GiayAdapter;
import com.example.manager.model.NewProduct;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClient;
import com.example.manager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class GiayActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    int page = 1;
    int type = 1;
    GiayAdapter giayAdapter;
    List<NewProduct> newProductList;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giay);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        type = getIntent().getIntExtra("type",1);
        AnhXa();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false) {
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == newProductList.size()-1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                newProductList.add(null);
                giayAdapter.notifyItemInserted(newProductList.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                newProductList.remove(newProductList.size()-1);
                giayAdapter.notifyItemRemoved(newProductList.size());
                page = page + 1;
                getData(page);
                giayAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (type == 2) {
            getSupportActionBar().setTitle("Quần Áo");
        } else if (type == 3) {
            getSupportActionBar().setTitle("Phụ kiện");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getProduct(page, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        newProductModel -> {
                            if(newProductModel.isSuccess()) {
                                if (giayAdapter == null) {
                                    newProductList = newProductModel.getResult();
                                    giayAdapter = new GiayAdapter(getApplicationContext(), newProductList);
                                    recyclerView.setAdapter(giayAdapter);
                                } else {
                                    int pos = newProductList.size() - 1;
                                    int added_num = newProductModel.getResult().size();
                                    for (int i = 0; i < added_num; i++) {
                                        newProductList.add(newProductModel.getResult().get(i));
                                    }
                                    giayAdapter.notifyItemRangeInserted(pos, added_num);
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), "No more data!", Toast.LENGTH_LONG).show();
                                isLoading = true;
                            }

                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Can't connect to Server", Toast.LENGTH_LONG).show();
                        }
                )
        );
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar_giay);
        recyclerView = findViewById(R.id.recycleview_giay);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        newProductList = new ArrayList<>();
    }
}
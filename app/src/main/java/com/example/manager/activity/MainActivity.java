package com.example.manager.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.manager.R;
import com.example.manager.adapter.NewProductAdapter;
import com.example.manager.adapter.productCategoryAdapter;
import com.example.manager.model.NewProduct;
import com.example.manager.model.ProductCategory;
import com.example.manager.model.User;
import com.example.manager.photo.Photo;
import com.example.manager.photo.PhotoViewPager2Adapter;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClient;
import com.example.manager.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import me.relex.circleindicator.CircleIndicator3;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewMainScreen;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;

    ViewPager2 viewPager2;

    CircleIndicator3 circleIndicator3;
    productCategoryAdapter productCategoryAdapter;
    List<ProductCategory> ProductList;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<NewProduct> NewProductList;
    NewProductAdapter newProductAdapter;
    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;

    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager2.getCurrentItem() == listPhoto.size() - 1) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
            }
        }
    };

    private List<Photo> listPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if (Paper.book().read("user") != null) {
            User user = Paper.book().read("user");
            Utils.user_current = user;
        }
        getToken();
        Anhxa();
        ActionBar();
        listPhoto = getListPhoto();
        PhotoViewPager2Adapter adapter = new PhotoViewPager2Adapter(listPhoto);
        viewPager2.setAdapter(adapter);

        circleIndicator3.setViewPager(viewPager2);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5000);
            }
        });

        if(isConnected(this)) {
            getProductCategory();
            getNewProduct();
            getEventClick();
        } else {
            Toast.makeText(getApplicationContext(), "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (!TextUtils.isEmpty(s)) {
                            compositeDisposable.add(apiBanHang.updateToken(Utils.user_current.getId(), s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {

                                            },
                                            throwable -> {
                                                Log.d("log", throwable.getMessage());
                                            }
                                    ));
                        }
                    }
                });
    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("yyyy");
                switch (i) {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent giay = new Intent(getApplicationContext(), GiayActivity.class);
                        giay.putExtra("type", 1);
                        startActivity(giay);
                        break;
                    case 2:
                        Intent quanao = new Intent(getApplicationContext(), GiayActivity.class);
                        quanao.putExtra("type", 2);

                        startActivity(quanao);
                        break;
                    case 3:
                        Intent phukien = new Intent(getApplicationContext(), GiayActivity.class);
                        phukien.putExtra("type", 3);
                        startActivity(phukien);
                        break;
                    case 4:
                        Intent thongtin = new Intent(getApplicationContext(), ThongTinActivity.class);
                        startActivity(thongtin);
                        break;
                    case 5:
                        Intent lienhe = new Intent(getApplicationContext(), LienHeActivity.class);
                        startActivity(lienhe);
                        break;
                    case 6:
                        Intent record = new Intent(getApplicationContext(), RecordActivity.class);
                        startActivity(record);
                        break;
                    case 7:
                        Intent manage = new Intent(getApplicationContext(), ManageActivity.class);
                        startActivity(manage);
                        finish();
                        break;
                    case 8:
                        Paper.book().delete("user");
                        FirebaseAuth.getInstance().signOut();
                        Intent signin = new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(signin);
                        finish();
                        break;
                }
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
                                NewProductList = newProductModel.getResult();
                                newProductAdapter = new NewProductAdapter(getApplicationContext(), NewProductList);
                                recyclerViewMainScreen.setAdapter(newProductAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Cannot connect to server" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    private void getProductCategory() {
        compositeDisposable.add(apiBanHang.getProductCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        productCategoryModel -> {
                            if (productCategoryModel.isSuccess()) {
                                ProductList = productCategoryModel.getResult();
                                ProductList.add(new ProductCategory("Quản lí", ""));
                                ProductList.add(new ProductCategory("Đăng xuất", ""));
                                // khoi tao adapter
                                productCategoryAdapter = new productCategoryAdapter(getApplicationContext(), ProductList);
                                listViewManHinhChinh.setAdapter(productCategoryAdapter);
                            }
                        }
                ));
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        int totalItem = 0;
        for (int i = 0; i < Utils.arr_giohang.size(); i++) {
            totalItem = totalItem + Utils.arr_giohang.get(i).getNum();
        }
        badge.setText(String.valueOf(totalItem));
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 5000);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.adidas_ad));
        list.add(new Photo(R.drawable.ad2));
        list.add(new Photo(R.drawable.newyear));
        list.add(new Photo(R.drawable.super_ad));

        return list;
    }

    private void Anhxa() {
        imgsearch = findViewById(R.id.imgsearch);
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        recyclerViewMainScreen = findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerViewMainScreen.setLayoutManager(layoutManager);
        recyclerViewMainScreen.setHasFixedSize(true);
        navigationView = findViewById(R.id.navigationview);
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = findViewById(R.id.drawerlayout);
        viewPager2 = findViewById(R.id.viewpager2);
        circleIndicator3 = findViewById(R.id.circleindicator3);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framecart);
        // khoi tao list
        ProductList = new ArrayList<>();
        NewProductList = new ArrayList<>();
        if (Utils.arr_giohang == null) {
            Utils.arr_giohang = new ArrayList<>();
        } else {
            int totalItem = 0;
            for (int i = 0; i < Utils.arr_giohang.size(); i++) {
                totalItem = totalItem + Utils.arr_giohang.get(i).getNum();
            }
            badge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent giohang = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }



    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
}
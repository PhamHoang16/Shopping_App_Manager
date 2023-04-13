package com.example.manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.manager.R;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClient;
import com.example.manager.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangKyActivity extends AppCompatActivity {
    EditText email, password, reenterpass, username, mobile;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControl();
    }

    private void initControl() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp() {
        String str_email = email.getText().toString().trim();
        String str_pass = password.getText().toString().trim();
        String str_repass = reenterpass.getText().toString().trim();
        String str_username = username.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập mật khẩu!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_repass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập lại mật khẩu!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_username)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập username!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(str_mobile)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập số điện thoại!", Toast.LENGTH_LONG).show();
        } else {
            if (str_pass.equals(str_repass)) {
                compositeDisposable.add(apiBanHang.dangky(str_email, str_pass, str_username, str_mobile)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                userModel -> {
                                    if (userModel.isSuccess()) {
                                        Toast.makeText(getApplicationContext(), "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                                        Utils.user_current.setEmail(str_email);
                                        Utils.user_current.setPassword(str_pass);
                                        Intent intent = new Intent(getApplicationContext(), DangNhapActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                                }
                        ));
            } else {
                Toast.makeText(getApplicationContext(), "Mật khẩu chưa đúng!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        password = findViewById(R.id.pass);
        reenterpass = findViewById(R.id.reenterpass);
        username = findViewById(R.id.username);
        mobile = findViewById(R.id.mobile);
        button = findViewById(R.id.btndangky);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
package com.example.manager.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.manager.R;
import com.example.manager.databinding.ActivityThemspBinding;
import com.example.manager.model.MessageModel;
import com.example.manager.model.NewProduct;
import com.example.manager.retrofit.ApiBanHang;
import com.example.manager.retrofit.RetrofitClient;
import com.example.manager.utils.Utils;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    int type = 0;
    ActivityThemspBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String mediaPath;
    NewProduct productSua;
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThemspBinding.inflate(getLayoutInflater());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setContentView(binding.getRoot());
        initView();
        initData();
        Intent intent = getIntent();
        productSua = (NewProduct) intent.getSerializableExtra("sua");
        if (productSua == null) {
            //themsp
            flag = false;
        } else {
            //suasp
            flag = true;
            binding.btnThemsp.setText("Sửa sản phẩm");
            //show data
            binding.mota.setText(productSua.getDescription());
            binding.giasp.setText(productSua.getPrice()+"");
            binding.tensp.setText(productSua.getName());
            binding.hinhanh.setText(productSua.getPicture());
            binding.spinnerLoai.setSelection(productSua.getType());
        }

    }

    private void initData() {
        List<String> typeList = new ArrayList<>();
        typeList.add("Loại sản phẩm");
        typeList.add("Giày");
        typeList.add("Quần áo");
        typeList.add("Phụ kiện");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.btnThemsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    themSanpham();
                } else {
                    suasanpham();
                }
            }
        });
        binding.imgcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ThemSPActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void suasanpham() {
        String str_tensp = binding.tensp.getText().toString().trim();
        String str_giasp = binding.giasp.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        if (TextUtils.isEmpty(str_tensp) || TextUtils.isEmpty(str_giasp) ||
                TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || type == 0) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_LONG).show();

        } else {
            compositeDisposable.add(apiBanHang.updateProduct(str_tensp, str_giasp, str_hinhanh, str_mota, type, productSua.getID())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }


                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();

                            }
                    ));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPath = data.getDataString();
        uploadMultipleFiles();
        Log.d("log", "onActivityResult: " + mediaPath);
    }

    private void themSanpham() {
        String str_tensp = binding.tensp.getText().toString().trim();
        String str_giasp = binding.giasp.getText().toString().trim();
        String str_hinhanh = binding.hinhanh.getText().toString().trim();
        String str_mota = binding.mota.getText().toString().trim();
        if (TextUtils.isEmpty(str_tensp) || TextUtils.isEmpty(str_giasp) ||
                TextUtils.isEmpty(str_mota) || TextUtils.isEmpty(str_hinhanh) || type == 0) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_LONG).show();

        } else {
            compositeDisposable.add(apiBanHang.addProduct(str_tensp, str_giasp, str_hinhanh, str_mota, type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()) {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_LONG).show();
                                }


                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();

                            }
                    ));

        }
    }

    private String getPath(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }
        return result;
    }

        private void uploadMultipleFiles() {
            Uri uri = Uri.parse(mediaPath);
            File file = new File(getPath(uri));
            RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            Call<MessageModel> call = apiBanHang.uploadFile(fileToUpload);
            call.enqueue(new Callback< MessageModel >() {
                @Override
                public void onResponse(Call < MessageModel > call, Response< MessageModel > response) {
                    MessageModel serverResponse = response.body();
                    if (serverResponse != null) {
                        if (serverResponse.isSuccess()) {
                            binding.hinhanh.setText(serverResponse.getName());
                            //Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        //assert serverResponse != null;
                        Log.v("Response", serverResponse.toString());
                    }
                }
                @Override
                public void onFailure(Call < MessageModel > call, Throwable t) {
                    Log.d("log", t.getMessage());
                }
            });
        }


    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
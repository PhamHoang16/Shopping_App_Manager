package com.example.manager.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.manager.R;

import java.util.ArrayList;
import java.util.List;

public class ThemSPActivity extends AppCompatActivity {
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themsp);
        initView();
        initData();
    }

    private void initData() {
        List<String> typeList = new ArrayList<>();
        typeList.add("Giày");
        typeList.add("Quần áo");
        typeList.add("Phụ kiện");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, typeList);
        spinner.setAdapter(adapter);
    }

    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
    }
}
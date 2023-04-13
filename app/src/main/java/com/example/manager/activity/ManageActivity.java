package com.example.manager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.manager.R;

import soup.neumorphism.NeumorphCardView;

public class ManageActivity extends AppCompatActivity {
    NeumorphCardView themsp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        initView();
        initControl();
    }

    private void initView() {
        themsp = findViewById(R.id.neu_themsanpham);
    }

    private void initControl() {
        themsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThemSPActivity.class);
                startActivity(intent);
            }
        });
    }


}
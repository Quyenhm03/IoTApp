package com.example.iotapp;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class BaseActivity extends AppCompatActivity {

    protected Button btnHome, btnDetail, btnHistory, btnProfile;

    protected void init() {
        btnHome = findViewById(R.id.btnHome);
        btnDetail = findViewById(R.id.btnDetail);
        btnHistory = findViewById(R.id.btnActionHistory);
        btnProfile = findViewById(R.id.btnProfile);
    }

    protected void navigateActivity(Button btnCur, Button btnNext, int bgBtnCur, TextView txtCur, Class<? extends BaseActivity> nextActivity) {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCur.setBackgroundResource(bgBtnCur);
                txtCur.setTextColor(ContextCompat.getColor(BaseActivity.this, R.color.nomal_color));

                Intent intent = new Intent(BaseActivity.this, nextActivity);
                startActivity(intent);
            }
        });
    }

    protected void changeBackgroundButton(Button btn, int bgBtn, TextView txt) {
        btn.setBackgroundResource(bgBtn);
        txt.setTextColor(ContextCompat.getColor(BaseActivity.this, R.color.selected_color));
    }
}

package com.example.iotapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends BaseActivity{

    private TextView txtProfile;
    private EditText edtReport, edtGit, edtApiDoc;
    private Button btnReport, btnGit, btnApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

        txtProfile = findViewById(R.id.txtProfile);
        changeBackgroundButton(btnProfile, R.drawable.profile_select, txtProfile);

        edtReport = findViewById(R.id.edtReport);
        edtGit = findViewById(R.id.edtGit);
        edtApiDoc = findViewById(R.id.edtApiDoc);

        btnReport = findViewById(R.id.btnReport);
        btnGit = findViewById(R.id.btnGit);
        btnApi = findViewById(R.id.btnApi);

        solveGetLink(edtReport);
        solveGetLink(edtGit);
        solveGetLink(edtApiDoc);

        solveNavigate(edtReport, btnReport);
        solveNavigate(edtGit, btnGit);
        solveNavigate(edtApiDoc, btnApi);

        navbarControl();
    }

    private void navbarControl() {
        navigateActivity(btnProfile, btnHome, R.drawable.profile, txtProfile, MainActivity.class);
        navigateActivity(btnProfile, btnDetail, R.drawable.profile, txtProfile, DataSensorActivity.class);
        navigateActivity(btnProfile, btnHistory, R.drawable.profile, txtProfile, ActionHistoryActivity.class);
    }

    private void solveGetLink(EditText edt) {
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String link = edt.getText().toString();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Link", link);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(ProfileActivity.this, "Link is copied!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void solveNavigate(EditText edt, Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = edt.getText().toString();

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}

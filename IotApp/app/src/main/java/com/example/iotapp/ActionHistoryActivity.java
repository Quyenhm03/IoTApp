package com.example.iotapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotapp.Model.ActionHistory;
import com.example.iotapp.Model.DataSensor;
import com.example.iotapp.Service.ActionHistoryService;
import com.example.iotapp.Service.DataSensorService;

import java.util.ArrayList;
import java.util.List;

public class ActionHistoryActivity extends BaseActivity{

    private RecyclerView rcvHistory;
    private ActionHistoryAdapter actionHistoryAdapter;
    private TextView txtHistory;
    private Button btnSearchHis;
    private List<ActionHistory> list = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();

        txtHistory = findViewById(R.id.txtHistory);
        changeBackgroundButton(btnHistory, R.drawable.history_select, txtHistory);

        rcvHistory = findViewById(R.id.rcvHistory);
        actionHistoryAdapter = new ActionHistoryAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvHistory.setLayoutManager(linearLayoutManager);
        rcvHistory.setVerticalScrollBarEnabled(false);
        rcvHistory.setAdapter(actionHistoryAdapter);

        loadData();
        
        btnSearchHis = findViewById(R.id.btnSearchHis);
        btnSearchHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edtSearchHis = findViewById(R.id.edtSearchHis);
                String timeSearch = edtSearchHis.getText().toString();
                if (!timeSearch.matches("\\d{4}-\\d{2}-\\d{2}") && !timeSearch.matches("\\d{2}:\\d{2}:\\d{2}") && !timeSearch.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                    Toast.makeText(ActionHistoryActivity.this, "Please enter the correct date/time format!", Toast.LENGTH_SHORT).show();
                } else {
                    solveSearchHis(timeSearch);
                }
            }
        });

        navbarControl();
    }

    private void navbarControl() {
        navigateActivity(btnHistory, btnHome, R.drawable.history, txtHistory, MainActivity.class);
        navigateActivity(btnHistory, btnDetail, R.drawable.history, txtHistory, DataSensorActivity.class);
        navigateActivity(btnHistory, btnProfile, R.drawable.history, txtHistory, ProfileActivity.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData() {
        new Thread(() -> {
            ActionHistoryService actionHistoryService = new ActionHistoryService();
            list = actionHistoryService.getAllActionHistory();

            runOnUiThread(() -> {
                if(list != null && !list.isEmpty()) {
                    actionHistoryAdapter.setData(list);
                } else {
                    Log.d("Error", "No data.");
                }
            });
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void solveSearchHis(String timeSearch) {
        new Thread(() -> {
            ActionHistoryService actionHistoryService = new ActionHistoryService();
            List<ActionHistory> listSearch = actionHistoryService.getBySearch(timeSearch);

            runOnUiThread(() -> {
                if (listSearch != null && !listSearch.isEmpty()) {
                    actionHistoryAdapter.setData(listSearch);
                } else {
                    Log.d("error", "No data found or failed to retrieve data.");
                }
            });
        }).start();
    }
}

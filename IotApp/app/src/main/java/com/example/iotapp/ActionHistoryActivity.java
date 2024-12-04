package com.example.iotapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private int curPage = 0;
    private boolean isSearch = false, isLoading = false;

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

        rcvHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int totalItemCount = layoutManager.getItemCount();
                    int lastVisibleItem = layoutManager.findLastVisibleItemPosition();

                    if (!isSearch && !isLoading && lastVisibleItem >= totalItemCount - 1) {
                        isLoading = true;
                        curPage++;
                        loadData(curPage);
                    }

                }
            }
        });

        loadData(curPage);
        
        btnSearchHis = findViewById(R.id.btnSearchHis);
        btnSearchHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSearch = true;
                EditText edtSearchHis = findViewById(R.id.edtSearchHis);
                String timeSearch = edtSearchHis.getText().toString();
                if (! timeSearch.matches(".*\\d{1,2}-\\d{2}.*") && ! timeSearch.matches(".*\\d{1,2}-\\d{4}.*") &&
                        !timeSearch.matches("\\d{4}-\\d{2}-\\d{2}") &&  ! timeSearch.matches(".*\\d{1,2}.*") &&
                        !timeSearch.matches("\\d{2}:\\d{2}:\\d{2}") &&  ! timeSearch.matches(".*\\d{1,2}:\\d{2}.*") && !timeSearch.matches(".*\\d{2}-\\d{4}.*") &&
                        !timeSearch.matches(".*\\d{2}-\\d{2}.*") && !timeSearch.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
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
    private void loadData(int page) {
        new Thread(() -> {
            ActionHistoryService actionHistoryService = new ActionHistoryService();
            String API_GetData = "http://192.168.189.2:8000/actionhistory?page=" + page;
            list = actionHistoryService.getActionHistory(API_GetData, page);

            runOnUiThread(() -> {
                if(list != null && !list.isEmpty()) {
                    if(page == 0) {
                        actionHistoryAdapter.setData(list);
                    } else {
                        actionHistoryAdapter.addData(list);
                    }
                } else {
                    Log.d("error", "No data found or failed to retrieve data.");
                }
            });
            isLoading = false;
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
                    Toast.makeText(ActionHistoryActivity.this, "Successful search!", Toast.LENGTH_SHORT).show();
                } else {
                    actionHistoryAdapter.setData(listSearch);
                    Toast.makeText(ActionHistoryActivity.this, "No data found or failed to retrieve data!", Toast.LENGTH_SHORT).show();
                    Log.d("Error", "No data.");
                }
            });
        }).start();
    }
}

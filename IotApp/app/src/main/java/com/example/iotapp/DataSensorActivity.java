package com.example.iotapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotapp.Model.DataSensor;
import com.example.iotapp.Service.DataSensorService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DataSensorActivity extends BaseActivity {

    private RecyclerView rcvDetail;
    private DataSensorAdapter dataSensorAdapter;
    private TextView txtDetail;
    private Spinner spinner;
    private String[] items = {"Temp", "Humid", "Light", "Time"};
    private Button btnSearchData, btnSortTemp, btnSortHumid, btnSortLight, btnSortTime;
    private List<DataSensor> list;
    private List<String> options;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();

        txtDetail = findViewById(R.id.txtDetail);
        changeBackgroundButton(btnDetail, R.drawable.detail_select, txtDetail);

        rcvDetail = findViewById(R.id.rcvDetail);
        dataSensorAdapter = new DataSensorAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvDetail.setLayoutManager(linearLayoutManager);
        rcvDetail.setVerticalScrollBarEnabled(false);
        rcvDetail.setAdapter(dataSensorAdapter);

        loadData();

        navbarControl();

        handleSpinner();

        btnSortTemp = findViewById(R.id.btnSortTemp);
        btnSortHumid = findViewById(R.id.btnSortHumid);
        btnSortLight = findViewById(R.id.btnSortLight);
        btnSortTime = findViewById(R.id.btnSortTime);
        btnSearchData = findViewById(R.id.btnSearchData);

        options = new ArrayList<>();
        options.add("Increase");
        options.add("Decrease");

        handleSearch();

        handleSort(btnSortTemp, 1);
        handleSort(btnSortHumid, 2);
        handleSort(btnSortLight, 3);
        handleSort(btnSortTime, 4);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadData() {
        new Thread(() -> {
            DataSensorService dataSensorService = new DataSensorService();
            String API_GetData = "http://192.168.1.9:8000/datasensor";
            list = dataSensorService.getDataSensor(API_GetData);

            runOnUiThread(() -> {
                if (list != null && !list.isEmpty()) {
                    dataSensorAdapter.setData(list);
                } else {
                    Log.d("error", "No data found or failed to retrieve data.");
                }
            });
        }).start();
    }

    private void navbarControl() {
        navigateActivity(btnDetail, btnHome, R.drawable.detail, txtDetail, MainActivity.class);
        navigateActivity(btnDetail, btnHistory, R.drawable.detail, txtDetail, ActionHistoryActivity.class);
        navigateActivity(btnDetail, btnProfile, R.drawable.detail, txtDetail, ProfileActivity.class);
    }

    private void handleSpinner() {
        spinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner, items);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = items[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }

    private void handleSearch() {
        EditText edtSearchData = findViewById(R.id.edtSearchData);
        btnSearchData.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String searchText = edtSearchData.getText().toString().trim();
                String selectedTopic = spinner.getSelectedItem().toString();

                if (searchText.isEmpty() || selectedTopic.isEmpty()) {
                    Toast.makeText(DataSensorActivity.this, "Please fill out all information!", Toast.LENGTH_SHORT).show();
                } else {

                    if ((selectedTopic.equals("Temp") || selectedTopic.equals("Humid") || selectedTopic.equals("Light")) &&
                            !searchText.matches("[-+]?\\d*\\.\\d+|[-+]?\\d+")) {
                        Toast.makeText(DataSensorActivity.this, "Please enter a valid number!", Toast.LENGTH_SHORT).show();
                    }
                    else if (selectedTopic.equals("Time") &&
                            !searchText.matches("\\d{4}-\\d{2}-\\d{2}") &&
                            !searchText.matches("\\d{2}:\\d{2}:\\d{2}") &&
                            !searchText.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                        Toast.makeText(DataSensorActivity.this, "Please enter the correct date/time format!", Toast.LENGTH_SHORT).show();
                    } else {
                        solveSearchData(searchText, selectedTopic);
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void solveGetDataSearch(String API, String input) {
        new Thread(() -> {
            DataSensorService dataSensorService = new DataSensorService();
            List<DataSensor> listGet = dataSensorService.getBySearch(API, input);

            runOnUiThread(() -> {
                if (listGet != null && !listGet.isEmpty()) {
                    dataSensorAdapter.setData(listGet);
                } else {
                    Log.d("error", "No data found or failed to retrieve data.");
                }
            });
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void solveSearchData(String searchText, String selectedItem) {

        if (selectedItem.equals("Temp")) {
            String API_SearchTemp = "http://192.168.1.9:8000/datasensor/searchTemp?temp=";
            solveGetDataSearch(API_SearchTemp, searchText);
        }

        if(selectedItem.equals("Humid")) {
            String API_SearchHumid = "http://192.168.1.9:8000/datasensor/searchHumid?humid=";
            solveGetDataSearch(API_SearchHumid, searchText);
        }

        if(selectedItem.equals("Light")) {
            String API_SearchLight = "http://192.168.1.9:8000/datasensor/searchLight?light=";
            solveGetDataSearch(API_SearchLight, searchText);
        }

        if(selectedItem.equals("Time")) {
            String API_SearchTime = "http://192.168.1.9:8000/datasensor/searchTime?time=";
            solveGetDataSearch(API_SearchTime, searchText);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void solveGetDataSort(String API) {
        new Thread(() -> {
            DataSensorService dataSensorService = new DataSensorService();
            List<DataSensor> listGet = dataSensorService.getDataSensor(API);

            runOnUiThread(() -> {
                if (listGet != null && !listGet.isEmpty()) {
                    dataSensorAdapter.setData(listGet);
                } else {
                    Log.d("error", "No data found or failed to retrieve data.");
                }
            });
        }).start();
    }

    private void handleSort(Button btn, int type) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(DataSensorActivity.this, v);

                for (int i = 0; i < options.size(); i++) {
                    popupMenu.getMenu().add(0, i, i, options.get(i));
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case 0:
                                if (type == 1) {
                                    solveGetDataSort("http://192.168.1.9:8000/datasensor/sortTemp/increase");
                                } else if (type == 2) {
                                    solveGetDataSort("http://192.168.1.9:8000/datasensor/sortHumid/increase");
                                } else if (type == 3) {
                                    solveGetDataSort("http://192.168.1.9:8000/datasensor/sortLight/increase");
                                } else if (type == 4) {
                                    solveGetDataSort("http://192.168.1.9:8000/datasensor/sortTime/increase");
                                }
                                break;

                            case 1:
                                if (type == 1) {
                                    solveGetDataSort("http://192.168.1.9:8000/datasensor/sortTemp/decrease");
                                } else if (type == 2) {
                                    solveGetDataSort("http://192.168.1.9:8000/datasensor/sortHumid/decrease");
                                } else if (type == 3) {
                                    solveGetDataSort("http://192.168.1.9:8000/datasensor/sortLight/decrease");
                                } else if (type == 4) {
                                    solveGetDataSort("http://1192.168.1.9:8000/datasensor/sortTime/decrease");
                                }
                                break;

                            default:
                                return false;
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }
}

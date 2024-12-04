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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotapp.Model.DataSensor;
import com.example.iotapp.Service.DataSensorService;

import java.util.ArrayList;
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
    private int curPage = 0;
    private boolean isSearch = false, isSort = false;
    private boolean isLoading = false;
    private String API_sort, API_search;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datasensor);
        init();

        txtDetail = findViewById(R.id.txtDetail);
        changeBackgroundButton(btnDetail, R.drawable.detail_select, txtDetail);

        rcvDetail = findViewById(R.id.rcvDetail);
        dataSensorAdapter = new DataSensorAdapter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcvDetail.setLayoutManager(linearLayoutManager);
        rcvDetail.setVerticalScrollBarEnabled(false);
        rcvDetail.setAdapter(dataSensorAdapter);

        rcvDetail.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        if (!isSort) {
                            loadData(curPage);
                        } else {
                            loadDataSort(curPage);
                        }
                    }
                }
            }
        });

        loadData(curPage);

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
    private void loadData(int page) {
        new Thread(() -> {
            DataSensorService dataSensorService = new DataSensorService();
            String API_GetData = "http://192.168.189.2:8000/datasensor?page=" + page;
            list = dataSensorService.getDataSensor(API_GetData, page);

            runOnUiThread(() -> {
                if (list != null && !list.isEmpty()) {
                    if(page == 0) {
                        dataSensorAdapter.setData(list);
                    } else {
                        dataSensorAdapter.addData(list);
                    }
                } else {
                    Log.d("error", "No data found or failed to retrieve data.");
                }
                isLoading = false;
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
                isSearch = true;
                String searchText = edtSearchData.getText().toString().trim();
                String selectedTopic = spinner.getSelectedItem().toString();

                if (searchText.isEmpty() || selectedTopic.isEmpty()) {
                    Toast.makeText(DataSensorActivity.this, "Please fill out all information!", Toast.LENGTH_SHORT).show();
                } else {

                    if ((selectedTopic.equals("Temp") || selectedTopic.equals("Humid") || selectedTopic.equals("Light")) &&
                            !searchText.matches("[-+]?\\d*\\.\\d+|[-+]?\\d+")) {
                        Toast.makeText(DataSensorActivity.this, "Please enter a valid number!", Toast.LENGTH_SHORT).show();
                    }
                    else if (selectedTopic.equals("Time") && ! searchText.matches(".*\\d{1,2}-\\d{2}.*") && ! searchText.matches(".*\\d{1,2}-\\d{4}.*") &&
                            !searchText.matches("\\d{4}-\\d{2}-\\d{2}") &&  ! searchText.matches(".*\\d{1,2}.*") &&
                            !searchText.matches("\\d{2}:\\d{2}:\\d{2}") &&  ! searchText.matches(".*\\d{1,2}:\\d{2}.*") && !searchText.matches(".*\\d{2}-\\d{4}.*") &&
                            !searchText.matches(".*\\d{2}-\\d{2}.*") && !searchText.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
                        Toast.makeText(DataSensorActivity.this, "Please enter the correct date/time format!", Toast.LENGTH_SHORT).show();
                    } else {
                        solveSearchData(searchText, selectedTopic);
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadDataSearch(String input) {
        new Thread(() -> {
            DataSensorService dataSensorService = new DataSensorService();
            List<DataSensor> listGet = dataSensorService.getBySearch(API_search, input);

            runOnUiThread(() -> {
                if (listGet != null && !listGet.isEmpty()) {
                    dataSensorAdapter.setData(listGet);
                    Toast.makeText(DataSensorActivity.this, "Successful search!", Toast.LENGTH_SHORT).show();
                } else {
                    dataSensorAdapter.setData(listGet);
                    Toast.makeText(DataSensorActivity.this, "No data found or failed to retrieve data!", Toast.LENGTH_SHORT).show();
                    Log.d("error", "No data found or failed to retrieve data.");
                }
            });
        }).start();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void solveSearchData(String searchText, String selectedItem) {
        String apiUrl = "http://192.168.189.2:8000/datasensor";

        if (selectedItem.equals("Temp")) {
            API_search = apiUrl + "/searchTemp?temp=";
            loadDataSearch(searchText);
        }

        if(selectedItem.equals("Humid")) {
            API_search = apiUrl + "/searchHumid?humid=";
            loadDataSearch(searchText);
        }

        if(selectedItem.equals("Light")) {
            API_search = apiUrl + "/searchLight?light=";
            loadDataSearch(searchText);
        }

        if(selectedItem.equals("Time")) {
            API_search = apiUrl +  "/searchTime?time=";
            loadDataSearch(searchText);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadDataSort(int page) {
        new Thread(() -> {
            DataSensorService dataSensorService = new DataSensorService();
            List<DataSensor> listGet = dataSensorService.getDataSensor(API_sort+ "?page=" + page, page);

            runOnUiThread(() -> {
                if (listGet != null && !listGet.isEmpty()) {
                    if(page == 0) {
                        dataSensorAdapter.setData(listGet);
                    } else {
                        dataSensorAdapter.addData(listGet);
                    }
                } else {
                    Log.d("error", "No data found or failed to retrieve data.");
                }
                isLoading = false;
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
                        curPage = 0;
                        isSort = true;
                        String apiUrl = "http://192.168.189.2:8000/datasensor";

                        switch (item.getItemId()) {
                            case 0:
                                if (type == 1) {
                                    API_sort = apiUrl + "/sortTemp/increase";
                                    loadDataSort(curPage);
                                } else if (type == 2) {
                                    API_sort = apiUrl + "/sortHumid/increase";
                                    loadDataSort(curPage);
                                } else if (type == 3) {
                                    API_sort = apiUrl + "/sortLight/increase";
                                    loadDataSort(curPage);
                                } else if (type == 4) {
                                    API_sort = apiUrl + "/sortTime/increase";
                                    loadDataSort(curPage);
                                }
                                break;

                            case 1:
                                if (type == 1) {
                                    API_sort = apiUrl + "/sortTemp/decrease";
                                    loadDataSort(curPage);
                                } else if (type == 2) {
                                    API_sort = apiUrl + "/sortHumid/decrease";
                                    loadDataSort(curPage);
                                } else if (type == 3) {
                                    API_sort = apiUrl + "/sortLight/decrease";
                                    loadDataSort(curPage);
                                } else if (type == 4) {
                                    API_sort = apiUrl + "/sortTime/decrease";
                                    loadDataSort(curPage);
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

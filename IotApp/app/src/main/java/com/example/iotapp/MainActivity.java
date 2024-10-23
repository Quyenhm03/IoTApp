package com.example.iotapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.example.iotapp.Model.ActionHistory;
import com.example.iotapp.Model.DataSensor;
import com.example.iotapp.Service.ActionHistoryService;
import com.example.iotapp.Service.DataSensorService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity {

    private LineChart lineChart;
    private TextView txtHome;
    private Switch swtFan, swtLight, swtConditioner;
    private ImageView imgFan, imgLight, imgConditioner;
    private List<DataSensor> list = new ArrayList<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private double curTemp, curHumid, curLight;
    private Handler handler = new Handler();
    private Runnable runnable;
    private boolean isStart;
    private ProgressBar progressTemperature, progressHumidity, progressLight;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStart = true;
        init();

        txtHome = findViewById(R.id.txtHome);
        changeBackgroundButton(btnHome, R.drawable.home_select, txtHome);

        startDataFetching();

        navbarControl();

        solveSwitch();
    }

    private void setUpLineChart() {
        lineChart = findViewById(R.id.lineChart);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setBackgroundColor(Color.WHITE);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setLabelCount(5);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(true);

        // draw line temp
        int[] colorsTemp = {Color.parseColor("#FF0000"), Color.parseColor("#FFFFFF")};
        GradientDrawable gradientDrawableTemp = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsTemp);
        gradientDrawableTemp.setShape(GradientDrawable.RECTANGLE);
        gradientDrawableTemp.setAlpha(255);

        LineDataSet tempSet = new LineDataSet(tempValues(), "Temperature");
        tempSet.setDrawFilled(true);
        tempSet.setColor(Color.parseColor("#FF0000"));
        tempSet.setFillDrawable(gradientDrawableTemp);

        // draw line humid
        int[] colorsHumid = {Color.parseColor("#1C86EE"), Color.parseColor("#FFFFFF")};
        GradientDrawable gradientDrawableHumid = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsHumid);
        gradientDrawableHumid.setShape(GradientDrawable.RECTANGLE);
        gradientDrawableHumid.setAlpha(100);

        LineDataSet humidSet = new LineDataSet(humidValues(), "Humidity");
        humidSet.setDrawFilled(true);
        humidSet.setColor(Color.parseColor("#1C86EE"));
        humidSet.setFillDrawable(gradientDrawableHumid);

        // draw line light
        int[] colorsLight = {Color.parseColor("#FFD700"), Color.parseColor("#FFFFFF")};
        GradientDrawable gradientDrawableLight = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsLight);
        gradientDrawableLight.setShape(GradientDrawable.RECTANGLE);
        gradientDrawableLight.setAlpha(120);

        LineDataSet lightSet = new LineDataSet(lightValues(), "Light");
        lightSet.setDrawFilled(true);
        lightSet.setColor(Color.parseColor("#FFD700"));
        lightSet.setFillDrawable(gradientDrawableLight);
        lightSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(tempSet);
        dataSets.add(humidSet);
        dataSets.add(lightSet);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        lineChart.invalidate();
    }

    private List<Entry> tempValues() {
        ArrayList<Entry> tempValue = new ArrayList<>();
        int start = list.size()-10;
        for(int i = start; i < list.size(); i++) {
            tempValue.add(new Entry(i-start, (float)list.get(i).getTemp()));
        }
        return tempValue;
    }

    private List<Entry> humidValues() {
        ArrayList<Entry> humidValue = new ArrayList<>();
        int start = list.size()-10;
        for(int i = start; i < list.size(); i++) {
            humidValue.add(new Entry(i-start, (float)list.get(i).getHumid()));
        }
        return humidValue;
    }

    private List<Entry> lightValues() {
        ArrayList<Entry> lightValue = new ArrayList<>();
        int start = list.size()-10;
        for(int i = start; i < list.size(); i++) {
            lightValue.add(new Entry(i-start, (float)list.get(i).getLight()));
        }
        return lightValue;
    }

    private void updateChart() {
        lineChart.getData().getDataSetByIndex(0).clear();
        lineChart.getData().getDataSetByIndex(1).clear();
        lineChart.getData().getDataSetByIndex(2).clear();

        int start = list.size() - 10;
        for (int i = start; i < list.size(); i++) {
            ((LineDataSet) lineChart.getData().getDataSetByIndex(0)).addEntry(new Entry(i - start, (float) list.get(i).getTemp()));
            ((LineDataSet) lineChart.getData().getDataSetByIndex(1)).addEntry(new Entry(i - start, (float) list.get(i).getHumid()));
            ((LineDataSet) lineChart.getData().getDataSetByIndex(2)).addEntry(new Entry(i - start, (float) list.get(i).getLight()));
        }

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    private void startDataFetching() {
        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                solveGetData();
                handler.postDelayed(this, 5000);
            }
        };
        handler.post(runnable);
    }

    private void stopDataFetching() {
        handler.removeCallbacks(runnable);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void solveGetData() {
        new Thread(() -> {
            DataSensorService dataSensorService = new DataSensorService();
            list = dataSensorService.getDataSensor("http://192.168.1.9:8000/datasensor");

            runOnUiThread(() -> {
                if (list != null && !list.isEmpty()) {
                    if(isStart){
                        setUpLineChart();
                        isStart = false;
                    } else
                        updateChart();
                    curTemp = list.get(list.size() - 1).getTemp();
                    curHumid = list.get(list.size() - 1).getHumid();
                    curLight = list.get(list.size() - 1).getLight();
                    solveCurrentFigure();

                    Log.d("Get datasensor", "success");
                } else {
                    Log.d("error", "No data found or failed to retrieve data.");
                }
            });
        }).start();
    }

    private void navbarControl() {
        navigateActivity(btnHome, btnDetail, R.drawable.home, txtHome, DataSensorActivity.class);
        navigateActivity(btnHome, btnHistory, R.drawable.home, txtHome, ActionHistoryActivity.class);
        navigateActivity(btnHome, btnProfile, R.drawable.home, txtHome, ProfileActivity.class);
    }

    private void solveSwitch() {
        imgFan = findViewById(R.id.imgFan);
        imgLight = findViewById(R.id.imgLight);
        imgConditioner = findViewById(R.id.imgConditioner);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setDuration(1000);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        swtFan = findViewById(R.id.switch_fan);
        swtLight = findViewById(R.id.switch_light);
        swtConditioner = findViewById(R.id.switch_conditioner);

        loadSwitchStates();

        swtFan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSwitchState("fan", isChecked);

            if (isChecked) {
                imgFan.startAnimation(rotateAnimation);
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(now);

               solvePostActionHistory(new ActionHistory("Fan", "On", currentTime));
            } else {
                imgFan.clearAnimation();
                imgFan.setImageResource(R.drawable.fan);

                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(now);

                solvePostActionHistory(new ActionHistory("Fan", "Off", currentTime));
            }
        });

        swtLight.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSwitchState("light", isChecked);

            if (isChecked) {
                imgLight.setImageResource(R.drawable.light_on);
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(now);

                solvePostActionHistory(new ActionHistory("Light", "On", currentTime));
            } else {
                imgLight.setImageResource(R.drawable.light_off);

                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(now);

                solvePostActionHistory(new ActionHistory("Light", "Off", currentTime));
            }
        });

        swtConditioner.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSwitchState("conditioner", isChecked);

            if (isChecked) {
                imgConditioner.setImageResource(R.drawable.conditioner_on);

                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(now);

                solvePostActionHistory(new ActionHistory("Conditioner", "On", currentTime));
            } else {
                imgConditioner.setImageResource(R.drawable.conditioner);

                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(now);

                solvePostActionHistory(new ActionHistory("Conditioner", "Off", currentTime));
            }
        });
    }

    private void loadSwitchStates() {
        SharedPreferences sharedPreferences = getSharedPreferences("switch_states", MODE_PRIVATE);

        swtFan.setChecked(sharedPreferences.getBoolean("fan", false));
        if (swtFan.isChecked()) {
            RotateAnimation rotateAnimation = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);

            rotateAnimation.setDuration(1000);
            rotateAnimation.setInterpolator(new LinearInterpolator());
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            imgFan.startAnimation(rotateAnimation);
        } else {
            imgFan.clearAnimation();
            imgFan.setImageResource(R.drawable.fan);
        }

        swtLight.setChecked(sharedPreferences.getBoolean("light", false));
        if (swtLight.isChecked()) {
            imgLight.setImageResource(R.drawable.light_on);
        } else {
            imgLight.setImageResource(R.drawable.light_off);
        }

        swtConditioner.setChecked(sharedPreferences.getBoolean("conditioner", false));
        if (swtConditioner.isChecked()) {
            imgConditioner.setImageResource(R.drawable.conditioner_on);
        } else {
            imgConditioner.setImageResource(R.drawable.conditioner);
        }
    }

    private void saveSwitchState(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences("switch_states", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void solvePostActionHistory(ActionHistory actionHistory) {
        ActionHistoryService actionHistoryService = new ActionHistoryService();

        executorService.execute(() -> {
            boolean result = actionHistoryService.postActionHistory(actionHistory);
            if (result) {
                Log.d("Post action history", "Successfully posted.");
            } else {
                Log.e("Post action history", "Failed to post.");
            }
        });
    }

    private void solveCurrentFigure() {
        TextView txtTemperature = findViewById(R.id.txtTemperature);
        TextView txtStateTemp = findViewById(R.id.txtStateTemp);
        progressTemperature = findViewById(R.id.prgTemperature);
        TextView txtHumidity = findViewById(R.id.txtHumidity);
        TextView txtStateHumid = findViewById(R.id.txtStateHumid);
        progressHumidity = findViewById(R.id.prgHumidity);
        TextView txtLight = findViewById(R.id.txtLight);
        TextView txtStateLight = findViewById(R.id.txtStateLight);
        progressLight = findViewById(R.id.prgLight);

        txtTemperature.setText(curTemp + "");
        progressTemperature.setProgress((int) curTemp);

        txtHumidity.setText(curHumid + "");
        progressHumidity.setProgress((int) curHumid);

        txtLight.setText(curLight + "");
        progressLight.setProgress((int) curLight);

        // Xác định trạng thái nhiệt độ
        if (curTemp < 20) {
            txtStateTemp.setText("Low");
            txtStateTemp.setTextColor(Color.RED);
            txtStateTemp.setTypeface(null, Typeface.BOLD);
        } else if (curTemp > 30) {
            txtStateTemp.setText("High");
            txtStateTemp.setTextColor(Color.RED);
            txtStateTemp.setTypeface(null, Typeface.BOLD);
        } else {
            txtStateTemp.setText("Normal");
        }

        // Xác định trạng thái độ ẩm
        if (curHumid < 40) {
            txtStateHumid.setText("Low");
            txtStateHumid.setTextColor(Color.RED);
            txtStateHumid.setTypeface(null, Typeface.BOLD);
           // stopHumidityBlinking(progressHumidity);
        } else if (curHumid > 60) {
            txtStateHumid.setText("High");
            txtStateHumid.setTextColor(Color.RED);
            txtStateHumid.setTypeface(null, Typeface.BOLD);
            //startHumidityBlinking(progressHumidity);
        } else {
            txtStateHumid.setText("Normal");
            //stopHumidityBlinking(progressHumidity);
        }

        // Xác định trạng thái ánh sáng
        if (curLight < 100) {
            txtStateLight.setText("Low");
            txtStateLight.setTextColor(Color.RED);
            txtStateLight.setTypeface(null, Typeface.BOLD);
        } else if (curLight > 300) {
            txtStateLight.setText("High");
            txtStateLight.setTextColor(Color.RED);
            txtStateLight.setTypeface(null, Typeface.BOLD);
        } else {
            txtStateLight.setText("Normal");
        }
    }

//    private boolean isBlinking = false;
//
//    private void startHumidityBlinking(ProgressBar progressHumidity) {
//        if (!isBlinking) {
//            isBlinking = true;
//            handler.post(blinkRunnable);
//        }
//    }
//
//    private void stopHumidityBlinking(ProgressBar progressHumidity) {
//        isBlinking = false;
//        handler.removeCallbacks(blinkRunnable);
//        progressHumidity.setVisibility(View.VISIBLE); // Đảm bảo ProgressBar hiển thị
//    }
//
//    private Runnable blinkRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (isBlinking) {
//                progressHumidity.setVisibility(progressHumidity.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
//                handler.postDelayed(this, 500);
//            }
//        }
//    };

    @Override
    protected void onStop() {
        super.onStop();
        stopDataFetching();
    }
}
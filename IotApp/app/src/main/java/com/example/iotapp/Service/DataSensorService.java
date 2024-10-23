package com.example.iotapp.Service;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.iotapp.Model.DataSensor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DataSensorService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<DataSensor> getDataSensor(String API) {
        List<DataSensor> list = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(API);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Kiểm tra mã phản hồi
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                // Đọc phản hồi từ API
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Phân tích JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());
                JsonNode dataNode = jsonNode.get("data");

                for (int i = 0; i < dataNode.size(); i++) {
                    JsonNode sensorNode = dataNode.get(i);
                    double temp = sensorNode.get("temp").asDouble();
                    double humid = sensorNode.get("humid").asDouble();
                    double light = sensorNode.get("light").asDouble();
                    String time = sensorNode.get("time").asText();

                    Instant instant = Instant.parse(time);

                    ZonedDateTime localDateTime = instant.atZone(ZoneId.systemDefault());

                    String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    list.add(new DataSensor(i + 1, temp, humid, light, formattedTime));
                }
            }

        } catch (Exception e) {
            Log.e("DataSensorService", "Error: ", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return list;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<DataSensor> getBySearch(String API, String input) {
        List<DataSensor> list = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            String API_SearchHumid = API + input;
            URL url = new URL(API_SearchHumid);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Kiểm tra mã phản hồi
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                // Đọc phản hồi từ API
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Phân tích JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());
                JsonNode dataNode = jsonNode.get("data");

                for (int i = 0; i < dataNode.size(); i++) {
                    JsonNode sensorNode = dataNode.get(i);
                    double temp = sensorNode.get("temp").asDouble();
                    double humid = sensorNode.get("humid").asDouble();
                    double light = sensorNode.get("light").asDouble();
                    String time = sensorNode.get("time").asText();

                    Instant instant = Instant.parse(time);

                    ZonedDateTime localDateTime = instant.atZone(ZoneId.systemDefault());

                    String formattedTime = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                    list.add(new DataSensor(i + 1, temp, humid, light, formattedTime));
                }
            }

        } catch (Exception e) {
            Log.e("DataSensorService", "Error: ", e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return list;
    }

}
package com.example.iotapp.Service;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.iotapp.Model.ActionHistory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActionHistoryService {
    private static final String API_URL = "http://192.168.189.2:8000/actionhistory";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<ActionHistory> getActionHistory(String API, int page) {
        List<ActionHistory> list = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(API);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());
                JsonNode dataNode = jsonNode.get("data");

                for(int i = 0; i < dataNode.size(); i++) {
                    JsonNode actionHistory = dataNode.get(i);
                    String device = actionHistory.get("device").asText();
                    String action = actionHistory.get("action").asText();
                    String time = actionHistory.get("time").asText();

                    list.add(new ActionHistory(i+1+page*50, device, action, time));
                }
            }

        } catch(Exception e) {
            Log.e("ActionHistoryService", "Error: " + e);
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return list;
    }

    public boolean postActionHistory(ActionHistory actionHistory) {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(API_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInputString = objectMapper.writeValueAsString(actionHistory);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                Log.e("Post action history", "Response: " + response.toString());
            }

            return responseCode == HttpURLConnection.HTTP_OK;

        } catch(Exception e) {
            Log.e("ActionHistoryService", "Error: " + e);
            return false;
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<ActionHistory> getBySearch(String input) {
        List<ActionHistory> list = new ArrayList<>();
        HttpURLConnection conn = null;

        try {
            String API_Search = "http://192.168.189.2:8000/actionhistory/searchTime?time=" + input;
            URL url = new URL(API_Search);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(response.toString());
                JsonNode dataNode = jsonNode.get("data");

                for(int i = 0; i < dataNode.size(); i++) {
                    JsonNode actionHistory = dataNode.get(i);
                    String device = actionHistory.get("device").asText();
                    String action = actionHistory.get("action").asText();
                    String time = actionHistory.get("time").asText();

                    list.add(new ActionHistory(i+1, device, action, time));
                }
            }

        } catch(Exception e) {
            Log.e("ActionHistoryService", "Error: " + e);
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return list;
    }
}

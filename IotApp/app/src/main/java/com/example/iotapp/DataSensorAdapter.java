package com.example.iotapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.example.iotapp.Model.DataSensor;

public class DataSensorAdapter extends RecyclerView.Adapter<DataSensorAdapter.DataSensorViewHolder> {

    private Context context;
    private List<DataSensor> listDataSensor;

    public DataSensorAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<DataSensor> list) {
        this.listDataSensor = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DataSensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_row, parent, false);
        return new DataSensorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataSensorViewHolder holder, int position) {
        DataSensor row = listDataSensor.get(position);
        if(row == null) {
            return;
        }

        holder.txtIdDetail.setText(String.valueOf(row.getId()));
        holder.txtTempDetail.setText(String.valueOf(row.getTemp()));
        holder.txtHumiDetail.setText(String.valueOf(row.getHumid()));
        holder.txtLightDetail.setText(String.valueOf(row.getLight()));
        holder.txtTimeDetail.setText(row.getTime());
    }

    @Override
    public int getItemCount() {
        if(listDataSensor != null) {
            return listDataSensor.size();
        }
        return 0;
    }

    public class DataSensorViewHolder extends RecyclerView.ViewHolder {

        private TextView txtIdDetail, txtTempDetail, txtHumiDetail, txtLightDetail, txtTimeDetail;

        public DataSensorViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIdDetail = itemView.findViewById(R.id.txtIdDetail);
            txtTempDetail = itemView.findViewById(R.id.txtTempDetail);
            txtHumiDetail = itemView.findViewById(R.id.txtHumiDetail);
            txtTimeDetail = itemView.findViewById(R.id.txtTimeDetail);
            txtLightDetail = itemView.findViewById(R.id.txtLightDetail);
        }
    }
}
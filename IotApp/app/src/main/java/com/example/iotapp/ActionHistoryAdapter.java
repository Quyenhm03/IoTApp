package com.example.iotapp;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iotapp.Model.ActionHistory;
import com.example.iotapp.Model.DataSensor;

import java.util.List;

public class ActionHistoryAdapter extends RecyclerView.Adapter<ActionHistoryAdapter.ActionHistoryViewHolder> {

    private Context context;
    private List<ActionHistory> listHis;

    public ActionHistoryAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ActionHistory> listHis) {
        this.listHis = listHis;
        notifyDataSetChanged();
    }

    public void addData(List<ActionHistory> list) {
        int startPosition = listHis.size();
        listHis.addAll(list);
        notifyItemRangeInserted(startPosition, listHis.size());
    }

    @NonNull
    @Override
    public ActionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        return new ActionHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActionHistoryViewHolder holder, int position) {
        ActionHistory row = listHis.get(position);
        if(row == null) {
            return;
        }

        holder.txtIdHis.setText(String.valueOf(row.getId()));
        holder.txtDeviceHis.setText(String.valueOf(row.getDevice()));
        holder.txtActionHis.setText(String.valueOf(row.getAction()));
        holder.txtTimeHis.setText(row.getTime());

        holder.txtTimeHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Time", row.getTime());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Copy successful!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listHis != null) {
            return listHis.size();
        }
        return 0;
    }

    public class ActionHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView txtIdHis, txtActionHis, txtDeviceHis, txtTimeHis;

        public ActionHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIdHis = itemView.findViewById(R.id.txtIdHis);
            txtDeviceHis = itemView.findViewById(R.id.txtDeviceHis);
            txtActionHis = itemView.findViewById(R.id.txtActionHis);
            txtTimeHis = itemView.findViewById(R.id.txtTimeHis);
        }
    }
}

package com.rifat.qrscanner.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rifat.qrscanner.R;
import com.rifat.qrscanner.activity.model.Bar;

import java.util.List;

public class BarcodeAdapter extends RecyclerView.Adapter<BarcodeAdapter.ViewHolder> {

    private List<Bar> barList;

    public BarcodeAdapter(List<Bar> barList) {
        this.barList = barList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.barcode_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bar barcode = barList.get(position);

        holder.id.setText(String.valueOf(barcode.getId()));
        holder.barcode.setText(barcode.getBarcode());
        holder.time.setText(barcode.getDatetime());


    }

    @Override
    public int getItemCount() {
        return barList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id, barcode, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.idTV);
            barcode = itemView.findViewById(R.id.barcodeTV);
            time = itemView.findViewById(R.id.timeTV);

        }
    }
}

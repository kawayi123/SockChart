package com.hash.socettestdemo.adaper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hash.socettestdemo.R;

import java.util.List;

public class ChartTimeTypeAdaper extends RecyclerView.Adapter<ChartTimeTypeAdaper.ViewHolder> {
    List<String> chartTypeList;
    Context context;
    public ChartTimeTypeAdaper(Context context, List<String> chartTypeList) {
        this.chartTypeList = chartTypeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChartTimeTypeAdaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chart_time_type , parent , false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChartTimeTypeAdaper.ViewHolder holder, int position) {
        String itemName = chartTypeList.get(position);
        holder.tvLine3.setText(itemName);
    }

    @Override
    public int getItemCount() {
        return  this.chartTypeList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvLine3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLine3 = itemView.findViewById(R.id.tv_line3);

        }
    }
}

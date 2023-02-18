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

public class ChartTypeAdaper extends RecyclerView.Adapter<ChartTypeAdaper.ViewHolder> {
    List<String> chartTypeList;
    Context context;
    public ChartTypeAdaper(Context context, List<String> chartTypeList) {
        this.chartTypeList = chartTypeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChartTypeAdaper.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chart_type , parent , false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChartTypeAdaper.ViewHolder holder, int position) {
        String itemName = chartTypeList.get(position);
        holder.tvLine3.setText(itemName);
        switch (position){
            case 0:
                Drawable top1 = context.getResources().getDrawable(R.drawable.line);
                holder.tvLine3.setCompoundDrawablesWithIntrinsicBounds(null, top1 , null, null);
                break;
            case 1:
                Drawable top2 = context.getResources().getDrawable(R.drawable.kline);
                holder.tvLine3.setCompoundDrawablesWithIntrinsicBounds(null, top2 , null, null);
                break;
            case 2:
                Drawable top3 = context.getResources().getDrawable(R.drawable.area_chart);
                holder.tvLine3.setCompoundDrawablesWithIntrinsicBounds(null, top3 , null, null);
                break;
            case 3:
                Drawable top4 = context.getResources().getDrawable(R.drawable.template_bar);
                holder.tvLine3.setCompoundDrawablesWithIntrinsicBounds(null, top4 , null, null);
                break;
        }
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

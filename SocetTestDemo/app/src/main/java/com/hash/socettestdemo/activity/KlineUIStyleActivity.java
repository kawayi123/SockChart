package com.hash.socettestdemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.hash.socettestdemo.R;
import com.hash.socettestdemo.adaper.ChartTimeTypeAdaper;
import com.hash.socettestdemo.adaper.ChartTypeAdaper;

import java.util.ArrayList;
import java.util.List;

public class KlineUIStyleActivity extends AppCompatActivity {
    List<String> chartTypeList = new ArrayList<>();
    List<String> chartTimeTypeList = new ArrayList<>();

    RecyclerView rv1,rv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kline_uistyle);
        rv1 = findViewById(R.id.rv);
        rv2 = findViewById(R.id.rv1);
        chartTypeList.add("Line Chart");
        chartTypeList.add("Candles");
        chartTypeList.add("Aera Line");
        chartTypeList.add("Bar Chart");

        chartTimeTypeList.add("5S");
        chartTimeTypeList.add("30S");
        chartTimeTypeList.add("1M");
        chartTimeTypeList.add("5M");
        chartTimeTypeList.add("15M");
        chartTimeTypeList.add("30M");
        chartTimeTypeList.add("1H");
        chartTimeTypeList.add("2H");

        //ChartTypeAdaper

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv1.setLayoutManager(linearLayoutManager);
        ChartTypeAdaper adaper =new ChartTypeAdaper(this,chartTypeList);
        rv1.setAdapter(adaper);

        //ChartTimeTypeAdaper
        int space = 12;
        rv2.addItemDecoration(new SpacesItemDecoration(space));
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv2.setLayoutManager(linearLayoutManager2);
        ChartTimeTypeAdaper adaper2 =new ChartTimeTypeAdaper(this,chartTimeTypeList);
        rv2.setAdapter(adaper2);

    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
//            outRect.bottom = space;
//            outRect.top = space;

            // Add top margin only for the first item to avoid double space between items
//            if (parent.getChildPosition(view) == 0)
//                outRect.top = space;
        }
    }
}
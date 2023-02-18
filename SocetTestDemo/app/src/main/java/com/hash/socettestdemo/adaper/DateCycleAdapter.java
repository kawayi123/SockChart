package com.hash.socettestdemo.adaper;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hash.socettestdemo.R;
import com.hash.socettestdemo.socket.DataBean;
//import com.vincent.story.DataBean;
//import com.vincent.story.R;

import java.util.List;

/**
 * 时间周期适配器
 */
public class DateCycleAdapter extends BaseQuickAdapter<DataBean, BaseViewHolder> {

    private DateListener listener;

    private Context mContext;
    public void setListener(DateListener listener) {
        this.listener = listener;
    }

    public DateCycleAdapter(Context context, int layoutResId, @Nullable List<DataBean> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DataBean item) {
        helper.setText(R.id.tv_data, item.getData());
        if (item.isSelected())
            ((TextView) helper.getView(R.id.tv_data)).setTextColor(ContextCompat.getColor(mContext, R.color.color_6384FF));
        else
            ((TextView) helper.getView(R.id.tv_data)).setTextColor(ContextCompat.getColor(mContext, R.color.white));

        helper.getView(R.id.tv_data).setOnClickListener(l -> {
//            initData();
            listener.chooseDate(item.getData(), item.getPosition());
        });

    }

//
//    public void initData() {
////        List<DataBean> list = getData();
//        for (DataBean bean : getData()) {
//            bean.setSelected(false);
//        }
//    }

    public interface DateListener {
        void chooseDate(String period, int position);
    }

}

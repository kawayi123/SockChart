package com.hash.socettestdemo.wiget;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hash.socettestdemo.R;
import com.hash.socettestdemo.wiget.segmented.SegmentedControlItem;
import com.hash.socettestdemo.wiget.segmented.SegmentedControlView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BtnBottomDialog extends BottomSheetDialogFragment {
    private SegmentedControlView mScv1;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getActivity() == null) return super.onCreateDialog(savedInstanceState);


        BottomSheetDialog dialog = new BottomSheetDialog(getActivity(), R.style.BottomSheet);

        View root = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_layout, null);
        mScv1 = (SegmentedControlView)root.findViewById(R.id.scv1);

        dialog.setContentView(root);

        //设置宽度
        ViewGroup.LayoutParams params = root.getLayoutParams();
        params.height = (int) (0.75 *
                getResources().getDisplayMetrics().heightPixels);


        root.setLayoutParams(params);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomSheet);
        }

        List<SegmentedControlItem> segmentedControlItem = createItem();
        mScv1.addItems(segmentedControlItem);
        mScv1.setOnSegItemClickListener(new SegmentedControlView.OnSegItemClickListener() {
            @Override
            public void onItemClick(SegmentedControlItem item, int position) {
                String msg = String.format(Locale.getDefault(), "click scv1 selected:%d", position);
                Log.d(TAG, msg);
            }
        });
        return dialog;

    }

    private List<SegmentedControlItem> createItem(){
        List<SegmentedControlItem> items = new ArrayList<>();
        items.add(new SegmentedControlItem(getResources().getString(R.string.watichlist)));
        items.add(new SegmentedControlItem(getResources().getString(R.string.crypto)));
        items.add(new SegmentedControlItem(getResources().getString(R.string.forex)));
        return items;
    }
    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        //在show之前设置弹出动画
        super.show(manager, tag);
        //在show之后设置关闭动画
    }
}


package com.hash.socettestdemo.wiget;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hash.socettestdemo.R;

public class BottomSheetDialogFragment extends AppCompatDialogFragment {
    public BottomSheetDialogFragment() {
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){
        Window window = new BottomSheetDialog(this.getContext(),getTheme()).getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.BottomSheet);
        }
        return new BottomSheetDialog(this.getContext(),getTheme());

    }
}

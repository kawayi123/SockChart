package com.hash.socettestdemo.activity.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.zhangke.websocket.WebSocketHandler;

public abstract class BaseActivity extends com.duxl.baselib.ui.activity.BaseActivity {

    private final String TAG = "BaseActivity";
//    private ActivityResultLauncher<Intent> mIntentActivityResultLauncher;
//    public static final String KEY_INTERNAL_REQUEST_CODE = "internalRequestCode";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mIntentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
//            int requestCode = 0;
//            if (result != null && result.getData() != null) {
//                requestCode = result.getData().getIntExtra(KEY_INTERNAL_REQUEST_CODE, 0);
//            }
//            onActivityResult(requestCode, result);
//        });
    }

    @Override
    protected void initView(View v) {
        super.initView(v);
//        if (isNightMode()) {
//            setStateBarLightMode();
//        } else {
//            setStateBarDarkMode();
//        }
    }

    @Override
    public void onBackPressed() {
//        onClickActionBack(findViewById(com.duxl.baselib.R.id.iv_action_back));
    }


    /**
     * 是否是深色模式
     *
     * @return
     */
//    protected boolean isNightMode() {
////        int uiMode = getResources().getConfiguration().uiMode;
////        return (uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
//    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!WebSocketHandler.getDefault().isConnect()) {
            Log.i(TAG, "尝试重连");
            WebSocketHandler.getDefault().reconnect();
        }
    }

    protected void goActivity(Class clsActivity) {
        goActivity(clsActivity, null);
    }

    protected void goActivity(Class clsActivity, Bundle extras) {
        Intent intent = new Intent(this, clsActivity);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(intent);
    }

//    protected void goActivity(Class clsActivity, int requestCode) {
//        goActivity(clsActivity, null, requestCode);
//    }

//    protected void goActivity(Class clsActivity, Bundle extras, int requestCode) {
//        Intent intent = new Intent(this, clsActivity);
//        intent.putExtra(KEY_INTERNAL_REQUEST_CODE, requestCode);
//        if (extras != null) {
//            intent.putExtras(extras);
//        }
//        mIntentActivityResultLauncher.launch(intent);
//    }

//    protected void setResult2(int resultCode) {
//        setResult2(resultCode, null);
//    }
//
//    protected void setResult2(int resultCode, Intent data) {
//        if (data == null) {
//            data = new Intent();
//        }
//        data.putExtra(KEY_INTERNAL_REQUEST_CODE, getIntent().getIntExtra(KEY_INTERNAL_REQUEST_CODE, 0));
//        super.setResult(resultCode, data);
//    }


//    protected void onActivityResult(int requestCode, ActivityResult result) {
//
//    }

//
//    protected void slideOutNone() {
//        overridePendingTransition(0, 0);
//    }
//
//    protected boolean hasPermission(String permission) {
//        return ContextCompat.checkSelfPermission(this, permission)
//                == PackageManager.PERMISSION_GRANTED;
//    }
}

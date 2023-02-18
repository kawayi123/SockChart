package com.hash.socettestdemo.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.duxl.baselib.BaseApplication;
import com.duxl.baselib.http.GlobalHttpConfig;
import com.duxl.baselib.utils.Utils;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;

public class App extends BaseApplication {

    private final String TAG = "App";

    private int mCurrentUIModel;
    private Handler loopConnectHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.setApp(this);
        initWebSocket();
//        mCurrentUIModel = getResources().getConfiguration().uiMode;
    }

    @Override
    public GlobalHttpConfig getGlobalHttpConfig() {
        return new AppGlobalHttpConfig();
    }

    private void initWebSocket() {
        WebSocketSetting setting = new WebSocketSetting();
        // wss://echo.websocket.org
        //setting.setConnectUrl("wss://echo.websocket.org");
        setting.setConnectUrl(Constants.SOCKET_SERVER);

        //Timeout
        setting.setConnectTimeout(15 * 1000);

        //Setting LostTimeout
        setting.setConnectionLostTimeout(60);

        //setReconnectFrequency
        setting.setReconnectFrequency(60);


        setting.setReconnectWithNetworkChanged(true);


        WebSocketManager manager = WebSocketHandler.init(setting);

        manager.start();

        WebSocketHandler.registerNetworkChangedReceiver(this);

        if (loopConnectHandler == null) {
            HandlerThread loopConnectHandlerThread = new HandlerThread("loopConnectHandlerThread");
            loopConnectHandlerThread.start();
            loopConnectHandler = new Handler(loopConnectHandlerThread.getLooper());
        }
        loopConnectHandler.post(loopCheckConnect);
    }

    private Runnable loopCheckConnect = new Runnable() {
        @Override
        public void run() {
            if (!WebSocketHandler.getDefault().isConnect()) {
                Log.i(TAG, "socket is disConnect, try reconnect");
                WebSocketHandler.getDefault().reconnect();
            } else {
                Log.i(TAG, "socket is connected");
            }
            loopConnectHandler.postDelayed(loopCheckConnect, WebSocketHandler.getDefault().isConnect() ? 1000 : 50);
        }
    };


}

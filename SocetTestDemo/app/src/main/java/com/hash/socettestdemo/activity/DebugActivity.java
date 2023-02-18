package com.hash.socettestdemo.activity;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

//import com.exchange2currency.ef.currencyconvert.grpc.Price;
import com.exchange2currency.ef.currencyconvert.grpc.Price;
import com.google.gson.Gson;
import com.hash.socettestdemo.R;
import com.hash.socettestdemo.activity.base.BaseActivity;
import com.hash.socettestdemo.socket.Dispatch;
import com.hash.socettestdemo.socket.SocketSender;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import org.java_websocket.framing.Framedata;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class DebugActivity extends BaseActivity {
    //TODO Socket响应开始时间
    Long statTime = 0L;
    //TODO Socket响应结束时间
    Long endTime = 0L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebSocketHandler.getDefault().addListener(socketListener);
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_debug;
    }

    public void getKlineUIStyle(View view){
        Intent goIntent = new Intent(DebugActivity.this,KlineUIStyleActivity.class);
        startActivity(goIntent);

    }

    public void getKlineData(View view) {
        Long startTime = new BigDecimal( SocketSender.date2TimeStamp("2023-01-05 23:18:24","yyyy-MM-dd HH:mm:ss")).longValue();
        Long endTime = new BigDecimal( SocketSender.date2TimeStamp("2023-01-04 23:18:24","yyyy-MM-dd HH:mm:ss")).longValue();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("start_time",startTime);
            jsonObject.put("end_time",endTime);
            jsonObject.put("type",2);
            jsonObject.put("symbol","BTCETH");
            jsonObject.put("cid",SocketSender.getRandom(10));
            jsonObject.put("cmd",15);
            System.out.println("========req data======"+jsonObject.toString());
            SocketSender.sendGetKLineRequest(SocketSender.getRandom(10)+"","BTCETH",2,startTime,endTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static void getRequest(String tokenTo,String tokenFrom,String dateUnit) {
        Dispatch.I.submit(new GetRequestRunnable(tokenTo,tokenFrom,dateUnit));
    }

    private static class GetRequestRunnable implements Runnable {
        String tokenTo;
        String tokenFrom;
        String dateUnit;
        public GetRequestRunnable(String tokenTo, String tokenFrom,String dateUnit) {
            this.tokenFrom = tokenFrom;
            this.tokenTo = tokenTo;
            this.dateUnit = dateUnit;
        }

        @Override
        public void run() {
            SocketSender.sendGetQuotationRequest(tokenTo,  tokenFrom, dateUnit, "");
        }
    }
    public void getNewKlineDataList(View view){
        SocketSender.sendGetQuotationRequest("ETH", "BTC", "24H", "");
    }
    public void getNewKlinePage(View view){
        Intent goIntent = new Intent(DebugActivity.this, KlineActivity.class);
        startActivity(goIntent);
    }
    /**
     * 获取行情列表
     *
     * @param view
     */
    public void onGetQuotation(View view) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                statTime = System.currentTimeMillis();
//                getRequest("USD", "CNY", "24H");
//            }
//        }).start();
        statTime = System.currentTimeMillis();
//        SocketSender.sendSymbolsRateRequest("USD,BTC,USDT,DOGE,ETH,FJD");
        SocketSender.sendGetQuotationRequest("ETH", "BTC", "24H", "");

//        getRequest("ETH", "BTC", "24H");

    }

    /**
     * 获取行情列表Stop
     *
     * @param view
     */
    public void onGetQuotationStop(View view) {
        SocketSender.sendGetQuotationStopRequest();
    }

    /**
     * 获取一个币对的价格比
     *
     * @param view
     */
    public void getSymbolsRateRequest(View view) {
        SocketSender.sendSymbolsRateRequest("USD,BTC,USDT,DOGE,ETH,FJD");
    }

    /**
     * 获取当前位置的初始5个货币
     *
     * @param v
     * @deprecated
     */
    public void defaultCurrency(View v) {
        byte[] bytes = Price.GetDefaultCurrencyTokensListRequest.newBuilder()
                .setCmd(13)
                .setCid(SocketSender.getRandom(10)+"")
                .build().toByteArray();
        WebSocketHandler.getDefault().send(bytes);
        log("发送请求：GetDefaultCurrencyTokensListRequest");
    }

    /**
     * 获取当前位置默认货币
     *
     * @param v
     */
    public void currentDefaultCurrency(View v) {
        SocketSender.sendCurrentDefaultCurrency(6, "CN");
    }

    /**
     * 获取货币类型
     *
     * @param v
     * @deprecated
     */
    public void getCurrencyTokens(View v) {
        byte[] bytes = Price.GetCurrencyTokensRequest.newBuilder()
                .setCmd(7)
                .setCid(SocketSender.getRandom(10)+"")
                .setToken("CNY")
                .build().toByteArray();
        WebSocketHandler.getDefault().send(bytes);
        log("发送请求：GetCurrencyTokensRequest");
    }

    /**
     * 获取货币类型列表
     *
     * @param v
     */
    public void getCurrencyTokensList(View v) {
        SocketSender.sendCurrencyTokensList();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        WebSocketHandler.getDefault().removeListener(socketListener);
    }

    private SocketListener socketListener = new SimpleListener() {
        @Override
        public void onConnected() {
            super.onConnected();
            log("onConnected 连接成功");
        }

        @Override
        public void onConnectFailed(Throwable e) {
            super.onConnectFailed(e);
            log("onConnectFailed 连接失败");
        }

        @Override
        public void onDisconnect() {
            super.onDisconnect();
            log("onDisconnect");
        }

        @Override
        public <T> void onMessage(String message, T data) {
            super.onMessage(message, data);
            log("onMessage(message)");
        }

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            super.onSendDataError(errorResponse);
            log("onSendDataError");
        }

        @Override
        public <T> void onMessage(ByteBuffer bytes, T data) {
            super.onMessage(bytes, data);
            log("onMessage(bytes)");
            try {
                Price.CommandHead commandHead = Price.CommandHead.parseFrom(bytes);
                log("收到响应cmd：" + commandHead.getCmd());
                switch (commandHead.getCmd()) {
                    case 15://kline行情
                        Price.GetKLineResponse getQuotationResponse1 = Price.GetKLineResponse.parseFrom(bytes);
                        Gson gson9 = new Gson();
                        String getQuotationResponseJson9 = gson9.toJson(getQuotationResponse1);
                        log("kline收到行情应数据: \n" + getQuotationResponseJson9);
                        break;
                    case 2: // 行情数据
                        endTime = System.currentTimeMillis();
                        long currTime = endTime - statTime;
                        log("收到行情应时差（1）: \n" + currTime+
                                "====收到行情应时差（2）===="+currTime / 100+
                                "====收到行情应时差（3）===="+new BigDecimal(currTime / 100 / 60).setScale(6,BigDecimal.ROUND_DOWN).toString());
                        Price.GetQuotationResponse getQuotationResponse = Price.GetQuotationResponse.parseFrom(bytes);
                        Gson gson = new Gson();

                        String getQuotationResponseJson = gson.toJson(getQuotationResponse);
                        if (!getQuotationResponse.getIsReal()){
                            log("收到行情应数据1: \n" + getQuotationResponseJson);

                        }else {
                            log("收到行情应数据: \n" + getQuotationResponseJson);

                        }
                        break;
                    case 4:
                        Price.GetQuotationStopResponse getQuotationStopResponse = Price.GetQuotationStopResponse.parseFrom(bytes);
                        Gson gsonResponse7 = new Gson();
                        String getQuotationResp7 = gsonResponse7.toJson(getQuotationStopResponse);
                        log("收到行情应Stop数据: \n" + getQuotationResp7);
                        break;

                    case 6:
                        Price.GetSymbolsRateResponse getSymbolsRateResponse = Price.GetSymbolsRateResponse.parseFrom(bytes);
                        Gson gsonResponse = new Gson();
                        String getQuotationResp = gsonResponse.toJson(getSymbolsRateResponse);
                        log("收到一个币对的价格比数据：\n" + getQuotationResp);
                        break;

                    case 14:
                        Price.GetDefaultCurrencyTokensListResponse getDefaultCurrencyTokensListResponse = Price.GetDefaultCurrencyTokensListResponse.parseFrom(bytes);
                        Gson gsonResponse1 = new Gson();
                        String getQuotationResp1 = gsonResponse1.toJson(getDefaultCurrencyTokensListResponse);
                        log("收到默认货币数据：\n" + getQuotationResp1);
                        break;
                    case 12:
                        Price.GetCurrentCurrencyTokensResponse getCurrentCurrencyTokensResponse = Price.GetCurrentCurrencyTokensResponse.parseFrom(bytes);
                        Gson gsonResponse6 = new Gson();
                        String getQuotationResp6 = gsonResponse6.toJson(getCurrentCurrencyTokensResponse);
                        log("收到当前位置默认货币数据: \n" + getQuotationResp6);
                        break;

                    case 8:
                        Price.GetCurrencyTokensResponse getCurrencyTokensResponse = Price.GetCurrencyTokensResponse.parseFrom(bytes);
                        Gson gsonResponse5 = new Gson();
                        String getQuotationResp5 = gsonResponse5.toJson(getCurrencyTokensResponse);
                        log("收到货币类型数据: \n" + getQuotationResp5);
                        break;
                    case 10:
                        Price.GetCurrencyTokensListResponse getCurrencyTokensListResponse = Price.GetCurrencyTokensListResponse.parseFrom(bytes);
                        Gson gsonResponse4 = new Gson();
                        String getQuotationResp4 = gsonResponse4.toJson(getCurrencyTokensListResponse);
                        log("收到货币类型列表数据: \n" + getQuotationResp4);
                        break;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPing(Framedata framedata) {
            super.onPing(framedata);
            log("发送 ping");
        }

        @Override
        public void onPong(Framedata framedata) {
            super.onPong(framedata);
            log("发送 pong");
        }
    };


    private void log(String msg) {
        Log.d("DebugActivity", msg);
    }
}
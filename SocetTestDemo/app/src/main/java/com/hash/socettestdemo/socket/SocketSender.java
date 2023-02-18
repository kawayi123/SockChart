package com.hash.socettestdemo.socket;

import android.util.Log;

import com.duxl.baselib.utils.NullUtils;
//import com.exchange2currency.ef.currencyconvert.grpc.Price;
import com.exchange2currency.ef.currencyconvert.grpc.Price;
import com.google.gson.Gson;
import com.google.protobuf.GeneratedMessageV3;
import com.zhangke.websocket.WebSocketHandler;

import java.text.SimpleDateFormat;
import java.util.Random;
//import com.hash.coinconvert.utils.DateUtil;
import com.zhangke.websocket.WebSocketHandler;

/**
 * socket send data class
 */
public class SocketSender {

    private static final String TAG = "SocketSender";

    private SocketSender() {

    }
    /**
     * Request a Kline Quote
     *
     * @param cid          Random （int32）
     * @param symbol       sample：BTCETH
     * @param type         timeUnit  1, 1hour  2, 1day 1hour=1， 1day=2
     * @param start_time   lang utc time , millisecond
     * @param end_time     lang  ust time, 0: to now , millisecond
     */
    public static void sendGetKLineRequest(String cid,String symbol,int type, long start_time,long end_time){
        Price.GetKLineRequest message =  Price.GetKLineRequest.newBuilder()
                .setCmd(15)
                .setCid(getRandom(10)+"")
                .setCid(cid)
                .setSymbol(symbol)
                .setType(type)
                .setStartTime(start_time)
                .setEndTime(end_time)
                .build();
        send(message);
    }
    /**
     * Request a list of quotes
     *
     * @param tokenFrom
     * @param tokenTo
     * @param dateUnit
     * @param lang
     */
    public static void sendGetQuotationRequest(String tokenFrom, String tokenTo, String dateUnit, String lang) {
        Price.GetQuotationRequest message = Price.GetQuotationRequest.newBuilder()
                .setCmd(1)
                .setCid(getRandom(10)+"")
                .setTokenFrom(tokenFrom)
                .setTokenTo(tokenTo)
                .setDateUnit(dateUnit)
                .setLang(NullUtils.format(lang).toString())
                .build();
        send(message);

    }

    /**
     * Request for a list of kline quotes
     *
     * @param symbol  // default 0： leave blank
     * @param type  //utc time , millisecond, 0, leave blank
     * @param start_time  //utc time , millisecond, 0, default
     * @param end_time // ust time, 0: to now , millisecond
     */
    public static void sendGetTradeLineRequest(String symbol, int type,long start_time,long end_time) {
        Price.GetTradeLineRequest message = Price.GetTradeLineRequest.newBuilder()
                .setCmd(1)
                .setCid(getRandom(10)+"")
                .setSymbol(symbol)
                .setType(type)
                .setStartTime(start_time)
                .setEndTime(end_time)
                .build();
        send(message);
    }




    /**
     * Get the market listStop
     */
    public static void sendGetQuotationStopRequest() {
        Price.GetQuotationStopRequest message = Price.GetQuotationStopRequest.newBuilder()
                .setCmd(3)
                .setCid(getRandom(10)+"")
                .build();
        send(message);
    }


    /**
     * Get the price ratio of a currency pair
     * @param tokens sample：USD,BTC,USDT,DOGE,ETH,FJD
     */
    public static void sendSymbolsRateRequest(String tokens) {
        Price.GetSymbolsRateRequest message = Price.GetSymbolsRateRequest.newBuilder()
                .setCmd(5)
                .setCid(getRandom(10)+"")
                .setTokens(tokens)
                .build();
        send(message);
    }


    /**
     * Get current location default currency
     * @param count
     * @param location location，sample：CN
     */
    public static void sendCurrentDefaultCurrency(int count, String location) {
        Price.GetCurrentCurrencyTokensRequest message = Price.GetCurrentCurrencyTokensRequest.newBuilder()
                .setCmd(11)
                .setCount(count)
                .setCid(getRandom(10)+"")
                .setLocation(NullUtils.format(location).toString())
                .build();
        send(message);
    }

    /**
     * Get a list of currency types
     */
    public static void sendCurrencyTokensList() {
        Price.GetCurrencyTokensListRequest message = Price.GetCurrencyTokensListRequest.newBuilder()
                .setCmd(9)
                .setCid(getRandom(10)+"")
                .build();
        send(message);
    }

    /**
     * Sending Messaging
     *
     * @param message
     */
    private static void send(GeneratedMessageV3 message) {
        try {
            Gson gson = new Gson();
// Object -->json
            String sendJson = gson.toJson(message);
//            String sendJson = JsonFormat.printer().print(message);
            Log.d(TAG, "send socket data: " + sendJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WebSocketHandler.getDefault().send(message.toByteArray());
    }
    //getRandom
    public static int getRandom(int number){
        Random random = new Random(System.currentTimeMillis());
        int index = random.nextInt(number);
        return index;
    }
    public static String date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date).getTime() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}


package com.xiyoumobile.xiyoumobileim;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.LinkedBlockingQueue;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WebSocketConnect implements Runnable{
    public static final int CONNECT_CLOSE = 0;
    public static final int CONNECT_OPEN = 1;
    public static final int ON_MESSAGE = 2;
    public static OkHttpClient client = new OkHttpClient();
    private Handler handler;

    LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>(128);
    private boolean flag = true;

    public WebSocketConnect(Handler handler) {
        this.handler = handler;
    }

    public WebSocket webSocket;

    public void sendMessage(String message){
        if (webSocket == null)
            return;
        try {
            queue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
            handler.obtainMessage(CONNECT_CLOSE);
            flag = false;
            webSocket.close(1000,null);
        }
    }

    @Override
    public void run() {
        Request request = new Request.Builder()
                .url("ws://139.199.20.248:8080/im/chatRoom")
                .build();
        client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                Log.d("websocket","onOpen");
                WebSocketConnect.this.webSocket = webSocket;
                handler.sendMessage(handler.obtainMessage(CONNECT_OPEN));
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                Log.d("websocket","onMessage");
                Log.d("websocket",text);
                handler.sendMessage(handler.obtainMessage(ON_MESSAGE,text));
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                Log.d("websocket","onClosing");
                super.onClosing(webSocket, code, reason);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                Log.d("websocket","onClosed");
               handler.sendMessage( handler.obtainMessage(CONNECT_CLOSE));
                dealConnection();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                Log.d("websocket","onFailure");
                handler.sendMessage(handler.obtainMessage(CONNECT_CLOSE));
                dealConnection();
            }
        });

        try{
            while(flag){
                String message = queue.take();
                if (webSocket != null){
                    webSocket.send(createMessageObject(message));
                }else{
                    flag = false;
                }
            }
        }catch (InterruptedException | JSONException e){
            e.printStackTrace();
            handler.sendMessage(handler.obtainMessage(CONNECT_CLOSE));
            dealConnection();
        }
    }

    public void dealConnection(){
        if (webSocket!=null)
            webSocket.close(1000,null);
        this.webSocket = null;
        queue.clear();
        flag = false;
    }

    public String createMessageObject(String msg) throws JSONException {
        JSONObject object = new JSONObject();
        object.put("user",User.userName);
        object.put("text",msg);
        return object.toString();
    }
}

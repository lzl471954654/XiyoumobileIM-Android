package com.xiyoumobile.xiyoumobileim;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView recyclerView;
    WebSocketConnect webSocketConnect;
    MessageAdapter adapter;
    TextView send;
    EditText editText;
    ArrayList<MessageObject> messageObjects;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        getSupportActionBar().hide();
        initView();
        webSocketConnect = new WebSocketConnect(handler);
        Thread thread = new Thread(webSocketConnect);
        thread.start();
    }

    public void initView(){
        messageObjects = new ArrayList<>();
        adapter = new MessageAdapter(messageObjects,this);
        recyclerView = findViewById(R.id.chat_room_list);
        editText = findViewById(R.id.chat_room_input);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        send = findViewById(R.id.chat_room_send_button);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chat_room_send_button:{
                String message = editText.getText().toString();
                sendMessage(message);
                editText.getText().clear();
                break;
            }
        }
    }

    private void sendMessage(String message){
        webSocketConnect.sendMessage(message);
        MessageObject messageObject = new MessageObject(new MessageData(MessageData.TEXT_TYPE,message,User.userName),true);
        messageObjects.add(messageObject);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageObjects.size()-1);
    }

    private void getMessage(String message) throws JSONException {
        JSONObject jsonObject = new JSONObject(message);
        String username = jsonObject.getString("user");
        String text = jsonObject.getString("text");
        MessageObject messageObject = new MessageObject(new MessageData(MessageData.TEXT_TYPE,text,username),false);
        messageObjects.add(messageObject);
        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messageObjects.size()-1);
        Log.d("room",message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case WebSocketConnect.ON_MESSAGE:{
                    String message = (String)msg.obj;
                    try {
                        getMessage(message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case WebSocketConnect.CONNECT_OPEN:{
                    Toast.makeText(ChatRoomActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
                    break;
                }
                case WebSocketConnect.CONNECT_CLOSE:{
                    Toast.makeText(ChatRoomActivity.this,"连接中断",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };
}

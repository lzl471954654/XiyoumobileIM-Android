package com.xiyoumobile.xiyoumobileim;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.Serializable;

public class MessageData implements Serializable {
    public static final int TEXT_TYPE = 1;
    public static final int IMAGE_TYPE = 2;
    private int type;
    private String data;
    private String name;

    public MessageData(int type, String data, String name) {
        this.type = type;
        this.data = data;
        this.name = name;
    }

    public MessageData(int type, String data) {
        this.type = type;
        this.data = data;
    }

    public Bitmap getImage(){
        byte[] bytes = Base64.decode(data,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

package com.xiyoumobile.xiyoumobileim;

import android.graphics.Bitmap;

public class MessageObject {
    public static final int ME_TYPE = 1;
    public static final int OTHER_PEOPLE_TYPE = 2;
    private MessageData messageData;
    private boolean isMe;

    public MessageObject(MessageData messageData, boolean isMe) {
        this.messageData = messageData;
        this.isMe = isMe;
    }

    public boolean isMe(){
        return isMe;
    }

    public int getDataType(){
        return messageData.getType();
    }

    public Bitmap getImageData(){
        return messageData.getImage();
    }

    public String getTextData(){
        return messageData.getData();
    }

    public String getName(){
        return messageData.getName();
    }
}

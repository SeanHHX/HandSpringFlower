package com.cqupt.handspringflower.message;

public class MessageItem {

    private String msgTime;
    private String msgContent;

    public MessageItem(String msgTime, String msgContent) {
        this.msgTime = msgTime;
        this.msgContent = msgContent;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public String getMsgContent() {
        return msgContent;
    }
}

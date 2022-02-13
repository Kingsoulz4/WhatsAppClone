package com.example.whatappclone.models;

public class Message {
    String uID;
    String content;
    String messageID;
    Long timestamp;

    public Message() {
    }

    public Message(String uID, String content, String messageID, Long timestamp) {
        this.uID = uID;
        this.content = content;
        this.messageID = messageID;
        this.timestamp = timestamp;
    }

    public String getuID() {
        return uID;
    }

    public void setuID(String uID) {
        this.uID = uID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

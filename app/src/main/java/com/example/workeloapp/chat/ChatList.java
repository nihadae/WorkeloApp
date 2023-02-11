package com.example.workeloapp.chat;

public class ChatList {
    private String mobile, message, date, time;

    public ChatList(String mobile, String message, String date, String time) {
        this.mobile = mobile;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getMobile() {
        return mobile;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}

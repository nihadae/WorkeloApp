package com.example.workeloapp.messages;

public class MessagesList {
    private String username;
    private String lastMessage;
    private String chatKey;
    private int unseenMessages;

    public MessagesList(String username, String lastMessage, int unseenMessages, String chatKey) {
        this.username = username;
        this.lastMessage = lastMessage;
        this.unseenMessages = unseenMessages;
        this.chatKey = chatKey;
    }

    public String getUsername() {
        return username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public String getChatKey() {
        return chatKey;
    }
}

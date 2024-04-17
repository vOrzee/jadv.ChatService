package ru.netology.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private final String senderName;
    private final String text;
    private final LocalDateTime timestamp;

    public Message(String senderName, String text) {
        this.senderName = senderName;
        this.text = text;
        this.timestamp = LocalDateTime.now();
    }

    public String getSenderName() {
        return senderName;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s: %s", getFormattedTimestamp(), senderName, text);
    }
}



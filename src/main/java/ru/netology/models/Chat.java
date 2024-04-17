package ru.netology.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Chat {
    private final User user1;
    private final User user2;
    private final List<Message> messages;

    public Chat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.messages = new ArrayList<>();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void sendMessageFrom(User sender, String text) {
        if (sender.equals(user1) && user2.getWriter() != null) {
            user2.getWriter().println(sender.getName() + ": " + text);
        } else if (sender.equals(user2) && user1.getWriter() != null) {
            user1.getWriter().println(sender.getName() + ": " + text);
        }
        addMessage(new Message(sender.getName(), text));
    }

    public User getUser1() {
        return user1;
    }

    public User getUser2() {
        return user2;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<String> getChatHistory() {
        return messages.stream()
                .map(Message::toString)
                .collect(Collectors.toList());
    }

}

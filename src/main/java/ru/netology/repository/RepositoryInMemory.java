package ru.netology.repository;

import ru.netology.models.Chat;
import ru.netology.models.User;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RepositoryInMemory implements Repository {

    private Map<String, User> users = new ConcurrentHashMap<>();
    private Map<String, Chat> chats = new ConcurrentHashMap<>();

    private static RepositoryInMemory instance;

    private RepositoryInMemory() {
    }

    public static synchronized Repository getInstance() {
        if (instance == null) {
            instance = new RepositoryInMemory();
        }
        return instance;
    }

    @Override
    public User getUserByName(String name) {
        return users.get(name);
    }

    @Override
    public User createUser(String name, PrintWriter writer) {
        if (users.containsKey(name)) {
            User existingUser = users.get(name);
            existingUser.setWriter(writer);
            return existingUser;
        }
        User newUser = new User(name, writer);
        users.put(name, newUser);
        return newUser;
    }

    @Override
    public Chat getChatBetweenUsers(User user1, User user2) {
        String key = createChatKey(user1, user2);
        return chats.get(key);
    }

    @Override
    public Chat createChat(User user1, User user2) {
        String key = createChatKey(user1, user2);
        Chat newChat = new Chat(user1, user2);
        chats.put(key, newChat);
        return newChat;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<Chat> getAllChats() {
        return new ArrayList<>(chats.values());
    }

    private String createChatKey(User user1, User user2) {
        List<String> names = Arrays.asList(user1.getName(), user2.getName());
        Collections.sort(names);
        return String.join(":", names);
    }
}
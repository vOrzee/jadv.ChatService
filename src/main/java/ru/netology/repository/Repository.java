package ru.netology.repository;

import ru.netology.models.Chat;
import ru.netology.models.User;

import java.io.PrintWriter;
import java.util.List;

public interface Repository {
    User getUserByName(String name);
    User createUser(String name, PrintWriter writer);
    Chat getChatBetweenUsers(User user1, User user2);
    Chat createChat(User user1, User user2);
    List<User> getAllUsers();
    List<Chat> getAllChats();
}

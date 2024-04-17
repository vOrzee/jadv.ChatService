package ru.netology.server;

import static ru.netology.utils.Logger.logMessage;

import ru.netology.models.Chat;
import ru.netology.models.User;
import ru.netology.repository.Repository;
import ru.netology.repository.RepositoryInMemory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Repository repository = RepositoryInMemory.getInstance();

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        String inputLine;
        User currentUser = null;
        Chat activeChat = null;
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println("/myName - ваш логин");
            out.println("/start - с кем начать чат");
            out.println("/exit - закрыть чат");
            out.println("/close - завершить соединение");
            while (true) {
                inputLine = in.readLine();
                if (inputLine == null) break;
                if (inputLine.startsWith("/myName")) {
                    String name = inputLine.substring(8).trim();
                    currentUser = repository.getUserByName(name);
                    if (currentUser == null) {
                        currentUser = repository.createUser(name, out);
                        logMessage(name + " подключился.");
                        out.println("Привет " + currentUser.getName());
                    } else {
                        currentUser.setWriter(out);
                        logMessage(name + " переподключился.");
                    }
                    continue;
                }
                if (inputLine.startsWith("/start") && currentUser != null) {
                    String targetUserName = inputLine.substring(7).trim();
                    out.println("Вы хотите пообщаться с пользователем " + targetUserName);
                    User targetUser = repository.getUserByName(targetUserName);
                    if (targetUser == null) {
                        targetUser = repository.createUser(targetUserName, null);
                    }
                    activeChat = repository.getChatBetweenUsers(currentUser, targetUser);
                    if (activeChat == null) {
                        activeChat = repository.createChat(currentUser, targetUser);
                    }
                    out.println("Чат с " + targetUserName + " начат.");
                    logMessage(currentUser.getName() + " начал чат с " + targetUserName);

                    if (!activeChat.getMessages().isEmpty()) {
                        out.println("--- Начало истории чата ---");
                        activeChat.getChatHistory();
                        out.println("--- Конец истории чата ---");
                    }
                    continue;
                }
                if (inputLine.startsWith("/exit")) {
                    if (activeChat != null) {
                        activeChat = null;
                        out.println("Вы вышли из чата.");
                        logMessage(currentUser.getName() + " вышел из чата.");
                    }
                    continue;
                }
                if (inputLine.startsWith("/close")) {
                    break;
                }
                if (activeChat != null && currentUser != null) {
                    activeChat.sendMessageFrom(currentUser, inputLine);
                    logMessage("Сообщение от " + currentUser.getName() + " к " + (currentUser == activeChat.getUser1() ? activeChat.getUser2().getName() : activeChat.getUser1().getName()) + ": " + inputLine);
                    out.println("Отправлено");
                } else {
                    out.println("Сообщение не может быть отправлено. Нет активного чата или вы не представились, введите команду /myName ваше_имя.");
                    logMessage("Ошибка при отправке сообщения");
                }

            }
        } catch (IOException e) {
            logMessage("Ошибка при общении с клиентом: " + e.getMessage());
        } finally {
            try {
                in.close();
                out.close();
                clientSocket.close();
                logMessage((currentUser != null ? currentUser.getName() : "Клиент") + " отключился.");
            } catch (IOException e) {
                logMessage("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }
}



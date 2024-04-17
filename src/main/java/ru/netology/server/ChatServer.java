package ru.netology.server;

import java.io.*;
import java.net.*;

import static ru.netology.utils.Logger.logMessage;

public class ChatServer {
    private static final String SETTINGS_FILE = "./src/main/java/ru/netology/server/settings.txt";

    private int port;

    public ChatServer() {
        loadSettings();
    }

    protected ServerSocket createServerSocket(int port) throws IOException {
        return new ServerSocket(port);
    }

    private void loadSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
            port = Integer.parseInt(reader.readLine().trim());
            logMessage("Настройки загружены. Порт: " + port);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла настроек: " + e.getMessage());
            logMessage("Ошибка при чтении файла настроек: " + e.getMessage());
            port = 8080;
        }
    }

    public void start() {
        try (ServerSocket serverSocket = createServerSocket(port)) {
            if (serverSocket != null) {
                System.out.println("Сервер запущен на порту " + port);
                logMessage("Сервер запущен на порту " + port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new ClientHandler(clientSocket).start();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе сервера: " + e.getMessage());
            logMessage("Ошибка при работе сервера: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        new ChatServer().start();
    }

}


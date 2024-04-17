package ru.netology.client;

import java.io.*;
import java.net.Socket;

import static ru.netology.utils.Logger.logMessage;

public class ChatClient {
    private static final String SETTINGS_FILE = "./src/main/java/ru/netology/client/settings.txt";
    private static String serverAddress = "127.0.0.1";
    private static int serverPort;

    private static void loadSettings() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
            serverAddress = reader.readLine().trim();
            serverPort = Integer.parseInt(reader.readLine().trim());
            logMessage("Настройки загружены. Адрес: " + serverAddress + " Порт: " + serverPort);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла настроек: " + e.getMessage());
            logMessage("Ошибка при чтении файла настроек: " + e.getMessage());
            serverAddress = "127.0.0.1";
            serverPort = 8080;
        }
    }

    public static void main(String[] args) {
        try {
            loadSettings();
            Socket socket = new Socket(serverAddress, serverPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

            Thread serverListener = new Thread(() -> {
                String message;
                try {
                    while ((message = reader.readLine()) != null) {
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            serverListener.start();

            String userInput;
            while (true) {
                userInput = consoleReader.readLine();
                if (userInput.equalsIgnoreCase("/close")) {
                    break;
                }
                writer.println(userInput);
            }

            serverListener.join();
            socket.close();
            reader.close();
            writer.close();
            consoleReader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


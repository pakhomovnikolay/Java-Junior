package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    // Socket - абстракция, к которой можно подключиться
    // ip-address + port - socket
    // network - сеть - набор соединенных устройств
    // ip-address - это адрес устройства в какой-то сети
    // 8080 - http
    // 443 - https
    // 35 - smtp
    // 21 - ftp
    // 5432 - стандартный порт postgres
    // клиент подключается к серверу

    /**
     * Порядок взаимодействия:
     * 1. Клиент подключается к серверу
     * 2. Клиент посылает сообщение, в котором указан логин. Если на сервере уже есть подключеный клиент с таким логином, то соедение разрывается
     * 3. Клиент может посылать 3 типа команд:
     * 3.1 list - получить логины других клиентов
     * <p>
     * 3.2 send @login message - отправить личное сообщение с содержимым message другому клиенту с логином login
     * 3.3 send message - отправить сообщение всем с содержимым message
     */

    // 1324.132.12.3:8888
    public static void main(String[] args) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
        try (ServerSocket server = new ServerSocket(8888)) {
            System.out.println("Сервер запущен");

            while (true) {
                System.out.println("Ждем клиентского подключения");
                Socket client = server.accept();
                ClientHandler clientHandler = new ClientHandler(client, clients);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка во время работы сервера: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {

        private final Socket client;
        private final Scanner in;
        private final PrintWriter out;
        private final Map<String, ClientHandler> clients;
        private String clientLogin;

        public ClientHandler(Socket client, Map<String, ClientHandler> clients) throws IOException {
            this.client = client;
            this.clients = clients;

            this.in = new Scanner(client.getInputStream());
            this.out = new PrintWriter(client.getOutputStream(), true);
        }

        @Override
        public void run() {
            System.out.println("Подключен новый клиент");

            try {
                String loginRequest = in.nextLine();
                LoginRequest request = objectMapper.reader().readValue(loginRequest, LoginRequest.class);
                this.clientLogin = request.getLogin();
            } catch (IOException e) {
                System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
                String unsuccessfulResponse = createLoginResponse(false);
                out.println(unsuccessfulResponse);
                doClose();
                return;
            }

            System.out.println("Запрос от клиента: " + clientLogin);
            // Проверка, что логин не занят
            if (clients.containsKey(clientLogin)) {
                String unsuccessfulResponse = createLoginResponse(false);
                out.println(unsuccessfulResponse);
                doClose();
                return;
            }

            clients.put(clientLogin, this);
            String successfulLoginResponse = createLoginResponse(true);
            out.println(successfulLoginResponse);

            while (true) {
                String messageFromClient = in.nextLine();
                final String type;

                try {
                    AbstractRequest request = objectMapper.reader().readValue(messageFromClient, AbstractRequest.class);
                    type = request.getType();
                } catch (IOException e) {
                    System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
                    sendMessage("Не удалось прочитать сообщение: " + e.getMessage());
                    continue;
                }

                switch (type) {
                    case SendMessageRequest.TYPE:
                        try {
                            if (!sendMessageRequestControl(messageFromClient)) {
                                continue;
                            }
                        } catch (IOException e) {
                            sendMessage("Не удалось прочитать сообщение SendMessageRequest: " + e.getMessage());
                            continue;
                        }
                    case BroadcastMessageRequest.TYPE:
                        try {
                            if (!BroadcastMessageRequestControl(messageFromClient)) {
                                continue;
                            }
                        } catch (IOException e) {
                            sendMessage("Не удалось прочитать сообщение BroadcastMessageRequest: " + e.getMessage());
                        }
                        break;
                    case ListRequest.TYPE:
                        try {
                            if (!ListRequestControl()) {
                                continue;
                            }
                        } catch (IOException e) {
                            sendMessage("Не удалось прочитать сообщение ListRequest: " + e.getMessage());
                            continue;
                        }
                        break;
                    case DisconnectRequest.TYPE:
                        if (!DisconnectRequestControl()) {
                            continue;
                        }
                        return;
                    default:
                        System.err.println("Неизвестный тип сообщения: " + type);
                        sendMessage("Неизвестный тип сообщения: " + type);
                        break;
                }
            }
        }

        private void doClose() {
            try {
                in.close();
                out.close();
                client.close();
            } catch (IOException e) {
                System.err.println("Ошибка во время отключения клиента: " + e.getMessage());
            }
        }

        public void sendMessage(String message) {
            // TODO: нужно придумать структуру сообщения
            out.println(message);
        }

        private String createLoginResponse(boolean success) {
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setConnected(success);
            try {
                return objectMapper.writer().writeValueAsString(loginResponse);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Не удалось создать loginResponse: " + e.getMessage());
            }
        }

        /**
         * Метод проверки сообщения от клиента, предназначенного другому клиенту
         *
         * @param messageFromClient сообщение от клиента
         */
        private boolean sendMessageRequestControl(String messageFromClient) throws IOException {
            final SendMessageRequest request;
            try {
                request = objectMapper.reader().readValue(messageFromClient, SendMessageRequest.class);
                ClientHandler clientTo = clients.get(request.getRecipient());
                if (clientTo == null) {
                    sendMessage("Клиент с логином [" + request.getRecipient() + "] не найден");
                    return false;
                } else {
                    clientTo.sendMessage(request.getMessage());
                    return true;
                }
            } catch (IOException e) {
                System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
                throw new IOException(e.getMessage());
            }
        }

        /**
         * Метод проверки сообщения от клиента, для рассылки остальным клиентам
         *
         * @param messageFromClient сообщение от клиента
         */
        private boolean BroadcastMessageRequestControl(String messageFromClient) throws IOException {
            final BroadcastMessageRequest request;
            try {
                request = objectMapper.reader().readValue(messageFromClient, BroadcastMessageRequest.class);
                ClientHandler clientTo = clients.get(request.getSender());
                if (clientTo == null) {
                    sendMessage("Клиент с логином [" + request.getSender() + "] не найден");
                    return false;
                } else {
                    for (String client : clients.keySet()) {
                        if (!client.equals(request.getSender())) {
                            clients.get(client).sendMessage(request.getMessage());
                        }
                    }
                    return true;
                }
            } catch (IOException e) {
                System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
                throw new IOException(e.getMessage());
            }
        }

        private boolean ListRequestControl() throws IOException {
            final ListResponse response = new ListResponse();
            try {
                List<User> users = new ArrayList<>();
                ClientHandler clientTo = clients.get(clientLogin);
                for (String client : clients.keySet()) {
                    if (!client.equals(clientLogin)) {
                        User user = new User();
                        user.setLogin(client);
                        users.add(user);
                    }
                }
                response.setUsers(users);
                String sendMsgRequest = objectMapper.writeValueAsString(response);
                clientTo.sendMessage(sendMsgRequest);
                return true;
            } catch (IOException e) {
                System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
                throw new IOException(e.getMessage());
            }
        }

        private boolean DisconnectRequestControl() {
            clients.remove(clientLogin);
            String sendMsgRequest = "Клиент [" + clientLogin + "] отключился";
            System.out.println(sendMsgRequest);

            for (String client : clients.keySet()) {
                clients.get(client).sendMessage(sendMsgRequest);
            }
            doClose();
            return true;
        }
    }
}


//                    final BroadcastMessageRequest request;
//
//                    try {
//                        request = objectMapper.reader().readValue(msgFromClient, BroadcastMessageRequest.class);
//                    } catch (IOException e) {
//                        System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
//                        sendMessage("Не удалось прочитать сообщение BroadcastMessageRequest: " + e.getMessage());
//                        continue;
//                    }
//
//                    ClientHandler clientTo = clients.get(request.);
//                    if (clientTo == null) {
//                        sendMessage("Клиент с логином [" + request.getRecipient() + "] не найден");
//                        continue;
//                    }
//                    clientTo.sendMessage(request.getMessage());
//
//
//
//                    for (String client : clients.keySet()) {
//                        if ()
//                    }
package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.util.*;

public class ChatClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String clientLogin = "User_" + UUID.randomUUID().toString();
        Scanner console = new Scanner(System.in);

        try (Socket server = new Socket("localhost", 8888)) {
            System.out.println("Успешно подключились к серверу");

            try (PrintWriter out = new PrintWriter(server.getOutputStream(), true)) {
                Scanner in = new Scanner(server.getInputStream());

                String loginRequest = createLoginRequest(clientLogin);
                out.println(loginRequest);

                String loginResponseString = in.nextLine();
                if (!checkLoginResponse(loginResponseString)) {
                    // TODO: Можно обогатить причиной, чтобы клиент получал эту причину
                    // (логин уже занят, ошибка аутентификации\авторизации, ...)
                    System.out.println("Не удалось подключиться к серверу");
                    return;
                }

                // client <----------------> server
                // client getUsers ->        server
                // client <- (getUsers|sendMessage from client) server    <--------sendMessage client2
                //

                // Отдельный поток на чтение сообщений
//        ServerListener serverListener = new ServerListener(in);
//        new Thread(serverListener).start();
                new Thread(() -> {
                    while (true) {
                        // TODO: парсим сообщение в AbstractRequest
                        //  по полю type понимаем, что это за request, и обрабатываем его нужным образом
                        String msgFromServer = in.nextLine();
                        System.out.println("Сообщение от сервера: " + msgFromServer);
                    }
                }).start();


                while (true) {
                    System.out.println("Что хочу сделать?");
                    System.out.println("1. Послать сообщение другу");
                    System.out.println("2. Послать сообщение всем");
                    System.out.println("3. Получить список логинов");
                    System.out.println("4. Чтобы завершить работу");
                    System.out.print("Введите желаемое дейтсвие, указав цифру: ");
                    System.out.println();
                    String type = console.nextLine();

                    if (type.equals("1")) {
                        SendMessageRequest request = new SendMessageRequest();
                        System.out.print("Введите сообщение: ");
                        request.setMessage(console.nextLine());

                        System.out.print("Укажите имя друга: ");
                        request.setRecipient(console.nextLine());

                        String sendMsgRequest = objectMapper.writeValueAsString(request);
                        out.println(sendMsgRequest);

                    } else if (type.equals("2")) {
                        BroadcastMessageRequest request = new BroadcastMessageRequest();
                        System.out.print("Введите сообщение: ");
                        request.setMessage(console.nextLine());

                        request.setSender(clientLogin);
                        String sendMsgRequest = objectMapper.writeValueAsString(request);
                        out.println(sendMsgRequest);

                    } else if (type.equals("3")) {
                        ListRequest request = new ListRequest();
                        String sendMsgRequest = objectMapper.writeValueAsString(request);
                        out.println(sendMsgRequest);

                    } else if (type.equals("4")) {
                        DisconnectRequest request = new DisconnectRequest();
                        request.setRecipient(clientLogin);
                        String sendMsgRequest = objectMapper.writeValueAsString(request);
                        out.println(sendMsgRequest);
                        break;

                    }
                }
                in.close();
                out.close();
                server.close();
            }
        } catch (IOException e) {
            System.err.println("Ошибка во время подключения к серверу: " + e.getMessage());
        }
        System.out.println("Отключились от сервера");

    }



    private static String createLoginRequest(String login) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(login);

        try {
            return objectMapper.writeValueAsString(loginRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка JSON: " + e.getMessage());
        }
    }

    private static boolean checkLoginResponse(String loginResponse) {
        try {
            LoginResponse resp = objectMapper.reader().readValue(loginResponse, LoginResponse.class);
            return resp.isConnected();
        } catch (IOException e) {
            System.err.println("Ошибка чтения JSON: " + e.getMessage());
            return false;
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(Duration.ofMinutes(5));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

//  private static class ServerListener implements Runnable {
//    private final Scanner in;
//    private final Map<String, List<Consumer<String>>> subscribers = new ConcurrentHashMap<>();
//
//    public ServerListener(Scanner in) {
//      this.in = in;
//    }
//
//    public void subscribe(String type, Consumer<String> consumer) {
//      List<Consumer<String>> consumers = subscribers.getOrDefault(type, new ArrayList<>());
//      consumers.add(consumer);
//      subscribers.put(type, consumers);
//    }
//
//    @Override
//    public void run() {
//      while (true) {
//        String msgFromServer = in.nextLine();
//
//        // TODO: парсим сообщение в AbstractRequest
//        //  по полю type понимаем, что это за request, и обрабатываем его нужным образом
//        String type = null;
//
//        subscribers.getOrDefault(type, List.of()).forEach(it -> {
//          it.accept(msgFromServer);
//        });
//        subscribers.remove(type);
//
//        System.out.println("Сообщение от сервера: " + msgFromServer);
//      }
//    }
//  }

}

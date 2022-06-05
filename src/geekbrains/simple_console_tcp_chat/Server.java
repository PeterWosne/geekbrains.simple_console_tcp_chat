package geekbrains.simple_console_tcp_chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 8189;
    private static ArrayList<Handler> handlers;

    public Server() {
        handlers = new ArrayList<>();
    }

    public static void main(String[] args) {
        new Server().start();
    }

    private void start()  {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started");
            while(true) {
                System.out.println("Waiting for connection...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                Handler handler = new Handler(socket, this);
                handlers.add(handler);
                handler.process();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mail(String message, int from) {
        for (int i = 0; i < handlers.size(); i++) {
            if((i + 1) != from) {
                handlers.get(i).send(message);
            }
        }
    }
}

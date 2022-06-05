package geekbrains.simple_console_tcp_chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Handler {
    private static int clientCounter = 0;
    public int clientNumber;

    private Socket socket;
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread handleThread;
    private String income;

    public Handler(Socket socket, Server server) {
        ++clientCounter;
        this.clientNumber = clientCounter;
        this.socket = socket;
        this.server = server;
        try {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process() {
        handleThread = new Thread(() -> {
            while(true) {
                try {
                    income = in.readUTF();
                    System.out.printf("Received message[%s] from client %d\n", income, clientNumber);
                    server.mail("[from " + clientNumber + "] " + income, clientNumber);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        handleThread.start();
    }

    public void send(String message) {
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package geekbrains.simple_console_tcp_chat;

import java.io.*;
import java.net.Socket;

public class ConsoleTCPClient {
    private static final int PORT = 8189;
    private static final String HOST = "127.0.0.1";

    private DataInputStream in;
    private DataOutputStream out;
    private Thread clientThread;

    public static void main(String[] args) {
        new ConsoleTCPClient().start();
    }

    private void start() {
        try(Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Client connected");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            startConsoleInput();

            while(!socket.isClosed()) {
                String income = in.readUTF();
                System.out.println(income);
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                shutdown();
                System.out.println("Client stopped");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startConsoleInput() {
        clientThread = new Thread(() -> {
            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("ENTER YOUR MESSAGE >>>");
                while(!Thread.currentThread().isInterrupted()) {
                    if(br.ready()) {
                        String outcome = br.readLine();
                        out.writeUTF(outcome);
                    }
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }

    private void shutdown() throws IOException {
        if(clientThread != null && clientThread.isAlive()) {
            clientThread.interrupt();
        }
    }
}

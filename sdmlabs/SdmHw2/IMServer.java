import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class IMServer {
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>()); // synchronizedList so that multiple threads can access it
    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try{
            serverSocket = new ServerSocket(2999);
            System.out.println("Server started");
            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                new Thread(handler).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(serverSocket != null){
                    serverSocket.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    static void broadcast(String msg) {
        synchronized (clients) {
            for(ClientHandler client: clients){
                client.send(msg);
            }
        }
    }


    static class ClientHandler implements Runnable{
        private Socket socket;
        private DataInputStream in;
        private DataOutputStream out;
        private String clientID;

        ClientHandler(Socket socket) {
            this.socket = socket;
            try{
                in= new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

                clientID = in.readUTF();
                System.out.println("Client ID: " + clientID);
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        void send(String msg) {
            try {
                out.writeUTF(msg);
                out.flush();
            } catch (IOException e) {
                System.out.println("Error sending message to " + clientID);
            }
        }

        @Override
        public void run() {
            try{
                while(true){
                    String message = in.readUTF();
                    System.out.println("Message received: " + message);
                    broadcast(message);
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            finally {
                try {
                    socket.close();
                } catch (IOException ignored) {}
                clients.remove(this);
            }
        }
    }
}

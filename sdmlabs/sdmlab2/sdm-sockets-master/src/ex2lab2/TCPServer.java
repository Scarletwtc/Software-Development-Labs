package ex2lab2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(2999);
            System.out.println("Server started on port 2999");
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);
                TCPServerMulti serverWorker = new TCPServerMulti(socket);
                serverWorker.start();
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close(); // we call close only if serverSocket exists, ( if it dnt exist it would be null)
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

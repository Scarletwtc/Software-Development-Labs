package ex1lab2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TCPMyServer {
    public static void main(String[] args){
        DataInputStream in = null;
        DataOutputStream out = null;
        Socket socket = null;
        ServerSocket serverSocket = null;
        try{
            //also listen on port 2999
            serverSocket = new ServerSocket(2999);
            System.out.println("Server is listening on port 2999 .....");
            socket = serverSocket.accept();
            System.out.println("Connected, socket is :"+ socket);
            in = new DataInputStream((new BufferedInputStream(socket.getInputStream())));
            out = new DataOutputStream((new BufferedOutputStream(socket.getOutputStream())));
            String incoming;
            while(true){
                incoming = in.readUTF();
                System.out.println(incoming + " was received");
                out.writeUTF(incoming);
                out.flush();

                if (incoming.equals("END")){
                    break;
                }
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                out.close();
                in.close();
                socket.close();
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

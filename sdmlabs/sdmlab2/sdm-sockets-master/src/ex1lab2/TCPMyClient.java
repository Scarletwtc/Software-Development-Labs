package ex1lab2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPMyClient {
    public static void main(String[] args) {
        DataInputStream in =null;
        DataOutputStream out = null;
        Socket socket = null;
        try{
            socket = new Socket("127.0.0.1", 2999);
            System.out.println("Connected to server, socket is :"+ socket);
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            String socketInfo = "socket =" + socket;
            out.writeUTF(socketInfo);
            out.flush();
            System.out.println(socketInfo + " was sent");

            System.out.println(in.readUTF() + " was received");

            for(int i=0; i<10; i++){
                String line = " I am "+ i;
                out.writeUTF(line);
                out.flush();
                System.out.println(line + " was sent");
                String reply = in.readUTF();
                System.out.println(reply + " was received");
            }

            out.writeUTF("END");
            out.flush();
            System.out.println("END was sent");
            String reply = in.readUTF();
            System.out.println(reply + " was received");

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (Exception e ){
            e.printStackTrace();
        }finally {
            try {
                out.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
}


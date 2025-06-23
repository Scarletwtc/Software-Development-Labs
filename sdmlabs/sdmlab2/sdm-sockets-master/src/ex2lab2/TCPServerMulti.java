package ex2lab2;

import ex1.TCPServerSimple;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.CollationKey;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TCPServerMulti extends Thread {
    private Socket socket;
    TCPServerMulti(Socket socket){
        this.socket = socket;
    }

    public void run(){
        DataInputStream in = null;
        DataOutputStream out = null;
        try{
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            String incoming;

            while(true){
                incoming = in.readUTF();
                System.out.println("Received from" + socket + ": " +incoming);
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

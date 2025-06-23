package ex2lab2;


import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPSingleClient extends Thread{
    private int clientID;

    public TCPSingleClient(int clientID){
        this.clientID = clientID;

    }

    @Override
    public void run(){
        try(
                Socket socket = new Socket("127.0.0.1", 2999);
                DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                ){
            System.out.println("Connected to server, socket is :"+ socket);
            String socketInfo = "socket =" + socket;
            out.writeUTF(socketInfo);
            out.flush();
            System.out.println(socketInfo + " was sent by "+ clientID  );
            System.out.println(in.readUTF() + " was received by "+ clientID  );

            for(int i=0; i<10; i++){
                String line = " I am "+ i;
                out.writeUTF(line);
                out.flush();
                System.out.println(line + " was sent by "+ clientID  );
                String reply = in.readUTF();
                System.out.println(reply + " was received by "+ clientID  );
            }
            out.writeUTF("END");
            out.flush();
            System.out.println("END was sent by "+ clientID  );
            String reply = in.readUTF();
            System.out.println(reply + " was received by "+ clientID  );
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}

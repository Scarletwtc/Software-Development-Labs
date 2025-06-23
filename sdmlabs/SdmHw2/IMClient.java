import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IMClient {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java IMClient <clientID> <serverAddress> <port>");
            return;
        }

        String clientID = args[0];
        String serverAddress = args[1];
        int port = Integer.parseInt(args[2]);

        Socket socket = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        Scanner scan = null;
        try {
            socket = new Socket(serverAddress, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(clientID);
            out.flush();

            final DataInputStream finalIn = in;
            final String finalClientID = clientID;

            ExecutorService es = Executors.newSingleThreadExecutor();
            es.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            String incoming = finalIn.readUTF();
                            String[] parts = incoming.split(":", 3);
                            if (parts.length < 3)
                                continue;
                            String sender = parts[0];
                            String receiver = parts[1];
                            String messageText = parts[2];
                            if (receiver.equals(finalClientID)) {
                                System.out.println("Message from " + sender + ": " + messageText);
                            }
                        }
                    } catch (IOException e) {
                        System.out.println("Disconnected from server.");
                    }
                }
            });

            scan = new Scanner(System.in);
            System.out.println("Enter messages in the format: <receiverID> <message text>");
            while (true) {
                String input = scan.nextLine();
                if (input.equalsIgnoreCase("END"))
                    break;
                String[] parts = input.split(" ", 2);
                if (parts.length < 2) {
                    System.out.println("Invalid input. Format: <receiverID> <message text>");
                    continue;
                }
                String receiverID = parts[0];
                String messageText = parts[1];
                String fullMessage = clientID + ":" + receiverID + ":" + messageText;
                out.writeUTF(fullMessage);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (scan != null)
                    scan.close();
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

//javac --release 17 -d out/production/SdmHw2 IMClient.java
// java -cp out/production/SdmHw2 IMClient user 127.0.0.1 2999
// write this in terminal to run IMClient
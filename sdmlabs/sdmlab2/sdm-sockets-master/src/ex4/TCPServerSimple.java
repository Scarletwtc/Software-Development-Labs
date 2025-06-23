package ex4;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class TCPServerSimple {
	public static void main(String[] args) {
		DataInputStream in = null;
		DataOutputStream out = null;
		Socket socket = null;
		ServerSocket serverSocket = null;
		try {
			// listen on port 2999
			serverSocket = new ServerSocket(2999);
			socket = serverSocket.accept();
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			//create a thread that continuously sends PING to the client
			ExecutorService es= Executors.newSingleThreadExecutor();
			DataOutputStream finalOut = out;
			es.submit(()->{
				while(true){
					finalOut.writeUTF("PING");
					finalOut.flush();
					sleep(5000);
				}
			});
			es.shutdown();
			while(true){
				String incoming = in.readUTF();
				System.out.println(incoming + " was received");
				out.writeUTF(incoming);
				out.flush();
			}
		}
		catch (EOFException e) {
			System.out.println("Connection closed");
		}

		catch (IOException e) {
			e.printStackTrace();
		} finally {
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
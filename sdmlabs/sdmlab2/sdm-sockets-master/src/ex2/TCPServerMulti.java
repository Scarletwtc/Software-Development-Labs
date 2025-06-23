package ex2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerMulti {

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(2999);
			while (true) {
				Socket socket = serverSocket.accept();
				ServerWorker sw = new ServerWorker(socket);
				sw.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

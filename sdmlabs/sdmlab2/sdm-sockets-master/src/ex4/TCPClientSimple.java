package ex4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPClientSimple {
	public static void main(String[] args) {
		DataInputStream in = null;
		DataOutputStream out = null;
		Socket socket = null;
		Scanner scan=null;
		try {
			socket = new Socket("127.0.0.1", 2999);
			out = new DataOutputStream(socket.getOutputStream());
			out.flush();
			in = new DataInputStream(socket.getInputStream());
			ExecutorService es= Executors.newSingleThreadExecutor();
			DataInputStream finalIn = in;
			es.submit(()->{
				while(true) {
					String incoming = finalIn.readUTF();
					System.out.println("Client received " + incoming);
				}
			});
			es.shutdown();
			scan = new Scanner(System.in);
			while(true) {
				String s = scan.nextLine();
				if(s.equals("END"))
					break;
				out.writeUTF(s);
				out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		 finally {
			try {
				scan.close();
				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
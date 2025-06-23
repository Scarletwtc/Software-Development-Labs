package ex2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ServerWorker extends Thread {
    private Socket socket;
    ServerWorker(Socket socket) {
        this.socket = socket;
    }
    public void run() {
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
        	in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			String incoming;
			out.writeUTF("What is your birthday? yyyy-MM-dd format");
			out.flush();
			incoming = in.readUTF();
			System.out.println(incoming + " was received");
			DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate inputDate=null;
			boolean fail=false;
			try {
				inputDate = LocalDate.parse(incoming, dateformat);
			} catch (DateTimeParseException ex) {
				out.writeUTF("Wrong format!!!");
				out.flush();
				fail=true;
				
			}
			if(!fail) {
			LocalDate today = LocalDate.now();
			if (today.getMonth().equals(inputDate.getMonth()) && today.getDayOfMonth() == inputDate.getDayOfMonth())
				out.writeUTF("Happy birthday!");
			else
				out.writeUTF("Not today!");
			out.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
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
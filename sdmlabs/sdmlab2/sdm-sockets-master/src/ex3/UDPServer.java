package ex3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UDPServer {
	static DatagramSocket socket;

	public static void main(String[] args) {
		try {
			socket = new DatagramSocket(2999);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		DatagramPacket receivedPacket;
		DatagramPacket sentPacket;
		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
		ByteArrayInputStream byteArrayInput = null;
		DataInputStream in = null;
		DataOutputStream out = new DataOutputStream(byteArrayOutput);
		receivedPacket = new DatagramPacket(new byte[255], 255);
		try {
			socket.receive(receivedPacket);
			out.writeUTF("What is your birthday? yyyy-MM-dd format");
			out.flush();
			byte[] content = byteArrayOutput.toByteArray();
			sentPacket = new DatagramPacket(content, content.length, receivedPacket.getAddress(),
					receivedPacket.getPort());
			socket.send(sentPacket);
			socket.receive(receivedPacket);
			byteArrayInput = new ByteArrayInputStream(receivedPacket.getData());
			in = new DataInputStream(byteArrayInput);
			byteArrayOutput.reset();
			String incoming = in.readUTF();
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
			content = byteArrayOutput.toByteArray();
			sentPacket = new DatagramPacket(content, content.length, receivedPacket.getAddress(),
					receivedPacket.getPort());
			socket.send(sentPacket);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
	}
}
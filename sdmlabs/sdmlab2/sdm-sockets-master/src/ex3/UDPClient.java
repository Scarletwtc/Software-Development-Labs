package ex3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPClient {
	static DatagramSocket socket;

	public static void main(String[] args) {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		DatagramPacket receivedPacket;
		DatagramPacket sentPacket;
		ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
		ByteArrayInputStream byteArrayInput = null;
		DataOutputStream out = new DataOutputStream(byteArrayOutput);
		DataInputStream in = null;
		receivedPacket = new DatagramPacket(new byte[1], 1);
		try {
			byte[] content = new byte[1];
			sentPacket = new DatagramPacket(content, 1, InetAddress.getByName("127.0.0.1"), 2999);
			socket.send(sentPacket);
			receivedPacket = new DatagramPacket(new byte[255], 255);
			socket.receive(receivedPacket);
			byteArrayInput = new ByteArrayInputStream(receivedPacket.getData());
			in = new DataInputStream(byteArrayInput);
			System.out.print(in.readUTF());
			Scanner scan = new Scanner(System.in);
			String s = scan.nextLine();
			scan.close();
			out.writeUTF(s);
			out.flush();
			content = byteArrayOutput.toByteArray();
			sentPacket = new DatagramPacket(content, content.length, InetAddress.getByName("127.0.0.1"), 2999);
			socket.send(sentPacket);
			socket.receive(receivedPacket);
			byteArrayInput = new ByteArrayInputStream(receivedPacket.getData());
			in = new DataInputStream(byteArrayInput);
			System.out.print(in.readUTF());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			socket.close();
		}
	}
}

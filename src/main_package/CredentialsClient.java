package main_package;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class CredentialsClient {

	private final int MAX_PACKET_SIZE = 1024;
	private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE];
	InetAddress serverAddress = null;
	private int serverPort = 8192;
	private Thread listenThread;
	public volatile boolean listening = false;
	DatagramSocket socket;

	public CredentialsClient() {
		try {
			serverAddress = InetAddress.getByName("192.168.0.109");
			
			try {
				socket = new DatagramSocket();
			} catch (SocketException e) {
				e.printStackTrace();
			} // binds the user to available port and address

			listening = true;
			listenThread = new Thread(() -> {
				listen();
			});
			listenThread.start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public volatile DatagramPacket lastReceivedPacket = null;
	public volatile boolean lastReceivedPacketIsProcessed = false;
	
	public DatagramPacket getLastReceivedPacket() {

		if (lastReceivedPacketIsProcessed == true) {
			return null;
		} else {
			lastReceivedPacketIsProcessed = true;
			return this.lastReceivedPacket;
		}
	}
	
	private void listen() {

		DatagramPacket packet;

		while (listening) {

			packet = new DatagramPacket(receivedDataBuffer, MAX_PACKET_SIZE);

			try {
				socket.receive(packet);
				this.lastReceivedPacket = packet;
				this.lastReceivedPacketIsProcessed = false;
				
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	
	public void sendToServer(byte[] data) {

		assert (socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);

		try {
			socket.send(packet);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}

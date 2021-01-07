package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import UI.ConnectionToLobbyAnimation;

public class Client {

	public enum Error {
		NONE, INVALID_HOST, SOCKET_EXCEPTION
	}

	private int playerID;

	private String serverIpAddress;
	private int serverPort;
	private InetAddress serverAddress;

	private int lobbyPort;
	private InetAddress lobbyAddress;

	private Thread listenThread;
	public volatile boolean listening = false;
	private final int MAX_PACKET_SIZE = 1024;
	private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE];

	private Error errorCode = Error.NONE;

	private DatagramSocket socket;

	public Client(String serverIpAddress, int serverPort, int playerID) {

		this.serverIpAddress = serverIpAddress;
		this.serverPort = serverPort;
		this.playerID = playerID;
	}

	public void setLobby() {

		this.lobbyPort = this.lastReceivedPacket.getPort();
		this.lobbyAddress = this.lastReceivedPacket.getAddress();

		System.out.println(this.lobbyPort);
		System.out.println(this.lobbyAddress);
		System.out.println("player id = " + this.playerID);
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public boolean connect() {

		try {
			/*
			 * the destination (server) is the same as the origin (client), since I
			 * initially develop the program on the same pc localhost
			 */
			// serverAddress = InetAddress.getByName(serverIpAddress);
//			System.out.println("Server address is ------------------------------>  " + serverAddress.toString());

			serverAddress = InetAddress.getByName("192.168.0.109");

		} catch (UnknownHostException e) {
			e.printStackTrace();
			errorCode = Error.INVALID_HOST;
			return false;
		}

		try {
			socket = new DatagramSocket(); // binds the user to available port and address

		} catch (SocketException e) {

			e.printStackTrace();
			errorCode = Error.SOCKET_EXCEPTION;
			return false;
		}

		listening = true;
		listenThread = new Thread(() -> {
			listen();
		});
		listenThread.start();

		sendConnectionToLobbyPacket();
		// wait for server to reply
		// TODO - launch an animation FINDING AVAILABLE LOBBY and restricts the user
		// from interacting with the app, as long as
		// the server receives the request and creates/finds a new lobby
		// the address and the port of the lobby will be stored in
		// private int lobbyPort;
		// private InetAddress lobbyAddress;
		// JOptionPane.showMessageDialog(null, "Waiting for the server to respond...");

		// animation.setAnimationVisible(true);

		return true;
	}

	private void sendConnectionToLobbyPacket() {

		// TODO - send a request to the server; the request will be intended to signal
		// the server that the client requires to get
		// an address and a port of an available lobby, so it could start the connection
		// with the lobby
		// the server will send the required data and from then on, the client will be
		// able to send data to the lobby

		byte[] data = new StringBuilder().append("6 ").append(this.playerID).append(" STOP").toString().getBytes();
		// 6 -> ARCADE

		sendToServer(data);
	}

	private void sendToServer(byte[] data) {

		assert (socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);

		try {
			socket.send(packet);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void sendToLobby(byte[] data) {
		assert (socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, lobbyAddress, lobbyPort);

		try {
			socket.send(packet);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public Error getErrorCode() {

		return this.errorCode;
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

	private void dumpPacket(DatagramPacket packet) {

		System.out.println("Dumping packet");
		System.out.println(packet.getAddress());
		System.out.println(packet.getPort());
		System.out.println(new String(packet.getData()) + "\n");
	}
}

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
	private volatile boolean listening = false;
	private final int MAX_PACKET_SIZE = 1024;
	private byte[] receivedDataBuffer = new byte[MAX_PACKET_SIZE];

	private Error errorCode = Error.NONE;

	private DatagramSocket socket;

	ConnectionToLobbyAnimation animation;

	/*
	 * @param host ex. format: 192.168.1.1.5000
	 */
//	public Client(String host) {
//
//		String[] parts = host.split(":");
//
//		if (parts.length != 2) {
//			errorCode = Error.INVALID_HOST;
//			return;
//		}
//
//		ipAddress = parts[0];
//
//		try {
//			serverPort = Integer.parseInt(parts[1]);
//		} catch (NumberFormatException e) {
//			errorCode = Error.INVALID_HOST;
//			return;
//		}
//	}

	/*
	 * @param host ex. format: 192.168.1.1
	 * 
	 * @param port ex. format: 5000
	 */

	public Client(String serverIpAddress, int serverPort, int playerID) {

		this.serverIpAddress = serverIpAddress;
		this.serverPort = serverPort;
		this.playerID = playerID;
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
			serverAddress = InetAddress.getByName(serverIpAddress);

		} catch (UnknownHostException e) {
			e.printStackTrace();
			errorCode = Error.INVALID_HOST;
			return false;
		}

		try {
			socket = new DatagramSocket(); // binds it to available port and address
			// System.out.println(socket.getPort());
		} catch (SocketException e) {
			// TODO Auto-generated catch block
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
		animation = new ConnectionToLobbyAnimation();
		animation.setAnimationVisible(true);
		return true;
	}

	private void sendConnectionToLobbyPacket() {

		// TODO - send a request to the server; the request will be intended to signal
		// the server that the client requires to get
		// an address and a port of an available lobby, so it could start the connection
		// with the lobby
		// the server will send the required data and from then on, the client will be
		// able to send data to the lobby

		byte[] data = new StringBuilder().append("3 1 STOP").toString().getBytes();
		// byte[] data = this.playerID.toString().getBytes();

		send(data);
	}

	private void send(byte[] data) {

		assert (socket.isConnected());
		DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);

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
				// closeConnectionToLobbyAnimationUponConnection(new String(packet.getData()));
				dumpPacket(packet);
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	private void dumpPacket(DatagramPacket packet) {

		System.out.println(packet.getAddress());
		System.out.println(packet.getPort());
		System.out.println(new String(packet.getData()) + "\n");
	}

	private void closeConnectionToLobbyAnimationUponConnection(String data) {

		if (data.startsWith("CONNECTED TO LOBBY")) {
			animation.setAnimationVisible(false);
		}
	}
}

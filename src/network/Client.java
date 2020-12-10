package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {

	public enum Error {
		NONE, INVALID_HOST, SOCKET_EXCEPTION
	}

	private Integer playerID;
	private String ipAddress;
	private int serverPort;
	private Error errorCode = Error.NONE;

	private InetAddress serverAddress;

	private DatagramSocket socket;

	/*
	 * @param host ex. format: 192.168.1.1.5000
	 */
	public Client(String host) {

		String[] parts = host.split(":");

		if (parts.length != 2) {
			errorCode = Error.INVALID_HOST;
			return;
		}

		ipAddress = parts[0];

		try {
			serverPort = Integer.parseInt(parts[1]);
		} catch (NumberFormatException e) {
			errorCode = Error.INVALID_HOST;
			return;
		}
	}

	/*
	 * @param host ex. format: 192.168.1.1
	 * 
	 * @param port ex. format: 5000
	 */

	public Client(String host, int serverPort, int playerID) {

		this.ipAddress = host;
		this.serverPort = serverPort;
		this.playerID = new Integer(playerID);
	}

	public boolean connect() {

		try {
			/*
			 *  the destination (server) is the same as the origin (client), 
			 *  since I initially develop the program on the same pc
			 *  localhost
			 */
			serverAddress = InetAddress.getByName(ipAddress); 
			
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

		sendConnectionPacket();
		// wait for server to reply
		return true;
	}

	private void sendConnectionPacket() {

		byte[] data = new StringBuilder().append(this.playerID).toString().getBytes();

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
}

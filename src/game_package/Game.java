package game_package;

import java.net.InetAddress;

import main_package.Player;
import network.Client;

public abstract class Game {

	private GameType gameType;
	private MapType map;
	public boolean gameStatus;
	private Client client;
	public String userName;
	public DataProcessor dataProcessor;
	public GameInfo gameInfo;
	private Player userPlayer;

	public Game(Player userPlayer, String serverAddress, int serverPort) {

		this.userPlayer = userPlayer;
		this.client = new Client(serverAddress, serverPort, this.userPlayer.getPlayerID());
		this.dataProcessor = new DataProcessor(userName); // we MUST have this, since this is going to convert our byte
															// [] data packets to the server
	}

	public void start() {
		server.startGame();
	}

	public void end() {
		dataProcessor.displayConclusion();
		server.endGame();
	}

}

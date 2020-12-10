package game_package;

import network.DataProcessor;
import network.Server;

abstract class Game {

	private GameType gameType;
	private MapType map;
	public boolean gameStatus;
	private Server server;
	public String userName;
	public DataProcessor dataProcessor;
	public GameInfo gameInfo;

	public Game() {

		this.server = new Server();
		this.dataProcessor = new DataProcessor(userName);
	}

	public void start() {
		server.startGame();
	}

	public void end() {
		dataProcessor.displayConclusion();
		server.endGame();
	}

}

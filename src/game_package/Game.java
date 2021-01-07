package game_package;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import main_package.Menu;
import network.Client;
import network.DataProcessor;
import network.InGameIntention;
import network.PacketIntention;

public abstract class Game extends JPanel implements KeyListener {

	// this should be deleted, implemented correctly in other way
	private boolean iAmDead = false;

	private MapType map;
	private Client client;
	public String userName;
	public DataProcessor dataProcessor;
	public GameInfo gameInfo;

	private final String serverAddress = "192.168.0.109";
	private final int serverPort = 8192;

	private volatile Map<Integer, InGamePlayer> players = new HashMap<Integer, InGamePlayer>();
	private volatile ArrayList<Integer> playersID = new ArrayList<Integer>();
	// the client player will always be the first InGamePlayer [0]
	private int playerID = -1;

	public Game(int playerID, String userName) {
		System.out.println(userName);
		this.userName = userName;
		this.playerID = playerID;
		this.players.put(playerID, new InGamePlayer(playerID, userName));

		this.client = new Client(serverAddress, serverPort, playerID);
		this.client.connect();
		// here I should wait until the connection has been made

		this.dataProcessor = new DataProcessor(client.getPlayerID(), userName);
	}

	public void startGame() { // this method will be called from outside; when the user presses the play
								// button
		gameThread.start(); // start the listening thread
	}

	public void endGame() {
		gameOn = false;
		this.client.listening = false;
	}

	private boolean gameOn = false;
	Thread gameThread = new Thread(() -> {

		interceptClientPackages();
	});

	private void interceptClientPackages() {

		while (this.client.listening) { // while the client listens (receives packages)

			try {
				this.dataProcessor.initNewPacket(this.client.getLastReceivedPacket().getData());

			} catch (NullPointerException npe) {
				continue;
			}

			try {
				if (this.dataProcessor.isConnectedToLobbyPacket() && gameOn == false) {

					this.players.get(this.playerID).team = this.dataProcessor.getTeam();
					this.client.setLobby(); // this is very important, since it establishes connection to the lobby
					this.gameOn = true;
					System.out.println("Connected to lobby - starting the game");
					initialize();
					// animation.dispose()
					continue;
				}
			} catch (NullPointerException npe) {
				// we do not do anything
				System.out.println("npe");
			}

			if (this.dataProcessor.isAddNewPlayerToLobby()) {

				System.out.println("adding a new player");
				int id = this.dataProcessor.getPlayerID();
				this.players.put(id, new InGamePlayer(id, "Player"));
				continue;
			}

			if (this.dataProcessor.isGameInfo()) {

				int id = this.dataProcessor.getPlayerID();

				// first, we inspect if the players exists in the lobby
				// if it does not, we won't bother doing additional processing

				if (!this.players.containsKey(id)) {
					// continue;

					// if the player is not in the lobby, add it
					this.players.put(id, new InGamePlayer(id, "Player"));
					System.out.println("added player with id ---------------------------------------> " + id);
					playersID.add(id);
				}

				switch (this.dataProcessor.getInGameIntention()) {

				case MOVE:
					//////// preluat angle si viteza
					int[] movementParameters = this.dataProcessor.getMovementParameters();

					int movementAngle = movementParameters[0];
					int nextCoordX = movementParameters[1];
					int nextCoordY = movementParameters[2];
					boolean team = movementParameters[3] == 1 ? true : false;

					this.players.get(id).setMoveParameters(movementAngle, nextCoordX, nextCoordY);
					this.players.get(id).team = team;
					// this.players.get(id).move();
					break;

				case STAY:
					// we do not bother to this.players.get(id).move(); it
					break;

				case DASH:
					///////// preluat angle si viteza care va fi mare
					int[] dashingParameters = this.dataProcessor.getMovementParameters();

					int dashingAngle = dashingParameters[0];
					int dashingSpeed = dashingParameters[1];

					this.players.get(id).setDashParameters(dashingAngle, dashingSpeed);
					this.players.get(id).dash();
					break;

				case ATTACK_WITH_SWORD:
					// int swordAttackAngle = this.dataProcessor.getAttackWithSwordParameters(); //
					// we won't use this for
					// now

					ArrayList<Integer> idsOfDeadPlayers = this.players.get(id).swingSword(this.players);

					for (int idOfDeadPlayer : idsOfDeadPlayers) {
						if (idOfDeadPlayer == playerID) {
							iAmDead = true;
						}
						this.players.get(idOfDeadPlayer).die();
					} // those who are touched will die

					break;

				case SHOOT:
					///////// UNIMPLEMENTED YET
					break;

				case KILL: // I won't use this for now; maybe never
					////////// PRELUAT ID-UL JUCATORULUI OMORAT

					int idOfKilledPlayer = this.dataProcessor.getKillingParameters();

					// we should check if that player exists

					if (!this.players.containsValue(idOfKilledPlayer)) {
						continue;
					}

					this.players.get(id).die();
					break;

				default:
					break;
				}
			}
			System.gc();
		}
	}

	// this next part will be intended to serve the UI
	private JFrame frame = new JFrame();
	UIHelper helper = new UIHelper();

	private Timer gameTimer = new Timer(10, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			if (players.get(playerID).isDead == true || iAmDead) {

				new Menu(userName);
				JOptionPane.showMessageDialog(null, "You lost!", "Game status", JOptionPane.INFORMATION_MESSAGE);
				gameThread.interrupt();
				frame.dispose();
				gameTimer.stop();
			}

			try {
				client.sendToLobby(players.get(playerID).move().getBytes());
			} catch (NullPointerException npe) {
				// do not send anything, just move
				players.get(playerID).move();
			}

			for (int currentPlayerID : playersID) {
				players.get(currentPlayerID).move();

				if (players.get(currentPlayerID).isDead == true) {

					new Menu(userName);
					JOptionPane.showMessageDialog(null, "You won!", "Game status", JOptionPane.INFORMATION_MESSAGE);
					gameThread.interrupt();
					frame.dispose();
					gameTimer.stop();
				}
			}

			repaint();
		}
	});

	protected void initialize() {
		// Here is defined the map of the game:

		setLayout(null);
		setBounds(0, 0, (int) helper.screenWidth, (int) helper.screenHeight);
		setDoubleBuffered(true); // smooth
		setFocusable(true);
		frame = new JFrame("SwordingAround");
		frame.add(this);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.setResizable(true);
		frame.pack();
		frame.setVisible(true);
		addKeyListener(this);
		gameTimer.start();
	}

	@Override
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		helper.drawBackground(g2, this);
		helper.drawPlayerID(g2, playerID);
		helper.drawAllCharacters(g2, this, players);
		helper.drawSwordAnimations(g2, this, players);
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {

		int code = e.getKeyCode();

		if (code == KeyEvent.VK_D) {

			this.players.get(playerID).dash();
		}

		if (code == KeyEvent.VK_SPACE) {

			ArrayList<Integer> idsOfDeadPlayers = this.players.get(playerID).swingSword(players);

			try {
				this.client.sendToLobby(new StringBuilder(
						String.valueOf(DataProcessor.packetIntentionToInt(PacketIntention.IN_GAME_INFO))).append(" ")
								.append(this.playerID).append(" ")
								.append(DataProcessor.gameInfoToInt(InGameIntention.ATTACK_WITH_SWORD)).append(" ")
								.append(" STOP").toString().getBytes());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {
				for (int idOfDeadPlayer : idsOfDeadPlayers) {

					this.players.get(idOfDeadPlayer).die();
				} // those who are touched will die
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			}
		}

		if (code == KeyEvent.VK_UP) { // N

			this.players.get(playerID).keyUp = true;
			this.players.get(playerID).stays = false;
		}

		if (code == KeyEvent.VK_DOWN) {// S

			this.players.get(playerID).keyDown = true;
			this.players.get(playerID).stays = false;
		}

		if (code == KeyEvent.VK_LEFT) { // V

			this.players.get(playerID).keyLeft = true;
			this.players.get(playerID).stays = false;
		}

		if (code == KeyEvent.VK_RIGHT) { // E

			this.players.get(playerID).keyRight = true;
			this.players.get(playerID).stays = false;
		}

		this.players.get(playerID).setAngleAccordingToKeys();
	}

	@Override
	public void keyReleased(KeyEvent e) {

		int code = e.getKeyCode();

		if (code == KeyEvent.VK_UP) { // N

			this.players.get(playerID).keyUp = false;
		}
		if (code == KeyEvent.VK_DOWN) { // S

			this.players.get(playerID).keyDown = false;
		}
		if (code == KeyEvent.VK_LEFT) { // V

			this.players.get(playerID).keyLeft = false;
		}
		if (code == KeyEvent.VK_RIGHT) { // E

			this.players.get(playerID).keyRight = false;
		}

		this.players.get(playerID).setAngleAccordingToKeys();
	}

	public static void main(String[] args) {
		new Arcade(1, "Citadin").initialize();
	}
}

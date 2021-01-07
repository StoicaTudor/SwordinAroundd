package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import game_package.Bullet;
import game_package.GameInfo;
import game_package.InGamePlayer;
import main_package.Pair;
import main_package.User;

public class DataProcessor {

	public String userName;
	/////////////// private ArrayList<Pair<User, GameInfo>> playersConclusion; /////
	/////////////// maybe useless
	private final int playerID;
	public String[] packetInfo = null;

	public DataProcessor(int playerID, String userName) {
		// TODO Auto-generated constructor stub
		this.playerID = playerID;
		this.userName = userName;
	}

	public void initNewPacket(byte[] dataToProccess) {
		// TODO Auto-generated method stub
		this.packetInfo = new String(dataToProccess).split(" ");
	}

	public JFrame displayConclusion() {
		// to implement
		return null;
	}

	public static PacketIntention intToPacketIntention(int integerIntention) {

		switch (integerIntention) {

		case 0:
			return PacketIntention.SIGN_UP;

		case 1:
			return PacketIntention.SIGN_IN;

		case 2:
			return PacketIntention.CONNECTED_TO_LOBBY;

		case 3:
			return PacketIntention.START_GAME;

		case 4:
			return PacketIntention.IN_GAME_INFO;

		case 5:
			return PacketIntention.END_GAME;

		case 6:
			return PacketIntention.NEW_PLAYER_TO_LOBBY;

		default:
			return PacketIntention.RESIDUAL;

		}
	}

	public static int packetIntentionToInt(PacketIntention packetIntention) {

		switch (packetIntention) {

		case SIGN_UP:
			return 0;

		case SIGN_IN:
			return 1;

		case CONNECTED_TO_LOBBY:
			return 2;

		case START_GAME:
			return 3;

		case IN_GAME_INFO:
			return 4;

		case END_GAME:
			return 5;

		case NEW_PLAYER_TO_LOBBY:
			return 6;

		default:
			return -1;

		}
	}

	public static InGameIntention intToInGameIntention(int integerGameInfo) {

		switch (integerGameInfo) {

		case 0:
			return InGameIntention.MOVE;

		case 1:
			return InGameIntention.STAY;

		case 2:
			return InGameIntention.DASH;

		case 3:
			return InGameIntention.ATTACK_WITH_SWORD;

		case 4:
			return InGameIntention.SHOOT;

		case 5:
			return InGameIntention.KILL;

		default:
			return InGameIntention.RESIDUAL;

		}
	}

	public static int gameInfoToInt(InGameIntention gameInfo) {

		switch (gameInfo) {

		case MOVE:
			return 0;

		case STAY:
			return 1;

		case DASH:
			return 2;

		case ATTACK_WITH_SWORD:
			return 3;

		case SHOOT:
			return 4;

		case KILL:
			return 5;

		default:
			return -1;

		}
	}

	public boolean isConnectedToLobbyPacket() {

		return (this.intToPacketIntention(Integer.parseInt(this.packetInfo[0])) == PacketIntention.CONNECTED_TO_LOBBY);
	}

	public boolean getTeam() {

		return (Integer.parseInt(this.packetInfo[1]) == 0);
	}

	public boolean isGameInfo() {

		try {
			return (this.intToPacketIntention(Integer.parseInt(this.packetInfo[0])) == PacketIntention.IN_GAME_INFO);
		} catch (NullPointerException npe) {
			return false;
		}
	}

	public boolean isAddNewPlayerToLobby() {

		try {
			return (this
					.intToPacketIntention(Integer.parseInt(this.packetInfo[0])) == PacketIntention.NEW_PLAYER_TO_LOBBY);
		} catch (NullPointerException npe) {
			return false;
		}
	}

	public int getPlayerID() {

		return Integer.parseInt(this.packetInfo[1]);
	}

	public InGameIntention getInGameIntention() {

		return this.intToInGameIntention(Integer.parseInt(this.packetInfo[2]));
	}

	public int[] getMovementParameters() {

		int[] movementParameters = new int[10];
		int index = 0;

		movementParameters[index++] = Integer.parseInt(this.packetInfo[3]);
		movementParameters[index++] = Integer.parseInt(this.packetInfo[4]);
		movementParameters[index++] = Integer.parseInt(this.packetInfo[5]);
		movementParameters[index++] = Integer.parseInt(this.packetInfo[6]);

		return movementParameters;
	}

	public int getAttackWithSwordParameters() {

		// angle at which character swings his sword
		return Integer.parseInt(this.packetInfo[3]);
	}

	public int getKillingParameters() {

		// id of killed player
		return Integer.parseInt(this.packetInfo[3]);
	}
}

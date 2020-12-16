package game_package;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class DataProcessor {

	class Bullet {

		/*
		 * the bullet it shot has a line trajectory and is defined by the equation y =
		 * a*x + b
		 */

		private int bulletA = 0;
		private int bulletB = 0;

		public Bullet(int bulletA, int bulletB) {

			this.bulletA = bulletA;
			this.bulletB = bulletB;
		}
	}

	private int currentDataIndex = 0;

	private final int playerID;
	private InetAddress serverAddress;
	private int serverPort;
	public int playerX = 100;
	public int playerY = 100;

	public int swordAttackX = -1; // suppose it did not attack
	public int swordAttackY = -1; // suppose it did not attack

	public List<Bullet> existingBullets;

	public DataProcessor(int playerID, InetAddress serverAddress, int serverPort) {

		this.playerID = playerID;
		this.currentDataIndex = 0;
		
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
	}

	public void processData() {

		processPlayerCoordinates();
		processSwordAttack();
		processBulletsFired();
	}

	private void processPlayerCoordinates() {

		this.playerX = this.data[this.currentDataIndex++];
		this.playerY = this.data[this.currentDataIndex++];
	}

	private void processSwordAttack() {

		this.swordAttackX = this.data[this.currentDataIndex++];
		this.swordAttackY = this.data[this.currentDataIndex++];
	}

	private void processBulletsFired() {

		existingBullets = new ArrayList<Bullet>();

		while (this.currentDataIndex < this.data.length) {

			this.existingBullets
					.add(new Bullet(this.data[this.currentDataIndex++], this.data[this.currentDataIndex++]));
		}
	}

}

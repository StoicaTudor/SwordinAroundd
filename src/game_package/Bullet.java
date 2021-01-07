package game_package;

import java.awt.Point;
import java.util.ArrayList;

public class Bullet {

	/*
	 * the bullet it shot has a line trajectory and is defined by the equation y =
	 * a*x + b
	 */

	private int bulletSpeed = 20;
	private Point bulletCoord = new Point(200, 200);
	private int bulletTrajectoryAngle = 0;

	public Bullet(Point bulletCoord, int bulletTrajectoryAngle) {

		this.bulletCoord = bulletCoord;
		this.bulletTrajectoryAngle = bulletTrajectoryAngle;
	}

	public ArrayList<Integer> bulletsTouchedAnyPlayers(ArrayList<InGamePlayer> players, int userPlayerID) {

		ArrayList<Integer> idsOfDeathPlayers = new ArrayList<Integer>();

		for (InGamePlayer currentPlayer : players) {

			if (currentPlayer.getPlayerID() != userPlayerID && bulletHitsPlayer(currentPlayer)) {
				idsOfDeathPlayers.add(currentPlayer.getPlayerID());
			}
		}
		return idsOfDeathPlayers;
	}

	public void moveBullet() {

		double movementSin = Math.sin(Math.toRadians(bulletTrajectoryAngle));
		double movementCos = Math.cos(Math.toRadians(bulletTrajectoryAngle));

		bulletCoord = new Point((int) (bulletCoord.x + bulletSpeed * movementCos),
				(int) (bulletCoord.y + bulletSpeed * movementSin));
	}

	private boolean bulletHitsPlayer(InGamePlayer player) {

		// !!! TO IMPLEMENT PROPERLY
		// THIS IMPLEMENTATION DOES NOT TAKE INTO ACCOUNT THE HITBOX OF THE CHARACTER,
		// NOR THE FACT THAT
		// A BULLET CAN SURPASS THE CHARACTER, EVEN THOUGH IT HIT IT, BECAUSE THE BULLET
		// MOVES 20 PIXELS PER ITERATION

		if (Math.abs(player.getPlayerCoord().x - this.bulletCoord.x) <= this.bulletSpeed
				&& Math.abs(player.getPlayerCoord().y - this.bulletCoord.y) <= this.bulletSpeed) {
			return true;
		}

		return false;
	}
}
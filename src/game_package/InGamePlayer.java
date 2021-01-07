package game_package;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Timer;

import network.DataProcessor;
import network.InGameIntention;
import network.PacketIntention;

public class InGamePlayer {

	public int characterWidth = 70;
	public int characterHeight = 100;
	public int swordWidth = 250;
	public int swordHeight = 300;
	public int swordRadius = 390;

	public String userName = "";
	private int playerID = -1;
	private Point coord = new Point(500, 500);
	private int speed = 8;
	private int dashSpeed = 200;
	private boolean dashCooldown = false;
	private int angle = 270;
	/*
	 * we will denote the angle as the position relative to the trigonometric circle
	 * for instance, if it goes right, according to the user, we will denote it as
	 * angle = 0 -> relative to the trigonometric circle if it goes NV, that will be
	 * angle = 135
	 */

	public boolean isDead = false;
	public boolean team = false;
	public boolean swingsSword = false;
	public boolean stays = true;
	public Rectangle hitBoxCharacter = new Rectangle(coord.x, coord.y, characterWidth, characterHeight);
	public Rectangle hitBoxSword = new Rectangle(coord.x, coord.y, swordWidth - characterWidth,
			swordHeight - characterHeight);

	public InGamePlayer(int playerID, String username) {
		this.playerID = playerID;
		this.userName = username;
	}

	public int getPlayerID() {
		return this.playerID;
	}

	public void setDashParameters(int angle, int speed) {
		this.angle = angle;
		this.dashSpeed = speed;
	}

	private Timer dashTimer = new Timer(3000, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			dashCooldown = false;
			swordTimer.stop();
		}
	});

	private void updateHitBoxes() {
		hitBoxCharacter = new Rectangle(coord.x, coord.y, characterWidth, characterHeight);
		hitBoxSword = new Rectangle(coord.x, coord.y, swordWidth - characterWidth, swordHeight - characterHeight);
	}

	public String dash() {
		// rapid change in coordinates (maybe an animation)

		if (!this.dashCooldown && !this.isDead) {

			this.dashCooldown = true;
			this.dashTimer.start();
			double movementSin = -Math.sin(Math.toRadians(angle));
			double movementCos = Math.cos(Math.toRadians(angle));

			coord = new Point((int) (coord.x + dashSpeed * movementCos), (int) (coord.y + dashSpeed * movementSin));
			updateHitBoxes();
			// or we could implement a mini teleportation

			return new StringBuilder(String.valueOf(DataProcessor.packetIntentionToInt(PacketIntention.IN_GAME_INFO)))
					.append(" ").append(this.playerID).append(" ")
					.append(DataProcessor.gameInfoToInt(InGameIntention.DASH)).append(" ").append(this.angle)
					.append(" ").append(this.dashSpeed).append(" STOP").toString();
		}

		return null;
	}

	public void setMoveParameters(int angle, int nextCoordX, int nextCoordY) {
		this.angle = angle;
		this.coord = new Point(nextCoordX, nextCoordY);
	}

	public void setStay() {
		this.speed = 0;
	}

	public String move() {

		if (this.stays == false && !this.isDead) {

			double movementSin = -Math.sin(Math.toRadians(angle)); // that is, because the y axis is rotated in the
																	// graphics domain
			double movementCos = Math.cos(Math.toRadians(angle));

			coord = new Point((int) (coord.x + speed * movementCos), (int) (coord.y + speed * movementSin));
			updateHitBoxes();

			return new StringBuilder(String.valueOf(DataProcessor.packetIntentionToInt(PacketIntention.IN_GAME_INFO)))
					.append(" ").append(this.playerID).append(" ")
					.append(DataProcessor.gameInfoToInt(InGameIntention.MOVE)).append(" ").append(this.angle)
					.append(" ").append(this.coord.x).append(" ").append(this.coord.y).append(" ").append(team ? 1 : 0)
					.append(" STOP").toString();
		}

		return null; /////////////////////// TO IMPLEMENT!!!
	}

	private Timer swordTimer = new Timer(300, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			swingsSword = false;
			swordTimer.stop();
		}
	});

	public ArrayList<Integer> swingSword(Map<Integer, InGamePlayer> players) {

		if (this.isDead) {
			return null;
		}

		this.swingsSword = true;
		swordTimer.start();
		ArrayList<Integer> idsOfDeadPlayers = new ArrayList<Integer>();

		Iterator it = players.entrySet().iterator();
		// it.next(); // in order to not analyze the same player which swings the sword

		while (it.hasNext()) {

			Map.Entry pair = (Map.Entry) it.next();
			InGamePlayer currentPlayer = (InGamePlayer) pair.getValue();

			currentPlayer.updateHitBoxes();

			if (this.playerID != currentPlayer.playerID && isHitBySword(currentPlayer)) {

				idsOfDeadPlayers.add(currentPlayer.playerID);
			}
		}

		return idsOfDeadPlayers;
	}

	private boolean isHitBySword(InGamePlayer currentPlayer) {

//		System.out.println(
//				"player coordinates -> " + currentPlayer.getPlayerCoord().x + "   " + currentPlayer.getPlayerCoord().y);
//
//		System.out.println("character -> x = " + currentPlayer.hitBoxCharacter.x + " y = "
//				+ currentPlayer.hitBoxCharacter.y + " width = " + currentPlayer.hitBoxCharacter.width + " height = "
//				+ currentPlayer.hitBoxCharacter.height);
//
//		System.out.println("sword -> x = " + this.hitBoxCharacter.x + " y = " + this.hitBoxCharacter.y + " width = "
//				+ this.hitBoxCharacter.width + " height = " + this.hitBoxCharacter.height);

		return (this.hitBoxSword.intersects(currentPlayer.hitBoxCharacter));
	}

	public void die() {
		this.isDead = true;
	}

	public Point getPlayerCoord() {
		return this.coord;
	}

	public int getAngle() {
		return this.angle;
	}

	public boolean keyUp = false;
	public boolean keyDown = false;
	public boolean keyRight = false;
	public boolean keyLeft = false;

	public void setAngleAccordingToKeys() {

		if (!keyUp && !keyRight && !keyLeft && !keyDown) {
			this.stays = true;
		}

		if (keyUp && keyRight) {
			this.angle = 45;
			return;
		} else {
			if (keyUp && keyLeft) {
				this.angle = 135;
				return;
			} else {
				if (keyDown && keyRight) {
					this.angle = 315;
					return;
				} else {
					if (keyDown && keyLeft) {
						this.angle = 225;
						return;
					} else {
						if (keyRight) {
							this.angle = 0;
							return;
						} else {
							if (keyUp) {
								this.angle = 90;
								return;
							} else {
								if (keyLeft) {
									this.angle = 180;
									return;
								} else {
									if (keyDown) {
										this.angle = 270;
										return;
									}
								}
							}
						}
					}
				}
			}
		}
	}
}

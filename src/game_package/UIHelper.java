package game_package;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class UIHelper {

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public int screenHeight = (int) (screenSize.getHeight() * 1.25);
	public int screenWidth = (int) (screenSize.getWidth() * 1.25);

	public UIHelper() {
		// TODO Auto-generated constructor stub
	}

	public void drawPlayerID(Graphics2D g2, int playerID) {

		g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g2.setColor(Color.BLACK);
		g2.drawString(new StringBuilder("my id : ").append(playerID).toString(), 600, 100);
	}

	public void drawBackground(Graphics2D g2, JPanel panel) {

		ImageIcon background = new ImageIcon(
				new StringBuilder(System.getProperty("user.dir")).append("\\").append("Resources").append("\\")
						.append("InGameResources").append("\\").append("Background.jpg").toString());
		g2.drawImage(background.getImage(), 0, 0, screenWidth, screenHeight, panel);
	}

	public void drawAllCharacters(Graphics2D g2, JPanel panel, Map<Integer, InGamePlayer> players) {

		// converting Map to ArrayList
		Collection<InGamePlayer> values = players.values();

		ArrayList<InGamePlayer> listOfPlayers = new ArrayList<InGamePlayer>(values);

		ImageIcon characterImage = null;

		for (InGamePlayer currentPlayer : listOfPlayers) {

			characterImage = new ImageIcon(new StringBuilder(System.getProperty("user.dir")).append("\\")
					.append("Resources").append("\\").append("InGameResources").append("\\")
					.append(getCharacterImageNameByAngleAndTeam(currentPlayer.getAngle(), currentPlayer.team))
					.toString());
			g2.drawImage(characterImage.getImage(), currentPlayer.getPlayerCoord().x, currentPlayer.getPlayerCoord().y,
					currentPlayer.characterWidth, currentPlayer.characterHeight, panel);

			g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2.setColor(Color.BLACK);
			g2.drawString(currentPlayer.userName, currentPlayer.getPlayerCoord().x, currentPlayer.getPlayerCoord().y);
		}
	}

	private String getCharacterImageNameByAngleAndTeam(int angle, boolean team) {

		StringBuilder imageName = new StringBuilder();

		switch (angle) {

		case 0:
			imageName.append("E");
			break;

		case 45:
			imageName.append("NE");
			break;

		case 90:
			imageName.append("N");
			break;

		case 135:
			imageName.append("NV");
			break;

		case 180:
			imageName.append("V");
			break;

		case 225:
			imageName.append("SV");
			break;

		case 270:
			imageName.append("S");
			break;

		case 315:
			imageName.append("SE");
			break;

		default:
			imageName.append("S");
			break;
		}

		if (team == true) {
			return imageName.append("BLACK.png").toString();
		}

		return imageName.append("BLUE.png").toString();
	}

	public void drawSwordAnimations(Graphics2D g2, JPanel panel, Map<Integer, InGamePlayer> players) {

		// converting Map to ArrayList
		Collection<InGamePlayer> values = players.values();

		ArrayList<InGamePlayer> listOfPlayers = new ArrayList<InGamePlayer>(values);

		ImageIcon characterSword = null;

		for (InGamePlayer currentPlayer : listOfPlayers) {

			if (currentPlayer.swingsSword) {
				Point playerCoord = currentPlayer.getPlayerCoord();

				characterSword = new ImageIcon(new StringBuilder(System.getProperty("user.dir")).append("\\")
						.append("Resources").append("\\").append("InGameResources").append("\\")
						.append(getSwordImageNameByAngle(currentPlayer.getAngle())).toString());
				g2.drawImage(characterSword.getImage(), playerCoord.x - currentPlayer.characterWidth,
						playerCoord.y - currentPlayer.characterHeight, currentPlayer.swordWidth,
						currentPlayer.swordHeight, panel);
			}
		}
	}

	private String getSwordImageNameByAngle(int angle) {

		StringBuilder imageName = new StringBuilder();

		switch (angle) {

		case 0:
			imageName.append("sE");
			break;

		case 45:
			imageName.append("sNE");
			break;

		case 90:
			imageName.append("sN");
			break;

		case 135:
			imageName.append("sNV");
			break;

		case 180:
			imageName.append("sV");
			break;

		case 225:
			imageName.append("sSV");
			break;

		case 270:
			imageName.append("sS");
			break;

		case 315:
			imageName.append("sSE");
			break;

		default:
			imageName.append("sS");
			break;
		}

		return imageName.append(".gif").toString();
	}

	public void drawDummySquare(Graphics2D g2) {

		g2.setColor(Color.RED);
		g2.fillRect(100, 100, 10, 10);
	}
}

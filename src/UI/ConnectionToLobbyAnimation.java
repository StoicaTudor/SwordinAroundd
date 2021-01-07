package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionToLobbyAnimation {

	private String animationName = "WaitingAnimation0.gif";
	private JFrame frame;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectionToLobbyAnimation window = new ConnectionToLobbyAnimation();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConnectionToLobbyAnimation() {
		initialize();
		this.setAnimationVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();

		panel = new JPanel();
		panel.setBounds(0, 0, 1000, 700);
		panel.setLayout(null);

		frame.setExtendedState(frame.MAXIMIZED_BOTH);
		// frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(panel);
	}

	public void setAnimationVisible(boolean visible) {
		frame.setVisible(visible);
		if (visible) {

		}
	}

	public boolean frameIsVisible() {
		return (this.frame.isVisible());
	}

	public void paintComponent(Graphics g) {
		System.out.println("here");
		panel.paintComponents(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(new ImageIcon(new StringBuilder(System.getProperty("user.dir")).append((char) (92))
				.append("Resources").append((char) (92)).append(animationName).toString()).getImage(), 200, 200, 70,
				100, panel);
		System.out.println(new StringBuilder(System.getProperty("user.dir")).append((char) (92)).append("Resources")
				.append((char) (92)).append(animationName).toString());
		System.gc();
	}
}

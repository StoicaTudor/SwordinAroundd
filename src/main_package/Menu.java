package main_package;

import javax.swing.*;

import game_package.Arcade;
import game_package.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Menu extends JPanel {
	
	String username = "";
	private JFrame frame = new JFrame();
	private JLabel version = new JLabel();
	private JButton teamMode = new JButton();

	private ActionListener teamModePressed = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {

			frame.dispose();
			Random rand = new Random();
			Game newGame = new Arcade(Math.abs(rand.nextInt()), username);
			newGame.startGame();
		}
	};

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(new ImageIcon(new StringBuilder(System.getProperty("user.dir")).append("\\").append("Resources")
				.append("\\").append("FramesResources").append("\\").append("menuGif.gif").toString()).getImage(), 0, 0,
				600, 400, this);
	}

	public Menu(String username) {

		this.username = username;
		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		frame.setResizable(true);

		setLayout(null);

		version.setText("This is a beta version:");
		version.setBounds(0, 0, 200, 25);
		version.setForeground(Color.WHITE);
		add(version);

		teamMode.setText("PLAY");
		teamMode.setBounds(250, 250, 100, 25);
		teamMode.setBorder(new RoundedBorder(10));
		teamMode.addActionListener(teamModePressed);
		teamMode.setForeground(Color.BLUE);
		add(teamMode);

		frame.add(this);
	}

	public static void main(String[] args) {
		new Menu("Citadin");
	}
}

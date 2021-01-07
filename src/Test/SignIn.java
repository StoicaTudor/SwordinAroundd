package Test;

import javax.swing.*;

public class SignIn {

	JFrame frame;
	JPanel panel;

	public SignIn() {
		
		initFrame();
		setButtons();
	}
	
	private void initFrame() {
		
		frame = new JFrame();
		panel = new JPanel();
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(panel);
		panel.setLayout(null);
		JLabel nameLabel = new JLabel();
		// Server.sendData - pentru a trimite TUDOR!!!
	}
	
	private void setButtons() {
		// TODO Miruna
	}

	public static void main(String[] args) {
		new SignIn();
	}
}

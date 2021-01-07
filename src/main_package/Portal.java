package main_package;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Portal {

	CredentialsClient client = new CredentialsClient();

	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();
	private JLabel username = new JLabel();
	private JLabel password = new JLabel();
	private JTextField usernameData = new JTextField();
	private JPasswordField passwordData = new JPasswordField();
	private JButton signIn = new JButton();
	private JButton signUp = new JButton();
	private JButton fullScreen = new JButton();
	static GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	private ActionListener SignInPressed = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Verify if the data is ok and than go to the game else print that the data is
			// not ok;

			client.sendToServer(new StringBuilder("1 ").append(usernameData.getText()).append(" ")
					.append(passwordData.getPassword()).append(" STOP").toString().getBytes());

			while (client.getLastReceivedPacket() == null) {

			}
			
			String[] content = new String(client.lastReceivedPacket.getData()).split(" ");

			if (content[0].equals("1")) {
				new Menu(usernameData.getText()).setVisible(true);
				client.listening = false;
				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "not the credentials we want, but the credentials we need",
						"Hold on cowboy", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	};
	private ActionListener SignUpPressed = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			client.sendToServer(new StringBuilder("0 ").append(usernameData.getText()).append(" ")
					.append(passwordData.getPassword()).append(" STOP").toString().getBytes());

			while (client.getLastReceivedPacket() == null) {

			}
			
			String[] content = new String(client.lastReceivedPacket.getData()).split(" ");

			if (content[0].equals("0")) {
				new Menu(usernameData.getText()).setVisible(true);
				client.listening = false;
				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(null, "not the credentials we want, but the credentials we need",
						"Hold on cowboy", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	};

	private ActionListener FullScreen = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			device.setFullScreenWindow(frame);
		}
	};

	public Portal() {

		frame.setSize(600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.add(panel);
		frame.setResizable(true);

		panel.setLayout(null);

		username.setText("USERNAME:");
		username.setBounds(100, 120, 80, 25);
		panel.add(username);

		password.setText("PASSWORD:");
		password.setBounds(100, 150, 80, 25);
		panel.add(password);

		usernameData.setBounds(200, 120, 165, 25);
		panel.add(usernameData);

		passwordData.setBounds(200, 150, 165, 25);
		panel.add(passwordData);

		signIn.setText("Sign In");
		signIn.setBounds(150, 190, 80, 25);
		signIn.addActionListener(SignInPressed);
		panel.add(signIn);

		signUp.setText("Sign Up");
		signUp.setBounds(285, 190, 80, 25);
		signUp.addActionListener(SignUpPressed);
		panel.add(signUp);

	}

	public static void main(String[] args) {
		new Portal();
	}
}

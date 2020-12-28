package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectionToLobbyAnimation {

	JLabel animationLabel;

	private final int ANIMATION_REFRESH_RATE = 3000;
	private ImageIcon image = new ImageIcon("src/Resources/Animation0.png");
	private int currentAnimation = 0;
	private final int NR_ANIMATIONS = 3;

	private JFrame frame;
	private Timer animationTimer = new Timer(ANIMATION_REFRESH_RATE, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			image = new ImageIcon(new StringBuilder("/src/Resources/Animation").append(currentAnimation++).append(".png").toString());
			animationLabel.setIcon(image);
			// it doesn't not show any image 
			System.out.println(currentAnimation);
			if (currentAnimation == NR_ANIMATIONS) {

				currentAnimation = 0;
			}
		}
	});

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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		// frame.setBounds(100, 100, 450, 300);
		frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		animationLabel = new JLabel();
		animationLabel.setBounds(0, 0, 1000, 500);
		frame.add(animationLabel);
	}

	public void setAnimationVisible(boolean visible) {
		frame.setVisible(visible);
		
		if(visible) {
			//Paint the image swing
		}
	}
}

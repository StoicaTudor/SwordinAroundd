package Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

//https://www.google.com/search?rlz=1C1CHBD_enRO916RO916&sxsrf=ALeKk02sNdLQ2lP82D_S8sRQPFACo6WF6A%3A1608392145682&ei=0R3eX_-PKc-aa6aFhugL&q=starting+a+java+swing+project+in+intellij+idea&oq=starting+a+java+swing+project+in+inte&gs_lcp=CgZwc3ktYWIQARgBMggIIRAWEB0QHjIICCEQFhAdEB4yCAghEBYQHRAeMggIIRAWEB0QHjoECAAQRzoICAAQCBAHEB46CAgAEAcQBRAeOgkIABDJAxAWEB46BggAEBYQHlDpG1i6W2D1ZGgAcAJ4AIABjAGIAZUXkgEEMS4yNZgBAKABAaoBB2d3cy13aXrIAQjAAQE&sclient=psy-ab#kpvalbx=_7x3eX4bkCYGXlwSOi4fQBA13;
//Functionality here; design in the next
public class Personaj extends JPanel implements KeyListener {
	private JFrame frame = new JFrame();
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	int screenHeight = (int) (screenSize.getHeight() * 1.25);
	int screenWidth = (int) (screenSize.getWidth() * 1.25);
	private int X = 100;
	private int Y = 100;
	private int x;
	private int y;
	private boolean u;
	private boolean d;
	private boolean l;
	private boolean r;

	private String imageName = "down-removebg-preview.png";

	private Image im;
	private Image back;

	public Personaj() {
		// Here is defined the map of the game:
		JLabel background;
		setLayout(null);
		setBounds(0, 0, (int) screenWidth, (int) screenHeight);
		setDoubleBuffered(true); // smooth
		setFocusable(true);
		frame = new JFrame("SwordingAround");
		frame.add(this);
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(this);
		frame.setResizable(true);
		frame.pack();
		frame.setVisible(true);
		ImageIcon i = new ImageIcon("images.png");
		background = new JLabel("", i, JLabel.CENTER);
		background.setBounds(0, 0, screenWidth, screenHeight);
		add(background);
		addKeyListener(this);

		t.start();
		u = false;
		d = false;
		r = false;
		l = false;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// ImageIcon(this.getClass().getClassLoader().getResource(imageName));

		ImageIcon character = new ImageIcon(new StringBuilder(System.getProperty("user.dir")).append("\\")
				.append("Resources").append("\\").append("InGameResources").append("\\").append(imageName).toString());

		g2.drawImage(character.getImage(), X, Y, 70, 100, this);

		if (u && r)
			goRightUp();
		else if (d && r)
			goRightDown();
		else if (u && l)
			goLeftUp();
		else if (d && l)
			goLeftDown();
		else if (u)
			goUp();
		else if (d)
			goDown();
		else if (r)
			goRight();
		else if (l)
			goLeft();
	}

	private Timer t = new Timer(10, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			X = X + x;
			Y = Y + y;
			repaint();
		}
	});

	public void goUp() {
		imageName = "up-removebg-preview.png";
		y = -5;
		x = 0;

	}

	public void goDown() {
		imageName = "down-removebg-preview.png";
		y = 5;
		x = 0;

	}

	public void goLeft() {
		imageName = "left-removebg-preview.png";
		y = 0;
		x = -5;

	}

	public void goRight() {
		imageName = "right-removebg-preview.png";
		y = 0;
		x = 5;

	}

	public void goRightDown() {
		imageName = "right-down-removebg-preview.png";
		y = 3;
		x = 3;

	}

	public void goRightUp() {
		imageName = "right-up-removebg-preview.png";
		y = -3;
		x = 3;
	}

	public void goLeftDown() {
		imageName = "left-down-removebg-preview.png";
		y = 3;
		x = -3;

	}

	public void goLeftUp() {
		imageName = "left-up-removebg-preview.png";
		y = -3;
		x = -3;

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_UP)
			u = true;
		if (code == KeyEvent.VK_DOWN)
			d = true;
		if (code == KeyEvent.VK_LEFT)
			l = true;
		if (code == KeyEvent.VK_RIGHT)
			r = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_UP)
			u = false;
		if (code == KeyEvent.VK_DOWN)
			d = false;
		if (code == KeyEvent.VK_LEFT)
			l = false;
		if (code == KeyEvent.VK_RIGHT)
			r = false;
	}

	public static void main(String[] args) {
		new Personaj();
	}
}

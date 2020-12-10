package Control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard {

	public static enum KeyboardMode {
		ARROWS, WASD
	}

	private KeyboardMode keyboardMode;
	private KeyListener currentKeyListenerSettings;

	public Keyboard(KeyboardMode keyboardMode) {

		changeKeyboardSettings(keyboardMode);
	}

	public void changeKeyboardSettings(KeyboardMode mode) {

		switch (mode) {

		case WASD:
			this.currentKeyListenerSettings = wasdKeyListener;
			break;

		case ARROWS:
			this.currentKeyListenerSettings = arrowsKeyListener;
			break;

		default:
			this.currentKeyListenerSettings = arrowsKeyListener;
			break;

		}
	}

	public KeyListener getCurrentKeyListenerSettings() {

		return this.currentKeyListenerSettings;
	}

	private KeyListener arrowsKeyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {

		}
	};

	private KeyListener wasdKeyListener = new KeyListener() {

		@Override
		public void keyTyped(KeyEvent e) {
			
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {

		}
	};
}

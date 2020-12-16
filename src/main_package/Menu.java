package main_package;

import javax.swing.JOptionPane;

import network.Client;

public class Menu {

	public Menu() {

	}

	public static void main(String[] args) {

		// new Menu();
		for (int i = 0; i < 1; i++) {

			Client client = new Client("localhost", 8192, i); // 64325
			client.connect();
			
			try {
			    Thread.sleep(100);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		}
	}
}

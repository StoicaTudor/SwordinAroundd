package Test;

import javax.swing.*;

public class SignIn {
    public SignIn() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        frame. setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(panel);
        panel.setLayout(null);
        JLabel nameLabel = new JLabel();
        //Server.sendData - pentru a trimite TUDOR!!!
    }
    public static void main(String[] args)
    {
        new SignIn ();
    }
}

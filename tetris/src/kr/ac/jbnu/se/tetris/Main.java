package kr.ac.jbnu.se.tetris;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main extends JFrame{
	JLabel statusbar;
	public Tetris game;
	private MainPage mainpage = null;
	
	
	public Main() {
		setSize(200, 400);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	

	public static void main(String[] args) {
		Main main = new Main();
		MainPage mainpage = new MainPage(main);
		main.add(mainpage);
		main.setLocationRelativeTo(null);
		main.setVisible(true);
	}
}

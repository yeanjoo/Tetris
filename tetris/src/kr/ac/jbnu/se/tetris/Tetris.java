package kr.ac.jbnu.se.tetris;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Tetris extends JFrame {

	JLabel statusbar;
	JLabel level ;
	JPanel gamePanel = new JPanel();
	Board board;
	StateMain statemain;
	StateMenu statemenu;
	// private Main main;

	public Tetris() {
		// this.main = main;
		this.setLayout(new BorderLayout());
		gamePanel.setLayout(new GridLayout(1, 2));
		statusbar = new JLabel("0");
		add(statusbar, BorderLayout.SOUTH);
		add(gamePanel);

		statemain = new StateMain(this);//statemain이 먼저 시작하니까 NextPiece 프레임 지정이 먼저 일어날것
		board = new Board(this);
		gamePanel.add(board);
		board.start();
//		statemain.currentScore.add(level);
		gamePanel.add(statemain);
		setSize(400, 400);
		setLocationRelativeTo(null);
		setTitle("Tetris");
		statemain.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public JLabel getStatusBar() {
		return statusbar;
	}

	// public static void main(String[] args) {
	// Tetris game = new Tetris();
	// JFrame frame = new JFrame();
	// frame.add(game);
	// frame.setSize(200, 400);
	// frame.setTitle("Tetris");
	// frame.setLocationRelativeTo(null);
	// frame.setVisible(true);
	// }
}
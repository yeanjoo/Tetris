package kr.ac.jbnu.se.tetris;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Tetris extends JFrame {

	JLabel statusbar;
	JLabel statusbar1;
	JLabel statusbar2;
	JPanel score = new JPanel();
	JLabel level ;
	JPanel gamePanel = new JPanel();
	Board board;
	Board board2;
	StateMain statemain;
	StateMenu statemenu;
	
	//1인용
	public Tetris() {
		this.setLayout(new BorderLayout());
		gamePanel.setLayout(new GridLayout(1, 2));
		statusbar = new JLabel("0");
		add(statusbar, BorderLayout.SOUTH);
		add(gamePanel);
		statemain = new StateMain(this);
		board = new Board(this,0);
		board.addKeyListener(new Key(board));
		board.setFocusable(true);
		gamePanel.add(board);
		board.start();
		gamePanel.add(statemain);
		setSize(400, 400);
		setLocationRelativeTo(null);
		setTitle("Tetris");
		statemain.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	//2인용
	public Tetris(int x) {
		this.setLayout(new BorderLayout());
		gamePanel.setLayout(new GridLayout(1, 2));
		score.setLayout(new GridLayout(1,2));
		statusbar1 = new JLabel("0");
		statusbar2 = new JLabel("0");
		statusbar2.setHorizontalAlignment(SwingConstants.RIGHT);
		add(score, BorderLayout.SOUTH);
		score.add(statusbar1);
		score.add(statusbar2);
		add(gamePanel);
		statemenu = new StateMenu(this);
		board = new Board(this,1);
		board2 = new Board(this,2,board);
		board.getBoard(board2);
		board.addKeyListener(new Key(board,board2));
		board.setFocusable(true);
		gamePanel.add(board);
		gamePanel.add(statemenu);
		gamePanel.add(board2);
		board.start();
		board2.start();
		setSize(600, 400);
		setLocationRelativeTo(null);
		setTitle("Tetris");
		statemenu.setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public JLabel getStatusBar() {
		return statusbar;
	}
	public JLabel getStatus1Bar() {
		return statusbar1;
	}
	public JLabel getStatus2Bar() {
		return statusbar2;
	}
	
}


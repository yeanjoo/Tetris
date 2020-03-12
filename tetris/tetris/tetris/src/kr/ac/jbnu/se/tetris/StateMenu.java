package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class StateMenu extends JPanel {
	
	final int BoardWidth = 13; // 가로 10칸
	final int BoardHeight = 18; // 세로 10칸
	
	public JPanel scoreBoard = new JPanel();;
	private JPanel sateBoard = new JPanel();
	private JPanel player1nextShapeSmall1 = new JPanel();
	private JPanel player1nextShapeSmall2 = new JPanel();
	private JPanel player1nextShapeSmall3 = new JPanel();
	private JPanel sateBoardLeft = new JPanel();
	private JPanel sateBoardRight = new JPanel();
	private JPanel nextShapeSmall1 = new JPanel();
	private JPanel nextShapeSmall2 = new JPanel();
	private JPanel nextShapeSmall3 = new JPanel();
	public JLabel player1Stack = new JLabel("");
	public JLabel player2Stack = new JLabel("");
	private Tetris tetris;
	private Shape[] next1Pie = new Shape[5];
	private Shape[] next2Pie = new Shape[5];

	private Queue<Shape> queue = new LinkedList<Shape>();

	public StateMenu() {
		// setScoreBoard();
	}

	public StateMenu(Tetris tetris) {
		this.tetris = tetris;
		initialize();
		setSize(200, 400);
	}

	private void initialize() {

		// 테두리 설정

		scoreBoard.setBorder(new LineBorder(Color.red, 1));
		sateBoard.setBorder(new LineBorder(Color.red, 1));
		player1nextShapeSmall1.setBorder(new LineBorder(Color.red, 1));
		player1nextShapeSmall2.setBorder(new LineBorder(Color.red, 1));
		player1nextShapeSmall3.setBorder(new LineBorder(Color.red, 1));
		sateBoardLeft.setBorder(new LineBorder(Color.red, 1));
		sateBoardRight.setBorder(new LineBorder(Color.red, 1));
		nextShapeSmall1.setBorder(new LineBorder(Color.red, 1));
		nextShapeSmall2.setBorder(new LineBorder(Color.red, 1));
		nextShapeSmall3.setBorder(new LineBorder(Color.red, 1));

		// Layout 설정
		scoreBoard.setLayout(new GridLayout(5, 1));
		setLayout(new GridLayout(2, 1));
		sateBoard.setLayout(new GridLayout(1, 2));
		sateBoardLeft.setLayout(new GridLayout(3, 1));
		sateBoardRight.setLayout(new GridLayout(3, 1));
		scoreBoard.setLayout(new GridLayout(1, 2));

		// 패널 추가
		this.add(sateBoard);
		this.add(scoreBoard);

		sateBoardRight.add(nextShapeSmall1);
		sateBoardRight.add(nextShapeSmall2);
		sateBoardRight.add(nextShapeSmall3);

		sateBoardLeft.add(player1nextShapeSmall1);
		sateBoardLeft.add(player1nextShapeSmall2);
		sateBoardLeft.add(player1nextShapeSmall3);

		sateBoard.add(sateBoardLeft);
		sateBoard.add(sateBoardRight);
		
		scoreBoard.add(player1Stack);
		scoreBoard.add(player2Stack);
		
		player2Stack.setHorizontalAlignment(SwingConstants.RIGHT);

	}

	public void now2Piece(Queue<Shape> queue) {
		this.queue = queue;
		for (int i = 0; i < 5; i++) {
			next2Pie[i] = this.queue.poll();
			this.queue.offer(next2Pie[i]);
		}
		repaint();
	}

	public void now1Piece(Queue<Shape> queue) {
		this.queue = queue;
		for (int i = 0; i < 5; i++) {
			next1Pie[i] = this.queue.poll();
			this.queue.offer(next1Pie[i]);
		}
		repaint();
	}

	
	int squareWidth() {
		return (int) getSize().getWidth() / BoardWidth;

	}

	int squareHeight() {
		return (int) getSize().getHeight() / BoardHeight;
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();
		Shape tmp = next2Pie[0];
		tmp = next2Pie[0];
		for (int i = 0; i < 4; ++i) {
			int x = 9 + tmp.x(i);
			int y = 10 - tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
					tmp.getShape());
		}

		tmp = next2Pie[1];
		for (int i = 0; i < 4; ++i) {
			int x = 9 + tmp.x(i);
			int y = 13- tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
					tmp.getShape());
		}
		tmp = next2Pie[2];
		for (int i = 0; i < 4; ++i) {
			int x = 9 + tmp.x(i);
			int y = 16 - tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
					tmp.getShape());
		}
		tmp = next1Pie[0];
		for (int i = 0; i < 4; ++i) {
			int x = 3 + tmp.x(i);
			int y = 10 - tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
					tmp.getShape());
		}
		tmp = next1Pie[1];
		for (int i = 0; i < 4; ++i) {
			int x = 3 + tmp.x(i);
			int y = 13 - tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
					tmp.getShape());
		}
		tmp = next1Pie[2];
		for (int i = 0; i < 4; ++i) {
			int x = 3 + tmp.x(i);
			int y = 16 - tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
					tmp.getShape());
		}

	}
	// drawSquare(g,100,200,nextPie.getShape());

	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
				new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204),
				new Color(218, 170, 0), new Color(192, 192, 192) };

		Color color = colors[shape.ordinal()];

		g.setColor(color);
		g.fillRect(x + 1, y + 1, 15 - 2, 19 - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + 19 - 1, x, y);
		g.drawLine(x, y, x + 15 - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + 19 - 1, x + 15 - 1, y + 19 - 1);
		g.drawLine(x + 15 - 1, y + 19 - 1, x + 15 - 1, y + 1);
	}

}
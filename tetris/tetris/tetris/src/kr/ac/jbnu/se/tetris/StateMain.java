package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.apache.commons.lang3.StringUtils;

public class StateMain extends JPanel {

	final int BoardWidth = 13; // 가로 10칸
	final int BoardHeight = 18; // 세로 10칸

	private HashMap<Integer, String> rankList = new HashMap<Integer, String>();
	private ArrayList<String> userList = new ArrayList<String>();
	private ArrayList<Integer> userScore = new ArrayList<Integer>();
	private ArrayList<Integer> userScoreTmp = new ArrayList<Integer>();
	public String scoreAll = "<html>";
	public JPanel scoreBoard = new JPanel();;
	private JPanel sateBoard = new JPanel();
	public JPanel currentScore = new JPanel();
	private JPanel nextShape = new JPanel();
	private JPanel sateBoardLeft = new JPanel();
	private JPanel sateBoardRight = new JPanel();
	private JPanel nextShapeSmall1 = new JPanel();
	private JPanel nextShapeSmall2 = new JPanel();
	private JPanel nextShapeSmall3 = new JPanel();
	public JLabel score[] = new JLabel[5];
//	public JLabel score = new JLabel("<html><br/><br/><br/><br/>실시간 점수판</html>");
	public JLabel level = new JLabel("<html><br/>1</html>");;
	private Tetris tetris;
	private Record record = new Record();
	private Shape[] nextPie = new Shape[5];

	private Queue<Shape> queue = new LinkedList<Shape>();

	public StateMain() {
		// setScoreBoard();
	}

	public StateMain(Tetris tetris) {
		this.tetris = tetris;
		initialize();
		setSize(200, 400);
		// setScoreBoard();
		String record = this.record.load();
		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - frameSize.width) / 2 + 185, (screenSize.height - frameSize.height) / 2 - 14);
	}

	int squareWidth() {
		return (int) getSize().getWidth() / BoardWidth;

	}

	int squareHeight() {
		return (int) getSize().getHeight() / BoardHeight;
	}

	private void initialize() {

		// 테두리 설정

		scoreBoard.setBorder(new LineBorder(Color.red, 1));
		sateBoard.setBorder(new LineBorder(Color.red, 1));
		currentScore.setBorder(new LineBorder(Color.red, 1));
		nextShape.setBorder(new LineBorder(Color.red, 1));
		sateBoardLeft.setBorder(new LineBorder(Color.red, 1));
		sateBoardRight.setBorder(new LineBorder(Color.red, 1));
		nextShapeSmall1.setBorder(new LineBorder(Color.red, 1));
		nextShapeSmall2.setBorder(new LineBorder(Color.red, 1));
		nextShapeSmall3.setBorder(new LineBorder(Color.red, 1));

		// Layout 설정
		scoreBoard.setLayout(new GridLayout(5, 1));
		setLayout(new GridLayout(2, 1));
		sateBoard.setLayout(new GridLayout(1, 2));
		sateBoardLeft.setLayout(new GridLayout(2, 1));
		sateBoardRight.setLayout(new GridLayout(3, 1));

		// 패널 추가
		this.add(sateBoard);
		this.add(scoreBoard);

		sateBoardRight.add(nextShapeSmall1);
		sateBoardRight.add(nextShapeSmall2);
		sateBoardRight.add(nextShapeSmall3);

		sateBoardLeft.add(currentScore);
		sateBoardLeft.add(nextShape);

		sateBoard.add(sateBoardLeft);
		sateBoard.add(sateBoardRight);

		// 요소 추가
		for (int i = 0; i < 5; i++) {
			score[i] = new JLabel("");
			scoreBoard.add(score[i]);
		}
		currentScore.add(level, SwingConstants.CENTER);

		// 요소 값 변경
		level.setFont(new Font("돋움", Font.BOLD, 22));
		for (int i = 0; i < 5; i++) {
			score[i].setFont(new Font("돋움", Font.BOLD, 15));
		}
	}

	public void nowPiece(Queue<Shape> queue) {
		this.queue = queue;
		for (int i = 0; i < 5; i++) {
			nextPie[i] = this.queue.poll();
			this.queue.offer(nextPie[i]);
		}
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);
		Dimension size = getSize();
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

		Shape tmp = nextPie[0];
		for (int i = 0; i < 4; ++i) {
			int x = 3 + tmp.x(i);
			int y = 11 - tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(), tmp.getShape());
		}
		tmp = nextPie[1];
		for (int i = 0; i < 4; ++i) {
			int x = 9 + tmp.x(i);
			int y = 10 - tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(), tmp.getShape());
		}
		tmp = nextPie[2];
		for (int i = 0; i < 4; ++i) {
			int x = 9 + tmp.x(i);
			int y = 13 - tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(), tmp.getShape());
		}
		tmp = nextPie[3];
		for (int i = 0; i < 4; ++i) {
			int x = 9 + tmp.x(i);
			int y = 16 - tmp.y(i);
			drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(), tmp.getShape());
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

	public String showCurrentRank(int score) {

		rankList = this.record.getHash();
		userList = this.record.getUser();
		userScore = this.record.getScore();

		userScore.add(score);
		rankList.put(score, "YOU");
		Collections.sort(userScore);
		Collections.reverse(userScore);

		int size = userScore.size();
		if (size > 5) {
			size = 5;
		}
		for (int i = 0; i < size; i++) {
			if (i == userScore.indexOf(score)) {
				this.score[i].setForeground(new Color(255, 0, 0));
			} else {
				this.score[i].setForeground(new Color(0, 0, 0));
			}
			String data = StringUtils.rightPad(rankList.get(userScore.get(i)), 10) + ":"
					+ (Integer.toString((userScore.get(i) / 100) * 100)) + "\r\n";
			this.score[i].setText(data);
		}

		rankList.remove(score);
		userScore.remove(userScore.indexOf(score));

		return scoreAll;

	}

}
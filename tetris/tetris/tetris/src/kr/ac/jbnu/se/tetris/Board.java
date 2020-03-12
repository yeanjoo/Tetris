package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	final private int BoardWidth = 10; // ���� 10ĭ
	final private int BoardHeight = 22; // ���� 10ĭ

	// ������� ȿ����
	Sound backgroundMusic = new Sound(true);
	Sound soundEffect = new Sound(false);

	// ���
	Record record = new Record();

	Timer timer;
	boolean isFallingFinished = false;
	boolean isStarted = false;
	boolean isPaused = false;
	boolean isReverse = false;
	boolean isconfigReverse = false;
	private int is2Player;

	private int upBlockLevel = 10000;
	private int reverseLevel = 20000;
	private int level = 1;
	private int top = 0;
	private int numLinesRemoved = 0;
	// ���ó�� ���������³��߿�
	public int curX = 0;
	public int curY = 0;
	public int guideX = 0;
	public int guideY = 0;
	JLabel statusbar;
	JLabel statusbar1;
	JLabel statusbar2;

	Board enemy;
	private int stack;

	Shape nextPiece;
	Shape curPiece;
	Shape guidePiece;
	Tetrominoes[] board;
	Tetrominoes[] cpBoard = new Tetrominoes[BoardWidth * BoardHeight];
	Tetris tetris;

	Queue<Shape> queue = new LinkedList<Shape>();

	public Board() {
	}

	public Board(Tetris tetris, int x) {
		is2Player = x;
		curPiece = new Shape();
		guidePiece = new Shape();
		timer = new Timer(400, this); // why this?
		timer.start(); // ??
		if (is2Player == 0) {
			statusbar = tetris.getStatusBar();
		} else if (is2Player == 1) {
			statusbar1 = tetris.getStatus1Bar();
		} else {
			statusbar2 = tetris.getStatus2Bar();
		}
		board = new Tetrominoes[BoardWidth * BoardHeight];
		this.tetris = tetris;

		for (int i = 0; i < 5; i++) {
			queue.offer(new Shape(1));
		}
		this.setFocusable(true);
		clearBoard();
	}

	public Board(Tetris tetris, int x, Board board) {
		is2Player = x;
		curPiece = new Shape();
		enemy = board;
		guidePiece = new Shape();

		timer = new Timer(400, this);// Ÿ�̸�
		timer.start();

		if (is2Player == 0) {
			statusbar = tetris.getStatusBar();
		} else if (is2Player == 1) {
			statusbar1 = tetris.getStatus1Bar();
		} else {
			statusbar2 = tetris.getStatus2Bar();
		}
		this.board = new Tetrominoes[BoardWidth * BoardHeight];

		this.tetris = tetris;

		for (int i = 0; i < 5; i++) {
			queue.offer(new Shape(1));
		}

		this.setFocusable(true);
		clearBoard();
	}

	public void getBoard(Board board) {
		this.enemy = board;
	}

	public void actionPerformed(ActionEvent e) {

		if (isFallingFinished) {
			isFallingFinished = false;
			newPiece();
		} else {
			oneLineDown();
		}
	}

	int squareWidth() {
		return (int) getSize().getWidth() / BoardWidth;

	}

	int squareHeight() {
		return (int) getSize().getHeight() / BoardHeight;
	}

	Tetrominoes shapeAt(int x, int y) {

		return board[(y * BoardWidth) + x]; // board �� ������� X ������ �þ�鼭 �����ప�� 2���� ����

	}

	Tetrominoes shapeAtReverse(int x, int y) {
		return cpBoard[(y * BoardWidth) + x]; // board �� ������� X ������ �þ�鼭 �����ప�� 2���� ����

	}

	public void start() {
		if (isPaused)
			return;

		isStarted = true;
		isFallingFinished = false;
		numLinesRemoved = 0;
		upBlockLevel = 10000;
		reverseLevel = 20000;
		level = 1;

		if (is2Player == 0) {
			statusbar.setText(String.valueOf(numLinesRemoved));
		} else if (is2Player == 1) {
			statusbar1.setText(String.valueOf(numLinesRemoved));
		} else if (is2Player == 2) {
			statusbar2.setText(String.valueOf(numLinesRemoved));
		}

		clearBoard();
		backgroundMusic.Play("sound/test.wav");
		backgroundMusic.setSound((float) Configuration.backSoundVoulme);
		if (is2Player == 0) {
			tetris.statemain.showCurrentRank(numLinesRemoved);
		}
		newPiece();
		timer.setDelay(400);
		timer.start();
	}

	public static <T> void reverse(Tetrominoes[] cpBoard) {
		Collections.reverse(Arrays.asList(cpBoard));
	}

	public void pause() {
		if (!isStarted)
			return;

		isPaused = !isPaused;

		if (isPaused) {

			timer.stop();
			backgroundMusic.Stop();

			if (is2Player == 0) {
				statusbar.setText("paused");
			} else if (is2Player == 1) {
				statusbar1.setText("paused");
			} else if (is2Player == 2) {
				statusbar2.setText("paused");
			}

		} else {

			timer.start();
			backgroundMusic.Keep();

			if (is2Player == 0) {
				statusbar.setText(String.valueOf(numLinesRemoved));
			} else if (is2Player == 1) {
				statusbar1.setText(String.valueOf(numLinesRemoved));
			} else if (is2Player == 2) {
				statusbar2.setText(String.valueOf(numLinesRemoved));
			}
		}
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);

		Dimension size = getSize();

		int backGroundColor;
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

		backGroundColor = (top * 10) + 20;// ��׶��� �÷��ٲٱ�
		g.setColor(new Color(backGroundColor, 0, 0));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		System.arraycopy(board, 0, cpBoard, 0, board.length);// �迭����
		reverse(cpBoard);

		for (int i = 0; i < BoardHeight; ++i) {

			for (int j = 0; j < BoardWidth; ++j) {

				if (isReverse == true) {

					Tetrominoes shape = shapeAtReverse(j, BoardHeight - i - 1);

					if (shape != Tetrominoes.NoShape)
						drawSquare(g, 0 + j * squareWidth(), i * squareHeight() + boardTop, shape);
				} else {
					Tetrominoes shape = shapeAt(j, BoardHeight - i - 1);
					if (shape != Tetrominoes.NoShape)
						drawSquare(g, 0 + j * squareWidth(), i * squareHeight() + boardTop, shape);
				} // <- ���⵵ �ߺ� ���� �� ���� ������

			}
		}

		if (curPiece.getShape() != Tetrominoes.NoShape) {

			int x;
			int y;

			for (int i = 0; i < 4; ++i) {

				if (isReverse == true) {
					x = BoardWidth - 1 - (curX + curPiece.x(i));
					y = BoardHeight - 1 - (curY - curPiece.y(i));
				} else {
					x = curX + curPiece.x(i);
					y = curY - curPiece.y(i);
				}

				drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						curPiece.getShape());
			}
		}

		// ���̵� ��ǥ ����ִ� ���ϲ��� �Ƹ�...
		if (guidePiece.getShape() != Tetrominoes.NoShape) {

			int x;
			int y;

			for (int i = 0; i < 4; ++i) {

				if (isReverse == true) {
					x = BoardWidth - 1 - (guideX + guidePiece.x(i));
					y = BoardHeight - 1 - (guideY - guidePiece.y(i));
				} else {
					x = guideX + guidePiece.x(i);
					y = guideY - guidePiece.y(i);// ���̵���ǥ ����ִ°�
				}

				drawSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						guidePiece.getShape());

			}
		}
	}

	public void dropDown() {
		int newY = curY;
		while (newY > 0) {
			if (!tryMove(curPiece, curX, newY - 1)) {

				break;
			}
			--newY;
		}
		pieceDropped();
	}

	public void guideDown() {
		int newY = curY;
		while (newY > 0) {
			if (!guideMove(guidePiece, curX, newY - 1)) {
				break;
			}
			--newY;
		}
		repaint();
	}

	public void oneLineDown() {
		if (!tryMove(curPiece, curX, curY - 1)) // �Ʒ��� ������
			pieceDropped(); // �Լ� ȣ��
	}

	private void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i) // ��� ������ ���鼭
			board[i] = Tetrominoes.NoShape; // NoShape ���� ��ȯ��
	}

	private void pieceDropped() {
		int x;
		int y;
		for (int i = 0; i < 4; ++i) { // 4���� ���� Ȯ��
			x = curX + curPiece.x(i); // 0,0 ���� ��ġ���� 4������ġ Ȯ�� X ��
			y = curY - curPiece.y(i); // 0,0 ���� ��ġ���� 4������ġ Ȯ�� Y ��
			board[(y * BoardWidth) + x] = curPiece.getShape(); // �� ������ ���� �ٲ���
		}

		removeFullLines();
		findTop();
		if (!isFallingFinished)
			newPiece();

	}

	// private void send(Shape ne) {
	// tetris.statemain.nowPiece(ne);
	// }

	private void send(Queue<Shape> queue) {
		if (is2Player == 0) {
			tetris.statemain.nowPiece(queue);
		} else if (is2Player == 1) {
			tetris.statemenu.now1Piece(queue);
		} else if (is2Player == 2) {
			tetris.statemenu.now2Piece(queue);
		}
	}

	private void newPiece() {
		curPiece.setShape(queue.poll().getShape());
		queue.offer(new Shape(1));
		send(queue);

		curX = BoardWidth / 2; // ���� X ��ǥ
		curY = BoardHeight - 1 + curPiece.minY(); // ���� Y ��ǥ
		guidePiece = curPiece;
		guideDown();

		if (!tryMove(curPiece, curX, curY)) { // ������ �� ������
			curPiece.setShape(Tetrominoes.NoShape); // ���� ������ NoShape�� �ٲ�
			timer.stop(); // �ð��� ���߰�
			isStarted = false; // ���� ���¸� false�� �ٲٰ�

			if (is2Player == 0) {
				String name = JOptionPane.showInputDialog(null, "�̸��� �Է��ϼ���.", "���", JOptionPane.PLAIN_MESSAGE);
				if (name == null || name == "")
					name = "user";

				try {
					record.reNewRecord(name, numLinesRemoved);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (is2Player == 0) {
				statusbar.setText("game over"); // ���� ����
			} else if (is2Player == 1) {
				statusbar1.setText("game over"); // ���� ����
				enemy.statusbar2.setText("game over");
				takeGameOver();
			} else if (is2Player == 2) {
				statusbar2.setText("game over"); // ���� ����
				enemy.statusbar1.setText("game over");
				takeGameOver();
			}
		}
	}

	public void takeGameOver() {
		enemy.timer.stop(); // �ð��� ���߰�
		enemy.isStarted = false; // ���� ���¸� false�� �ٲٰ�

		if (is2Player == 1) {
			enemy.statusbar2.setText("You Win");
		}
		if (is2Player == 2) {
			enemy.statusbar1.setText("You Win");
		}
	}

	public boolean tryMove(Shape newPiece, int newX, int newY) { // true false ���� ���ϱ⵵ ������ �� ���µ�
																	// ������//guideShape�� �μ��� �߰��ϸ� ���
		for (int i = 0; i < 4; ++i) { // ���� 4���� ���°� ���� �����̱� ����
			int x = newX + newPiece.x(i); // ������ �������� X�� �̵�
			int y = newY - newPiece.y(i); // ������ �������� Y�� �̵�

			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) // �����ϼ� ���� ������ ��� false ��ȯ
				return false;

			if (shapeAt(x, y) != Tetrominoes.NoShape) // Ȯ���� ������ NoShape�� �ƴ� ��� false ��ȯ
				return false;
		}

		curPiece = newPiece; // ���ο� ������ �� �� ��������
		curX = newX; // ���ο� X���� �� X������
		curY = newY; // ���ο� Y���� �� Y������
		guideDown();
		repaint(); // paint()�� �ٽ� ����

		return true; // false�� ��ȯ���� ���� ��쿡 true ��ȯ
	}

	public boolean guideMove(Shape newPiece, int newX, int newY) { // true false ���� ���ϱ⵵ ������ �� ���µ� ������
		for (int i = 0; i < 4; ++i) { // ���� 4���� ���°� ���� �����̱� ����
			int x = newX + newPiece.x(i); // ������ �������� X�� �̵�
			int y = newY - newPiece.y(i); // ������ �������� Y�� �̵�
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) // �����ϼ� ���� ������ ��� false ��ȯ
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape) // Ȯ���� ������ NoShape�� �ƴ� ��� false ��ȯ
				return false;
		}
		guidePiece = newPiece; // ���ο� ������ �� �� ��������
		guideX = newX; // ���ο� X���� �� X������
		guideY = newY; // ���ο� Y���� �� Y������
		repaint(); // paint()�� �ٽ� ����
		return true; // false�� ��ȯ���� ���� ��쿡 true ��ȯ
	}

	private void findTop() {
		for (int i = BoardHeight - 1; i >= 0; --i) {
			for (int j = 0; j < BoardWidth; ++j) {
				if (shapeAt(j, i) != Tetrominoes.NoShape) {
					top = i;
					i = 0;
					break;
				}
			}
		}
	}

	public void createNewLines() {
		for (int i = BoardHeight - 1; i > 0; --i) {
			for (int j = 0; j < BoardWidth; ++j)
				board[(i * BoardWidth) + j] = shapeAt(j, i - 1);
		}
		for (int j = 0; j < BoardWidth; j++) {
			board[j] = Tetrominoes.HardShape;
		}
		Random r = new Random();
		int x = Math.abs(r.nextInt()) % BoardWidth + 1;
		board[x] = Tetrominoes.NoShape;
	}

	private void sendLevel() {
		if (is2Player == 0)
			tetris.statemain.level.setText(Integer.toString(level));
	}

	private void sendScore() {
		if (is2Player == 0)
			tetris.statemain.showCurrentRank(numLinesRemoved);
	}

	private void removeFullLines() {
		int numFullLines = 0;

		for (int i = BoardHeight - 1; i >= 0; --i) { // ���ΰ� (22) ���� (0) ����
			boolean lineIsFull = true; // flag true (�ϼ��� ����)

			for (int j = 0; j < BoardWidth; ++j) { // ���ΰ�(0) ���� (10) ����
				if (shapeAt(j, i) == Tetrominoes.NoShape) { // ���� �� ���� NoShape �̸�
					lineIsFull = false; // flag false (�ϼ��� ����)
					break; // �ƴ� ��� �ٷ� ��������.
				}
			}

			if (lineIsFull) { // ���� flag �� true �ϼ��� ������ ���� ���

				++numFullLines; // �ϼ��� ���� �� ++
				for (int k = i; k < BoardHeight - 1; ++k) { // ��ĭ �����ֱ� ���� K���� ���� Ȯ�� i ������ ū ��ġ�϶�
					for (int j = 0; j < BoardWidth; ++j) // ����� ĭ�� ���鼭
						board[(k * BoardWidth) + j] = shapeAt(j, k + 1); // �� ��ġ�� ���� �ϳ��� ����
				}
			}
		}

		if (numFullLines >= 3) {
			numLinesRemoved += 1000 * (numFullLines - 2);
		}

		if (numFullLines > 0) { // ���� ��ĭ�̶� �������ٸ�
			numLinesRemoved += numFullLines * 1000; // ���� ������ +
			sendScore();

			if (is2Player != 0) {
				if (is2Player == 1) {
					stack += numFullLines;
					if (stack >= 4) {
						stack -= 4;
						enemy.createNewLines();
					}
					String tmp = "";
					for (int i = 0; i < stack; i++) {
						tmp += "X";
					}
					tetris.statemenu.player1Stack.setText(tmp);
				}
				if (is2Player == 2) {
					stack += numFullLines;
					if (stack >= 4) {
						stack -= 4;
						enemy.createNewLines();
					}
					String tmp = "";
					for (int i = 0; i < stack; i++) {
						tmp += "X";
					}
					tetris.statemenu.player2Stack.setText(tmp);

				}
			}

			if (numLinesRemoved / reverseLevel >= 1 && Configuration.isReverse == false) {// ������
				reverseLevel += 20000;
				if (!isReverse)
					isReverse = true;
				else
					isReverse = false;
			}

			if (numLinesRemoved / 2000 > level) {
				level = numLinesRemoved / 2000;
				sendLevel();
				if (400 - level * 5 != 0) {
					timer.setDelay(400 - level * 5);
				}

			}
			if (numLinesRemoved / upBlockLevel >= 1) {
				upBlockLevel += 10000;
				if (upBlockLevel > 50000) {
					createNewLines();
				}
				createNewLines();
			}

			soundEffect.Play("sound/test_1.wav");
			soundEffect.setSound((float) Configuration.effectSoundVolume); // ���� ����

			if (is2Player == 0) {
				statusbar.setText(String.valueOf(numLinesRemoved)); // ���¹ٿ� text �ٽ� ����
			} else if (is2Player == 1) {
				statusbar1.setText(String.valueOf(numLinesRemoved)); // ���¹ٿ� text �ٽ� ����
			} else if (is2Player == 2) {
				statusbar2.setText(String.valueOf(numLinesRemoved)); // ���¹ٿ� text �ٽ� ����
			}
			isFallingFinished = true; // �� ������ ���� True (why?)
			curPiece.setShape(Tetrominoes.NoShape); // �� ���� NoShape ?
			repaint();
		}

	}

	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
				new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204),
				new Color(218, 170, 0), new Color(192, 192, 192) };

		Color color = colors[shape.ordinal()];

		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight() - 1, x, y);
		g.drawLine(x, y, x + squareWidth() - 1, y);

		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight() - 1, x + squareWidth() - 1, y + squareHeight() - 1);
		g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1, x + squareWidth() - 1, y + 1);

	}
}
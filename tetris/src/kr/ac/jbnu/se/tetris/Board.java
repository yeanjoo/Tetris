package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

	final int BoardWidth = 10; // ���� 10ĭ
	final int BoardHeight = 22; // ���� 10ĭ

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
	boolean is2Player = false;

	int upBlockLevel = 10000;
	int reverseLevel = 20000;
	int level = 1;
	int top = 0;
	int numLinesRemoved = 0;
	int curX = 0;
	int curY = 0;
	int guideX = 0;
	int guideY = 0;
	JLabel statusbar;
	Shape curPiece;
	Shape guidePiece;
	Shape nextPiece;
	NextPiece Preview;
	ArrayList<Shape> list = new ArrayList<Shape>();
	Tetrominoes[] board;
	Tetrominoes[] cpBoard = new Tetrominoes[BoardWidth * BoardHeight];
	StateMain statemain = new StateMain();

	public Board(Tetris tetris) {
		curPiece = new Shape();
		guidePiece = new Shape();
		setPiece();
		Preview= new NextPiece(list);//�ʱⰪ�� ������
		timer = new Timer(400, this); // why this?
		timer.start(); // ??
		statusbar = tetris.getStatusBar();
		board = new Tetrominoes[BoardWidth * BoardHeight];
		this.setFocusable(true);
		addKeyListener(new TAdapter());
		clearBoard();
	}

	private void setPiece() {
	      for(int i=0;i<4;i++) {
	         nextPiece.setRandomShape();
	         list.add(nextPiece);
	      }
	}//�������� �ǽ��� ����� ����Ʈ�� �߰�

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
		statusbar.setText(String.valueOf(numLinesRemoved));
		clearBoard();

		backgroundMusic.Play("sound/test.wav");

		newPiece();
		timer.setDelay(400);
		timer.start();
	}

	public static <T> void reverse(Tetrominoes[] cpBoard) {
		Collections.reverse(Arrays.asList(cpBoard));
	}

	private void pause() {
		if (!isStarted)
			return;

		isPaused = !isPaused;
		if (isPaused) {
			timer.stop();
			backgroundMusic.Stop();
			statusbar.setText("paused");
		} else {
			timer.start();
			backgroundMusic.Keep();
			statusbar.setText(String.valueOf(numLinesRemoved));
		}
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);

		Dimension size = getSize();

		int backGroundColor;
		backGroundColor = (top * 10) + 20;
		g.setColor(new Color(backGroundColor, 0, 0));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();
		System.arraycopy(board, 0, cpBoard, 0, board.length);
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
				}

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

		if (guidePiece.getShape() != Tetrominoes.NoShape) {
			int x;
			int y;
			for (int i = 0; i < 4; ++i) {
				if (isReverse == true) {
					x = BoardWidth - 1 - (guideX + guidePiece.x(i));
					y = BoardHeight - 1 - (guideY - guidePiece.y(i));
				} else {
					x = guideX + guidePiece.x(i);
					y = guideY - guidePiece.y(i);
				}
				drawGuideSquare(g, 0 + x * squareWidth(), boardTop + (BoardHeight - y - 1) * squareHeight(),
						guidePiece.getShape());
			}
		}
	}

	private void dropDown() {
		int newY = curY;
		while (newY > 0) {
			if (!tryMove(curPiece, curX, newY - 1)) {

				break;
			}
			--newY;
		}
		pieceDropped();
	}

	private void guideDown() {
		int newY = curY;
		while (newY > 0) {
			if (!guideMove(guidePiece, curX, newY - 1)) {
				break;
			}
			--newY;
		}
		repaint();
	}

	private void oneLineDown() {
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

	private void newPiece() {
		
		curPiece.setRandomShape(); // ���ο� �ǽ��� �����Լ��� ����
		list.add(curPiece);//�װ� ����Ʈ�� �־��ְ� �� �ڿ� �ڵ����� �־���
		curPiece=list.get(0);//ù��° �� �ڵ����� ������ �о��ֱ� ������
		list.remove(0); //ù��° �� �����ֱ�
		Preview.putNextPiece(list);//���� �ǽ��� ������ ���ο� ����Ʈ�� �����信 �ִ´�
		
		curX = BoardWidth / 2; // ���� X ��ǥ
		curY = BoardHeight - 1 + curPiece.minY(); // ���� Y ��ǥ
		guidePiece = curPiece;
		guideDown();
		
		if (!tryMove(curPiece, curX, curY)) { // ������ �� ������
			curPiece.setShape(Tetrominoes.NoShape); // ���� ������ NoShape�� �ٲ�
			timer.stop(); // �ð��� ���߰�
			isStarted = false; // ���� ���¸� false�� �ٲٰ�
			String name = JOptionPane.showInputDialog(null, "�̸��� �Է��ϼ���.", "���", JOptionPane.PLAIN_MESSAGE);
			if (name == null || name == "")
				name = "user";

			try {
				record.reNewRecord(name, numLinesRemoved);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			statusbar.setText("game over"); // ���� ����
		}
	}

	private boolean tryMove(Shape newPiece, int newX, int newY) { // true false ���� ���ϱ⵵ ������ �� ���µ� ������
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

	private boolean guideMove(Shape newPiece, int newX, int newY) { // true false ���� ���ϱ⵵ ������ �� ���µ� ������
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

	private void createNewLines() {
		for (int i = BoardHeight - 1; i > 0; --i) { // ���ΰ� (22) ���� (0) ����
			for (int j = 0; j < BoardWidth; ++j) // ���ĭ
				board[(i * BoardWidth) + j] = shapeAt(j, i - 1); // �� ��ġ�� ���� �ϳ��� �ø�
		}
		for (int j = 0; j < BoardWidth; j++) {
			board[j] = Tetrominoes.HardShape;
		}
		Random r = new Random();
		int x = Math.abs(r.nextInt()) % BoardWidth + 1; // 8���� �� 1 ����
		board[x] = Tetrominoes.NoShape;
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
			// statemain.showCurrentRank(numLinesRemoved);

			if (numLinesRemoved / reverseLevel >= 1) {
				reverseLevel += 20000;
				if (!isReverse)
					isReverse = true;
				else
					isReverse = false;
			}

			if (numLinesRemoved / 2000 > level) {
				level = numLinesRemoved / 2000;
//				statemain.level.setText(String.valueOf(numLinesRemoved));
//				statemain.setLevel(level);
				if (400 - level * 10 != 0) {
					timer.setDelay(400 - level * 1);
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
			statusbar.setText(String.valueOf(numLinesRemoved)); // ���¹ٿ� text �ٽ� ����
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

	private void drawGuideSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color colors[] = { new Color(0, 0, 0, 122), new Color(204, 102, 102, 122), new Color(102, 204, 102, 122),
				new Color(102, 102, 204, 122), new Color(204, 204, 102, 122), new Color(204, 102, 204, 122),
				new Color(102, 204, 204, 122), new Color(218, 170, 0, 122) };

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

	class TAdapter extends KeyAdapter {
		public void keyPressed(KeyEvent e) {

			if (!isStarted || curPiece.getShape() == Tetrominoes.NoShape) {
				return;
			}

			int keycode = e.getKeyCode();

			if (keycode == 'p' || keycode == 'P') {
				pause();
				return;
			}

			if (isPaused)
				return;

			switch (keycode) {
			case KeyEvent.VK_LEFT:
				tryMove(curPiece, curX - 1, curY);
				break;
			case KeyEvent.VK_RIGHT:
				tryMove(curPiece, curX + 1, curY);
				break;
			case KeyEvent.VK_DOWN:
				tryMove(curPiece.rotateRight(), curX, curY);
				guideMove(guidePiece.rotateRight(), curX, curY);
				guideDown();
				break;
			case KeyEvent.VK_UP:
				tryMove(curPiece.rotateLeft(), curX, curY);
				guideMove(guidePiece.rotateLeft(), curX, curY);
				guideDown();
				break;
			case KeyEvent.VK_SPACE:
				dropDown();
				break;
			case KeyEvent.VK_0:
				start();
				backgroundMusic.Stop();
				timer.start();
				break;
			case 'd':
				oneLineDown();
				break;
			case 'D':
				oneLineDown();
				break;
			}

		}
	}
}
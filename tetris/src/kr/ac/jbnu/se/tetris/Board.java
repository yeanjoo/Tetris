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

	final int BoardWidth = 10; // 가로 10칸
	final int BoardHeight = 22; // 세로 10칸

	// 배경음악 효과음
	Sound backgroundMusic = new Sound(true);
	Sound soundEffect = new Sound(false);

	// 기록
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
		Preview= new NextPiece(list);//초기값을 보낸다
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
	}//랜덤으로 피스를 만들어 리스트에 추가

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

		return board[(y * BoardWidth) + x]; // board 가 순서대로 X 축으로 늘어나면서 세로축값이 2차로 증가

	}

	Tetrominoes shapeAtReverse(int x, int y) {
		return cpBoard[(y * BoardWidth) + x]; // board 가 순서대로 X 축으로 늘어나면서 세로축값이 2차로 증가

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
		if (!tryMove(curPiece, curX, curY - 1)) // 아래로 움직임
			pieceDropped(); // 함수 호출
	}

	private void clearBoard() {
		for (int i = 0; i < BoardHeight * BoardWidth; ++i) // 모든 공간을 돌면서
			board[i] = Tetrominoes.NoShape; // NoShape 으로 전환함
	}

	private void pieceDropped() {
		int x;
		int y;
		for (int i = 0; i < 4; ++i) { // 4개의 상태 확인
			x = curX + curPiece.x(i); // 0,0 기준 위치에서 4가지위치 확인 X 축
			y = curY - curPiece.y(i); // 0,0 기준 위치에서 4가지위치 확인 Y 축
			board[(y * BoardWidth) + x] = curPiece.getShape(); // 위 상태의 값을 바꿔줌
		}

		removeFullLines();
		findTop();
		if (!isFallingFinished)
			newPiece();

	}

	private void newPiece() {
		
		curPiece.setRandomShape(); // 새로운 피스를 랜덤함수로 생성
		list.add(curPiece);//그걸 리스트에 넣어주고 맨 뒤에 자동으로 넣어줌
		curPiece=list.get(0);//첫번째 값 자동으로 앞으로 밀어주기 때문에
		list.remove(0); //첫번째 값 지워주기
		Preview.putNextPiece(list);//현재 피스가 지워진 새로운 리스트를 프리뷰에 넣는다
		
		curX = BoardWidth / 2; // 시작 X 좌표
		curY = BoardHeight - 1 + curPiece.minY(); // 시작 Y 좌표
		guidePiece = curPiece;
		guideDown();
		
		if (!tryMove(curPiece, curX, curY)) { // 움직일 수 없으면
			curPiece.setShape(Tetrominoes.NoShape); // 현제 도형을 NoShape로 바꾸
			timer.stop(); // 시간을 멈추고
			isStarted = false; // 실행 상태를 false로 바꾸고
			String name = JOptionPane.showInputDialog(null, "이름을 입력하세요.", "기록", JOptionPane.PLAIN_MESSAGE);
			if (name == null || name == "")
				name = "user";

			try {
				record.reNewRecord(name, numLinesRemoved);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			statusbar.setText("game over"); // 게임 오버
		}
	}

	private boolean tryMove(Shape newPiece, int newX, int newY) { // true false 값을 비교하기도 하지만 현 상태도 움직임
		for (int i = 0; i < 4; ++i) { // 각각 4개의 상태가 같이 움직이기 위함
			int x = newX + newPiece.x(i); // 움직인 방향으로 X축 이동
			int y = newY - newPiece.y(i); // 움직인 방향으로 Y축 이동
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) // 움직일수 없는 방향일 경우 false 반환
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape) // 확인한 공간이 NoShape이 아닐 경우 false 반환
				return false;
		}

		curPiece = newPiece; // 새로운 도형이 이 현 도형으로
		curX = newX; // 새로운 X값이 현 X값으로
		curY = newY; // 새로운 Y값이 현 Y값으로
		guideDown();
		repaint(); // paint()를 다시 실행
		return true; // false를 반환하지 않을 경우에 true 반환
	}

	private boolean guideMove(Shape newPiece, int newX, int newY) { // true false 값을 비교하기도 하지만 현 상태도 움직임
		for (int i = 0; i < 4; ++i) { // 각각 4개의 상태가 같이 움직이기 위함
			int x = newX + newPiece.x(i); // 움직인 방향으로 X축 이동
			int y = newY - newPiece.y(i); // 움직인 방향으로 Y축 이동
			if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight) // 움직일수 없는 방향일 경우 false 반환
				return false;
			if (shapeAt(x, y) != Tetrominoes.NoShape) // 확인한 공간이 NoShape이 아닐 경우 false 반환
				return false;
		}
		guidePiece = newPiece; // 새로운 도형이 이 현 도형으로
		guideX = newX; // 새로운 X값이 현 X값으로
		guideY = newY; // 새로운 Y값이 현 Y값으로

		repaint(); // paint()를 다시 실행
		return true; // false를 반환하지 않을 경우에 true 반환
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
		for (int i = BoardHeight - 1; i > 0; --i) { // 세로값 (22) 에서 (0) 까지
			for (int j = 0; j < BoardWidth; ++j) // 모든칸
				board[(i * BoardWidth) + j] = shapeAt(j, i - 1); // 그 위치의 값을 하나씩 올림
		}
		for (int j = 0; j < BoardWidth; j++) {
			board[j] = Tetrominoes.HardShape;
		}
		Random r = new Random();
		int x = Math.abs(r.nextInt()) % BoardWidth + 1; // 8가지 중 1 선택
		board[x] = Tetrominoes.NoShape;
	}

	private void removeFullLines() {
		int numFullLines = 0;

		for (int i = BoardHeight - 1; i >= 0; --i) { // 세로값 (22) 에서 (0) 까지
			boolean lineIsFull = true; // flag true (완성된 라인)

			for (int j = 0; j < BoardWidth; ++j) { // 가로값(0) 에서 (10) 까지
				if (shapeAt(j, i) == Tetrominoes.NoShape) { // 만약 그 값이 NoShape 이면
					lineIsFull = false; // flag false (완성된 라인)
					break; // 아닐 경우 바로 빠져나옴.
				}
			}

			if (lineIsFull) { // 만약 flag 가 true 완성된 라인이 있을 경우

				++numFullLines; // 완성된 라인 수 ++
				for (int k = i; k < BoardHeight - 1; ++k) { // 한칸 내려주기 위해 K값을 통해 확인 i 값보다 큰 위치일때
					for (int j = 0; j < BoardWidth; ++j) // 비어질 칸을 돌면서
						board[(k * BoardWidth) + j] = shapeAt(j, k + 1); // 그 위치의 값을 하나씩 내림
				}
			}
		}

		if (numFullLines >= 3) {
			numLinesRemoved += 1000 * (numFullLines - 2);
		}

		if (numFullLines > 0) { // 만약 한칸이라도 지워졌다면
			numLinesRemoved += numFullLines * 1000; // 지운 갯수에 +
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
			statusbar.setText(String.valueOf(numLinesRemoved)); // 상태바에 text 다시 정리
			isFallingFinished = true; // 다 떨어진 상태 True (why?)
			curPiece.setShape(Tetrominoes.NoShape); // 현 상태 NoShape ?
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
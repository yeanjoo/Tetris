package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

//StateMain Frame으로 넣을 것
public class NextPiece extends JPanel {
	Tetrominoes board;
	Shape nextPiece;
	final int BoardWidth = 3; // 가로 3칸
	final int BoardHeight = 3; // 세로 3칸
	private int x = 0;
	private int y = 0;
	JPanel panel[] = new JPanel[3];
	JFrame frame;
	ArrayList<Shape> array = new ArrayList<Shape>();
	StateMain main = new StateMain();
	Container contain = new Container();
//	NextPiece piece = new NextPiece(); 안되면 이것 자체의 객체를 생성해서 넣어보기

	// 생성자..!
	public NextPiece(ArrayList<Shape> array) {
		this.setBorder(new LineBorder(Color.red, 1));
		for (int i = 1; i < array.size(); i++) {// 첫번째 값만 지워지면 자동으로 알아서 1번째 값이 0번째가 된다 처음에 첫번째 값은 현재 도형이기 때문에 지워주고 시작
			this.array.add(i, array.get(i));// arrayList에다가 넣어주기
			nextPiece = array.get(i);// nextPiece에다가 arrayList에서 가져온 값 넣어주기

//			main.nextShapeSmall1.repaint();// 이렇게 외부에서 패널으르 가져와서 그릴 수 있는지를 모르겠다...
			panel[i].repaint();//이게 될지 모르겠다. 안되면 일일히 panel[i].좌표 이렇게도 함 해보기
			main.add(this);// 그냥 현재 클래스자체에 그려서 main에 넣어버리기 이러면 안될 것 같은데...
//			main.nextShapeSmall1.add(this);// 굳이 해야하나...?
			// 처음에만 이렇게 3개 더해주기..! 해당 패널이 하나려나....?

		}
	}

	public NextPiece(StateMain main) {
		this.main = main;
	}

	public void putNextPiece(ArrayList<Shape> array) {// 다시 넣기 즉 값 갱신
		for (int i = 0; i < array.size() - 1; i++) {// 프리뷰에서는 3개만
			this.array.add(i, array.get(i));// arrayList에다가 넣어주기
			nextPiece = array.get(i);// nextPiece에다가 arrayList에서 가져온 값 넣어주기
			repaint();// 3개가 다시 그려질까...?
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < 4; i++) {// 해당Panel에 그려줄것
			int x = nextPiece.x(i) + 1;
			int y = nextPiece.y(i) + 1;// 3x3 패널에 그려질 것
//	         int x =  2 + nextPiece.x(i);
//	         int y = 20 - nextPiece.y(i);
			drawSquare(g, x * squareWidth(), (y) * squareHeight(), nextPiece.getShape());
//			 (BoardHeight - y - 1) * squareHeight(),

		}
	}

	private void drawSquare(Graphics g, int x, int y, Tetrominoes shape) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102), new Color(102, 204, 102),
				new Color(102, 102, 204), new Color(204, 204, 102), new Color(204, 102, 204), new Color(102, 204, 204),
				new Color(218, 170, 0) };

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

	int squareWidth() {// 한칸 넓이 길이
		return (int) getSize().getWidth() / BoardWidth;
	}

	int squareHeight() {// 한칸 높이 길이
		return (int) getSize().getHeight() / BoardHeight;
	}

}
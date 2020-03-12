package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

//StateMain Frame���� ���� ��
public class NextPiece extends JPanel {
	Tetrominoes board;
	Shape nextPiece;
	final int BoardWidth = 3; // ���� 3ĭ
	final int BoardHeight = 3; // ���� 3ĭ
	private int x = 0;
	private int y = 0;
	JPanel panel[] = new JPanel[3];
	JFrame frame;
	ArrayList<Shape> array = new ArrayList<Shape>();
	StateMain main = new StateMain();
	Container contain = new Container();
//	NextPiece piece = new NextPiece(); �ȵǸ� �̰� ��ü�� ��ü�� �����ؼ� �־��

	// ������..!
	public NextPiece(ArrayList<Shape> array) {
		this.setBorder(new LineBorder(Color.red, 1));
		for (int i = 1; i < array.size(); i++) {// ù��° ���� �������� �ڵ����� �˾Ƽ� 1��° ���� 0��°�� �ȴ� ó���� ù��° ���� ���� �����̱� ������ �����ְ� ����
			this.array.add(i, array.get(i));// arrayList���ٰ� �־��ֱ�
			nextPiece = array.get(i);// nextPiece���ٰ� arrayList���� ������ �� �־��ֱ�

//			main.nextShapeSmall1.repaint();// �̷��� �ܺο��� �г����� �����ͼ� �׸� �� �ִ����� �𸣰ڴ�...
			panel[i].repaint();//�̰� ���� �𸣰ڴ�. �ȵǸ� ������ panel[i].��ǥ �̷��Ե� �� �غ���
			main.add(this);// �׳� ���� Ŭ������ü�� �׷��� main�� �־������ �̷��� �ȵ� �� ������...
//			main.nextShapeSmall1.add(this);// ���� �ؾ��ϳ�...?
			// ó������ �̷��� 3�� �����ֱ�..! �ش� �г��� �ϳ�����....?

		}
	}

	public NextPiece(StateMain main) {
		this.main = main;
	}

	public void putNextPiece(ArrayList<Shape> array) {// �ٽ� �ֱ� �� �� ����
		for (int i = 0; i < array.size() - 1; i++) {// �����信���� 3����
			this.array.add(i, array.get(i));// arrayList���ٰ� �־��ֱ�
			nextPiece = array.get(i);// nextPiece���ٰ� arrayList���� ������ �� �־��ֱ�
			repaint();// 3���� �ٽ� �׷�����...?
		}
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < 4; i++) {// �ش�Panel�� �׷��ٰ�
			int x = nextPiece.x(i) + 1;
			int y = nextPiece.y(i) + 1;// 3x3 �гο� �׷��� ��
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

	int squareWidth() {// ��ĭ ���� ����
		return (int) getSize().getWidth() / BoardWidth;
	}

	int squareHeight() {// ��ĭ ���� ����
		return (int) getSize().getHeight() / BoardHeight;
	}

}
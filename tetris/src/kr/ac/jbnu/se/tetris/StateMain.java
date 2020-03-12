package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.apache.commons.lang3.StringUtils;

public class StateMain extends JPanel {
	private HashMap<Integer, String> rankList = new HashMap<Integer, String>();
	private ArrayList<String> userList = new ArrayList<String>();
	private ArrayList<Integer> userScore = new ArrayList<Integer>();
	private String scoreAll = "<html>";
	public JPanel scoreBoard = new JPanel();;
	private JPanel sateBoard = new JPanel();
	public JPanel currentScore = new JPanel();
	private JPanel nextShape = new JPanel();
	private JPanel sateBoardLeft = new JPanel();
	private JPanel sateBoardRight = new JPanel();
	//들어가야할 판넬
	private JPanel nextShapeSmall1 = new JPanel();
	private JPanel nextShapeSmall2 = new JPanel();
	private JPanel nextShapeSmall3 = new JPanel();
	
	NextPiece nextPanel=new NextPiece(this);//이 프레임을 NextPiece에게 넘겨주기
	private JLabel score = new JLabel("test");
//	public JLabel level = new JLabel("1");;
	private Tetris tetris;
	private Record record = new Record();

	public StateMain() {
		setScoreBoard();
	}

	public StateMain(Tetris tetris) {
		this.tetris = tetris;
		initialize();
		setSize(200, 400);
		setScoreBoard();
		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - frameSize.width) / 2 + 185, (screenSize.height - frameSize.height) / 2 - 14);
		// setLocationRelativeTo(null);
		//setTitle("State");
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
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
//		scoreBoard.add(score);
	}		
//		currentScore.add(level);
//
//	}
//
//	public void setLevel(int level) {
//		this.level.setText(String.valueOf(level));
//		System.out.println(level);
//		this.level.revalidate();
//		this.level.repaint();
//	}

	private void setScoreBoard() {
		rankList = record.getRankList();
		userList = record.getUserList();
		userScore = record.getUserScore();
		sort();
		score.setText(scoreAll);
	}

	private void sort() {
		int size = userScore.size();
		if (size > 5) {
			size = 5;
		}
		for (int i = 0; i < size; i++) {
			String data = StringUtils.rightPad(rankList.get(userScore.get(i)), 10) + ":"
					+ (Integer.toString((userScore.get(i) / 100) * 100)) + "\r\n";
			scoreAll += data + "<br/><br/>";

		}
		scoreAll += "</html>";
//		System.out.println(scoreAll);
	}

	public void showCurrentRank(int score) {
		System.out.println("호출");
		rankList.put(score, "YOU");
		userList.add("YOU");
		userScore.add(score);
		Collections.sort(userScore);
		Collections.reverse(userScore);
		sort();
		this.score.setText(scoreAll);
		setScoreBoard();
		repaint();
		// this.score.paintImmediately(this.score.getVisibleRect());
	}

}
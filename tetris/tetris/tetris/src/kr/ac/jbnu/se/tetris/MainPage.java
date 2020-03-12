package kr.ac.jbnu.se.tetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainPage extends JPanel {
	private String mainImgPath = "image/logo.png";
	private String rankingImgPath = "image/Ranker.png";
	private String setImgPath = "image/set.jpg";
	private String play1ImgPath = "image/player1.jpg";
	private String play2ImgPath = "image/player2.jpg";

	private Record record = new Record();

	private JLabel imageLable;
	private JButton play1;
	private JButton play2;
	private JButton recode;
	private JButton set;
	private Main main;
	private Configuration config;

	public MainPage(Main main) {
		this.main = main;
		initialize();
	}

	// 이미지 사이즈 조절
	private ImageIcon setImgSize(String imgPath, int w, int h) {
		ImageIcon originIcon = new ImageIcon(imgPath);
		Image originImg = originIcon.getImage();
		Image changedImg = originImg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(changedImg);
		return image;
	}

	// 이미지 배경 지우기
	private void cleanImg(JButton button, int w, int h) {
		button.setPreferredSize(new Dimension(w, h));
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
	}

	private void initialize() {

		// 패널 추가
		JPanel top = new JPanel();
		JPanel bntpanel = new JPanel();

		// 패널 색 관리
		bntpanel.setBackground(new Color(255, 255, 255));
		this.setBackground(new Color(255, 255, 255));
		top.setBackground(new Color(255, 255, 255));

		// 패널 Layout 관리
		this.setLayout(new BorderLayout());
		top.setLayout(new BorderLayout());

		// 이미지 사이즈 조절
		imageLable = new JLabel(setImgSize(mainImgPath, 180, 110));
		recode = new JButton(setImgSize(rankingImgPath, 28, 28));
		set = new JButton(setImgSize(setImgPath, 28, 28));
		play1 = new JButton(setImgSize(play1ImgPath, 70, 10));
		play2 = new JButton(setImgSize(play2ImgPath, 70, 10));

		// img but
		set.setBackground(new Color(255, 255, 255));
		cleanImg(recode, 28, 28);
		cleanImg(set, 28, 28);
		cleanImg(play1, 70, 10);
		cleanImg(play2, 70, 10);

		// but Action
		play2.addActionListener(new MyActionListener());
		play1.addActionListener(new MyActionListener());
		recode.addActionListener(new MyActionListener());
		set.addActionListener(new MyActionListener());

		// 패널에 추가
		top.add(recode, BorderLayout.WEST);
		top.add(set, BorderLayout.EAST);

		bntpanel.add(play1, BorderLayout.WEST);
		bntpanel.add(play2, BorderLayout.EAST);

		this.add(imageLable, BorderLayout.CENTER);
		this.add(bntpanel, BorderLayout.SOUTH);
		this.add(top, BorderLayout.NORTH);
	}

	class MyActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == play1) {
				main.game = new Tetris();
				main.game.setVisible(true);
				main.setVisible(false);
			} else if (e.getSource() == play2) {
				System.out.println("11");
				main.game = new Tetris(1);
				main.game.setVisible(true);
				main.setVisible(false);

			} else if (e.getSource() == recode) {
				JOptionPane.showMessageDialog(null, record.load(), "Ranking", JOptionPane.PLAIN_MESSAGE);
			} else if (e.getSource() == set) {
//				JOptionPane.showMessageDialog(null, "!" , "!", JOptionPane.PLAIN_MESSAGE);
				config = new Configuration();
			}

		}
	}

}

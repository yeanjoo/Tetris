package kr.ac.jbnu.se.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Configuration extends JFrame {

	// �������� ������ �� Ŭ���� ������ ���� (���������� �𸣰ڴ�)
	static public float backSoundVoulme = 0.7f; // ��׶��� ������ ����
	static public float effectSoundVolume = 0.7f; // ȿ���� ������ ����
	static public boolean isReverse = false; // ������ ����

	// GUI�κ�
	private JPanel contentPane;

	// ������
	public Configuration() {

		// �⺻����
		this.setVisible(true);
		setBounds(100, 100, 537, 451);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// ��ư����
		JPanel setKey = new JPanel();
		setKey.setBounds(0, 46, 519, 307);
		contentPane.add(setKey);
		setKey.setLayout(null);
		setKey.setVisible(false);

		JLabel caution = new JLabel("�ٲٰ� ������ Ű�� �����ּ���"); // <-���������� �ʹ� ��ȿ�����ε� ��......
		caution.setVisible(false);
		caution.setBackground(Color.WHITE);
		caution.setFont(new Font("����", Font.BOLD, 15));
		caution.setBounds(180, 267, 278, 30);
		setKey.add(caution);

		JButton up = new JButton("��");
		up.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				caution.setVisible(true);
				up.addKeyListener(new SettingKey("upKey"));
				up.requestFocus();
			}

		});

		up.setBounds(361, 165, 68, 39);
		setKey.add(up);

		JButton down = new JButton("��������");
		down.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				caution.setVisible(true);
				down.addKeyListener(new SettingKey("lineDownKey"));
				down.requestFocus();

			}
		});
		down.setBounds(59, 219, 205, 33);
		setKey.add(down);

		JButton rotate = new JButton("�Ʒ�");
		rotate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				caution.setVisible(true);
				rotate.addKeyListener(new SettingKey("downKey"));
				rotate.requestFocus();
			}
		});
		rotate.setBounds(361, 216, 68, 39);
		setKey.add(rotate);

		JButton right = new JButton("������");
		right.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				caution.setVisible(true);
				right.addKeyListener(new SettingKey("rightKey"));
				right.requestFocus();

			}
		});
		right.setBounds(439, 216, 78, 39);
		setKey.add(right);

		JButton left = new JButton("��");

		left.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				caution.setVisible(true);
				left.addKeyListener(new SettingKey("leftKey"));
				left.requestFocus();

			}
		});

		left.setBounds(290, 216, 68, 39);
		setKey.add(left);

		JButton start = new JButton("����");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				caution.setVisible(true);
				start.addKeyListener(new SettingKey("pauseKey"));
				start.requestFocus();

			}
		});
		start.setBounds(14, 106, 68, 39);
		setKey.add(start);

		JButton onePerson = new JButton("1��");
		onePerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingKey.isTwo = false;
				setKey.setBackground(Color.GREEN);
			}
		});
		onePerson.setBounds(14, 12, 217, 27);
		setKey.add(onePerson);

		JButton twoPerson = new JButton("2��");
		twoPerson.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SettingKey.isTwo = true;
				setKey.setBackground(Color.CYAN);
			}
		});
		twoPerson.setBounds(288, 12, 217, 27);
		setKey.add(twoPerson);

		// ȯ�漳��
		JPanel configuration = new JPanel();
		configuration.setBounds(0, 46, 519, 307);
		contentPane.add(configuration);
		configuration.setLayout(null);

		// �����̵�
		JSlider backSound = new JSlider();
		backSound.setMajorTickSpacing(10);
		backSound.setPaintLabels(true);
		backSound.setMaximum(100);
		backSound.setBounds(108, 55, 301, 44);
		configuration.add(backSound);

		JSlider effectSound = new JSlider();
		effectSound.setMaximum(100);
		effectSound.setMajorTickSpacing(10);
		effectSound.setPaintLabels(true);
		effectSound.setBounds(108, 144, 301, 44);
		configuration.add(effectSound);

		// üũ�ڽ�
		JCheckBox backSoundOff = new JCheckBox("���Ұ�");
		backSoundOff.setBounds(429, 55, 80, 26);
		configuration.add(backSoundOff);

		backSoundOff.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (backSoundOff.isSelected())
					backSound.setEnabled(false);
				else
					backSound.setEnabled(true);
			}
		});

		JCheckBox effectSoundOff = new JCheckBox("���Ұ�");
		effectSoundOff.setBounds(429, 144, 80, 26);
		configuration.add(effectSoundOff);

		effectSoundOff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (effectSoundOff.isSelected())
					effectSound.setEnabled(false);
				else
					effectSound.setEnabled(true);
			}
		});

		// �ؽ�Ʈ
		JTextPane textPane = new JTextPane();
		textPane.setText("�������");
		textPane.setBounds(14, 55, 80, 26);
		configuration.add(textPane);

		JTextPane textPane_1 = new JTextPane();
		textPane_1.setText("ȿ����");
		textPane_1.setBounds(14, 144, 80, 26);
		configuration.add(textPane_1);

		JToggleButton Reverse = new JToggleButton("�߷¿��� ȿ�� ����");
		Reverse.setBounds(162, 238, 190, 27);
		configuration.add(Reverse);

		JButton btnNewButton = new JButton("ȯ�漳��");

		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				configuration.setVisible(true);
				setKey.setVisible(false);
			}
		});
		btnNewButton.setBounds(56, 12, 105, 27);
		contentPane.add(btnNewButton);

		JButton button = new JButton("Ű����");

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				configuration.setVisible(false);
				setKey.setVisible(true);
			}
		});

		button.setBounds(314, 12, 105, 27);
		contentPane.add(button);

		JButton Apply = new JButton("����");
		Apply.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (backSoundOff.isSelected())
					backSoundVoulme = 0;
				else
					backSoundVoulme = (float) backSound.getValue() / 100; // ���� ����

				if (effectSoundOff.isSelected())
					effectSoundVolume = 0;
				else
					effectSoundVolume = (float) effectSound.getValue() / 100;

				isReverse = Reverse.isSelected();

				dispose();
			}

		});

		Apply.setBounds(211, 365, 105, 27);
		contentPane.add(Apply);

	}

	public float getBackVoulme() {
		return backSoundVoulme;
	}

	public float geteffectVoulme() {
		return effectSoundVolume;
	}

}
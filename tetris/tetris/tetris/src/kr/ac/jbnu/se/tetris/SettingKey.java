package kr.ac.jbnu.se.tetris;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SettingKey implements KeyListener {

	static protected int pauseKey = 'p';// 대문자도 포함시킬것, 멈춤키는 하나인듯

	// 1p
	static protected int upKey = KeyEvent.VK_UP;// static으로 바꿔보기
	static protected int downKey = KeyEvent.VK_DOWN;
	static protected int leftKey = KeyEvent.VK_LEFT;
	static protected int rightKey = KeyEvent.VK_RIGHT;
	static protected int lineDownKey = KeyEvent.VK_SPACE;
	static protected int oneLineDownKey = 'm';
	static protected int startKey = KeyEvent.VK_9;// 초기값
	
	// 2p
	static protected int upKeyTwo = KeyEvent.VK_W;
	static protected int downKeyTwo = KeyEvent.VK_S;
	static protected int leftKeyTwo = KeyEvent.VK_A;
	static protected int rightKeyTwo = KeyEvent.VK_D;
	static protected int lineDownKeyTwo = KeyEvent.VK_SHIFT;
	static protected int oneLineDownKeyTwo = 'q';
	static protected int startKeyTwo = KeyEvent.VK_0;

	protected String key;
	protected static boolean isTwo = false;

	public SettingKey() {

	}

	public SettingKey(String key) {
		System.out.println(isTwo);
		this.key = key;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		int keyCode = e.getKeyCode();// 키코드 번호 받아오는 거

		if (isTwo == false)

			switch (key) {

			case "upKey": // 스트링을 통해 key를 받아올 것
				System.out.println("업키");
				upKey = keyCode;
				break;

			case "lineDownKey":
				System.out.println("라인다운");
				lineDownKey = keyCode;
				break;

			case "downKey":
				System.out.println("다운");
				downKey = keyCode;
				break;

			case "leftKey":
				System.out.println("왼쪽");
				leftKey = keyCode;
				break;

			case "rightKey":
				System.out.println("오른쪽");
				rightKey = keyCode;
				break;

			case "pauseKey":
				System.out.println("멈춤");
				pauseKey = keyCode;
				break;

			default:
				break;

			}

		else {
			switch (key) {

			case "upKey":
				upKeyTwo = keyCode;
				break;

			case "lineDown":
				lineDownKeyTwo = keyCode;
				break;

			case "downKey":
				downKeyTwo = keyCode;
				break;

			case "leftKey":
				leftKeyTwo = keyCode;
				break;

			case "rightKey":
				rightKeyTwo = keyCode;
				break;

			case "pauseKey":
				pauseKey = keyCode;
				break;

			default:
				break;

			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}

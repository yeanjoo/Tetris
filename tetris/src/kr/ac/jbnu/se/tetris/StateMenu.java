package kr.ac.jbnu.se.tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JLabel;

import org.apache.commons.lang3.StringUtils;

public class StateMenu extends JLabel{
	
	private HashMap<Integer, String> rankList = new HashMap<Integer, String>();
	private ArrayList <String> userList = new ArrayList<String>();
	private ArrayList <Integer> userScore = new ArrayList<Integer>();
	private String scoreAll = "";
	
	public StateMenu(Tetris tetris) {
		this.setText("test");
		rankList = tetris.board.record.getRankList();
		userList = tetris.board.record.getUserList();
		userScore = tetris.board.record.getUserScore();
	}
	
	public void sortRank(int score) {
		rankList.put(score, "YOU");
		userList.add("YOU");
		userScore.add(score);
		Collections.sort(userScore);
		Collections.reverse(userScore);
		sort();
		this.setText(scoreAll);
	}
	
	private void sort() {
		int size = userScore.size();
		if(size > 5) {
			size = 5;
		}
		for(int i = 0; i < size; i++) {
            String data = StringUtils.rightPad(rankList.get(userScore.get(i)),10) +":"+ (Integer.toString((userScore.get(i)/100) * 100)) +"\r\n";
            scoreAll += data;
        }
		
	}

}

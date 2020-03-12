package kr.ac.jbnu.se.tetris;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

public class Record {

	private String recordTxtPath = "rank/rank.txt";
	private HashMap<Integer, String> rankList = new HashMap<Integer, String>();
	private ArrayList<String> userList = new ArrayList<String>();
	private ArrayList<Integer> userScore = new ArrayList<Integer>();
	private String newUser;
	private int newScore;
	private String scoreBoard = "";



	public Record() {

		try {
			readRank();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList getUser() {
		return userList;
	}
	public ArrayList getScore() {
		return userScore;
	}
	public HashMap getHash() {
		return rankList;
	}

	public void reNewRecord(String user, int Score) throws IOException {
		newUser = user;
		newScore = Score+1;
		readRank();
		scoreBoard = "";
		sortList();
		writeRank();
	}

	private void writeRank() throws IOException {

		FileWriter fw = new FileWriter(recordTxtPath,false);
		int size = userScore.size();
		if (size > 5) {
			size = 5;
		}
		for (int i = 0; i < size; i++) {
			String data = StringUtils.rightPad(rankList.get(userScore.get(i)), 10) + ":"
					+ (Integer.toString((userScore.get(i) / 100) * 100)) + "\r\n";
			scoreBoard += data;
			fw.write(data); 
		}
		fw.close();

	}
	private void readRank() throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(recordTxtPath));
		StringTokenizer str;
		rankList.clear();
		userScore.clear();
		userList.clear();
		for (int i = 0;; i++) {
			String line = br.readLine();
			if (line == null)
				break;
			scoreBoard = scoreBoard + line + "\r\n";
			str = new StringTokenizer(line, ":");
			userList.add(str.nextToken());
			userScore.add(Integer.parseInt(str.nextToken())+1);
			overlapCheck(i);
			rankList.put(userScore.get(i), userList.get(i));
		}
		br.close();
	}

	private void overlapCheck() {
		if (rankList.containsKey(newScore)) {
			newScore += 1;
		} else {
			return;
		}
		overlapCheck();
	}

	private void overlapCheck(int i) {
		if (rankList.containsKey(userScore.get(i))) {
			int tmp = userScore.get(i);
			userScore.remove(i);
			userScore.add(tmp + 1);
		} else {
			return;
		}
		overlapCheck(i);
	}

	

	private void sortList() {

		overlapCheck();
		rankList.put(newScore, newUser);
		userList.add(newUser);
		userScore.add(newScore);
		Collections.sort(userScore);
		Collections.reverse(userScore);

	}


	public String load() {
		if (scoreBoard == "") {
			scoreBoard = "No Record";
		}
		return scoreBoard;
	}

}
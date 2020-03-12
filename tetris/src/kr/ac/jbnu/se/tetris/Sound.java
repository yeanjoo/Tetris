package kr.ac.jbnu.se.tetris;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	private Clip clip;
	private boolean loop;

	public Sound(boolean loop) {
		this.loop = loop;
	}

	public void Play(String fileName) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(new File(fileName));
			clip = AudioSystem.getClip();
			clip.stop();
			clip.open(ais);
			clip.start();
			if (loop == true) {

				clip.loop(-1);
			}

		} catch (Exception ex) {
		}
	}

	public void Stop() {
		clip.stop();
	}

	public void Keep() {
		clip.start();
	}

}

package kr.ac.jbnu.se.tetris;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class Sound {
	private Clip clip;
	private boolean loop;

	FloatControl gainControl;
	AudioInputStream ais;
	AudioFormat format;
	DataLine.Info info;
	float range;

	public Sound(boolean loop) {
		this.loop = loop;
	}

	public void Play(String fileName) {
		try {
			
			ais = AudioSystem.getAudioInputStream(new File(fileName));
			clip = AudioSystem.getClip();
			format = ais.getFormat();
			info = new DataLine.Info(Clip.class, format);
			clip = (Clip) AudioSystem.getLine(info);
			clip.stop();
			clip.open(ais);
			clip.start();
			
			// 컨트롤 얻어오기
			gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			range = gainControl.getMaximum() - gainControl.getMinimum();

			if (loop == true) {

				clip.loop(-1);
			}

		} catch (Exception e) {
			System.out.println("eer :" + e);

		}
	}

	public void Stop() {
		clip.stop();
	}

	public void Keep() {
		clip.start();
	}

	public void setSound(float Voulme) {

		float gainBack = (range * Voulme) + gainControl.getMinimum();
		gainControl.setValue(gainBack);

	}
}

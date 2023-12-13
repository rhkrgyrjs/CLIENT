package sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlayEffectSound 
{
	public static void playFlip()
	{
		playSound("sound/flip.wav");
	}
	
	public static void playRing()
	{
		playSound("sound/ring.wav");
	}
	
	public static void playSound(String filePath) 
	{
        Thread soundThread = new Thread(() -> {
            try {
                File soundFile = new File(filePath);

                // AudioInputStream 생성
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);

                // Clip 열기
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                // 효과음 플레이
                clip.start();

                // 플레이 종료까지 대기
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                });

                while (clip.isOpen()) {
                    try {
                        Thread.sleep(10); // 재생 중인 동안 기다림
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });

        soundThread.start();
    }
}

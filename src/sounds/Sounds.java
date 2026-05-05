package Sounds;

import javax.sound.sampled.*;

public class Sounds {

    public static void playPlace() {
        playSound("place.wav");
    }

    public static void playwin() {
        playSound("winner.wav");
    }

    public static void playstrongmove() {
        playSound("click.wav");
    }

    public static void playdefeat() {
        playSound("defeat.wav");
    }

    public static void playgoodmove() {
        playSound("victory.wav");
    }

    private static void playSound(String fileName) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                Sounds.class.getResource("/Sounds/" + fileName)
            );

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });

            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Clip musicClip;

    public static void playMusic() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                Sounds.class.getResource("/Sounds/NotStolen.wav")
            );

            musicClip = AudioSystem.getClip();
            musicClip.open(audioIn);
            FloatControl gainControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-15.0f);

            musicClip.loop(Clip.LOOP_CONTINUOUSLY); 
            musicClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
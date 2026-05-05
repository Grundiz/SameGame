package sounds;

import javax.sound.sampled.*;

public class Sounds {

    public static void playPlace() {
        playSound("place.wav");
    }

    private static void playSound(String fileName) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(
                Sounds.class.getResource("/sounds/" + fileName)
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
}
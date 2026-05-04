Public class sounds {  // den accepterar wav-filer
public static void playSound(String path) {   // detta är för enstaka ljud som tex move:
    try {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    } catch (Exception e) {
            e.printStackTrace();
        }
}
public static void maintheme(String path) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                    playSound(path);
                }
            });

            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

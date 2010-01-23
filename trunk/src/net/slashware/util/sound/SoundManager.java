package net.slashware.util.sound;

public class SoundManager {
	private static Thread jLayerMP3PlayerThread;
	public static void initialize() {
		jLayerMP3PlayerThread = new Thread(new JLayerMP3Player());
		jLayerMP3PlayerThread.start();
	}
	
	public static void playFile(String file){
		JLayerMP3Player.setMP3(file);
		JLayerMP3Player.setInstruction(JLayerMP3Player.INS_LOAD);
		jLayerMP3PlayerThread.interrupt();
	    
	}

	public static void stop() {
		JLayerMP3Player.setInstruction(JLayerMP3Player.INS_STOP);
		jLayerMP3PlayerThread.interrupt();
	}
}

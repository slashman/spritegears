package net.slashware.util.sound;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class JLayerMP3Player implements Runnable{
    private static String currentMP3File = "__noneYet";
    private static int currentInstruction;
   
    public static final int INS_LOAD = 1;
    public static final int INS_STOP = 0;
    public static final int INS_DIE = 2;
    public static final int INS_PLAYING = 3;
   
    public static void setMP3(String pMP3File){
        currentMP3File = pMP3File;
    }
   
    public static void setInstruction(int instruction){
        currentInstruction = instruction;
    }
    
    public synchronized void run(){
        boolean leave = false;
        out: while (true){
            if (currentInstruction == INS_DIE){
                break out;
            }
            if (currentInstruction == INS_STOP){
                currentMP3File= "__noneYet";
            }
            if (currentInstruction == INS_LOAD){
            	currentInstruction = INS_PLAYING;
            }
            if (currentMP3File.equals("__noneYet")){
                try {
                    this.wait();
                } catch (InterruptedException ie) {
                    continue;
                }
            }
            File soundFile = new File(currentMP3File);
            if(!soundFile.exists() || soundFile.isDirectory() || !soundFile.canRead()) {
                System.out.println("Invalid MP3 file: "+currentMP3File);
                 try {
                    this.wait();
                } catch (InterruptedException ie) {
                    continue;
                }
            }
            leave = false;
            Player player = null;
            while (!leave) {
                try {
                    player = new Player(new FileInputStream(soundFile));
                    in: while (true) {
                        /*try {
                            Thread.sleep(1000); // Check every second
                        } catch(InterruptedException ignore) {
                            leave = true;
                            break;
                        }*/
                        player.play(20);
                        if (currentInstruction == INS_STOP){
                            currentMP3File= "__noneYet";
                            player.close();
                            leave = true;
                        }
                        if (currentInstruction == INS_DIE){
                            break out;
                        }
                        if (currentInstruction == INS_LOAD){
                            leave = true;
                            continue out;
                        }
                        if (player.isComplete()){
                            player.close();
                            break in;
                        }
                    }
                } catch (IOException ioe) {
                    System.out.println("I/O Error for "+currentMP3File);
                    leave = true;
                } catch (JavaLayerException uafe) {
                    System.out.println("UnsupportedAudioFileException for "+currentMP3File);
                    leave = true;
                }
            }
            player.close();
        }
    }
   
    public static void main(String[] args){
        new Thread(new JLayerMP3Player()).start();
        JLayerMP3Player.setMP3("music/Wyvern - upbeat.mp3");

//        JLayerMP3Player.setMP3("music/Craig Stern - Exploring the Depths.mp3");
        JLayerMP3Player.setInstruction(INS_LOAD);
       
        try {
            Thread.sleep(10000);
            JLayerMP3Player.setInstruction(INS_DIE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
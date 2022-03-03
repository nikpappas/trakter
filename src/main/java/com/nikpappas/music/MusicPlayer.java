package com.nikpappas.music;

import processing.core.PApplet;
import processing.sound.SoundFile;

public class MusicPlayer {
    SoundFile soundA;


    PApplet pApplet;
    private String file;

    MusicPlayer(PApplet pApplet) {
        this.pApplet = pApplet;
    }

    public synchronized void loadFile(String file) {
        this.file = file;
        if (file == null || file.trim().equals("")) {
            return;
        }
        try {
            if (isPlaying()) {
                soundA.stop();

            }
            soundA = new SoundFile(pApplet, file);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public boolean hasSong() {
        return soundA != null;
    }

    public boolean isPlaying() {
        return hasSong() && soundA.isPlaying();
    }

    public void play() {
        if (hasSong()) {
            soundA.play();
        }
    }

    public void playPause() {
        if (hasSong()) {
            if (soundA.isPlaying()) {
                soundA.pause();
            } else {
                soundA.play();
            }
        }
    }

    public void stop() {
        if (hasSong()) {
            soundA.stop();
        }
    }

    public float getPosition() {
        if (!hasSong()) {
            return 0;
        }
        return soundA.percent();
    }

    public void setPosition(float percent) {
        var posFrame = (int) (percent * soundA.frames());
        System.out.println("frame " + posFrame);
        soundA.jumpFrame(posFrame);
    }

    public void setVolume(float vol) {
        if (hasSong()) {

            soundA.amp(vol);
        }
    }

    public int getFrames() {
        return soundA.frames();
    }

    public boolean isInLast(int seconds) {
        return soundA.duration() - seconds < soundA.position();
    }

    public float crossfadePercent() {
        return (soundA.duration() - soundA.position()) / 30;
    }

    public String getFile(){
        return file;
    }
}

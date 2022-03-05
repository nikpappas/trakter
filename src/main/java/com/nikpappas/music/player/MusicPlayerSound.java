package com.nikpappas.music.player;

import com.nikpappas.music.PlaylistEntry;
import processing.core.PApplet;
import processing.sound.Amplitude;
import processing.sound.SoundFile;

public class MusicPlayerSound implements MusicPlayer {
    private final Amplitude amp;
    SoundFile soundA;


    PApplet pApplet;
    private PlaylistEntry playing;

    public MusicPlayerSound(PApplet pApplet) {
        this.pApplet = pApplet;
        this.amp = new Amplitude(pApplet);
    }

    @Override
    public synchronized void loadFile(PlaylistEntry toLoad) {
        if (toLoad == null || toLoad.getFile() == null || toLoad.getFile().trim().equals("")) {
            return;
        }
        this.playing = toLoad;
        System.out.println(toLoad.getDisplayName());
        try {
            if (isPlaying()) {
                soundA.stop();
            }
            soundA = new SoundFile(pApplet, toLoad.getFile(), false);
            amp.input(soundA);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean hasSong() {
        return soundA != null;
    }

    @Override
    public boolean isPlaying() {
        return hasSong() && soundA.isPlaying();
    }

    @Override
    public synchronized void play() {
        if (hasSong() && !isPlaying()) {
            try {
                soundA.stop();
                soundA.play();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                playing.setError(true);
                soundA = null;
                playing = null;
            }
        }
    }

    @Override
    public void playPause() {
        if (hasSong()) {
            if (soundA.isPlaying()) {
                soundA.pause();
            } else {
                soundA.play();
            }
        }
    }

    @Override
    public void stop() {
        if (hasSong()) {
            soundA.stop();
        }
    }

    @Override
    public float getPosition() {
        if (!hasSong()) {
            return 0;
        }
        return soundA.percent();
    }

    @Override
    public void setPosition(float percent) {
        try {
            var posFrame = (int) (percent * soundA.frames());
            System.out.println("frame " + posFrame);
            soundA.jumpFrame(posFrame);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void setVolume(float vol) {
        if (hasSong()) {
            soundA.amp(vol);
        }
    }

    public int getFrames() {
        return soundA.frames();
    }

    @Override
    public boolean isInLast(int seconds) {
        return soundA.duration() - seconds < soundA.position();
    }

    @Override
    public float crossfadePercent() {
        return (soundA.duration() - soundA.position()) / 30;
    }

    @Override
    public PlaylistEntry getPlaying() {
        return playing;
    }

    @Override
    public float level() {
        return amp.analyze();
    }

}

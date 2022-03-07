package com.nikpappas.music.player;

import com.nikpappas.music.PlaylistEntry;
import processing.core.PApplet;
import processing.sound.Amplitude;
import processing.sound.BeatDetector;
import processing.sound.SoundFile;

public class MusicPlayerSound extends MusicPlayerBase {


    PApplet pApplet;
    private PlaylistEntry playing;

    public MusicPlayerSound(PApplet pApplet) {
        super(new Amplitude(pApplet), new BeatDetector(pApplet));
        this.pApplet = pApplet;
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
                stop();
            }
            setSong(new SoundFile(pApplet, toLoad.getFile(), false));

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
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

    public int getFrames() {
        return soundA.frames();
    }


    @Override
    public PlaylistEntry getPlaying() {
        return playing;
    }

    @Override
    public float[] getBuffer() {
        var toRet = new float[soundA.channels() * soundA.frames()];
        soundA.read(toRet);
        return toRet;
    }

}

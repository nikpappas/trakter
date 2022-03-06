package com.nikpappas.music.player;

import com.nikpappas.music.MusicPlayerGUI;
import com.nikpappas.music.PlaylistEntry;
import processing.sound.Amplitude;
import processing.sound.AudioSample;
import processing.sound.BeatDetector;
import processing.sound.SoundFile;

public class MusicPlayerSample implements MusicPlayer {
    private PlaylistEntry playing;
    private final MusicPlayerGUI pApplet;

    AudioSample soundA;
    private final Amplitude amp;
    private final BeatDetector beat;
    private float playRate;

    public MusicPlayerSample(MusicPlayerGUI pApplet) {
        this.pApplet = pApplet;
        amp = new Amplitude(pApplet);
        beat = new BeatDetector(pApplet);
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
            SoundFile soundB = new SoundFile(pApplet, toLoad.getFile(), false);
            var numOfFrames = soundB.channels() * soundB.frames();
            soundA = new AudioSample(pApplet, numOfFrames, soundB.sampleRate() * soundB.channels());
            float[] samples = new float[numOfFrames];
            soundB.read(samples);
            soundA.write(samples);
            amp.input(soundA);
            beat.input(soundA);
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

    }

    @Override
    public void stop() {
        soundA.stop();
    }

    @Override
    public float getPosition() {
        if (hasSong()) {
            return soundA.percent();
        } else {
            return 0;
        }
    }

    @Override
    public void setPlayRate(float rate){
        if(playRate == rate){
            return;
        }
        playRate = rate;
        soundA.rate(rate);
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

    @Override
    public boolean isInLast(int seconds) {
        return false;
    }

    @Override
    public float crossfadePercent() {
        return 0;
    }

    @Override
    public PlaylistEntry getPlaying() {
        return playing;
    }

    @Override
    public float level() {
        return amp.analyze();
    }

    @Override
    public boolean isBeat() {
        return beat.isBeat();
    }

}

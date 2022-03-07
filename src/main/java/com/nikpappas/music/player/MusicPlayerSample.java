package com.nikpappas.music.player;

import com.nikpappas.music.MusicPlayerGUI;
import com.nikpappas.music.PlaylistEntry;
import processing.sound.Amplitude;
import processing.sound.AudioSample;
import processing.sound.BeatDetector;
import processing.sound.SoundFile;

public class MusicPlayerSample extends MusicPlayerBase {
    private PlaylistEntry playing;
    private final MusicPlayerGUI pApplet;

    private AudioSample soundA;

    public MusicPlayerSample(MusicPlayerGUI pApplet) {
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
                soundA.stop();
            }
            SoundFile soundB = new SoundFile(pApplet, toLoad.getFile(), false);
            var numOfFrames = soundB.frames();
            soundA = new AudioSample(pApplet, numOfFrames, true, soundB.sampleRate());
            float[] samples = new float[2 * numOfFrames];
            soundB.read(samples);
            soundA.write(samples);
            setSong(soundA);
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

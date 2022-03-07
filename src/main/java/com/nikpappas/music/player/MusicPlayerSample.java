package com.nikpappas.music.player;

import com.nikpappas.music.MusicPlayerGUI;
import com.nikpappas.music.PlaylistEntry;
import processing.sound.Amplitude;
import processing.sound.AudioSample;
import processing.sound.BeatDetector;
import processing.sound.SoundFile;

import static com.nikpappas.music.MusicPlayerGUI.BEAT_SENSITIVITY;
import static com.nikpappas.music.MusicPlayerGUI.X_FADE_TIME;

public class MusicPlayerSample implements MusicPlayer {
    private PlaylistEntry playing;
    private final MusicPlayerGUI pApplet;

    private AudioSample soundA;
    private final Amplitude amp;
    private final BeatDetector beat;
    private float playRate = 1f;

    public MusicPlayerSample(MusicPlayerGUI pApplet) {
        this.pApplet = pApplet;
        amp = new Amplitude(pApplet);
        beat = new BeatDetector(pApplet);
        beat.sensitivity(BEAT_SENSITIVITY);

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
        if (hasSong()) {
            return soundA.percent();
        } else {
            return 0;
        }
    }

    @Override
    public void setPlayRate(float rate) {
        if (playRate == rate || !hasSong()) {
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
    public float getDuration() {
        return 0;
    }

    @Override
    public void setVolume(float vol) {
        if (hasSong()) {
            soundA.amp(vol);
        }
    }

    @Override
    public boolean isInLast(int seconds) {
        return soundA.duration() - seconds < soundA.position();
    }

    @Override
    public float crossfadePercent() {
        return (soundA.duration() - soundA.position()) / X_FADE_TIME;
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

    @Override
    public float[] getBuffer() {
        var toRet = new float[soundA.channels() * soundA.frames()];
        soundA.read(toRet);
        return toRet;
    }

    @Override
    public int getFramePosition() {
        if (hasSong()) {
            return soundA.positionFrame();
        } else {
            return 0;
        }
    }

    @Override
    public int getFrameRate() {
        return hasSong() ? soundA.sampleRate() : 0;
    }

    @Override
    public float getRemaining() {
        return hasSong() ? soundA.position() - soundA.duration() : 0;
    }

}

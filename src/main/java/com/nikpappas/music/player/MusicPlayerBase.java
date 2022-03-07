package com.nikpappas.music.player;

import processing.sound.Amplitude;
import processing.sound.AudioSample;
import processing.sound.BeatDetector;

import static com.nikpappas.music.MusicPlayerGUI.BEAT_SENSITIVITY;
import static com.nikpappas.music.MusicPlayerGUI.X_FADE_TIME;

public abstract class MusicPlayerBase<T extends AudioSample> implements MusicPlayer {
    protected T soundA;
    private final Amplitude amp;
    private final BeatDetector beat;

    private float playRate = 1f;


    protected MusicPlayerBase(Amplitude amp, BeatDetector beat) {
        this.amp = amp;
        this.beat = beat;
        beat.sensitivity(BEAT_SENSITIVITY);
    }


    protected void setSong(T soundA) {
        this.soundA = soundA;
        amp.input(soundA);
        beat.input(soundA);
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
    public void setVolume(float vol) {
        if (hasSong()) {
            soundA.amp(vol);
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
    public float getPosition() {
        if (hasSong()) {
            return soundA.percent();
        } else {
            return 0;
        }
    }

    @Override
    public float getDuration() {
        return hasSong() ? soundA.duration() : 0;
    }


    @Override
    public boolean isInLast(int seconds) {
        return soundA.duration() - seconds < soundA.position();
    }

    @Override
    public float crossfadePercent() {
        return hasSong() ? (soundA.duration() - soundA.position()) / X_FADE_TIME : 0f;
    }

    @Override
    public int getFrameRate() {
        return hasSong() ? soundA.sampleRate() : 0;
    }

    @Override
    public float getRemaining() {
        return hasSong() ? soundA.position() - soundA.duration() : 0;
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
    public void setPlayRate(float rate) {
        if (playRate == rate || !hasSong()) {
            return;
        }
        playRate = rate;
        soundA.rate(rate);
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

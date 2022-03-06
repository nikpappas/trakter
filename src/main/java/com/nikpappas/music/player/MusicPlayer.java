package com.nikpappas.music.player;

import com.nikpappas.music.PlaylistEntry;
import processing.sound.SoundObject;

public interface MusicPlayer {

    void loadFile(PlaylistEntry toLoad);

    boolean hasSong();

    boolean isPlaying();

    void play();

    void playPause();

    void stop();

    float getPosition();

    void setPlayRate(float rate);

    void setPosition(float percent);

    void setVolume(float vol);

    boolean isInLast(int seconds);

    float crossfadePercent();

    PlaylistEntry getPlaying();

    float level();

    boolean isBeat();

    float[] getBuffer();

    int getFramePosition();
}

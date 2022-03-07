package com.nikpappas.music.player;

import com.nikpappas.music.PlaylistEntry;

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

    float getDuration();

    void setVolume(float vol);

    boolean isInLast(int seconds);

    float crossfadePercent();

    PlaylistEntry getPlaying();

    float level();

    boolean isBeat();

    float[] getBuffer();

    int getFramePosition();

    int getFrameRate();

    float getRemaining();
}

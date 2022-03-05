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

    void setPosition(float percent);

    void setVolume(float vol);

    boolean isInLast(int seconds);

    float crossfadePercent();

    PlaylistEntry getPlaying();
}

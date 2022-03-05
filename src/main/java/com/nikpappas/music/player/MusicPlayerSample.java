package com.nikpappas.music.player;

import com.nikpappas.music.PlaylistEntry;

public class MusicPlayerSample implements MusicPlayer{
    @Override
    public void loadFile(PlaylistEntry toLoad) {

    }

    @Override
    public boolean hasSong() {
        return false;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public void play() {

    }

    @Override
    public void playPause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public float getPosition() {
        return 0;
    }

    @Override
    public void setPosition(float percent) {

    }

    @Override
    public void setVolume(float vol) {

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
        return null;
    }
}

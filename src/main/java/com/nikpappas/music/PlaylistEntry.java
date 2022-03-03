package com.nikpappas.music;

public class PlaylistEntry {
    private boolean playingA;
    private boolean playingB;
    private boolean selected;
    private final String file;
    private final String displayName;

    public PlaylistEntry(boolean playingA, boolean playingB, boolean selected, String file, String displayName) {
        this.playingA = playingA;
        this.playingB = playingB;
        this.selected = selected;
        this.file = file;
        this.displayName = displayName;
    }

    public static PlaylistEntry of(String file, String displayName) {
        return new PlaylistEntry(false, false, false, file, displayName);

    }

    public boolean isPlayingA() {
        return playingA;
    }

    public boolean isPlayingB() {
        return playingB;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getFile() {
        return file;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setPlayingA(boolean playingA) {
        this.playingA = playingA;
    }

    public void setPlayingB(boolean playingB) {
        this.playingB = playingB;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

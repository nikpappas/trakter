package com.nikpappas.music;

import java.util.Objects;

public class PlaylistEntry {
    private boolean playingA;
    private boolean playingB;
    private boolean selected;
    private final String file;
    private final String displayName;
    private boolean isError;

    public PlaylistEntry(boolean playingA, boolean playingB, boolean selected, boolean errored, String file, String displayName) {
        this.playingA = playingA;
        this.playingB = playingB;
        this.selected = selected;
        this.file = file;
        this.displayName = displayName;
        this.isError = errored;
    }

    public static PlaylistEntry of(String file, String displayName) {
        return new PlaylistEntry(false, false, false, false, file, displayName);

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

    public boolean isError() {
        return isError;
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
    public void setError(boolean error) {
        isError = error;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaylistEntry that = (PlaylistEntry) o;

        if (playingA != that.playingA) return false;
        if (playingB != that.playingB) return false;
        if (selected != that.selected) return false;
        if (!Objects.equals(file, that.file)) return false;
        return Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        int result = (playingA ? 1 : 0);
        result = 31 * result + (playingB ? 1 : 0);
        result = 31 * result + (selected ? 1 : 0);
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        return result;
    }

}

package com.nikpappas.music.playlist;

import com.nikpappas.music.PlaylistEntry;

import java.util.LinkedList;
import java.util.Optional;

public class PlaylistStructure {
    private final LinkedList<PlaylistEntry> playlist = new LinkedList<>();
    int index = -1;


    public void add(PlaylistEntry entry) {
        playlist.add(entry);
    }

    public boolean isEmpty() {
        return playlist.isEmpty();
    }

    public int size() {
        return playlist.size();
    }

    public PlaylistEntry get(int i) {
        return playlist.get(i);
    }

    public void setPlayingA(PlaylistEntry entry) {
        entry.setPlayingA(true);
        playlist.stream().filter(x -> !x.equals(entry)).forEach(x -> x.setPlayingA(false));
    }

    public void setPlayingB(PlaylistEntry entry) {
        entry.setPlayingB(true);
        playlist.stream().filter(x -> !x.equals(entry)).forEach(x -> x.setPlayingB(false));
    }

    public void remove(PlaylistEntry entry) {
        playlist.remove(entry);
    }

    public void add(int index, PlaylistEntry entry) {
        playlist.add(index, entry);
    }

    public PlaylistEntry getTrack(int i) {
        if (i < 0 || playlist.isEmpty()) {
            return null;
        }
        System.out.println(playlist.get(i));
        return playlist.get(i);
    }

    public Optional<PlaylistEntry> getNextTrack() {
        if (playlist.isEmpty()) return Optional.empty();
        index = (index + 1) % playlist.size();
        return Optional.ofNullable(getTrack(index));
    }

    public Optional<PlaylistEntry> getPrevTrack() {
        if (playlist.isEmpty()) return Optional.empty();
        index = index - 1;
        if (index < 0) {
            index = playlist.size() - 1;
        }

        return Optional.ofNullable(getTrack(index));
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setIndexFromEntry(PlaylistEntry entry) {
        this.index = playlist.indexOf(entry);
    }
}

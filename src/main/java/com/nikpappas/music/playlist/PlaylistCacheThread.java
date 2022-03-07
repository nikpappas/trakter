package com.nikpappas.music.playlist;

import com.nikpappas.music.MusicPlayerGUI;
import processing.sound.SoundFile;

import java.util.HashSet;
import java.util.Set;

public class PlaylistCacheThread implements Runnable {
    private final MusicPlayerGUI applet;
    private final PlaylistStructure playlist;

    Set<Integer> loadingTracks = new HashSet<>();

    public PlaylistCacheThread(MusicPlayerGUI applet, PlaylistStructure playlist) {
        this.playlist = playlist;
        this.applet = applet;
    }


    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            int index = playlist.getIndex();
            for (int i = 0; i < playlist.size(); i++) {
                var entry = playlist.get(i);
                var sound = entry.getSound();
                if (i < (index - 1) || i > (index + 2)) {
                    if (sound != null) {
                        sound.removeFromCache();
                        entry.setSound(null);
                        System.out.println("deleting " + i);
                    }
                } else {
                    if (sound == null && !loadingTracks.contains(i)) {
                        System.out.println("loading " + i);
                        loadingTracks.add(i);
                        final var indexToRemove = i;
                        applet.dispatchAsync(() -> {
                            entry.setSound(new SoundFile(applet, entry.getFile()));
                            loadingTracks.remove(indexToRemove);
                        }).run();
                    }
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

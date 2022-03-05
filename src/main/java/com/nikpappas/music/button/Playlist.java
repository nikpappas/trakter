package com.nikpappas.music.button;

import com.nikpappas.music.MusicPlayerGUI;
import com.nikpappas.music.PlaylistEntry;
import processing.event.MouseEvent;

import java.awt.*;

import static com.nikpappas.music.MusicPlayerGUI.RED;
import static com.nikpappas.music.Player.PLAYER_A;
import static com.nikpappas.music.Player.PLAYER_B;


public class Playlist implements Button {
    public static final int TRACK_HEIGHT = 30;
    private final Rectangle limits;
    private final int initOffset;
    private int offset;
    private MusicPlayerGUI musicPlayerGUI;
    private int x;
    private int y;
    private DraggedSong draggedSong;
    private DraggedSong candidateDraggedSong;

    public Playlist(MusicPlayerGUI musicPlayerGUI, int x, int y) {
        this.musicPlayerGUI = musicPlayerGUI;
        this.x = x;
        this.y = y;
        this.offset = y + TRACK_HEIGHT;
        this.initOffset = y + TRACK_HEIGHT;
        this.limits = new Rectangle(x, y, musicPlayerGUI.width, musicPlayerGUI.height - y);

    }

    @Override
    public void listenClick(MouseEvent me) {
        draggedSong = null;
        candidateDraggedSong = null;
        var mouseY = me.getY();
        if (!limits.contains(me.getX(), mouseY)) {
            return;
        }
        var playlist = musicPlayerGUI.getPlaylist();
        if (playlist.isEmpty()) {
            return;
        }
        var selectedIndex = getPlaylistIndexFromMouse(mouseY);
        if (me.getCount() == 2) {
            musicPlayerGUI.dispatchAsync(() -> musicPlayerGUI.loadTrack(playlist.get(selectedIndex))).run();
        } else {
            for (int i = 0; i < playlist.size(); i++) {
                if (i == selectedIndex) {
                    playlist.get(i).setSelected(true);
                } else {
                    playlist.get(i).setSelected(false);
                }
            }
        }
    }


    @Override
    public void draw() {
        var curOffset = 0;
        for (int i = 0; i < musicPlayerGUI.getPlaylist().size(); i++) {
            PlaylistEntry file = musicPlayerGUI.getPlaylist().get(i);
            if (curOffset + offset < initOffset - TRACK_HEIGHT) {
                curOffset += TRACK_HEIGHT;
                continue;
            }
            if (file.isSelected()) {
                musicPlayerGUI.fill(200);
                musicPlayerGUI.rect(x, curOffset + offset - TRACK_HEIGHT / 2, musicPlayerGUI.width, TRACK_HEIGHT);
            }
            var toDisplay = "";
            if (file.isPlayingA()) {
                toDisplay += "A   ";

            }
            if (file.isPlayingB()) {
                toDisplay += "B   ";
            }
            toDisplay += file.getDisplayName();
            musicPlayerGUI.fill(100);
            musicPlayerGUI.text(toDisplay, x + 10, curOffset + offset);
            if (file.isError()) {
                musicPlayerGUI.fill(RED.getRGB());
                musicPlayerGUI.text("ERROR", musicPlayerGUI.width - 40, curOffset + offset);
            }
            curOffset += TRACK_HEIGHT;
        }
        if (draggedSong != null) {
            musicPlayerGUI.text(draggedSong.entry.getDisplayName(), draggedSong.x, draggedSong.y);
        }
    }

    public void scroll(float amount) {
        var newOffset = offset - (int) amount;
        var limit = (musicPlayerGUI.getPlaylist().size() - 9) * TRACK_HEIGHT;
        if (newOffset > y - limit && newOffset < y + TRACK_HEIGHT) {
            offset = newOffset;
        }
        musicPlayerGUI.text(newOffset, 90, 80);
    }

    public void listenDragged(MouseEvent me) {
        if (candidateDraggedSong != null) {
            draggedSong = candidateDraggedSong;
            candidateDraggedSong = null;
        }
        if (draggedSong != null) {
            draggedSong = new DraggedSong(me.getX(), me.getY(), draggedSong.entry);
        }

    }

    public void mousePressed(MouseEvent me) {
        if (musicPlayerGUI.getPlaylist().isEmpty()) {
            return;
        }
        if (limits.contains(me.getX(), me.getY())) {
            candidateDraggedSong = new DraggedSong(me.getX(), me.getY(), getTrackFromMouse(me.getY()));
        }

    }

    public void mouseReleased(MouseEvent me) {
        if (draggedSong != null) {
            if (limits.contains(me.getX(), me.getY())) {
                var playlist = musicPlayerGUI.getPlaylist();
                var index = getPlaylistIndexFromMouse(me.getY());
                playlist.remove(draggedSong.entry);
                playlist.add(index, draggedSong.entry);
            } else {
                var mPwidth = musicPlayerGUI.width;
                var entry = draggedSong.entry;
                if (me.getX() < mPwidth / 2) {
                    musicPlayerGUI.dispatchAsync(() -> musicPlayerGUI.loadTrack(PLAYER_A, entry)).run();
                } else if (me.getX() > mPwidth / 2 && me.getX() < mPwidth) {
                    musicPlayerGUI.dispatchAsync(() -> musicPlayerGUI.loadTrack(PLAYER_B, entry)).run();
                }
            }
        }
        draggedSong = null;
        candidateDraggedSong = null;
    }

    private int getPlaylistIndexFromMouse(int y) {
        return (y - offset + TRACK_HEIGHT / 2) / TRACK_HEIGHT;
    }

    private PlaylistEntry getTrackFromMouse(int y) {
        var selectedIndex = getPlaylistIndexFromMouse(y);
        return musicPlayerGUI.getPlaylist().get(selectedIndex);
    }
}

class DraggedSong {
    final int x;
    final int y;
    final PlaylistEntry entry;

    public DraggedSong(int x, int y, PlaylistEntry entry) {
        this.x = x;
        this.y = y;
        this.entry = entry;
    }

}
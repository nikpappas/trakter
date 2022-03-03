package com.nikpappas.music.button;

import com.nikpappas.music.MusicPlayerGUI;
import com.nikpappas.music.PlaylistEntry;
import processing.event.MouseEvent;

import java.awt.*;

public class Playlist implements Button {
    private final Rectangle limits;
    private final int initOffset;
    private int offset;
    private MusicPlayerGUI musicPlayerGUI;
    private int x;
    private int y;
    private Runnable dispatchAsync;

    public Playlist(MusicPlayerGUI musicPlayerGUI, int x, int y, Runnable dispatchAsync) {
        this.musicPlayerGUI = musicPlayerGUI;
        this.x = x;
        this.y = y;
        this.dispatchAsync = dispatchAsync;
        this.offset = y + 30;
        this.initOffset = y + 30;
        this.limits = new Rectangle(x, y, musicPlayerGUI.width, musicPlayerGUI.height - y);

    }

    @Override
    public void listenClick(MouseEvent me) {
        var mouseY = me.getY();
        if (!limits.contains(me.getX(), mouseY)) {
            return;
        }
        var playlist = musicPlayerGUI.getPlaylist();
        System.out.println((mouseY - offset + 15) / 30);
        musicPlayerGUI.loadTrack(playlist.get((mouseY - offset + 15) / 30).getFile());
    }

    @Override
    public void draw() {
        var curOffset = 0;
        for(int i=0;i< musicPlayerGUI.getPlaylist().size();i++) {
            PlaylistEntry file = musicPlayerGUI.getPlaylist().get(i);
            if(curOffset+offset<initOffset-30){
                curOffset+=30;
                continue;
            }
            if (file.isSelected()) {
                musicPlayerGUI.fill(200);
                musicPlayerGUI.rect(x, curOffset + offset, musicPlayerGUI.width, 30);
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
            curOffset += 30;
        }
    }

    public void scroll(float amount){
        var newOffset =offset-(int)amount;
        var limit = (musicPlayerGUI.getPlaylist().size()-9)*30;
        if(newOffset>y-limit&&newOffset< y+30){
            offset = newOffset;
        }
        musicPlayerGUI.text(newOffset, 90,80);
    }
}

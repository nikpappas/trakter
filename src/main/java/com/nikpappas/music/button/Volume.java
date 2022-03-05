package com.nikpappas.music.button;

import com.nikpappas.music.player.MusicPlayerSound;
import com.nikpappas.music.MusicPlayerGUI;
import processing.event.MouseEvent;

import java.awt.*;

import static com.nikpappas.music.MusicPlayerGUI.RED;


public class Volume implements Button {
    private final Rectangle limits;
    private MusicPlayerGUI musicPlayerGUI;
    private final MusicPlayerSound player;
    private final int x;
    private int height;
    private float val;

    public Volume(MusicPlayerGUI musicPlayerGUI, MusicPlayerSound player, int x, int height) {
        this.musicPlayerGUI = musicPlayerGUI;
        this.player = player;
        this.x = x;
        this.height = height;
        this.val = 0.8f;
        this.limits = new Rectangle(x,30,30,height);
    }

    @Override
    public void listenClick(MouseEvent me) {
        if(limits.contains(me.getX(), me.getY())){
         setVolume(1-(me.getY()-30f)/height);
        }
    }

    @Override
    public void draw() {
        musicPlayerGUI.fill(100);
        musicPlayerGUI.rect(x, 15, 15, height);
        musicPlayerGUI.fill(RED.getRGB());
        musicPlayerGUI.rect(x, 15 + height * (1 - val), 15, height * val);
    }

    public void setVolume(float val) {
        if (val > 1 || val < 0) {
            return;
        }
        this.val = val;

        player.setVolume(val);

    }
}

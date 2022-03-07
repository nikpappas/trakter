package com.nikpappas.music.button;

import com.nikpappas.music.MusicPlayerGUI;
import com.nikpappas.music.player.MusicPlayer;
import processing.event.MouseEvent;

import java.awt.*;

import static com.nikpappas.music.MusicPlayerGUI.*;
import static java.lang.String.format;

public class Tracker implements Button {

    private final MusicPlayerGUI pApplet;
    private MusicPlayer player;
    private final int x;
    private final int y;
    private final Rectangle limits;

    public Tracker(MusicPlayerGUI pApplet, MusicPlayer player, int x, int y) {
        this.pApplet = pApplet;
        this.player = player;
        this.x = x;
        this.y = y;
        this.limits = new Rectangle(x, y, pApplet.width / 2, 25);
    }

    @Override
    public void listenMouseRelease(MouseEvent me) {
        if (!limits.contains(me.getX(), me.getY())) {
            return;
        }
        var position = (me.getX() - x) / ((float) limits.width);
        System.out.println("new pos " + position);
        player.setPosition(position);
    }

    @Override
    public void draw() {
        var pos = player.getPosition();
        pApplet.fill(LIGHT_GREY.getRGB());
        pApplet.stroke(DARK_GREY.getRGB());
        pApplet.rect(limits.x, limits.y, limits.width, limits.height);
        pApplet.noStroke();
        pApplet.fill(RED.getRGB());
        pApplet.rect(limits.x, limits.y + limits.height / 4, limits.width * pos / 100, limits.height / 2);
        pApplet.fill(DARK_GREY.getRGB());
        pApplet.text(format("-%s/%s", secToTimeString(player.getRemaining()), secToTimeString(player.getDuration())), limits.x + limits.width - 120f, limits.y + limits.height / 2+5);
    }
}

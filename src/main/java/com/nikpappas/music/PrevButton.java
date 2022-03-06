package com.nikpappas.music;

import com.nikpappas.music.button.Button;
import com.nikpappas.music.shapes.ResponsiveShapes;
import processing.event.MouseEvent;

import java.awt.*;

import static com.nikpappas.music.MusicPlayerGUI.DARK_GREY;

public class PrevButton implements Button {

    private MusicPlayerGUI musicPlayerGUI;
    private final float x;
    private final float y;
    private final float width;
    private final Rectangle limits;

    private final ResponsiveShapes shapes;
    private final Runnable behaviour;

    public PrevButton(MusicPlayerGUI musicPlayerGUI, float x, float y, float width, ResponsiveShapes shapes, Runnable behaviour) {
        this.musicPlayerGUI = musicPlayerGUI;
        this.x = x;
        this.y = y;
        this.width = width;
        this.limits = new Rectangle((int) x, (int) y, (int) width, (int) width);
        this.shapes = shapes;
        this.behaviour = behaviour;

    }

    @Override
    public void listenClick(MouseEvent me) {
        if (limits.contains(me.getX(), me.getY())) {
            System.out.println("play");
            behaviour.run();
        }
    }

    @Override
    public void draw() {
        musicPlayerGUI.fill(DARK_GREY.getRGB());
        shapes.responsiveTriangleLeft(x, y, width / 2, width);
        shapes.responsiveTriangleLeft(x + width / 2, y, width / 2, width);
    }

}

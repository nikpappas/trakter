package com.nikpappas.music.button;

import com.nikpappas.music.shapes.ResponsiveShapes;
import processing.event.MouseEvent;

import java.awt.*;

public class SquareButton implements Button {
    private final float x;
    private final float y;
    private final float width;
    private final Runnable behaviour;
    private ResponsiveShapes shapes;
    private final Rectangle limits;

    public SquareButton(float x, float y, float width, ResponsiveShapes shapes, Runnable behaviour) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.shapes = shapes;
        this.behaviour = behaviour;
        this.limits = new Rectangle((int) x, (int) y, (int) width, (int) width);
    }


    @Override
    public void listenClick(MouseEvent me) {
        if (limits.contains(me.getX(), me.getY())) {
            behaviour.run();
        }

    }

    @Override
    public void draw() {
        shapes.square(x, y, width);
    }
}

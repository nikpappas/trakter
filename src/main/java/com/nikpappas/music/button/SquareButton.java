package com.nikpappas.music.button;

import com.nikpappas.music.shapes.ResponsiveShapes;
import processing.event.MouseEvent;

import java.awt.*;

import static com.nikpappas.music.MusicPlayerGUI.LIGHT_GREY;
import static com.nikpappas.music.MusicPlayerGUI.STROKE;

public class SquareButton implements Button {
    private final float x;
    private final float y;
    private final float width;
    private final Runnable behaviour;
    private ResponsiveShapes shapes;
    private final Rectangle limits;
    private static final Color DEFAULT_COLOUR = LIGHT_GREY;
    private Color fgColour = DEFAULT_COLOUR;

    public SquareButton(float x, float y, float width, ResponsiveShapes shapes, Runnable behaviour) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.shapes = shapes;
        this.behaviour = behaviour;
        this.limits = new Rectangle((int) x, (int) y, (int) width, (int) width);
    }


    @Override
    public void listenMouseRelease(MouseEvent me) {
        if (limits.contains(me.getX(), me.getY())) {
            behaviour.run();
        }

    }

    public void setFgColour(Color rgb) {
        this.fgColour = rgb;
    }

    @Override
    public void draw() {
        shapes.fill(fgColour.getRGB());
        shapes.stroke(STROKE.getRGB());
        shapes.square(x, y, width);
    }
}

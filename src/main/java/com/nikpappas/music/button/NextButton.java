package com.nikpappas.music.button;

import com.nikpappas.music.shapes.ResponsiveShapes;
import processing.event.MouseEvent;

import java.awt.*;

public class NextButton implements Button {
    private final float x, y, width;
    private final Rectangle limits;

    private final ResponsiveShapes shapes;
    private final Runnable behaviour;

    public NextButton(float x, float y, float width, ResponsiveShapes shapes, Runnable behaviour) {
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
            System.out.println("next");
            behaviour.run();
        }
    }

    @Override
    public void draw() {

        shapes.responsiveTriangle(x, y, width/2, width);
        shapes.responsiveTriangle(x+width/2, y, width/2, width);
    }


}

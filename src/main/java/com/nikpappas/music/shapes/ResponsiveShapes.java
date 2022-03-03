package com.nikpappas.music.shapes;

import processing.core.PApplet;

public class ResponsiveShapes {

    PApplet pApplet;

    public ResponsiveShapes(PApplet pApplet) {
        this.pApplet = pApplet;
    }

    public void responsiveTriangle(float x, float y, float width, float height) {
        pApplet.triangle(x, y, x, y + height, x + width, y + height / 2);
    }

    public void responsiveTriangleLeft(float x, float y, float width, float height) {
        pApplet.triangle(x + width, y, x + width, y + height, x, y + height / 2);
    }

    public void square(float x, float y, float width) {
        pApplet.square(x, y, width);
    }

}

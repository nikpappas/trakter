package com.nikpappas.music.display;

import processing.core.PApplet;

import java.util.Arrays;

import static com.nikpappas.music.MusicPlayerGUI.*;
import static java.lang.Math.min;

public class Waveform implements Display {
    private final float height;
    private final float width;
    private final int x;
    private final int y;
    private float[] values;
    private int position;
    private PApplet pApplet;

    private static final float[] ZERO_FLOAT = new float[20000];

    static {
        Arrays.fill(ZERO_FLOAT, 0f);
    }

    public Waveform(PApplet pApplet, int x, int y, float width) {
        this.values = ZERO_FLOAT;
        this.position = 0;
        this.pApplet = pApplet;
        this.width = width;
        this.height = 40;
        this.x = x;
        this.y = y;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void draw() {
        pApplet.stroke(STROKE.getRGB());
        pApplet.fill(DARK_GREY.getRGB());
        pApplet.rect(x, y, width, height);
        var toDisplay = (float) min(220000, values.length);
        var offsetY = y + height / 2f;
        var toDisplayLimit = position + toDisplay;
        var jumpingFrames = 80;
        pApplet.stroke(YELLOW.getRGB());
        for (int i = position; i < toDisplayLimit; i += jumpingFrames) {
            pApplet.point(x + (width * (i - position) / toDisplay), 0.5f * values[i] * height + offsetY);
        }
    }
}

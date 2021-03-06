package com.nikpappas.music.display;

import processing.core.PApplet;

import java.util.Arrays;

import static com.nikpappas.music.MusicPlayerGUI.*;
import static java.lang.Math.min;

public class Waveform implements Display {
    private static final int RANGE = 320000;
    private static final int HALF_RANGE = RANGE/2;
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

    public Waveform(PApplet pApplet, int x, int y, float width, float height) {
        this.values = ZERO_FLOAT;
        this.position = 0;
        this.pApplet = pApplet;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public void setValues(float[] values) {
        this.values = values;
    }

    public void setPosition(int position) {
        this.position = position * 2;
    }

    @Override
    public void draw() {
        pApplet.stroke(STROKE.getRGB());
        pApplet.fill(DARK_GREY.getRGB());
        pApplet.rect(x, y, width, height);
        var toDisplay = (float) min(RANGE, values.length);
        var offsetY = y + height / 2f;
        var jumpingFrames = 80;
        var offsetX = max(position - HALF_RANGE, 0);
        var toDisplayLimit = position + toDisplay / 2f;
        pApplet.stroke(YELLOW.getRGB());
        for (int i = offsetX; i < toDisplayLimit && i < (values.length); i += jumpingFrames) {

            float x1 = x + (width * (i - position) / toDisplay + width / 2f);
//            float x2 = x + (width * (i+jumpingFrames/2f - position) / toDisplay + width / 2f);
            float y1 = .5f * values[i] * (height-5) + offsetY;
//            float y2 =.5f * values[i+jumpingFrames/2] * (height-5) + offsetY;
//            pApplet.line(x1,y1,x2,y2);
            pApplet.point(x1, 0.5f * values[i] * (height-15) + offsetY);
        }
        pApplet.stroke(STROKE.getRGB());
        pApplet.line(x + width / 2, y, x + width / 2, y + height);

    }
}

package com.nikpappas.music.button;

import com.nikpappas.music.MusicPlayerGUI;
import processing.event.MouseEvent;

import java.awt.*;
import java.util.function.Consumer;

import static java.lang.String.format;

public class HSlider implements Button {
    private static final Color BG_COLOUR = new Color(0x6E6E88);
    private static final Color FG_COLOUR = new Color(0x29462E);

    private final int x;
    private final int y;
    private final Rectangle limits;
    private final Consumer<Float> behaviour;
    private final MusicPlayerGUI musicPlayerGUI;
    private final float lowerLimit;
    private final float upperLimit;
    private int width;
    private int height;
    private Color bgColour;
    private Color fgColour;
    private float value;
    private boolean drawText;

    public HSlider(MusicPlayerGUI musicPlayerGUI, int x, int y, int width, int height, Consumer<Float> behaviour) {
        this(musicPlayerGUI, x, y, width, height, 0f, 1f, 0.8f, behaviour);
    }

    public HSlider(MusicPlayerGUI musicPlayerGUI, int x, int y, int width, int height, float lowerLimit, float upperLimit, float defaultValue, Consumer<Float> behaviour) {
        this.musicPlayerGUI = musicPlayerGUI;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.value = defaultValue;
        this.limits = new Rectangle(x, y, width, height);
        this.behaviour = behaviour;
    }

    @Override
    public void listenMouseRelease(MouseEvent me) {
        if (limits.contains(me.getX(), me.getY())) {
            setValue((upperLimit - lowerLimit) * (me.getX() - x) / width + lowerLimit);
        }


    }

    public void setValue(float value) {
        this.value = value;
        behaviour.accept(this.value);

    }

    @Override
    public void draw() {
        musicPlayerGUI.fill(getBgColourOr(BG_COLOUR).getRGB());
        musicPlayerGUI.rect(x, y, width, height);
        musicPlayerGUI.fill(getFgColourOr(FG_COLOUR).getRGB());
        var valWidth = width * ((value - lowerLimit) / (upperLimit - lowerLimit));
        musicPlayerGUI.rect(x + valWidth-2.5f, y - 5, 5, height + 5);
        if (drawText) {
            musicPlayerGUI.text(format("%2.1f%%", (value - 1f) * 100), x + 5, y + 15);
        }
    }

    public float getValue() {
        return value;
    }

    public void setBgColour(Color bgColour) {
        this.bgColour = bgColour;
    }

    public void setFgColour(Color fgColour) {
        this.fgColour = fgColour;
    }
    public void setDrawText(boolean drawText){
        this.drawText = drawText;
    }

    private Color getFgColourOr(Color defaultColour) {
        return fgColour != null ? fgColour : defaultColour;
    }

    private Color getBgColourOr(Color defaultColour) {
        return bgColour != null ? bgColour : defaultColour;
    }
}

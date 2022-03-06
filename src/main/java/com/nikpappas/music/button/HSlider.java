package com.nikpappas.music.button;

import com.nikpappas.music.MusicPlayerGUI;
import processing.event.MouseEvent;

import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

public class HSlider implements Button {
    private static final Color BG_COLOUR = new Color(100);
    private static final Color FG_COLOUR = new Color(200);

    private final int x;
    private final int y;
    private final Rectangle limits;
    private final Consumer<Float> behaviour;
    private int width;
    private int height;
    private float lowerLimit;
    private float upperLimit;
    private Optional<Color> bgColour;
    private Optional<Color> fgColour;
    private MusicPlayerGUI musicPlayerGUI;
    private float value;

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
    public void listenClick(MouseEvent me) {
        if (limits.contains(me.getX(), me.getY())) {
            this.value = (upperLimit - lowerLimit) * (me.getX() - x) / width + lowerLimit;
            behaviour.accept(this.value);
        }


    }

    @Override
    public void draw() {
        musicPlayerGUI.fill(bgColour.orElse(BG_COLOUR).getRGB());
        musicPlayerGUI.rect(x, y, width, height);
        musicPlayerGUI.fill(fgColour.orElse(FG_COLOUR).getRGB());
        var valWidth = width * ((value - lowerLimit) / (upperLimit - lowerLimit));
        musicPlayerGUI.rect(x + valWidth, y - 5, 5, height + 5);
        musicPlayerGUI.text(value, x, y - 15);

    }

    public float getValue() {
        return value;
    }

    public void setBgColour(Color bgColour) {
        this.bgColour = Optional.ofNullable(bgColour);
    }

    public void setFgColour(Color fgColour) {
        this.fgColour = Optional.ofNullable(fgColour);
    }
}

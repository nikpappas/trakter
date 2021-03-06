package com.nikpappas.music.button;

import com.nikpappas.music.MusicPlayerGUI;
import processing.event.MouseEvent;

import java.util.function.Consumer;

import static com.nikpappas.music.MusicPlayerGUI.LIGHT_GREY;
import static com.nikpappas.music.MusicPlayerGUI.RED;

public class Xfade implements Button {
    HSlider hSlider;
    private boolean active;

    public Xfade(MusicPlayerGUI musicPlayerGUI, int x, int y, int width, int height, Consumer<Float> behaviour) {
        this.hSlider = new HSlider(musicPlayerGUI, x, y, width, height, 0, 1, 0.5f, behaviour);
        hSlider.setBgColour(LIGHT_GREY);
        hSlider.setFgColour(RED);
    }

    @Override
    public void listenMouseRelease(MouseEvent me) {

        hSlider.listenMouseRelease(me);
        active = false;
    }

    public void listenDrag(MouseEvent me) {
        if (active) {
            hSlider.listenMouseRelease(me);
        }
    }

    public void listenPress(MouseEvent me) {
        if (hSlider.contains(me.getX(), me.getY())) {
            active = true;
        }
    }

    @Override
    public void draw() {
        hSlider.draw();
    }

    public void setValue(float val) {
        hSlider.setValue(val);
    }

    public float getValue() {
        return hSlider.getValue();
    }
}

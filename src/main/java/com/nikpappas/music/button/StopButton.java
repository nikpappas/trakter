package com.nikpappas.music.button;

import com.nikpappas.music.player.MusicPlayer;
import com.nikpappas.music.shapes.ResponsiveShapes;
import processing.event.MouseEvent;

import static com.nikpappas.music.MusicPlayerGUI.DARK_GREY;


public class StopButton implements Button {
    private final SquareButton squareButton;

    public StopButton(MusicPlayer player, int x, int y, int width, ResponsiveShapes shapes) {
        this.squareButton = new SquareButton(x, y, width, shapes, player::stop);
        squareButton.setFgColour(DARK_GREY);
    }

    @Override
    public void listenMouseRelease(MouseEvent me) {
        squareButton.listenMouseRelease(me);
    }

    @Override
    public void draw() {
        squareButton.draw();
    }
}

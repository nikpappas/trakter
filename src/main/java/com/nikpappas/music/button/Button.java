package com.nikpappas.music.button;

import processing.event.MouseEvent;

public interface Button {
    void listenClick(MouseEvent me);
    void draw();
}

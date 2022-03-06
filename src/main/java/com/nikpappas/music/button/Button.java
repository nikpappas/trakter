package com.nikpappas.music.button;

import com.nikpappas.music.display.Display;
import processing.event.MouseEvent;

public interface Button extends Display {
    void listenMouseRelease(MouseEvent me);
}

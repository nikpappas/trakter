package com.nikpappas.music;

import com.nikpappas.music.button.Button;
import com.nikpappas.music.button.*;
import com.nikpappas.music.display.Display;
import com.nikpappas.music.display.Waveform;
import com.nikpappas.music.player.MusicPlayer;
import com.nikpappas.music.player.MusicPlayerSample;
import com.nikpappas.music.player.MusicPlayerSound;
import com.nikpappas.music.playlist.PlaylistStructure;
import com.nikpappas.music.shapes.ResponsiveShapes;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DropTarget;
import java.util.List;
import java.util.*;

import static com.nikpappas.music.Player.PLAYER_A;
import static com.nikpappas.music.Player.PLAYER_B;
import static java.lang.String.format;


public class MusicPlayerGUI extends PApplet {

    public static final int BEAT_SENSITIVITY = 15;
    public static final int TITLE_LIMIT = 60;
    public static final float PITCH_LIMITS = 0.2f;

    public static final Color DARK_GREY = new Color(100, 100, 100);
    public static final Color DARKER_GREY = new Color(59, 63, 65);
    public static final Color LIGHT_GREY = new Color(200, 200, 200);
    public static final Color YELLOW = new Color(238, 220, 141);
    public static final Color STROKE = new Color(50, 50, 50);
    public static final Color RED = new Color(200, 60, 50);
    public static final Color BLUE = new Color(100, 60, 150);
    public static final Color GREEN = new Color(69, 173, 83);
    MusicPlayer playerA;
    MusicPlayer playerB;
    EnumMap<Player, MusicPlayer> players = new EnumMap<>(Player.class);

    public static final Set<String> VALID_EXTENTIONS = Set.of("mp3", "wav");
    public static final int X_FADE_TIME = 30;
    public static final int SMALL_COMPONENTS = 20;
    private Volume volumeB;
    private Volume volumeA;
    private Waveform waveA;
    private Waveform waveB;

    public static void main(String[] args) {
        PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
    }

    DropTarget dt = new DropTarget();
    PlaylistStructure playlist = new PlaylistStructure();
    Playlist playlistComponent;


    FileHandler fileHandler = new FileHandler(playlist);
    List<Button> buttons = new ArrayList<>();
    List<Display> displays = new ArrayList<>();

    @Override
    public void settings() {
        size(600, 500);
    }

    @Override
    public void setup() {
//        surface.setResizable(true);

        playerA = new MusicPlayerSound(this);
        playerB = new MusicPlayerSample(this);
        players.put(PLAYER_A, playerA);
        players.put(PLAYER_B, playerB);
        volumeA = new Volume(this, playerA, width / 2 - 40, height / 3 - 30);
        volumeB = new Volume(this, playerB, width - 40, height / 3 - 30);
        playlistComponent = new Playlist(this, 0, height / 3);

        ResponsiveShapes shapes = new ResponsiveShapes(this);
        buttons.add(new PrevButton(this, 10, 10, 30, shapes, dispatchAsync(() -> playlist.getPrevTrack().ifPresent((e) -> loadTrack(PLAYER_A, e)))));
        buttons.add(new PlayButton(50, 10, 30, shapes, () -> playerA.playPause()));
        buttons.add(new StopButton(playerA, 90, 10, 30, shapes));
        buttons.add(new NextButton(130, 10, 30, shapes, dispatchAsync(() -> playlist.getNextTrack().ifPresent((e) -> loadTrack(PLAYER_A, e)))));
        buttons.add(playlistComponent);
        buttons.add(volumeA);
        buttons.add(new Tracker(this, playerA, 0, height / 3 - 30));
        // Reset playing rate
        HSlider rateA = new HSlider(this, 10, height / 3 - 60, width / 3, 20, 1 - PITCH_LIMITS, 1 + PITCH_LIMITS, 1, rate -> playerA.setPlayRate(rate));
        rateA.setBgColour(DARK_GREY);
        rateA.setFgColour(BLUE);
        buttons.add(new SquareButton(width / 2f - 70, height / 3f - 60, SMALL_COMPONENTS, shapes, () -> rateA.setValue(1f)));
        buttons.add(rateA);

        buttons.add(new PrevButton(this, width / 2f + 10, 10, 30, shapes, dispatchAsync(() -> playlist.getPrevTrack().ifPresent((e) -> loadTrack(PLAYER_B, e)))));
        buttons.add(new PlayButton(width / 2f + 50, 10, 30, shapes, () -> playerB.playPause()));
        buttons.add(new StopButton(playerB, width / 2 + 90, 10, 30, shapes));
        buttons.add(new NextButton(width / 2f + 130, 10, 30, shapes, dispatchAsync(() -> playlist.getNextTrack().ifPresent((e) -> loadTrack(PLAYER_B, e)))));
        buttons.add(volumeB);
        buttons.add(new Tracker(this, playerB, width / 2, height / 3 - 30));
        // Reset playing rate
        HSlider rateB = new HSlider(this, width / 2 + 10, height / 3 - 60, width / 3, 20, 1 - PITCH_LIMITS, 1 + PITCH_LIMITS, 1, rate -> playerB.setPlayRate(rate));
        rateB.setBgColour(DARK_GREY);
        rateB.setFgColour(BLUE);

        buttons.add(new SquareButton(width - 70f, height / 3f - 60, SMALL_COMPONENTS, shapes, () -> rateB.setValue(1)));
        buttons.add(rateB);
        waveA = new Waveform(this, 10, 50, width / 3f);
        displays.add(waveA);
        waveB = new Waveform(this, width / 2 + 10, 50, width / 3f);
        displays.add(waveB);
        background(43);
        setUpDragAndDropListener();
    }


    @Override
    public void draw() {
        background(43);
        fill(DARKER_GREY.getRGB());
        noStroke();
        rect(0, 0, width / 2f, height / 3f);
        rect(width / 2f, 0, width / 2f, height / 3f);
        fill(DARK_GREY.getRGB());
        if (playerA.getPlaying() != null) {
            text(truncate(playerA.getPlaying().getDisplayName()), 10, 60);
        }
        if (playerB.getPlaying() != null) {
            text(truncate(playerB.getPlaying().getDisplayName()), width / 2f + 10, 60);
        }
        text(playlist.getIndex(), width - 30f, 10);
        stroke(STROKE.getRGB());
        line(width / 2f, 0, width / 2f, height / 3f);
        buttons.forEach(x -> {
            stroke(STROKE.getRGB());
            x.draw();
        });
        if (playerA.isPlaying() && playerA.isInLast(X_FADE_TIME)) {
            if (!playerB.isPlaying()) {
                var trackToLoad = playlist.getNextTrack();
                trackToLoad.ifPresent(entry -> loadTrack(PLAYER_B, entry));
            }
            volumeB.setVolume(1 - playerA.crossfadePercent());
            volumeA.setVolume(playerA.crossfadePercent());
        }
        if (playerB.isPlaying() && playerB.isInLast(X_FADE_TIME)) {
            if (!playerA.isPlaying()) {
                var trackToLoad = playlist.getNextTrack();
                trackToLoad.ifPresent(entry -> loadTrack(PLAYER_A, entry));
            }
            volumeA.setVolume(1 - playerB.crossfadePercent());
            volumeB.setVolume(playerB.crossfadePercent());
        }
        if (playerA.isBeat()) {
            fill(GREEN.getRGB());
            circle(width / 2f - 60, 20, 20);
        }
        if (playerB.isBeat()) {
            fill(GREEN.getRGB());
            circle(width - 60f, 20, 20);
        }
        text(mouseX + "," + mouseY, width - 60, height - 10);
        waveA.setPosition(playerA.getFramePosition());
        waveB.setPosition(playerB.getFramePosition());
        displays.forEach(Display::draw);
    }

    private String truncate(String displayName) {
        if (displayName.length() < TITLE_LIMIT) {
            return displayName;
        } else {
            return format("%s...", displayName.substring(0, TITLE_LIMIT - 3));
        }
    }


    public Runnable dispatchAsync(Runnable run) {
        return () -> {
            var thread = new Thread(run);
            thread.start();
        };

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == 32) {
            playerA.playPause();
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        playlistComponent.listenClick(e);
    }


    @Override
    public void mouseReleased(MouseEvent me) {
        buttons.forEach(b -> b.listenMouseRelease(me));
    }

    @Override
    public void mousePressed(MouseEvent me) {
        playlistComponent.mousePressed(me);
    }


    @Override
    public void mouseDragged(MouseEvent me) {
        playlistComponent.listenDragged(me);
    }

    @Override
    public void mouseWheel(MouseEvent me) {
        playlistComponent.scroll(me.getCount());
    }


    public PlaylistStructure getPlaylist() {
        return playlist;
    }

    public void loadTrack(PlaylistEntry entry) {
        loadTrack(PLAYER_A, entry);
    }

    public void loadTrack(Player playerEnum, PlaylistEntry entry) {
        var player = players.get(playerEnum);
        if (playerEnum.equals(PLAYER_A)) {
            playlist.setPlayingA(entry);
        } else {
            playlist.setPlayingB(entry);
        }
        player.loadFile(entry);
        if (playerEnum.equals(PLAYER_A)) {
            waveA.setValues(player.getBuffer());
        } else {
            waveB.setValues(player.getBuffer());
        }

        player.play();
    }

    private void setUpDragAndDropListener() {
        JFrame frame;
        frame = (JFrame) ((processing.awt.PSurfaceAWT.SmoothCanvas) surface.getNative()).getFrame();
        frame.setDropTarget(dt);
        try {
            dt.addDropTargetListener(new DragAndDropListener(fileHandler));
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }
}


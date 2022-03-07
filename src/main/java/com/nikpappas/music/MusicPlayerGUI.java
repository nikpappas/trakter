package com.nikpappas.music;

import com.nikpappas.music.button.Button;
import com.nikpappas.music.button.*;
import com.nikpappas.music.display.Display;
import com.nikpappas.music.display.Waveform;
import com.nikpappas.music.player.MusicPlayer;
import com.nikpappas.music.player.MusicPlayerSample;
import com.nikpappas.music.player.MusicPlayerSound;
import com.nikpappas.music.playlist.PlaylistCacheThread;
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

    public static final int BEAT_SENSITIVITY = 40;
    public static final int TITLE_LIMIT = 40;
    public static final int PLAYLIST_TITLE_LIMIT = 90;
    public static final float PITCH_LIMITS = 0.2f;
    public static final float X_FADER_WIDTH = 0.7f;


    public static final Color DARK_GREY = new Color(100, 100, 101);
    public static final Color DARKER_GREY = new Color(59, 63, 65);
    public static final Color LIGHT_GREY = new Color(200, 200, 202);
    public static final Color YELLOW = new Color(245, 228, 154);
    public static final Color STROKE = new Color(50, 50, 50);
    public static final Color RED = new Color(166, 47, 37);
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
    private boolean loadingA;
    private boolean loadingB;
    private Xfade xfade;

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
        size(600, 600);
    }

    @Override
    public void setup() {
//        surface.setResizable(true);
        dispatchAsync(new PlaylistCacheThread(this, playlist)).run();
        playerA = new MusicPlayerSound(this);
        playerB = new MusicPlayerSample(this);
        players.put(PLAYER_A, playerA);
        players.put(PLAYER_B, playerB);
        volumeA = new Volume(this, playerA, width / 2 - 40, height / 3 - 30, LEFT);
        volumeB = new Volume(this, playerB, width - 40, height / 3 - 30, RIGHT);

        ResponsiveShapes shapes = new ResponsiveShapes(this);
        buttons.add(new PrevButton(this, 10, 30, 30, shapes,
                dispatchAsync(() -> playlist.getPrevTrack().ifPresent(e -> loadTrackAndPlay(PLAYER_A, e)))));
        buttons.add(new PlayButton(50, 30, 30, shapes, () -> playerA.playPause()));
        buttons.add(new StopButton(playerA, 90, 30, 30, shapes));
        buttons.add(new NextButton(130, 30, 30, shapes,
                dispatchAsync(() -> playlist.getNextTrack().ifPresent(e -> loadTrackAndPlay(PLAYER_A, e)))));
        buttons.add(volumeA);
        buttons.add(new Tracker(this, playerA, 0, height / 3 - 30));
        // Reset playing rate
        HSlider rateA = new HSlider(this, 10, height / 3 - 60, width / 3, 20, 1 - PITCH_LIMITS, 1 + PITCH_LIMITS, 1, rate -> playerA.setPlayRate(rate));
        rateA.setBgColour(DARK_GREY);
        rateA.setFgColour(BLUE);
        rateA.setDrawText(true);
        buttons.add(new SquareButton(width / 2f - 70, height / 3f - 60, SMALL_COMPONENTS, shapes, () -> rateA.setValue(1f)));
        buttons.add(rateA);
        waveA = new Waveform(this, 10, 70, width / 3f, 60);
        displays.add(waveA);


        buttons.add(new PrevButton(this, width / 2f + 10, 30, 30, shapes,
                dispatchAsync(() -> playlist.getPrevTrack().ifPresent(e -> loadTrackAndPlay(PLAYER_B, e)))));
        buttons.add(new PlayButton(width / 2f + 50, 30, 30, shapes, () -> playerB.playPause()));
        buttons.add(new StopButton(playerB, width / 2 + 90, 30, 30, shapes));
        buttons.add(new NextButton(width / 2f + 130, 30, 30, shapes,
                dispatchAsync(() -> playlist.getNextTrack().ifPresent(e -> loadTrackAndPlay(PLAYER_B, e)))));
        buttons.add(volumeB);
        buttons.add(new Tracker(this, playerB, width / 2, height / 3 - 30));
        // Reset playing rate
        HSlider rateB = new HSlider(this, width / 2 + 10, height / 3 - 60, width / 3, 20, 1 - PITCH_LIMITS, 1 + PITCH_LIMITS, 1, rate -> playerB.setPlayRate(rate));
        rateB.setBgColour(DARK_GREY);
        rateB.setFgColour(BLUE);
        rateB.setDrawText(true);

        buttons.add(new SquareButton(width - 70f, height / 3f - 60, SMALL_COMPONENTS, shapes, () -> rateB.setValue(1)));
        buttons.add(rateB);
        waveB = new Waveform(this, width / 2 + 10, 70, width / 3f, 60);
        displays.add(waveB);
        xfade = new Xfade(this, (int) (width * (1 - X_FADER_WIDTH) / 2), height / 3 + 10, (int) (width * X_FADER_WIDTH), 30, x -> {
            volumeA.update();
            volumeB.update();
        });
        buttons.add(xfade);

        playlistComponent = new Playlist(this, 0, height / 3 + 50);
        buttons.add(playlistComponent);

        background(43);
        setUpDragAndDropListener();
    }


    @Override
    public void draw() {
        background(43);
        fill(DARKER_GREY.getRGB());
        noStroke();
        rect(0, 0, width / 2f, height / 3f + 60);
        rect(width / 2f, 0, width / 2f, height / 3f + 60);
        fill(LIGHT_GREY.getRGB());
        if (playerA.getPlaying() != null) {
            text(truncate(playerA.getPlaying().getDisplayName()), 10, 15);
        }
        if (playerB.getPlaying() != null) {
            text(truncate(playerB.getPlaying().getDisplayName()), width / 2f + 10, 15);
        }
        text(playlist.getIndex(), width - 30f, 10);
        fill(DARK_GREY.getRGB());
        rect(width / 2f-2, 0, 4, height / 3f+60);
        buttons.forEach(x -> {
            stroke(STROKE.getRGB());
            x.draw();
        });
        updateCrosfade();
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

    private void updateCrosfade() {
        if (playerA.isPlaying() && playerA.isInLast(X_FADE_TIME)) {
            if (!playerB.isPlaying() && !loadingB) {
                var trackToLoad = playlist.getNextTrack();
                if (trackToLoad.isPresent()) {
                    loadingB = true;
                    dispatchAsync(() -> loadTrackAndPlay(PLAYER_B, trackToLoad.get())).run();
                }
            }
            xfade.setValue(1 - playerA.crossfadePercent());
            return;
        }
        if (playerB.isPlaying() && playerB.isInLast(X_FADE_TIME)) {
            if (!playerA.isPlaying() && !loadingA) {
                var trackToLoad = playlist.getNextTrack();
                if (trackToLoad.isPresent()) {
                    loadingA = true;
                    dispatchAsync(() -> loadTrackAndPlay(PLAYER_A, trackToLoad.get())).run();
                }
            }
            xfade.setValue(playerB.crossfadePercent());
        }

    }

    public Xfade getXfade() {
        return xfade;
    }

    private static String truncate(String displayName) {
        return truncate(displayName, TITLE_LIMIT);

    }

    public static String truncate(String displayName, int limit) {
        if (displayName.length() < limit) {
            return displayName;
        } else {
            return format("%s...", displayName.substring(0, limit - 3));
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
        if(ke.getKeyCode()==BACKSPACE){
            playlist.removeSelected();
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
        xfade.listenDrag(me);
    }

    @Override
    public void mouseWheel(MouseEvent me) {
        playlistComponent.scroll(me.getCount());
    }


    public PlaylistStructure getPlaylist() {
        return playlist;
    }

    public void loadTrackAndPlay(Player playerEnum, PlaylistEntry entry) {
        loadTrack(playerEnum, entry);
        var player = players.get(playerEnum);
        player.play();

    }

    public void loadTrack(Player playerEnum, PlaylistEntry entry) {
        var player = players.get(playerEnum);
        if (playerEnum.equals(PLAYER_A)) {
            playlist.setPlayingA(entry);
        } else {
            playlist.setPlayingB(entry);
        }
//        if (entry.getSound() == null) {
        player.loadFile(entry);
//        } else {
//            player.loadSound(entry);
//        }

        if (playerEnum.equals(PLAYER_A)) {
            waveA.setValues(player.getBuffer());
            loadingA = false;
        } else {
            waveB.setValues(player.getBuffer());
            loadingB = false;
        }

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

    public static String secToTimeString(float seconds) {
        var absSec = abs(seconds);
        var min = (int) absSec / 60;
        var sec = (int) absSec - min * 60;
        var dec = (int) ((absSec - (int) absSec) * 10);
        return String.format("%02d:%02d:%1d", min, sec, dec);
    }
}


package com.nikpappas.music;

import com.nikpappas.music.button.Button;
import com.nikpappas.music.button.*;
import com.nikpappas.music.player.MusicPlayer;
import com.nikpappas.music.player.MusicPlayerSample;
import com.nikpappas.music.player.MusicPlayerSound;
import com.nikpappas.music.shapes.ResponsiveShapes;
import processing.core.PApplet;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.sound.BeatDetector;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.nikpappas.music.Player.PLAYER_A;
import static com.nikpappas.music.Player.PLAYER_B;


public class MusicPlayerGUI extends PApplet {
    public static final Color RED = new Color(200, 60, 50);
    public static final Color GREEN = new Color(60, 180, 76);
    BeatDetector beatDetector;
    MusicPlayer playerA;
    MusicPlayerSound playerB;
    EnumMap<Player, MusicPlayer> players = new EnumMap<>(Player.class);

    public static final Set<String> VALID_EXTENTIONS = Set.of("mp3", "wav");
    private Volume volumeB;
    private Volume volumeA;

    public static void main(String[] args) {
        PApplet.main(Thread.currentThread().getStackTrace()[1].getClassName());
    }

    DropTarget dt = new DropTarget();
    LinkedList<PlaylistEntry> playlist = new LinkedList<>();
    Playlist playlistComponent;


    int index = 0;
    FileHandler fileHandler = new FileHandler(playlist);
    List<Button> buttons = new ArrayList<>();

    @Override
    public void settings() {
        size(500, 400);
    }

    @Override
    public void setup() {
//        surface.setResizable(true);
        beatDetector = new BeatDetector(this);

        playerA = new MusicPlayerSample(this);
        playerB = new MusicPlayerSound(this);
        players.put(PLAYER_A, playerA);
        players.put(PLAYER_B, playerB);
        volumeA = new Volume(this, playerA, width / 2 - 40, height / 3 - 30);
        volumeB = new Volume(this, playerB, width - 40, height / 3 - 30);
        playlistComponent = new Playlist(this, 0, height / 3);

        ResponsiveShapes shapes = new ResponsiveShapes(this);
        buttons.add(new PrevButton(this, 10, 10, 30, shapes, loadTrackToPlayer(playerA, this::getPrevTrack, t -> t.setPlayingA(true))));
        buttons.add(new PlayButton(50, 10, 30, shapes, () -> playerA.playPause()));
        buttons.add(new StopButton(90, 10, 30, shapes, () -> playerA.stop()));
        buttons.add(new NextButton(130, 10, 30, shapes, loadTrackToPlayer(playerA, this::getNextTrack, t -> t.setPlayingA(true))));
        buttons.add(playlistComponent);
        buttons.add(volumeA);
        buttons.add(new Tracker(this, playerA, 0, height / 3 - 30));

        buttons.add(new PrevButton(this, width / 2 + 10, 10, 30, shapes, loadTrackToPlayer(playerB, this::getPrevTrack, t -> t.setPlayingB(true))));
        buttons.add(new PlayButton(width / 2 + 50, 10, 30, shapes, () -> playerB.playPause()));
        buttons.add(new StopButton(width / 2 + 90, 10, 30, shapes, () -> playerB.stop()));
        buttons.add(new NextButton(width / 2 + 130, 10, 30, shapes, loadTrackToPlayer(playerB, this::getNextTrack, t -> t.setPlayingB(true))));
        buttons.add(volumeB);
        buttons.add(new Tracker(this, playerB, width / 2, height / 3 - 30));

        background(43);
        JFrame frame;
        frame = (JFrame) ((processing.awt.PSurfaceAWT.SmoothCanvas) surface.getNative()).getFrame();
        frame.setDropTarget(dt);
        try {
            dt.addDropTargetListener(new Listen(fileHandler));
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void draw() {
        background(43);
        fill(59, 63, 65);
        noStroke();
        rect(0, 0, width / 2f, height / 3f);
        rect(width / 2f, 0, width / 2f, height / 3f);
        fill(100);
        if (playerA.getPlaying() != null) {
            text(playerA.getPlaying().getDisplayName(), 10, 60);
        }
        if (playerB.getPlaying() != null) {
            text(playerB.getPlaying().getDisplayName(), width / 2f + 10, 60);
        }
        text(index, width - 30f, 10);
        stroke(50);
        line(width / 2f, 0, width / 2f, height / 3f);
        buttons.forEach(x -> {
            stroke(50);
            x.draw();
        });
        if (playerA.isPlaying() && playerA.isInLast(30)) {
            if (!playerB.isPlaying()) {
                var trackToLoad = getNextTrack();
                trackToLoad.ifPresent(entry -> loadTrack(PLAYER_B, entry));
            }
            volumeB.setVolume(1 - playerA.crossfadePercent());
            volumeA.setVolume(playerA.crossfadePercent());
        }
        if (playerB.isPlaying() && playerB.isInLast(30)) {
            if (!playerA.isPlaying()) {
                var trackToLoad = getNextTrack();
                trackToLoad.ifPresent(entry -> loadTrack(PLAYER_A, entry));
            }
            volumeA.setVolume(1 - playerB.crossfadePercent());
            volumeB.setVolume(playerB.crossfadePercent());
        }
        if (beatDetector.isBeat()) {
            fill(GREEN.getRGB());
            circle(width / 2, 100, 20);
        }

    }

    private Runnable loadTrackToPlayer(MusicPlayer player, Supplier<Optional<PlaylistEntry>> supplier, Consumer<PlaylistEntry> consumer) {

        var track = supplier.get();
        if (track.isEmpty()) {
            return () -> {
            };
        }
        playlist.stream().filter(x -> x.getFile().equals(player.getPlaying().getFile())).forEach(x -> {
            x.setPlayingA(false);
            x.setPlayingB(false);

        });
        consumer.accept(track.get());
        return dispatchAsync(() -> player.loadFile(track.get()));
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
        buttons.forEach(b -> b.listenClick(e));
    }


    @Override
    public void mouseReleased(MouseEvent me) {
        playlistComponent.mouseReleased(me);
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

    private Optional<PlaylistEntry> getNextTrack() {
        if (playlist.isEmpty()) return Optional.empty();
        index = (index + 1) % playlist.size();
        return Optional.ofNullable(getTrack(index));
    }

    private Optional<PlaylistEntry> getPrevTrack() {
        if (playlist.isEmpty()) return Optional.empty();
        index = index - 1;
        if (index < 0) {
            index = playlist.size() - 1;
        }

        return Optional.ofNullable(getTrack(index));
    }

    private PlaylistEntry getTrack(int i) {
        if (i < 0 || playlist.isEmpty()) {
            return null;
        }
        System.out.println(playlist.get(i));
        return playlist.get(i);
    }

    public LinkedList<PlaylistEntry> getPlaylist() {
        return playlist;
    }

    public void loadTrack(PlaylistEntry entry) {
        loadTrack(PLAYER_A, entry);
    }

    public void loadTrack(Player playerEnum, PlaylistEntry entry) {
        var player = players.get(playerEnum);
        if (playerEnum.equals(PLAYER_A)) {
            entry.setPlayingA(true);
            playlist.stream().filter(x -> !x.equals(entry)).forEach(x -> x.setPlayingA(false));
        } else {
            playlist.stream().filter(x -> !x.equals(entry)).forEach(x -> x.setPlayingB(false));
            entry.setPlayingB(true);
        }
        player.loadFile(entry);
        player.play();
    }
}

class Listen implements DropTargetListener {
    FileHandler fileHandler;

    public Listen(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        System.out.println("Enter");
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        System.out.println("Over");
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        System.out.println("Changed");

    }

    @Override
    public void dragExit(DropTargetEvent dte) {
        System.out.println("Exit");

    }

    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            dtde.acceptDrop(DnDConstants.ACTION_REFERENCE);
            var filePath = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
            System.out.println(filePath);
            fileHandler.loadFiles(filePath);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }

    }
}
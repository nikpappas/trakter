package com.nikpappas.music;

import com.nikpappas.music.button.Playlist;

import java.io.File;
import java.util.List;

import static com.nikpappas.music.MusicPlayerGUI.VALID_EXTENTIONS;

public class FileHandler {
    private final List<PlaylistEntry> playlist;

    public FileHandler(List<PlaylistEntry> playlist) {
        this.playlist = playlist;
    }

    public void loadFiles(List<File> files){
        files.stream()
                .filter(x -> x.isFile() && checkExtentions(x))
                .forEach(x -> {
                    System.out.println(x);
                    playlist.add(PlaylistEntry.of(x.toString(), x.getName()));
                });
    }

    private boolean checkExtentions(File x){
        System.out.println("FILE"+x);
        return checkExtentions(x.toString());
    }
    private boolean checkExtentions(String x){
        var toks = x.split("\\.");
        if(toks.length<=1){
            return false;
        }
        for(String ext: VALID_EXTENTIONS){
            if(toks[toks.length-1].equalsIgnoreCase(ext)){
                return true;
            }
        }
        return false;
    }

}

package com.nikpappas.music;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DragAndDropListener implements DropTargetListener {

    FileHandler fileHandler;

    public DragAndDropListener(FileHandler fileHandler) {
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
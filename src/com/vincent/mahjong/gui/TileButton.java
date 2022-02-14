package com.vincent.mahjong.gui;

import javax.swing.*;

public class TileButton extends JToggleButton {
    String tile;

    public TileButton(String tile, ImageIcon icon){
        this.tile = tile;
        this.setIcon(icon);
    }

    public String getTile(){
        return this.tile;
    }
}

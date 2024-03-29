package com.vincent.mahjong.gui;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;

@Getter
public class TileButton extends JToggleButton {

    String tile;

    public TileButton(String tile, ImageIcon icon){
        this.tile = tile;
        this.setIcon(icon);
    }
}

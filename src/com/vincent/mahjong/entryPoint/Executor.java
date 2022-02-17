package com.vincent.mahjong.entryPoint;

import com.vincent.mahjong.gui.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Executor extends JFrame{
    JButton beginButton;
    JPanel mainPanel;

    public Executor(){
        beginButton = new JButton("开始答题");
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 500));
        mainPanel.add(beginButton);

        beginButton.addActionListener(e-> {
            try {
                dispose();
                new MainFrame();
            } catch (UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (InstantiationException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        setTitle("何切300/301训练");
        add(mainPanel);
        setVisible(true);
        pack();
    }

    public static void main(String[] args)
        throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        new Executor();
    }
}

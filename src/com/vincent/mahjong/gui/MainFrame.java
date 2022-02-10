package com.vincent.mahjong.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame implements ActionListener {

    JFrame mainFrame = new JFrame("何切300系统");

    // 题号和总分
    int index = 0,total = 0;

    /**
     * Set up the main frame
     */
    public MainFrame(){
        mainFrame.setSize(400,400);
        mainFrame.setLocation(50,50);
        mainFrame.setVisible(true);

        mainFrame.setLayout(new GridLayout(4,1,10,10));
        try
        {
            //系统风格
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");//Nimbus风格
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void reset(){

    }

    private void determineAnswer(){

    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

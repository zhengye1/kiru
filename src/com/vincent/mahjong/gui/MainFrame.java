package com.vincent.mahjong.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhibo
 */
public class MainFrame extends JFrame implements ActionListener {

    // Create ConditionPanel
    JPanel conditionPanel = new JPanel();

    // Create Hand Panel
    JPanel handPanel = new JPanel();

    // Create confirm panel
    JPanel confirmPanel = new JPanel();

    // Crate Result Panel
    JPanel resultPanel = new JPanel();

    // Create Analyze Panel
    JPanel analyzePanel = new JPanel();

    JButton submitButton = new JButton("提交");
    JButton nextButton = new JButton("下一题");
    JCheckBox riichiCheck = new JCheckBox("立直");

    ButtonGroup handGroup = new ButtonGroup();
    TileButton[] hands = new TileButton[14];

    Map<String, TileButton> tileMap = new HashMap<>();
    Map<String, ImageIcon> tileImageMap = new HashMap<>();
    Map<String, ImageIcon> displayDoraMap = new HashMap<>();

    public MainFrame()
        throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        initComponents();
    }

    private void initComponents()
        throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Dimension tileDimension = new Dimension(30, 50);
        String[] tileString =
            {"0m", "1m", "2m", "3m", "4m", "5m", "6m", "7m", "8m", "9m", "0p", "1p", "2p", "3p", "4p", "5p", "6p", "7p",
                "8p", "9p", "0s", "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "1z", "2z", "3z", "4z", "5z",
                "6z", "7z"};

        // Create Icon
        // Create ToggleButton
        for (String tile : Arrays.asList(tileString)) {
            ImageIcon icon = new ImageIcon(getClass().getResource("../../../../resources/" + tile + ".png"));
            tileImageMap.put(tile, icon);
            TileButton tileButton = new TileButton(tile, icon);
            tileButton.addActionListener(this);
            tileMap.put(tile, tileButton);
            displayDoraMap.put("d" + tile,
                new ImageIcon(getClass().getResource("../../../../resources/d" + tile + ".png")));
        }

        submitButton.addActionListener(this);
        nextButton.addActionListener(this);
        riichiCheck.addActionListener(this);

        confirmPanel.add(submitButton);
        confirmPanel.add(nextButton);

        hands[0] = tileMap.get("6p");
        hands[1] = tileMap.get("7p");
        hands[2] = tileMap.get("7p");
        hands[3] = tileMap.get("8p");
        hands[4] = tileMap.get("1s");
        hands[5] = tileMap.get("1s");
        hands[6] = tileMap.get("2s");
        hands[7] = tileMap.get("2s");
        hands[8] = tileMap.get("3s");
        hands[9] = tileMap.get("4s");
        hands[10] = tileMap.get("5s");
        hands[11] = tileMap.get("7z");
        hands[12] = tileMap.get("7z");
        hands[13] = tileMap.get("0p");

        for (int i = 0; i < 14; i++) {
            handGroup.add(hands[i]);
            handPanel.add(hands[i]);
        }

        handPanel.add(riichiCheck);
        JLabel conditionLabel = new JLabel("场况");
        JLabel resultLabel = new JLabel("结果");
        JLabel analyzeLabel = new JLabel("解析");

        conditionLabel.setIcon(displayDoraMap.get("d2z"));
        conditionPanel.add(conditionLabel);
        resultPanel.add(resultLabel);
        analyzePanel.add(analyzeLabel);

        add(conditionPanel);
        add(handPanel);
        add(confirmPanel);
        add(resultPanel);
        add(analyzePanel);

        setTitle("何切300/301训练");
        setLayout(new GridLayout(5, 1));
        setSize(1024, 1024);
        setLocation(50, 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == riichiCheck && riichiCheck.isSelected()) {
            System.out.println("你选择了立直");
        } else {
            Object source = e.getSource();
            if (source instanceof TileButton) {
                System.out.println(((TileButton)source).getTile());
            }

        }
    }
}
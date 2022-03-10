package com.vincent.mahjong.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * @author Vincent Zheng
 */
public class MainFrame extends JFrame implements ActionListener {

    // Create ConditionPanel
    JPanel conditionPanel = new JPanel();
    JLabel situationLabel;
    JLabel displayDoraLabel;

    int averageAnalyzeHeight = 0;
    // Create Hand Panel
    JPanel handPanel = new JPanel();

    //fulou panel
    JPanel fulouPanel = new JPanel();

    // Create confirm panel
    JPanel confirmPanel = new JPanel();

    // Crate Result Panel
    JPanel resultPanel = new JPanel();

    // Create Analyze Panel
    JPanel analyzePanel = new JPanel();
    JLabel analyzeLabel = new JLabel();
    JLabel bookAnalyzeLabel = new JLabel();

    JButton submitButton = new JButton("提交");
    JButton nextButton = new JButton("下一题");
    JCheckBox riichiCheck = new JCheckBox("立直");

    ButtonGroup handGroup = new ButtonGroup();
    List<TileButton> hands = new ArrayList<>(14);
    String text = "你的选择是";
    JLabel choiceTextLabel = new JLabel(text);
    JLabel choiceTileLabel = new JLabel();
    JLabel riichiLabel = new JLabel("立直");

    String correctPercentageText = "";
    JLabel correctPercentageLabel = new JLabel(correctPercentageText);

    List<Question> questions = new ArrayList<>(300);
    Question q;
    String choice = "";
    int correctAnswer = 0, totalQuestion = 0;
    int qIndex = 0;
    TileButton previousSelection;
    Border compound;
    Border cyanBorder;
    Border defaultBorder;

    Map<String, ImageIcon> tileImageMap = new HashMap<>();
    Map<String, ImageIcon> displayDoraMap = new HashMap<>();
    Map<String, ImageIcon> analyzeMap = new HashMap<>();
    final String ROOTPATH = "../../../../resources/";

    public MainFrame()
        throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException,
        IOException {
        initComponents();
    }

    private void initComponents()
        throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException,
        IOException {
        defaultBorder = UIManager.getBorder("Button.border");
        cyanBorder = BorderFactory.createLineBorder(Color.CYAN, 2);
        compound = BorderFactory.createCompoundBorder(cyanBorder, defaultBorder);

        String[] tileString =
            {"0m", "1m", "2m", "3m", "4m", "5m", "6m", "7m", "8m", "9m", "0p", "1p", "2p", "3p", "4p", "5p", "6p", "7p",
                "8p", "9p", "0s", "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "1z", "2z", "3z", "4z", "5z",
                "6z", "7z"};

        // Create Icon
        // Create ToggleButton
        for (String tile : Arrays.asList(tileString)) {
            ImageIcon icon = new ImageIcon(getClass().getResource(ROOTPATH + tile + ".png"));
            tileImageMap.put(tile, icon);
            displayDoraMap.put("d" + tile, new ImageIcon(getClass().getResource(ROOTPATH + "d" + tile + ".png")));
        }

        // load all the analyze picture
        for (int i = 1; i <= 300; i++) {
            String index = String.format("%03d", i);
            ImageIcon analyzeIcon = new ImageIcon(getClass().getResource(ROOTPATH + index + "a.jpg"));
            averageAnalyzeHeight += analyzeIcon.getIconHeight();
            System.out.println("Index: " + index + " analyzeIcon Height?" + analyzeIcon.getIconHeight());
            analyzeMap.put("" + index, analyzeIcon);
        }

        submitButton.addActionListener(this);
        nextButton.addActionListener(this);
        nextButton.setEnabled(false);
        riichiCheck.addActionListener(this);

        confirmPanel.add(submitButton);
        confirmPanel.add(nextButton);

        situationLabel = new JLabel();
        displayDoraLabel = new JLabel();
        //analyzePanel.setPreferredSize(new Dimension(900, 300));
        analyzeLabel = new JLabel("解析");
        //analyzePanel.add(analyzeLabel);

        // Load the question
        loadQuestion();
        JLabel resultLabel = new JLabel("结果");
        resultPanel.add(resultLabel);
        //load the first question
        q = questions.get(qIndex);
        createQuestion(q);

        resultPanel.add(correctPercentageLabel);

        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        add(conditionPanel, c);

        c.gridy = 1;
        add(handPanel, c);

        c.gridy = 2;
        add(confirmPanel, c);
        c.gridy = 3;
        add(resultPanel, c);
        c.gridy = 4;
        add(analyzePanel, c);

        setTitle("何切300/301训练");
        setLocation(50, 50);
        setPreferredSize(new Dimension(1024, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setVisible(true);
    }

    private void createQuestion(Question q) {

        // 导入场况
        situationLabel.setText(q.getSituation());
        displayDoraLabel.setIcon(displayDoraMap.get("d" + q.getDisplayDora()));
        conditionPanel.add(situationLabel);
        conditionPanel.add(displayDoraLabel);

        // 导入手牌
        // 因为用的是数字+牌种类的形式，所以长度肯定能整除2
        String[] hands = q.getHands().split("(?<=\\G.{2})");

        //清空handGroup的东西
        Collections.list(handGroup.getElements()).stream().forEach((ab) -> {
            handGroup.remove(ab);
        });

        // 清空hand panel的东西
        Arrays.asList(handPanel.getComponents()).stream().forEach((ab) -> {
            handPanel.remove(ab);
        });

        for (String hand : hands) {
            TileButton handButton = new TileButton(hand, tileImageMap.get(hand));
            handButton.addActionListener(this);
            handButton.setPreferredSize(new Dimension(45, 55));
            handGroup.add(handButton);
            handPanel.add(handButton);
        }

        String fulous = q.getFulous();

        if (fulous != null && fulous.length() != 0) {
            // 用天凤牌谱的写法分别是 c1m2m3m 1p1pp1p
            // 这暂时不考虑有杠的情况
            // 也就是 c = 吃只能出现在index 0, 而p = 碰只能出现在index 0, index 2, index 4
            int index = 0;
            while (index != fulous.length()) {
                String fulou = fulous.substring(index, 7);
                JLabel firstTile = new JLabel();
                JLabel secondTile = new JLabel();
                JLabel thirdTile = new JLabel();

                // 吃上家或者碰上家的情况
                if ("c".equals(fulou.substring(0, 1)) || "p".equals(fulou.substring(0, 1))) {
                    firstTile.setIcon(
                        new RotatedIcon(tileImageMap.get(fulou.substring(1, 3)), RotatedIcon.Rotate.DOWN));
                    secondTile.setIcon(tileImageMap.get(fulou.substring(3, 5)));
                    thirdTile.setIcon(tileImageMap.get(fulou.substring(5, 7)));
                } else {
                    // 碰的情况
                    if ("p".equals(fulou.substring(2, 3))) {
                        ImageIcon tileIcon = tileImageMap.get(fulou.substring(0, 2));
                        secondTile.setIcon(new RotatedIcon(tileIcon, RotatedIcon.Rotate.DOWN));
                        firstTile.setIcon(tileIcon);
                        thirdTile.setIcon(tileIcon);
                    } else {
                        if ("p".equals(fulou.substring(4, 5))) {
                            ImageIcon tileIcon = tileImageMap.get(fulou.substring(0, 2));
                            thirdTile.setIcon(new RotatedIcon(tileIcon, RotatedIcon.Rotate.DOWN));
                            secondTile.setIcon(tileIcon);
                            firstTile.setIcon(tileIcon);
                        }
                    }
                }
                index = index + 7;
                fulouPanel.add(firstTile);
                fulouPanel.add(secondTile);
                fulouPanel.add(thirdTile);
            }
            handPanel.add(fulouPanel);
            riichiCheck.setVisible(false);
        } else {
            riichiCheck.setSelected(false);
            riichiCheck.setVisible(true);
            riichiCheck.setEnabled(true);
        }

        handPanel.add(riichiCheck);

        if (choiceTextLabel.getParent() != resultPanel) {
            resultPanel.add(choiceTextLabel);
        }
        if (choiceTileLabel.getParent() != resultPanel) {
            resultPanel.add(choiceTileLabel);
        }
        if (riichiLabel.getParent() != resultPanel) {
            resultPanel.add(riichiLabel);
        }

        choiceTextLabel.setVisible(false);
        choiceTileLabel.setVisible(false);
        riichiLabel.setVisible(false);

        submitButton.setEnabled(true);
        nextButton.setEnabled(false);
        bookAnalyzeLabel.setIcon(analyzeMap.get(q.getQNo()));
        bookAnalyzeLabel.setVisible(false);
        analyzePanel.add(bookAnalyzeLabel);
        //revalidate();

    }

    private void loadQuestion() throws IOException {
        //打开题目文件
        InputStream in = getClass().getResourceAsStream(ROOTPATH + "kiru.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        while (reader.ready()) {
            String line = reader.readLine();
            setQuestion(line);
        }
        Collections.shuffle(questions);
        System.out.println("问题已经加载" + questions);
    }

    private void setQuestion(String line) {
        String[] split = line.split("&&");
        System.out.println(Arrays.asList(split));
        Question q = new Question();
        // 原题号
        q.setQNo(split[0]);
        // 场况
        q.setSituation(split[1]);
        // 宝牌指示牌
        q.setDisplayDora(split[2]);
        // 手牌 （包括副露）
        String[] hands = split[3].split("\\|");
        q.setHands(hands[0]);
        if (hands.length > 1) {
            // 有副露
            q.setFulous(hands[1]);
        }
        // 答案
        q.setAnswer(split[4]);
        questions.add(q);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == riichiCheck && riichiCheck.isSelected()) {
            if (riichiCheck.isSelected()) {
                riichiLabel.setVisible(true);
            } else {
                riichiLabel.setVisible(false);
            }
            System.out.println("你选择了立直");
        } else {
            Object source = e.getSource();
            if (source instanceof TileButton) {
                if (previousSelection != null) {
                    previousSelection.setBorder(defaultBorder);
                    revalidate();
                }
                ((TileButton)source).setBorder(compound);
                ((TileButton)source).setBorderPainted(true);
                String tile = ((TileButton)source).getTile();
                choice = tile;
                choiceTileLabel.setIcon(tileImageMap.get(tile));

                choiceTextLabel.setVisible(true);
                choiceTileLabel.setVisible(true);
                if (riichiCheck.isSelected()) {
                    riichiLabel.setVisible(true);
                }

                previousSelection = ((TileButton)source);
                revalidate();
            }

            if (source == submitButton) {
                totalQuestion++;
                qIndex++;
                Enumeration<AbstractButton> enumeration = handGroup.getElements();
                if (handGroup.getSelection() == null) {
                    JOptionPane.showMessageDialog(this, "你还没选择", "Alert", JOptionPane.WARNING_MESSAGE);
                } else {
                    while (enumeration.hasMoreElements()) {
                        enumeration.nextElement().setEnabled(false);
                    }
                    riichiCheck.setEnabled(false);
                    submitButton.setEnabled(false);

                    // 分析结果
                    choice += (riichiCheck.isSelected()) ? "R" : "";
                    System.out.println("Choice " + choice + " answer " + q.getAnswer());
                    if (choice.equals(q.getAnswer())) {
                        correctAnswer++;
                    }

                    correctPercentageText =
                        ("目前你答对" + totalQuestion + "中的" + correctAnswer + "题，正确率为" + String.format("%.2f",
                            (correctAnswer / (totalQuestion * 1.0)) * 100) + "%");
                    correctPercentageLabel.setText(correctPercentageText);
                    resultPanel.add(correctPercentageLabel);
                    bookAnalyzeLabel.setVisible(true);
                    revalidate();

                    if ((qIndex == questions.size())) {
                        nextButton.setEnabled(false);
                    } else {
                        nextButton.setEnabled(true);
                    }

                }
            }

            if (source == nextButton) {
                q = questions.get(qIndex);
                createQuestion(q);
            }
        }
    }
}
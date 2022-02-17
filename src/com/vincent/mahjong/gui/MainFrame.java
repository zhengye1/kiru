package com.vincent.mahjong.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;

/**
 * @author zhibo
 */
public class MainFrame extends JFrame implements ActionListener {

    // Create ConditionPanel
    JPanel conditionPanel = new JPanel();
    JLabel situationLabel;
    JLabel displayDoraLabel;

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
    JLabel bookAnalyzeIcon = new JLabel();

    JButton submitButton = new JButton("提交");
    JButton nextButton = new JButton("下一题");
    JCheckBox riichiCheck = new JCheckBox("立直");

    ButtonGroup handGroup = new ButtonGroup();
    List<TileButton> hands = new ArrayList<>(14);
    String text = "你的选择是";
    JLabel choiceTextLabel = new JLabel(text);
    JLabel choiceTileLabel = new JLabel();

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
            displayDoraMap.put("d" + tile,
                new ImageIcon(getClass().getResource(ROOTPATH +"d" + tile + ".png")));
        }

        submitButton.addActionListener(this);
        nextButton.addActionListener(this);
        nextButton.setEnabled(false);
        riichiCheck.addActionListener(this);

        confirmPanel.add(submitButton);
        confirmPanel.add(nextButton);

        situationLabel = new JLabel();
        displayDoraLabel = new JLabel();

        // Load the question
        loadQuestion();

        //load the first question
        q = questions.get(qIndex);
        createQuestion(q);

        handPanel.add(riichiCheck);

        JLabel resultLabel = new JLabel("结果");
        //JLabel analyzeLabel = new JLabel("解析");

        resultPanel.add(resultLabel);
        //analyzePanel.add(analyzeLabel);


        add(conditionPanel);
        add(handPanel);

        add(confirmPanel);
        add(resultPanel);
        add(analyzePanel);

        setTitle("何切300/301训练");
        setLayout(new GridLayout(5, 1));
        //setPreferredSize(new Dimension(1100, 1000));
        setLocation(50, 50);
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

        for (String hand : hands) {
            TileButton handButton = new TileButton(hand, tileImageMap.get(hand));
            handButton.addActionListener(this);
            handButton.setPreferredSize(new Dimension(45, 55));
            handGroup.add(handButton);
            handPanel.add(handButton);
        }

        ImageIcon analyseImage = new ImageIcon(ROOTPATH + q.getQNo()+"a.png");
        bookAnalyzeIcon.setIcon(analyseImage);
        bookAnalyzeIcon.setVisible(true);
        bookAnalyzeIcon.setBounds(200, 200, analyseImage.getIconWidth(), analyseImage.getIconHeight());
        add(bookAnalyzeIcon);
        analyzePanel.add(bookAnalyzeIcon);
        System.out.println(bookAnalyzeIcon);
        //revalidate();

    }

    private void loadQuestion() throws IOException {
        //打开题目文件
        InputStream in = getClass().getResourceAsStream(ROOTPATH+"kiru.txt");
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
        }
        // 答案
        q.setAnswer(split[4]);
        questions.add(q);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == riichiCheck && riichiCheck.isSelected()) {
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
                System.out.println(tile);
                choiceTileLabel.setIcon(tileImageMap.get(tile));
                resultPanel.add(choiceTextLabel);
                resultPanel.add(choiceTileLabel);
                previousSelection = ((TileButton)source);
                revalidate();
            }

            if (source == submitButton) {
                totalQuestion++;
                Enumeration<AbstractButton> enumeration = handGroup.getElements();
                if (handGroup.getSelection() == null) {
                    JOptionPane.showMessageDialog(this, "你还没选择", "Alert", JOptionPane.WARNING_MESSAGE);
                } else {
                    while (enumeration.hasMoreElements()) {
                        enumeration.nextElement().setEnabled(false);
                    }
                    riichiCheck.setEnabled(false);
                    submitButton.setEnabled(false);
                    nextButton.setEnabled(true);
                    // 分析结果
                    if (choice.equals(q.getAnswer())) {
                        correctAnswer++;
                    }
                    System.out.println("你答对" + totalQuestion + "中的" + correctAnswer + "题，正确率为" + String.format("%.2f",
                        (correctAnswer / (totalQuestion * 1.0)) * 100) + "%");

                    //解析
                    bookAnalyzeIcon.setVisible(true);
                    revalidate();
                }
            }
        }
    }
}
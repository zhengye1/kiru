package com.vincent.mahjong.gui;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Question {
    Integer qNo;
    String situation;
    String doraIndicator;
    String hands;
    String fulous;
    String answer;
    String explanation;
}

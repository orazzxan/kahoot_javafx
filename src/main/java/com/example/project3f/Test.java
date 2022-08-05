package com.example.project3f;

import java.util.ArrayList;

public class Test extends Question {
    private String[] options;
    private int numOfOptions;
    private ArrayList<String> labels = new ArrayList<>();
    private int answerLabelIndex;

    Test() {}

    public void setOptions(String[] variants) {
        this.numOfOptions = variants.length;
        options = new String[numOfOptions];
        for(int i = 0; i < numOfOptions; i++) {
            options[i] = variants[i];
        }

        setLabels();
        shuffle();
    }

    public String getOptionAt(int index) throws IndexOutOfBoundsException{
        if(index >= 0 && index < numOfOptions)
            return options[index];
        else
            throw new IndexOutOfBoundsException();

    }

    public String getLabelAt(int index) {
        return labels.get(index);
    }

    public int getNumOfOptions() {
        return numOfOptions;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append((sandar++) + ". " + getDescription() + "\n");
        for(int i = 0; i < numOfOptions; i++) {
            builder.append(getOptionAt(i) + "\n");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();

    }

    public void setLabels() {
        for(int i = 0; i < numOfOptions; i++) {
            labels.add(String.valueOf((char)('A' + i)));
        }

    }
    public String getAnswerLabel() {
        return labels.get(answerLabelIndex);
    }

    public void shuffle() {
        for(int i = 0; i < numOfOptions; i++) {
            int index = (int)(Math.random() * numOfOptions);

            if(answerLabelIndex == i) {
                answerLabelIndex = index;
            }else if(answerLabelIndex == index){
                answerLabelIndex = i;
            }

            String temp = options[i];
            options[i] = options[index];
            options[index] = temp;

        }
    }
}
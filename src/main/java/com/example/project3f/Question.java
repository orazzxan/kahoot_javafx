package com.example.project3f;

public abstract class Question {
    private String description;
    private String answer;
    static int sandar = 1;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDescription() {
        return description;
    }

    public String getAnswer() {
        return answer;
    }

}
package com.example.project3f;

public class FillIn extends Question {
    @Override
    public String toString() {
        String surak = (sandar++) + ". " + getDescription().replace("{blank}", "_____");
        return surak;
    }
}
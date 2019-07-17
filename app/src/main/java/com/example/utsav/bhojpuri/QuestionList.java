package com.example.utsav.bhojpuri;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.util.List;

public class QuestionList {
    private String question;
    private int question_id;
    private Drawable imageView;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
    public Drawable getImage()
    {
        return  imageView;
    }
    public void setImage(Drawable imageView)
    {
        this.imageView=imageView;
    }


    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}


package com.example.utsav.bhojpuri;

public class SurveyRecordAdapterSetterGetter {



    int school_id,cluster_id;


    public SurveyRecordAdapterSetterGetter( int school_id, int cluster_id) {

        this.school_id = school_id;
        this.cluster_id = cluster_id;

    }


    public int getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(int cluster_id) {
        this.cluster_id = cluster_id;
    }

    public int getSchool_id() {
        return school_id;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

}



class setQuestionOption{
    int question_id;
    String option;
    String imageStr;

    public setQuestionOption(int question_id, String option,String imageStr) {
        this.question_id = question_id;
        this.option = option;
        this.imageStr = imageStr;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }


    public String getImageStr() {
        return imageStr;
    }

    public void setImageStr(String imageStr) {
        this.imageStr = imageStr;
    }
}


package com.pipe.avi.model;

import com.google.gson.annotations.SerializedName;

public class QuestionResponse {

    @SerializedName("idPREGUNTAS")
    private int id;

    @SerializedName("descripcion")
    private String question;

    @SerializedName("perfilesRIASEC")
    private String category;

    @SerializedName("generadaIA")
    private boolean generadaIA;

    @SerializedName("testId")
    private int testId;

    public int getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getCategory() {
        return category;
    }
}
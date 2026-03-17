package com.pipe.avi.model;

public class AnswerRequest {

    private String category;
    private int answer;
    private RiasecScores riasec_scores;

    public AnswerRequest(String category, int answer, RiasecScores riasec_scores) {
        this.category = category;
        this.answer = answer;
        this.riasec_scores = riasec_scores;
    }

    public String getCategory() {
        return category;
    }

    public int getAnswer() {
        return answer;
    }

    public RiasecScores getRiasec_scores() {
        return riasec_scores;
    }
}

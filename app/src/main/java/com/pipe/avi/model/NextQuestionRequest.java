package com.pipe.avi.model;

public class NextQuestionRequest {

    private int testId;
    private RiasecScores riasec_scores;
    private String session_id;

    public NextQuestionRequest(int testId, RiasecScores riasec_scores, String session_id) {
        this.testId = testId;
        this.riasec_scores = riasec_scores;
        this.session_id = session_id;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public RiasecScores getRiasec_scores() {
        return riasec_scores;
    }

    public void setRiasec_scores(RiasecScores riasec_scores) {
        this.riasec_scores = riasec_scores;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
}

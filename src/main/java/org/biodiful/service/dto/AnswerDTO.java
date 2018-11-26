package org.biodiful.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Answer entity.
 */
public class AnswerDTO implements Serializable {

    private Long id;

    @NotNull
    private String judgeID;

    @NotNull
    private String challenger1;

    @NotNull
    private String challenger2;

    @NotNull
    private String winner;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    private Long surveyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJudgeID() {
        return judgeID;
    }

    public void setJudgeID(String judgeID) {
        this.judgeID = judgeID;
    }

    public String getChallenger1() {
        return challenger1;
    }

    public void setChallenger1(String challenger1) {
        this.challenger1 = challenger1;
    }

    public String getChallenger2() {
        return challenger2;
    }

    public void setChallenger2(String challenger2) {
        this.challenger2 = challenger2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnswerDTO answerDTO = (AnswerDTO) o;
        if (answerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), answerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnswerDTO{" +
            "id=" + getId() +
            ", judgeID='" + getJudgeID() + "'" +
            ", challenger1='" + getChallenger1() + "'" +
            ", challenger2='" + getChallenger2() + "'" +
            ", winner='" + getWinner() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", survey=" + getSurveyId() +
            "}";
    }
}

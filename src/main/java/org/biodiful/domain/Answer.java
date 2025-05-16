package org.biodiful.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Answer.
 */
@Entity
@Table(name = "answer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "judge_id", nullable = false)
    private String judgeID;

    @NotNull
    @Column(name = "challenger_1", nullable = false)
    private String challenger1;

    @NotNull
    @Column(name = "challenger_2", nullable = false)
    private String challenger2;

    @NotNull
    @Column(name = "winner", nullable = false)
    private String winner;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @NotNull
    @Column(name = "pool_number", nullable = false)
    private Integer poolNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Survey survey;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Answer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJudgeID() {
        return this.judgeID;
    }

    public Answer judgeID(String judgeID) {
        this.setJudgeID(judgeID);
        return this;
    }

    public void setJudgeID(String judgeID) {
        this.judgeID = judgeID;
    }

    public String getChallenger1() {
        return this.challenger1;
    }

    public Answer challenger1(String challenger1) {
        this.setChallenger1(challenger1);
        return this;
    }

    public void setChallenger1(String challenger1) {
        this.challenger1 = challenger1;
    }

    public String getChallenger2() {
        return this.challenger2;
    }

    public Answer challenger2(String challenger2) {
        this.setChallenger2(challenger2);
        return this;
    }

    public void setChallenger2(String challenger2) {
        this.challenger2 = challenger2;
    }

    public String getWinner() {
        return this.winner;
    }

    public Answer winner(String winner) {
        this.setWinner(winner);
        return this;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Answer startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Answer endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Integer getPoolNumber() {
        return this.poolNumber;
    }

    public Answer poolNumber(Integer poolNumber) {
        this.setPoolNumber(poolNumber);
        return this;
    }

    public void setPoolNumber(Integer poolNumber) {
        this.poolNumber = poolNumber;
    }

    public Survey getSurvey() {
        return this.survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Answer survey(Survey survey) {
        this.setSurvey(survey);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Answer)) {
            return false;
        }
        return getId() != null && getId().equals(((Answer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", judgeID='" + getJudgeID() + "'" +
            ", challenger1='" + getChallenger1() + "'" +
            ", challenger2='" + getChallenger2() + "'" +
            ", winner='" + getWinner() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", poolNumber=" + getPoolNumber() +
            "}";
    }
}

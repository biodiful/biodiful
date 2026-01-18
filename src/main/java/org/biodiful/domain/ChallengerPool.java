package org.biodiful.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ChallengerPool.
 */
@Entity
@Table(name = "challenger_pool")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChallengerPool implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "pool_order", nullable = false)
    private Integer poolOrder;

    /**
     * The URL to the S3 folder containing challenger images.
     * Format: https://bucket-name.s3.region.amazonaws.com/path/to/folder/
     * Example: https://my-bucket.s3.us-east-1.amazonaws.com/survey-1/pool-1/
     */
    @NotNull
    @Column(name = "challengers_url", nullable = false)
    private String challengersURL;

    @NotNull
    @Column(name = "number_of_matches", nullable = false)
    private Integer numberOfMatches;

    @Lob
    @Column(name = "matches_description", nullable = false)
    private String matchesDescription;

    @Lob
    @Column(name = "introduction_message")
    private String introductionMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "challengerPools" }, allowSetters = true)
    private Survey survey;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ChallengerPool id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoolOrder() {
        return this.poolOrder;
    }

    public ChallengerPool poolOrder(Integer poolOrder) {
        this.setPoolOrder(poolOrder);
        return this;
    }

    public void setPoolOrder(Integer poolOrder) {
        this.poolOrder = poolOrder;
    }

    public String getChallengersURL() {
        return this.challengersURL;
    }

    public ChallengerPool challengersURL(String challengersURL) {
        this.setChallengersURL(challengersURL);
        return this;
    }

    public void setChallengersURL(String challengersURL) {
        this.challengersURL = challengersURL;
    }

    public Integer getNumberOfMatches() {
        return this.numberOfMatches;
    }

    public ChallengerPool numberOfMatches(Integer numberOfMatches) {
        this.setNumberOfMatches(numberOfMatches);
        return this;
    }

    public void setNumberOfMatches(Integer numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public String getMatchesDescription() {
        return this.matchesDescription;
    }

    public ChallengerPool matchesDescription(String matchesDescription) {
        this.setMatchesDescription(matchesDescription);
        return this;
    }

    public void setMatchesDescription(String matchesDescription) {
        this.matchesDescription = matchesDescription;
    }

    public String getIntroductionMessage() {
        return this.introductionMessage;
    }

    public ChallengerPool introductionMessage(String introductionMessage) {
        this.setIntroductionMessage(introductionMessage);
        return this;
    }

    public void setIntroductionMessage(String introductionMessage) {
        this.introductionMessage = introductionMessage;
    }

    public Survey getSurvey() {
        return this.survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public ChallengerPool survey(Survey survey) {
        this.setSurvey(survey);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChallengerPool)) {
            return false;
        }
        return getId() != null && getId().equals(((ChallengerPool) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChallengerPool{" +
            "id=" + getId() +
            ", poolOrder=" + getPoolOrder() +
            ", challengersURL='" + getChallengersURL() + "'" +
            ", numberOfMatches=" + getNumberOfMatches() +
            ", matchesDescription='" + getMatchesDescription() + "'" +
            ", introductionMessage='" + getIntroductionMessage() + "'" +
            "}";
    }
}

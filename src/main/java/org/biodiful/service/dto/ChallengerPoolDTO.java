package org.biodiful.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link org.biodiful.domain.ChallengerPool} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChallengerPoolDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer poolOrder;

    /**
     * The URL to the S3 folder containing challenger images.
     * Format: https://bucket-name.s3.region.amazonaws.com/path/to/folder/
     */
    @NotNull
    private String challengersURL;

    @NotNull
    private Integer numberOfMatches;

    @Lob
    private String matchesDescription;

    private Long surveyId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoolOrder() {
        return poolOrder;
    }

    public void setPoolOrder(Integer poolOrder) {
        this.poolOrder = poolOrder;
    }

    public String getChallengersURL() {
        return challengersURL;
    }

    public void setChallengersURL(String challengersURL) {
        this.challengersURL = challengersURL;
    }

    public Integer getNumberOfMatches() {
        return numberOfMatches;
    }

    public void setNumberOfMatches(Integer numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public String getMatchesDescription() {
        return matchesDescription;
    }

    public void setMatchesDescription(String matchesDescription) {
        this.matchesDescription = matchesDescription;
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
        if (!(o instanceof ChallengerPoolDTO)) {
            return false;
        }

        ChallengerPoolDTO challengerPoolDTO = (ChallengerPoolDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, challengerPoolDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChallengerPoolDTO{" +
            "id=" + getId() +
            ", poolOrder=" + getPoolOrder() +
            ", challengersURL='" + getChallengersURL() + "'" +
            ", numberOfMatches=" + getNumberOfMatches() +
            ", matchesDescription='" + getMatchesDescription() + "'" +
            ", surveyId=" + getSurveyId() +
            "}";
    }
}

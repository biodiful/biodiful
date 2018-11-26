package org.biodiful.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the Survey entity.
 */
public class SurveyDTO implements Serializable {

    private Long id;

    @NotNull
    private String surveyName;

    
    @Lob
    private String surveyDescription;

    @Lob
    private String contactsDescription;

    
    private String friendlyURL;

    private String photoURL;

    private String logosURL;

    @NotNull
    private String formURL;

    @NotNull
    private String challengersURL;

    private Integer numberOfMatches;

    
    @Lob
    private String matchesDescription;

    @NotNull
    private Boolean open;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }

    public String getContactsDescription() {
        return contactsDescription;
    }

    public void setContactsDescription(String contactsDescription) {
        this.contactsDescription = contactsDescription;
    }

    public String getFriendlyURL() {
        return friendlyURL;
    }

    public void setFriendlyURL(String friendlyURL) {
        this.friendlyURL = friendlyURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getLogosURL() {
        return logosURL;
    }

    public void setLogosURL(String logosURL) {
        this.logosURL = logosURL;
    }

    public String getFormURL() {
        return formURL;
    }

    public void setFormURL(String formURL) {
        this.formURL = formURL;
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

    public Boolean isOpen() {
        return open;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SurveyDTO surveyDTO = (SurveyDTO) o;
        if (surveyDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), surveyDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SurveyDTO{" +
            "id=" + getId() +
            ", surveyName='" + getSurveyName() + "'" +
            ", surveyDescription='" + getSurveyDescription() + "'" +
            ", contactsDescription='" + getContactsDescription() + "'" +
            ", friendlyURL='" + getFriendlyURL() + "'" +
            ", photoURL='" + getPhotoURL() + "'" +
            ", logosURL='" + getLogosURL() + "'" +
            ", formURL='" + getFormURL() + "'" +
            ", challengersURL='" + getChallengersURL() + "'" +
            ", numberOfMatches=" + getNumberOfMatches() +
            ", matchesDescription='" + getMatchesDescription() + "'" +
            ", open='" + isOpen() + "'" +
            "}";
    }
}

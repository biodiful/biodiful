package org.biodiful.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import org.biodiful.domain.enumeration.Language;

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
    private String challengersPool1URL;

    private String challengersPool2URL;

    private String challengersPool3URL;

    @NotNull
    private Integer numberOfMatchesPerPool;

    
    @Lob
    private String matchesDescription;

    @NotNull
    private Boolean open;

    @NotNull
    private Language language;

    @NotNull
    private Boolean uniqueChallengers;

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

    public String getChallengersPool1URL() {
        return challengersPool1URL;
    }

    public void setChallengersPool1URL(String challengersPool1URL) {
        this.challengersPool1URL = challengersPool1URL;
    }

    public String getChallengersPool2URL() {
        return challengersPool2URL;
    }

    public void setChallengersPool2URL(String challengersPool2URL) {
        this.challengersPool2URL = challengersPool2URL;
    }

    public String getChallengersPool3URL() {
        return challengersPool3URL;
    }

    public void setChallengersPool3URL(String challengersPool3URL) {
        this.challengersPool3URL = challengersPool3URL;
    }

    public Integer getNumberOfMatchesPerPool() {
        return numberOfMatchesPerPool;
    }

    public void setNumberOfMatchesPerPool(Integer numberOfMatchesPerPool) {
        this.numberOfMatchesPerPool = numberOfMatchesPerPool;
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Boolean isUniqueChallengers() {
        return uniqueChallengers;
    }

    public void setUniqueChallengers(Boolean uniqueChallengers) {
        this.uniqueChallengers = uniqueChallengers;
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
            ", challengersPool1URL='" + getChallengersPool1URL() + "'" +
            ", challengersPool2URL='" + getChallengersPool2URL() + "'" +
            ", challengersPool3URL='" + getChallengersPool3URL() + "'" +
            ", numberOfMatchesPerPool=" + getNumberOfMatchesPerPool() +
            ", matchesDescription='" + getMatchesDescription() + "'" +
            ", open='" + isOpen() + "'" +
            ", language='" + getLanguage() + "'" +
            ", uniqueChallengers='" + isUniqueChallengers() + "'" +
            "}";
    }
}

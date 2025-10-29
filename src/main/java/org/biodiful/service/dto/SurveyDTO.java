package org.biodiful.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.biodiful.domain.enumeration.Language;

/**
 * A DTO for the {@link org.biodiful.domain.Survey} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
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

    private Set<ChallengerPoolDTO> challengerPools = new HashSet<>();

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

    public Set<ChallengerPoolDTO> getChallengerPools() {
        return challengerPools;
    }

    public void setChallengerPools(Set<ChallengerPoolDTO> challengerPools) {
        this.challengerPools = challengerPools;
    }

    public Boolean getOpen() {
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

    public Boolean getUniqueChallengers() {
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
        if (!(o instanceof SurveyDTO)) {
            return false;
        }

        SurveyDTO surveyDTO = (SurveyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, surveyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
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
            ", challengerPools=" + getChallengerPools() +
            ", open='" + getOpen() + "'" +
            ", language='" + getLanguage() + "'" +
            ", uniqueChallengers='" + getUniqueChallengers() + "'" +
            "}";
    }
}

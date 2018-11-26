package org.biodiful.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Survey.
 */
@Entity
@Table(name = "survey")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "survey_name", nullable = false)
    private String surveyName;

    
    @Lob
    @Column(name = "survey_description", nullable = false)
    private String surveyDescription;

    @Lob
    @Column(name = "contacts_description")
    private String contactsDescription;

    
    @Column(name = "friendly_url", unique = true)
    private String friendlyURL;

    @Column(name = "photo_url")
    private String photoURL;

    @Column(name = "logos_url")
    private String logosURL;

    @NotNull
    @Column(name = "form_url", nullable = false)
    private String formURL;

    @NotNull
    @Column(name = "challengers_url", nullable = false)
    private String challengersURL;

    @Column(name = "number_of_matches")
    private Integer numberOfMatches;

    
    @Lob
    @Column(name = "matches_description", nullable = false)
    private String matchesDescription;

    @NotNull
    @Column(name = "jhi_open", nullable = false)
    private Boolean open;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public Survey surveyName(String surveyName) {
        this.surveyName = surveyName;
        return this;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyDescription() {
        return surveyDescription;
    }

    public Survey surveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
        return this;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }

    public String getContactsDescription() {
        return contactsDescription;
    }

    public Survey contactsDescription(String contactsDescription) {
        this.contactsDescription = contactsDescription;
        return this;
    }

    public void setContactsDescription(String contactsDescription) {
        this.contactsDescription = contactsDescription;
    }

    public String getFriendlyURL() {
        return friendlyURL;
    }

    public Survey friendlyURL(String friendlyURL) {
        this.friendlyURL = friendlyURL;
        return this;
    }

    public void setFriendlyURL(String friendlyURL) {
        this.friendlyURL = friendlyURL;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public Survey photoURL(String photoURL) {
        this.photoURL = photoURL;
        return this;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getLogosURL() {
        return logosURL;
    }

    public Survey logosURL(String logosURL) {
        this.logosURL = logosURL;
        return this;
    }

    public void setLogosURL(String logosURL) {
        this.logosURL = logosURL;
    }

    public String getFormURL() {
        return formURL;
    }

    public Survey formURL(String formURL) {
        this.formURL = formURL;
        return this;
    }

    public void setFormURL(String formURL) {
        this.formURL = formURL;
    }

    public String getChallengersURL() {
        return challengersURL;
    }

    public Survey challengersURL(String challengersURL) {
        this.challengersURL = challengersURL;
        return this;
    }

    public void setChallengersURL(String challengersURL) {
        this.challengersURL = challengersURL;
    }

    public Integer getNumberOfMatches() {
        return numberOfMatches;
    }

    public Survey numberOfMatches(Integer numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
        return this;
    }

    public void setNumberOfMatches(Integer numberOfMatches) {
        this.numberOfMatches = numberOfMatches;
    }

    public String getMatchesDescription() {
        return matchesDescription;
    }

    public Survey matchesDescription(String matchesDescription) {
        this.matchesDescription = matchesDescription;
        return this;
    }

    public void setMatchesDescription(String matchesDescription) {
        this.matchesDescription = matchesDescription;
    }

    public Boolean isOpen() {
        return open;
    }

    public Survey open(Boolean open) {
        this.open = open;
        return this;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Survey survey = (Survey) o;
        if (survey.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), survey.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Survey{" +
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

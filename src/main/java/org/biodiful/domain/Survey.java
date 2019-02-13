package org.biodiful.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import org.biodiful.domain.enumeration.Language;

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
    @Column(name = "challengers_pool_1_url", nullable = false)
    private String challengersPool1URL;

    @Column(name = "challengers_pool_2_url")
    private String challengersPool2URL;

    @Column(name = "challengers_pool_3_url")
    private String challengersPool3URL;

    @NotNull
    @Column(name = "number_of_matches_per_pool", nullable = false)
    private Integer numberOfMatchesPerPool;

    @Column(name = "number_of_matches_per_pool_2")
    private Integer numberOfMatchesPerPool2;

    @Column(name = "number_of_matches_per_pool_3")
    private Integer numberOfMatchesPerPool3;

    
    @Lob
    @Column(name = "matches_description", nullable = false)
    private String matchesDescription;

    @Lob
    @Column(name = "matches_description_pool_2")
    private String matchesDescriptionPool2;

    @Lob
    @Column(name = "matches_description_pool_3")
    private String matchesDescriptionPool3;

    @NotNull
    @Column(name = "jhi_open", nullable = false)
    private Boolean open;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @NotNull
    @Column(name = "unique_challengers", nullable = false)
    private Boolean uniqueChallengers;

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

    public String getChallengersPool1URL() {
        return challengersPool1URL;
    }

    public Survey challengersPool1URL(String challengersPool1URL) {
        this.challengersPool1URL = challengersPool1URL;
        return this;
    }

    public void setChallengersPool1URL(String challengersPool1URL) {
        this.challengersPool1URL = challengersPool1URL;
    }

    public String getChallengersPool2URL() {
        return challengersPool2URL;
    }

    public Survey challengersPool2URL(String challengersPool2URL) {
        this.challengersPool2URL = challengersPool2URL;
        return this;
    }

    public void setChallengersPool2URL(String challengersPool2URL) {
        this.challengersPool2URL = challengersPool2URL;
    }

    public String getChallengersPool3URL() {
        return challengersPool3URL;
    }

    public Survey challengersPool3URL(String challengersPool3URL) {
        this.challengersPool3URL = challengersPool3URL;
        return this;
    }

    public void setChallengersPool3URL(String challengersPool3URL) {
        this.challengersPool3URL = challengersPool3URL;
    }

    public Integer getNumberOfMatchesPerPool() {
        return numberOfMatchesPerPool;
    }

    public Survey numberOfMatchesPerPool(Integer numberOfMatchesPerPool) {
        this.numberOfMatchesPerPool = numberOfMatchesPerPool;
        return this;
    }

    public void setNumberOfMatchesPerPool(Integer numberOfMatchesPerPool) {
        this.numberOfMatchesPerPool = numberOfMatchesPerPool;
    }

    public Integer getNumberOfMatchesPerPool2() {
        return numberOfMatchesPerPool2;
    }

    public Survey numberOfMatchesPerPool2(Integer numberOfMatchesPerPool2) {
        this.numberOfMatchesPerPool2 = numberOfMatchesPerPool2;
        return this;
    }

    public void setNumberOfMatchesPerPool2(Integer numberOfMatchesPerPool2) {
        this.numberOfMatchesPerPool2 = numberOfMatchesPerPool2;
    }

    public Integer getNumberOfMatchesPerPool3() {
        return numberOfMatchesPerPool3;
    }

    public Survey numberOfMatchesPerPool3(Integer numberOfMatchesPerPool3) {
        this.numberOfMatchesPerPool3 = numberOfMatchesPerPool3;
        return this;
    }

    public void setNumberOfMatchesPerPool3(Integer numberOfMatchesPerPool3) {
        this.numberOfMatchesPerPool3 = numberOfMatchesPerPool3;
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

    public String getMatchesDescriptionPool2() {
        return matchesDescriptionPool2;
    }

    public Survey matchesDescriptionPool2(String matchesDescriptionPool2) {
        this.matchesDescriptionPool2 = matchesDescriptionPool2;
        return this;
    }

    public void setMatchesDescriptionPool2(String matchesDescriptionPool2) {
        this.matchesDescriptionPool2 = matchesDescriptionPool2;
    }

    public String getMatchesDescriptionPool3() {
        return matchesDescriptionPool3;
    }

    public Survey matchesDescriptionPool3(String matchesDescriptionPool3) {
        this.matchesDescriptionPool3 = matchesDescriptionPool3;
        return this;
    }

    public void setMatchesDescriptionPool3(String matchesDescriptionPool3) {
        this.matchesDescriptionPool3 = matchesDescriptionPool3;
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

    public Language getLanguage() {
        return language;
    }

    public Survey language(Language language) {
        this.language = language;
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Boolean isUniqueChallengers() {
        return uniqueChallengers;
    }

    public Survey uniqueChallengers(Boolean uniqueChallengers) {
        this.uniqueChallengers = uniqueChallengers;
        return this;
    }

    public void setUniqueChallengers(Boolean uniqueChallengers) {
        this.uniqueChallengers = uniqueChallengers;
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
            ", challengersPool1URL='" + getChallengersPool1URL() + "'" +
            ", challengersPool2URL='" + getChallengersPool2URL() + "'" +
            ", challengersPool3URL='" + getChallengersPool3URL() + "'" +
            ", numberOfMatchesPerPool=" + getNumberOfMatchesPerPool() +
            ", numberOfMatchesPerPool2=" + getNumberOfMatchesPerPool2() +
            ", numberOfMatchesPerPool3=" + getNumberOfMatchesPerPool3() +
            ", matchesDescription='" + getMatchesDescription() + "'" +
            ", matchesDescriptionPool2='" + getMatchesDescriptionPool2() + "'" +
            ", matchesDescriptionPool3='" + getMatchesDescriptionPool3() + "'" +
            ", open='" + isOpen() + "'" +
            ", language='" + getLanguage() + "'" +
            ", uniqueChallengers='" + isUniqueChallengers() + "'" +
            "}";
    }
}

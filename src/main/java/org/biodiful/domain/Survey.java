package org.biodiful.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.biodiful.domain.enumeration.Language;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Survey.
 */
@Entity
@Table(name = "survey")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Survey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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
    @Column(name = "open", nullable = false)
    private Boolean open;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "language", nullable = false)
    private Language language;

    @NotNull
    @Column(name = "unique_challengers", nullable = false)
    private Boolean uniqueChallengers;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Survey id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurveyName() {
        return this.surveyName;
    }

    public Survey surveyName(String surveyName) {
        this.setSurveyName(surveyName);
        return this;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyDescription() {
        return this.surveyDescription;
    }

    public Survey surveyDescription(String surveyDescription) {
        this.setSurveyDescription(surveyDescription);
        return this;
    }

    public void setSurveyDescription(String surveyDescription) {
        this.surveyDescription = surveyDescription;
    }

    public String getContactsDescription() {
        return this.contactsDescription;
    }

    public Survey contactsDescription(String contactsDescription) {
        this.setContactsDescription(contactsDescription);
        return this;
    }

    public void setContactsDescription(String contactsDescription) {
        this.contactsDescription = contactsDescription;
    }

    public String getFriendlyURL() {
        return this.friendlyURL;
    }

    public Survey friendlyURL(String friendlyURL) {
        this.setFriendlyURL(friendlyURL);
        return this;
    }

    public void setFriendlyURL(String friendlyURL) {
        this.friendlyURL = friendlyURL;
    }

    public String getPhotoURL() {
        return this.photoURL;
    }

    public Survey photoURL(String photoURL) {
        this.setPhotoURL(photoURL);
        return this;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getLogosURL() {
        return this.logosURL;
    }

    public Survey logosURL(String logosURL) {
        this.setLogosURL(logosURL);
        return this;
    }

    public void setLogosURL(String logosURL) {
        this.logosURL = logosURL;
    }

    public String getFormURL() {
        return this.formURL;
    }

    public Survey formURL(String formURL) {
        this.setFormURL(formURL);
        return this;
    }

    public void setFormURL(String formURL) {
        this.formURL = formURL;
    }

    public String getChallengersPool1URL() {
        return this.challengersPool1URL;
    }

    public Survey challengersPool1URL(String challengersPool1URL) {
        this.setChallengersPool1URL(challengersPool1URL);
        return this;
    }

    public void setChallengersPool1URL(String challengersPool1URL) {
        this.challengersPool1URL = challengersPool1URL;
    }

    public String getChallengersPool2URL() {
        return this.challengersPool2URL;
    }

    public Survey challengersPool2URL(String challengersPool2URL) {
        this.setChallengersPool2URL(challengersPool2URL);
        return this;
    }

    public void setChallengersPool2URL(String challengersPool2URL) {
        this.challengersPool2URL = challengersPool2URL;
    }

    public String getChallengersPool3URL() {
        return this.challengersPool3URL;
    }

    public Survey challengersPool3URL(String challengersPool3URL) {
        this.setChallengersPool3URL(challengersPool3URL);
        return this;
    }

    public void setChallengersPool3URL(String challengersPool3URL) {
        this.challengersPool3URL = challengersPool3URL;
    }

    public Integer getNumberOfMatchesPerPool() {
        return this.numberOfMatchesPerPool;
    }

    public Survey numberOfMatchesPerPool(Integer numberOfMatchesPerPool) {
        this.setNumberOfMatchesPerPool(numberOfMatchesPerPool);
        return this;
    }

    public void setNumberOfMatchesPerPool(Integer numberOfMatchesPerPool) {
        this.numberOfMatchesPerPool = numberOfMatchesPerPool;
    }

    public Integer getNumberOfMatchesPerPool2() {
        return this.numberOfMatchesPerPool2;
    }

    public Survey numberOfMatchesPerPool2(Integer numberOfMatchesPerPool2) {
        this.setNumberOfMatchesPerPool2(numberOfMatchesPerPool2);
        return this;
    }

    public void setNumberOfMatchesPerPool2(Integer numberOfMatchesPerPool2) {
        this.numberOfMatchesPerPool2 = numberOfMatchesPerPool2;
    }

    public Integer getNumberOfMatchesPerPool3() {
        return this.numberOfMatchesPerPool3;
    }

    public Survey numberOfMatchesPerPool3(Integer numberOfMatchesPerPool3) {
        this.setNumberOfMatchesPerPool3(numberOfMatchesPerPool3);
        return this;
    }

    public void setNumberOfMatchesPerPool3(Integer numberOfMatchesPerPool3) {
        this.numberOfMatchesPerPool3 = numberOfMatchesPerPool3;
    }

    public String getMatchesDescription() {
        return this.matchesDescription;
    }

    public Survey matchesDescription(String matchesDescription) {
        this.setMatchesDescription(matchesDescription);
        return this;
    }

    public void setMatchesDescription(String matchesDescription) {
        this.matchesDescription = matchesDescription;
    }

    public String getMatchesDescriptionPool2() {
        return this.matchesDescriptionPool2;
    }

    public Survey matchesDescriptionPool2(String matchesDescriptionPool2) {
        this.setMatchesDescriptionPool2(matchesDescriptionPool2);
        return this;
    }

    public void setMatchesDescriptionPool2(String matchesDescriptionPool2) {
        this.matchesDescriptionPool2 = matchesDescriptionPool2;
    }

    public String getMatchesDescriptionPool3() {
        return this.matchesDescriptionPool3;
    }

    public Survey matchesDescriptionPool3(String matchesDescriptionPool3) {
        this.setMatchesDescriptionPool3(matchesDescriptionPool3);
        return this;
    }

    public void setMatchesDescriptionPool3(String matchesDescriptionPool3) {
        this.matchesDescriptionPool3 = matchesDescriptionPool3;
    }

    public Boolean getOpen() {
        return this.open;
    }

    public Survey open(Boolean open) {
        this.setOpen(open);
        return this;
    }

    public void setOpen(Boolean open) {
        this.open = open;
    }

    public Language getLanguage() {
        return this.language;
    }

    public Survey language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Boolean getUniqueChallengers() {
        return this.uniqueChallengers;
    }

    public Survey uniqueChallengers(Boolean uniqueChallengers) {
        this.setUniqueChallengers(uniqueChallengers);
        return this;
    }

    public void setUniqueChallengers(Boolean uniqueChallengers) {
        this.uniqueChallengers = uniqueChallengers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Survey)) {
            return false;
        }
        return getId() != null && getId().equals(((Survey) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
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
            ", open='" + getOpen() + "'" +
            ", language='" + getLanguage() + "'" +
            ", uniqueChallengers='" + getUniqueChallengers() + "'" +
            "}";
    }
}

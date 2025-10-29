package org.biodiful.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "survey" }, allowSetters = true)
    private Set<ChallengerPool> challengerPools = new HashSet<>();

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

    public Set<ChallengerPool> getChallengerPools() {
        return this.challengerPools;
    }

    public void setChallengerPools(Set<ChallengerPool> challengerPools) {
        if (this.challengerPools != null) {
            this.challengerPools.forEach(i -> i.setSurvey(null));
        }
        if (challengerPools != null) {
            challengerPools.forEach(i -> i.setSurvey(this));
        }
        this.challengerPools = challengerPools;
    }

    public Survey challengerPools(Set<ChallengerPool> challengerPools) {
        this.setChallengerPools(challengerPools);
        return this;
    }

    public Survey addChallengerPool(ChallengerPool challengerPool) {
        this.challengerPools.add(challengerPool);
        challengerPool.setSurvey(this);
        return this;
    }

    public Survey removeChallengerPool(ChallengerPool challengerPool) {
        this.challengerPools.remove(challengerPool);
        challengerPool.setSurvey(null);
        return this;
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
            ", open='" + getOpen() + "'" +
            ", language='" + getLanguage() + "'" +
            ", uniqueChallengers='" + getUniqueChallengers() + "'" +
            "}";
    }
}

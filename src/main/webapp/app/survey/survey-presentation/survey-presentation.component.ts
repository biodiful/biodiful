import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { SurveyService } from 'app/entities/survey';
import { ISurvey } from 'app/shared/model/survey.model';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import moment from 'moment/src/moment';
import { Principal } from 'app/core';
import { SessionStorageService } from 'ngx-webstorage';
import { JhiLanguageService } from 'ng-jhipster';
import { Meta } from '@angular/platform-browser';

@Component({
    selector: 'jhi-survey-presentation',
    templateUrl: './survey-presentation.component.html',
    styleUrls: ['survey-presentation.css'],
    encapsulation: ViewEncapsulation.None
})
export class SurveyPresentationComponent implements OnInit {
    survey: ISurvey;
    surveyJudgesCount: number;
    surveyAbsoluteUrl: string;
    surveyAbsoluteUrlEncoded: string;

    constructor(
        private principal: Principal,
        private surveyService: SurveyService,
        private activatedRoute: ActivatedRoute,
        private sessionStorage: SessionStorageService,
        private languageService: JhiLanguageService,
        private router: Router,
        private meta: Meta
    ) {}

    ngOnInit() {
        this.reloadParentIframe();

        this.activatedRoute.data.subscribe(({ survey }) => {
            this.survey = survey;

            this.subscribeToCountResponse(this.surveyService.getSurveyJudgesCount(this.survey.id));

            this.initSurveyAbsoluteUrl();

            this.updateMetaTags();

            this.initLocale();
        });
    }

    updateMetaTags() {
        // this.meta.addTags([
        //     { name: 'og:title', content: this.survey.surveyName },
        //     { name: 'og:description', content: this.htmlToPlaintext(this.survey.surveyDescription) },
        //   ]);

        //TODO URL - remove first or update OK?
        this.meta.updateTag({ name: 'og:url', content: this.surveyAbsoluteUrl });
        this.meta.updateTag({ name: 'og:title', content: this.survey.surveyName });
        this.meta.updateTag({ name: 'og:description', content: this.firstSentence(this.survey.surveyDescription) });

        //TODO also update these tags on the homepage?
    }

    firstSentence(text) {
        return this.htmlToPlaintext(text).split('.')[0];
    }

    htmlToPlaintext(text) {
        return text ? String(text).replace(/<[^>]+>/gm, ' ') : '';
    }

    reloadParentIframe() {
        // Detect whether we're in an iframe (i.e. if we follow the link at the end of the google form)
        if (window.frameElement) {
            // Reload parent with curent path
            window.parent.location.href = '#/' + this.router.url;
        }
    }

    initSurveyAbsoluteUrl() {
        this.surveyAbsoluteUrl = 'https://www.biodiful.org/#';
        console.log(this.activatedRoute.snapshot.url);
        if (this.survey.friendlyURL) {
            this.surveyAbsoluteUrl += '/' + this.survey.friendlyURL;
        } else {
            this.surveyAbsoluteUrl += this.router.url;
        }

        this.surveyAbsoluteUrlEncoded = encodeURIComponent(this.surveyAbsoluteUrl);
    }

    initLocale() {
        const languageKey = this.survey.language.toLowerCase();
        this.sessionStorage.store('locale', languageKey);
        this.languageService.changeLanguage(languageKey);
    }

    private subscribeToCountResponse(result: Observable<Object>) {
        result.subscribe((res: HttpResponse<number>) => this.onCountSuccess(res), (res: HttpErrorResponse) => this.onError());
    }

    onError(): void {
        console.debug('Failed to retrieve the number of judges for this survey');
        //alert('An error occurred. Please try again later.');
    }

    onCountSuccess(response): void {
        console.debug('Number of distinct judges for this survey: ' + JSON.stringify(response));
        this.surveyJudgesCount = response.toString();
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    getFacebookUrl(): String {
        return `https://www.facebook.com/sharer/sharer.php?u=${this.surveyAbsoluteUrlEncoded}&amp;src=sdkpreparse`;
    }

    getTwitterUrl(): String {
        return `https://twitter.com/intent/tweet?text=${this.surveyAbsoluteUrlEncoded}`;
    }

    getMailToHref(): String {
        return `mailto:?Subject=${encodeURIComponent(this.survey.surveyName)}&body=${this.surveyAbsoluteUrlEncoded}`;
    }
}

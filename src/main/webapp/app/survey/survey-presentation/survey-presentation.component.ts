import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SurveyService } from 'app/entities/survey';
import { ISurvey } from 'app/shared/model/survey.model';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import moment from 'moment/src/moment';
import { Principal } from 'app/core';
import { SessionStorageService } from 'ngx-webstorage';
import { JhiLanguageService } from 'ng-jhipster';

@Component({
    selector: 'jhi-survey-presentation',
    templateUrl: './survey-presentation.component.html',
    styles: []
})
export class SurveyPresentationComponent implements OnInit {
    survey: ISurvey;
    surveyJudgesCount: number;

    constructor(
        private principal: Principal,
        private surveyService: SurveyService,
        private activatedRoute: ActivatedRoute,
        private sessionStorage: SessionStorageService,
        private languageService: JhiLanguageService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ survey }) => {
            this.survey = survey;

            this.subscribeToCountResponse(this.surveyService.getSurveyJudgesCount(this.survey.id));

            this.initLocale();
        });
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
}

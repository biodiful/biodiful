import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AnswerService } from 'app/entities/answer';
import { SurveyService } from 'app/entities/survey';
import { Answer, IAnswer } from 'app/shared/model/answer.model';
import { ISurvey } from 'app/shared/model/survey.model';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import moment from 'moment/src/moment';

@Component({
    selector: 'jhi-survey-presentation',
    templateUrl: './survey-presentation.component.html',
    styles: []
})
export class SurveyPresentationComponent implements OnInit {
    survey: ISurvey;
    surveyJudgesCount: number;

    constructor(private surveyService: SurveyService, private activatedRoute: ActivatedRoute, private answerService: AnswerService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ survey }) => {
            this.survey = survey;

            this.subscribeToCountResponse(this.surveyService.getSurveyAnswersCount(this.survey.id));
        });
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
}

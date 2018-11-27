import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AnswerService } from 'app/entities/answer';
import { SurveyService } from 'app/entities/survey';
import { Answer, IAnswer } from 'app/shared/model/answer.model';
import { ISurvey } from 'app/shared/model/survey.model';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import moment = require('moment');

@Component({
    selector: 'jhi-survey-presentation',
    templateUrl: './survey-presentation.component.html',
    styles: []
})
export class SurveyPresentationComponent implements OnInit {
    survey: ISurvey;
    isSaving: boolean;

    constructor(private surveyService: SurveyService, private activatedRoute: ActivatedRoute, private answerService: AnswerService) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ survey }) => {
            this.survey = survey;
        });
    }

    previousState() {
        window.history.back();
    }

    nextMatchState() {
        //For now, just save a fake answer
        let answer = new Answer(undefined, 'jugeIDtest', 'challenger1', 'challenger2', 'winner', moment(), moment(), 1);
        this.subscribeToSaveResponse(this.answerService.create(answer));
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>) {
        result.subscribe((res: HttpResponse<IAnswer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    onSaveError(): void {
        alert('Save error :( ');
    }

    onSaveSuccess(): void {
        alert('Save success!');
    }
}

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IAnswer } from 'app/shared/model/answer.model';
import { AnswerService } from './answer.service';
import { ISurvey } from 'app/shared/model/survey.model';
import { SurveyService } from 'app/entities/survey';

@Component({
    selector: 'jhi-answer-update',
    templateUrl: './answer-update.component.html'
})
export class AnswerUpdateComponent implements OnInit {
    answer: IAnswer;
    isSaving: boolean;

    surveys: ISurvey[];
    startTime: string;
    endTime: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private answerService: AnswerService,
        private surveyService: SurveyService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ answer }) => {
            this.answer = answer;
            this.startTime = this.answer.startTime != null ? this.answer.startTime.format(DATE_TIME_FORMAT) : null;
            this.endTime = this.answer.endTime != null ? this.answer.endTime.format(DATE_TIME_FORMAT) : null;
        });
        this.surveyService.query().subscribe(
            (res: HttpResponse<ISurvey[]>) => {
                this.surveys = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.answer.startTime = this.startTime != null ? moment(this.startTime, DATE_TIME_FORMAT) : null;
        this.answer.endTime = this.endTime != null ? moment(this.endTime, DATE_TIME_FORMAT) : null;
        if (this.answer.id !== undefined) {
            this.subscribeToSaveResponse(this.answerService.update(this.answer));
        } else {
            this.subscribeToSaveResponse(this.answerService.create(this.answer));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>) {
        result.subscribe((res: HttpResponse<IAnswer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackSurveyById(index: number, item: ISurvey) {
        return item.id;
    }
}

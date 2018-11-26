import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { ISurvey } from 'app/shared/model/survey.model';
import { SurveyService } from './survey.service';

@Component({
    selector: 'jhi-survey-update',
    templateUrl: './survey-update.component.html'
})
export class SurveyUpdateComponent implements OnInit {
    survey: ISurvey;
    isSaving: boolean;

    constructor(private dataUtils: JhiDataUtils, private surveyService: SurveyService, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ survey }) => {
            this.survey = survey;
        });
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.survey.id !== undefined) {
            this.subscribeToSaveResponse(this.surveyService.update(this.survey));
        } else {
            this.subscribeToSaveResponse(this.surveyService.create(this.survey));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISurvey>>) {
        result.subscribe((res: HttpResponse<ISurvey>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}

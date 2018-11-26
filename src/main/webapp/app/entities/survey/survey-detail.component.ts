import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { ISurvey } from 'app/shared/model/survey.model';

@Component({
    selector: 'jhi-survey-detail',
    templateUrl: './survey-detail.component.html'
})
export class SurveyDetailComponent implements OnInit {
    survey: ISurvey;

    constructor(private dataUtils: JhiDataUtils, private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
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
    previousState() {
        window.history.back();
    }
}

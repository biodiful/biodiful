import { Component, OnInit } from '@angular/core';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';

import { LoginModalService, Principal, Account } from 'app/core';
import { SurveyService } from 'app/entities/survey';
import { ISurvey } from 'app/shared/model/survey.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;
    openSurveys: ISurvey[];
    closedSurveys: ISurvey[];

    constructor(
        private principal: Principal,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private surveyService: SurveyService,
        private jhiAlertService: JhiAlertService
    ) {
        this.openSurveys = [];
        this.closedSurveys = [];
    }

    ngOnInit() {
        this.loadOpenSurveys();
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
    }

    loadOpenSurveys() {
        this.surveyService
            .query({
                page: 0,
                size: 30
            })
            .subscribe(
                (res: HttpResponse<ISurvey[]>) => this.initOpenSurveys(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    private initOpenSurveys(data: ISurvey[], headers: HttpHeaders) {
        //TODO create service method to retrieve list of open surveys
        for (let i = 0; i < data.length; i++) {
            if (data[i].open) {
                this.openSurveys.push(data[i]);
            } else {
                this.closedSurveys.push(data[i]);
            }
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}

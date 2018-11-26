import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISurvey } from 'app/shared/model/survey.model';
import { SurveyService } from './survey.service';

@Component({
    selector: 'jhi-survey-delete-dialog',
    templateUrl: './survey-delete-dialog.component.html'
})
export class SurveyDeleteDialogComponent {
    survey: ISurvey;

    constructor(private surveyService: SurveyService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.surveyService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'surveyListModification',
                content: 'Deleted an survey'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-survey-delete-popup',
    template: ''
})
export class SurveyDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ survey }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(SurveyDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.survey = survey;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}

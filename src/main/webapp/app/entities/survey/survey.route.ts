import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Survey } from 'app/shared/model/survey.model';
import { SurveyService } from './survey.service';
import { SurveyComponent } from './survey.component';
import { SurveyDetailComponent } from './survey-detail.component';
import { SurveyUpdateComponent } from './survey-update.component';
import { SurveyDeletePopupComponent } from './survey-delete-dialog.component';
import { ISurvey } from 'app/shared/model/survey.model';
import { SurveyPresentationComponent } from 'app/survey/survey-presentation/survey-presentation.component';
import { SurveyAnswerComponent } from 'app/survey/survey-answer/survey-answer.component';

@Injectable({ providedIn: 'root' })
export class SurveyResolve implements Resolve<ISurvey> {
    constructor(private service: SurveyService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Survey> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Survey>) => response.ok),
                map((survey: HttpResponse<Survey>) => survey.body)
            );
        }

        const friendlyUrl = route.params['friendlyUrl'] ? route.params['friendlyUrl'] : null;
        if (friendlyUrl) {
            return this.service.findByFriendlyUrl(friendlyUrl).pipe(
                filter((response: HttpResponse<Survey>) => response.ok),
                map((survey: HttpResponse<Survey>) => survey.body)
            );
        }

        return of(new Survey());
    }
}

export const surveyRoute: Routes = [
    {
        path: 'survey',
        component: SurveyComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'biodifulApp.survey.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'survey/:id/present',
        component: SurveyPresentationComponent,
        resolve: {
            survey: SurveyResolve
        },
        data: {
            authorities: [],
            pageTitle: 'biodifulApp.survey.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'survey/:id/answer',
        component: SurveyAnswerComponent,
        resolve: {
            survey: SurveyResolve
        },
        data: {
            authorities: [],
            pageTitle: 'biodifulApp.survey.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'survey/:id/view',
        component: SurveyDetailComponent,
        resolve: {
            survey: SurveyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'biodifulApp.survey.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'survey/new',
        component: SurveyUpdateComponent,
        resolve: {
            survey: SurveyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'biodifulApp.survey.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'survey/:id/edit',
        component: SurveyUpdateComponent,
        resolve: {
            survey: SurveyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'biodifulApp.survey.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':friendlyUrl',
        component: SurveyPresentationComponent,
        resolve: {
            survey: SurveyResolve
        },
        data: {
            authorities: [],
            pageTitle: 'biodifulApp.survey.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const surveyPopupRoute: Routes = [
    {
        path: 'survey/:id/delete',
        component: SurveyDeletePopupComponent,
        resolve: {
            survey: SurveyResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'biodifulApp.survey.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];

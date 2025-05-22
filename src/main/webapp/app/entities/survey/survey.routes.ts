import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SurveyResolve from './route/survey-routing-resolve.service';

const surveyRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/survey.component').then(m => m.SurveyComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/survey-detail.component').then(m => m.SurveyDetailComponent),
    resolve: {
      survey: SurveyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/survey-update.component').then(m => m.SurveyUpdateComponent),
    resolve: {
      survey: SurveyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/survey-update.component').then(m => m.SurveyUpdateComponent),
    resolve: {
      survey: SurveyResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default surveyRoute;

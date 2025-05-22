import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AnswerResolve from './route/answer-routing-resolve.service';

const answerRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/answer.component').then(m => m.AnswerComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/answer-detail.component').then(m => m.AnswerDetailComponent),
    resolve: {
      answer: AnswerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/answer-update.component').then(m => m.AnswerUpdateComponent),
    resolve: {
      answer: AnswerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/answer-update.component').then(m => m.AnswerUpdateComponent),
    resolve: {
      answer: AnswerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default answerRoute;

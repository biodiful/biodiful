import { Routes } from '@angular/router';

import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import SurveyResolve from './entities/survey/route/survey-routing-resolve.service';

const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./home/home.component'),
    title: 'home.title',
  },
  {
    path: '',
    loadComponent: () => import('./layouts/navbar/navbar.component'),
    outlet: 'navbar',
  },
  {
    path: 'admin',
    data: {
      authorities: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
    loadChildren: () => import('./admin/admin.routes'),
  },
  {
    path: 'account',
    loadChildren: () => import('./account/account.route'),
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component'),
    title: 'login.title',
  },
  {
    path: '',
    loadChildren: () => import(`./entities/entity.routes`),
  },
  {
    path: 'about',
    loadComponent: () => import('./about/about/about.component'),
    title: 'home.title',
  },
  {
    path: 'error',
    loadComponent: () => import('./layouts/error/error.component'),
    title: 'error.title',
  },
  {
    path: 'accessdenied',
    loadComponent: () => import('./layouts/error/error.component'),
    data: {
      errorMessage: 'error.http.403',
    },
    title: 'error.title',
  },
  {
    path: '404',
    loadComponent: () => import('./layouts/error/error.component'),
    data: {
      errorMessage: 'error.http.404',
    },
    title: 'error.title',
  },
  {
    path: ':friendlyUrl',
    loadComponent: () => import('app/survey/survey-presentation/survey-presentation.component').then(m => m.SurveyPresentationComponent),
    resolve: {
      survey: SurveyResolve,
    },
    data: {
      pageTitle: 'biodifulApp.survey.home.title',
    },
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];

export default routes;

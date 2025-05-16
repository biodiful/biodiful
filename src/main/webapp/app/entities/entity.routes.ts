import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'biodifulApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'survey',
    data: { pageTitle: 'biodifulApp.survey.home.title' },
    loadChildren: () => import('./survey/survey.routes'),
  },
  {
    path: 'answer',
    data: { pageTitle: 'biodifulApp.answer.home.title' },
    loadChildren: () => import('./answer/answer.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;

import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISurvey } from '../survey.model';
import { SurveyService } from '../service/survey.service';

const surveyResolve = (route: ActivatedRouteSnapshot): Observable<null | ISurvey> => {
  const id = route.params.id;
  if (id) {
    return inject(SurveyService)
      .find(id)
      .pipe(
        mergeMap((survey: HttpResponse<ISurvey>) => {
          if (survey.body) {
            return of(survey.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default surveyResolve;

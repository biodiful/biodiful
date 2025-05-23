import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnswer } from '../answer.model';
import { AnswerService } from '../service/answer.service';

const answerResolve = (route: ActivatedRouteSnapshot): Observable<null | IAnswer> => {
  const id = route.params.id;
  if (id) {
    return inject(AnswerService)
      .find(id)
      .pipe(
        mergeMap((answer: HttpResponse<IAnswer>) => {
          if (answer.body) {
            return of(answer.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default answerResolve;

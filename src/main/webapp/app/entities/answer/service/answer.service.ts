import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnswer, NewAnswer } from '../answer.model';

export type PartialUpdateAnswer = Partial<IAnswer> & Pick<IAnswer, 'id'>;

type RestOf<T extends IAnswer | NewAnswer> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestAnswer = RestOf<IAnswer>;

export type NewRestAnswer = RestOf<NewAnswer>;

export type PartialUpdateRestAnswer = RestOf<PartialUpdateAnswer>;

export type EntityResponseType = HttpResponse<IAnswer>;
export type EntityArrayResponseType = HttpResponse<IAnswer[]>;

@Injectable({ providedIn: 'root' })
export class AnswerService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/answers');

  create(answer: NewAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answer);
    return this.http
      .post<RestAnswer>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  createAll(answers: NewAnswer[]): Observable<EntityArrayResponseType> {
    const copy = this.convertDateArrayFromClient(answers);
    return this.http
      .post<RestAnswer[]>(this.resourceUrl + '/all', copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  update(answer: IAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answer);
    return this.http
      .put<RestAnswer>(`${this.resourceUrl}/${this.getAnswerIdentifier(answer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(answer: PartialUpdateAnswer): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(answer);
    return this.http
      .patch<RestAnswer>(`${this.resourceUrl}/${this.getAnswerIdentifier(answer)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAnswer[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAnswerIdentifier(answer: Pick<IAnswer, 'id'>): number {
    return answer.id;
  }

  compareAnswer(o1: Pick<IAnswer, 'id'> | null, o2: Pick<IAnswer, 'id'> | null): boolean {
    return o1 && o2 ? this.getAnswerIdentifier(o1) === this.getAnswerIdentifier(o2) : o1 === o2;
  }

  addAnswerToCollectionIfMissing<Type extends Pick<IAnswer, 'id'>>(
    answerCollection: Type[],
    ...answersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const answers: Type[] = answersToCheck.filter(isPresent);
    if (answers.length > 0) {
      const answerCollectionIdentifiers = answerCollection.map(answerItem => this.getAnswerIdentifier(answerItem));
      const answersToAdd = answers.filter(answerItem => {
        const answerIdentifier = this.getAnswerIdentifier(answerItem);
        if (answerCollectionIdentifiers.includes(answerIdentifier)) {
          return false;
        }
        answerCollectionIdentifiers.push(answerIdentifier);
        return true;
      });
      return [...answersToAdd, ...answerCollection];
    }
    return answerCollection;
  }

  protected convertDateArrayFromClient<T extends IAnswer | NewAnswer>(answers: T[]): RestOf<T>[] {
    const copy: RestOf<T>[] = [];
    for (const answer of answers) {
      copy.push(this.convertDateFromClient<T>(answer));
    }
    return copy;
  }

  protected convertDateFromClient<T extends IAnswer | NewAnswer | PartialUpdateAnswer>(answer: T): RestOf<T> {
    return {
      ...answer,
      startTime: answer.startTime?.toJSON() ?? null,
      endTime: answer.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAnswer: RestAnswer): IAnswer {
    return {
      ...restAnswer,
      startTime: restAnswer.startTime ? dayjs(restAnswer.startTime) : undefined,
      endTime: restAnswer.endTime ? dayjs(restAnswer.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAnswer>): HttpResponse<IAnswer> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAnswer[]>): HttpResponse<IAnswer[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}

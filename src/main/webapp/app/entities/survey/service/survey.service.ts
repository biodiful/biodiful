import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISurvey, NewSurvey } from '../survey.model';

export type PartialUpdateSurvey = Partial<ISurvey> & Pick<ISurvey, 'id'>;

export type EntityResponseType = HttpResponse<ISurvey>;
export type EntityArrayResponseType = HttpResponse<ISurvey[]>;

@Injectable({ providedIn: 'root' })
export class SurveyService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/surveys');

  create(survey: NewSurvey): Observable<EntityResponseType> {
    return this.http.post<ISurvey>(this.resourceUrl, survey, { observe: 'response' });
  }

  update(survey: ISurvey): Observable<EntityResponseType> {
    return this.http.put<ISurvey>(`${this.resourceUrl}/${this.getSurveyIdentifier(survey)}`, survey, { observe: 'response' });
  }

  partialUpdate(survey: PartialUpdateSurvey): Observable<EntityResponseType> {
    return this.http.patch<ISurvey>(`${this.resourceUrl}/${this.getSurveyIdentifier(survey)}`, survey, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISurvey>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISurvey[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSurveyIdentifier(survey: Pick<ISurvey, 'id'>): number {
    return survey.id;
  }

  compareSurvey(o1: Pick<ISurvey, 'id'> | null, o2: Pick<ISurvey, 'id'> | null): boolean {
    return o1 && o2 ? this.getSurveyIdentifier(o1) === this.getSurveyIdentifier(o2) : o1 === o2;
  }

  addSurveyToCollectionIfMissing<Type extends Pick<ISurvey, 'id'>>(
    surveyCollection: Type[],
    ...surveysToCheck: (Type | null | undefined)[]
  ): Type[] {
    const surveys: Type[] = surveysToCheck.filter(isPresent);
    if (surveys.length > 0) {
      const surveyCollectionIdentifiers = surveyCollection.map(surveyItem => this.getSurveyIdentifier(surveyItem));
      const surveysToAdd = surveys.filter(surveyItem => {
        const surveyIdentifier = this.getSurveyIdentifier(surveyItem);
        if (surveyCollectionIdentifiers.includes(surveyIdentifier)) {
          return false;
        }
        surveyCollectionIdentifiers.push(surveyIdentifier);
        return true;
      });
      return [...surveysToAdd, ...surveyCollection];
    }
    return surveyCollection;
  }
}

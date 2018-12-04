import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IAnswer } from 'app/shared/model/answer.model';

type EntityResponseType = HttpResponse<IAnswer>;
type EntityArrayResponseType = HttpResponse<IAnswer[]>;

@Injectable({ providedIn: 'root' })
export class AnswerService {
    public resourceUrl = SERVER_API_URL + 'api/answers';

    constructor(private http: HttpClient) {}

    create(answer: IAnswer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(answer);
        return this.http
            .post<IAnswer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    createAll(answers: IAnswer[]): Observable<EntityArrayResponseType> {
        const copy = this.convertDateArrayFromClient(answers);
        return this.http
            .post<IAnswer[]>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    update(answer: IAnswer): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(answer);
        return this.http
            .put<IAnswer>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IAnswer>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IAnswer[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateArrayFromClient(answers: IAnswer[]): IAnswer[] {
        let copy = [];
        for (let answer of answers) {
            copy.push(this.convertDateFromClient(answer));
        }
        return copy;
    }

    protected convertDateFromClient(answer: IAnswer): IAnswer {
        const copy: IAnswer = Object.assign({}, answer, {
            startTime: answer.startTime != null && answer.startTime.isValid() ? answer.startTime.toJSON() : null,
            endTime: answer.endTime != null && answer.endTime.isValid() ? answer.endTime.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.startTime = res.body.startTime != null ? moment(res.body.startTime) : null;
            res.body.endTime = res.body.endTime != null ? moment(res.body.endTime) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((answer: IAnswer) => {
                answer.startTime = answer.startTime != null ? moment(answer.startTime) : null;
                answer.endTime = answer.endTime != null ? moment(answer.endTime) : null;
            });
        }
        return res;
    }
}

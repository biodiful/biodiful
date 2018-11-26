import { Moment } from 'moment';

export interface IAnswer {
    id?: number;
    judgeID?: string;
    challenger1?: string;
    challenger2?: string;
    winner?: string;
    startTime?: Moment;
    endTime?: Moment;
    surveyId?: number;
}

export class Answer implements IAnswer {
    constructor(
        public id?: number,
        public judgeID?: string,
        public challenger1?: string,
        public challenger2?: string,
        public winner?: string,
        public startTime?: Moment,
        public endTime?: Moment,
        public surveyId?: number
    ) {}
}

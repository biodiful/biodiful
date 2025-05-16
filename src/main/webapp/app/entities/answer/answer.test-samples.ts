import dayjs from 'dayjs/esm';

import { IAnswer, NewAnswer } from './answer.model';

export const sampleWithRequiredData: IAnswer = {
  id: 25973,
  judgeID: 'whoa anenst refine',
  challenger1: 'times excluding volunteer',
  challenger2: 'supposing gad sharply',
  winner: 'mortally certainly',
  startTime: dayjs('2018-11-26T07:02'),
  endTime: dayjs('2018-11-25T17:55'),
  poolNumber: 12263,
};

export const sampleWithPartialData: IAnswer = {
  id: 23134,
  judgeID: 'why midst',
  challenger1: 'once yearningly',
  challenger2: 'sway unlearn',
  winner: 'lawful',
  startTime: dayjs('2018-11-26T11:52'),
  endTime: dayjs('2018-11-26T07:57'),
  poolNumber: 24209,
};

export const sampleWithFullData: IAnswer = {
  id: 29585,
  judgeID: 'yum',
  challenger1: 'devastation',
  challenger2: 'broadside lazily',
  winner: 'primary harmful violent',
  startTime: dayjs('2018-11-26T14:45'),
  endTime: dayjs('2018-11-26T13:12'),
  poolNumber: 17197,
};

export const sampleWithNewData: NewAnswer = {
  judgeID: 'ugh',
  challenger1: 'bid',
  challenger2: 'archaeology throughout',
  winner: 'throughout',
  startTime: dayjs('2018-11-26T06:41'),
  endTime: dayjs('2018-11-25T18:10'),
  poolNumber: 26971,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

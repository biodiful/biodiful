import dayjs from 'dayjs/esm';

import { IAnswer, NewAnswer } from './answer.model';

export const sampleWithRequiredData: IAnswer = {
  id: 25973,
  judgeID: 'bè envers rechercher',
  challenger1: 'en faveur de à la faveur de élaborer',
  challenger2: 'au point que ennuyer alentour',
  winner: 'autrefois mal',
  startTime: dayjs('2018-11-26T07:02'),
  endTime: dayjs('2018-11-25T17:55'),
  poolNumber: 12263,
};

export const sampleWithPartialData: IAnswer = {
  id: 23134,
  judgeID: 'tandis que au-dedans de',
  challenger1: 'à condition que vraiment',
  challenger2: 'tarder vérifier',
  winner: 'insipide',
  startTime: dayjs('2018-11-26T11:52'),
  endTime: dayjs('2018-11-26T07:57'),
  poolNumber: 24209,
};

export const sampleWithFullData: IAnswer = {
  id: 29585,
  judgeID: 'cuicui',
  challenger1: 'guide',
  challenger2: 'attendrir tout à fait',
  winner: 'novice gigantesque triangulaire',
  startTime: dayjs('2018-11-26T14:45'),
  endTime: dayjs('2018-11-26T13:12'),
  poolNumber: 17197,
};

export const sampleWithNewData: NewAnswer = {
  judgeID: 'miaou',
  challenger1: 'diplomate',
  challenger2: 'commis de cuisine en dépit de',
  winner: 'en dépit de',
  startTime: dayjs('2018-11-26T06:41'),
  endTime: dayjs('2018-11-25T18:10'),
  poolNumber: 26971,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

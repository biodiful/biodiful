import { ISurvey, NewSurvey } from './survey.model';

export const sampleWithRequiredData: ISurvey = {
  id: 31920,
  surveyName: 'but better',
  surveyDescription: '../fake-data/blob/hipster.txt',
  formURL: 'yuck so',
  challengersPool1URL: 'that fortunately',
  numberOfMatchesPerPool: 6745,
  matchesDescription: '../fake-data/blob/hipster.txt',
  open: true,
  language: 'FR',
  uniqueChallengers: true,
};

export const sampleWithPartialData: ISurvey = {
  id: 21497,
  surveyName: 'thyme palatable',
  surveyDescription: '../fake-data/blob/hipster.txt',
  logosURL: 'excepting reproachfully mmm',
  formURL: 'against',
  challengersPool1URL: 'gown extremely',
  challengersPool2URL: 'generously so',
  numberOfMatchesPerPool: 21441,
  matchesDescription: '../fake-data/blob/hipster.txt',
  matchesDescriptionPool3: '../fake-data/blob/hipster.txt',
  open: true,
  language: 'ES',
  uniqueChallengers: true,
};

export const sampleWithFullData: ISurvey = {
  id: 15877,
  surveyName: 'decongestant',
  surveyDescription: '../fake-data/blob/hipster.txt',
  contactsDescription: '../fake-data/blob/hipster.txt',
  friendlyURL: 'abaft confound',
  photoURL: 'deafening',
  logosURL: 'offset off whoever',
  formURL: 'advertisement even rule',
  challengersPool1URL: 'major awkwardly',
  challengersPool2URL: 'partial nor hassle',
  challengersPool3URL: 'against ouch fast',
  numberOfMatchesPerPool: 4032,
  numberOfMatchesPerPool2: 22003,
  numberOfMatchesPerPool3: 5321,
  matchesDescription: '../fake-data/blob/hipster.txt',
  matchesDescriptionPool2: '../fake-data/blob/hipster.txt',
  matchesDescriptionPool3: '../fake-data/blob/hipster.txt',
  open: true,
  language: 'FR',
  uniqueChallengers: false,
};

export const sampleWithNewData: NewSurvey = {
  surveyName: 'spiteful wisely',
  surveyDescription: '../fake-data/blob/hipster.txt',
  formURL: 'after',
  challengersPool1URL: 'loudly',
  numberOfMatchesPerPool: 356,
  matchesDescription: '../fake-data/blob/hipster.txt',
  open: true,
  language: 'ES',
  uniqueChallengers: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

import { ISurvey, NewSurvey } from './survey.model';

export const sampleWithRequiredData: ISurvey = {
  id: 31920,
  surveyName: 'but better',
  surveyDescription: '../fake-data/blob/hipster.txt',
  formURL: 'yuck so',
  open: true,
  language: 'FR',
  uniqueChallengers: true,
  challengerPools: [
    {
      id: 1,
      poolOrder: 1,
      challengersURL: 'https://api.flickr.com/pool1',
      numberOfMatches: 10,
      matchesDescription: '../fake-data/blob/hipster.txt',
    },
  ],
};

export const sampleWithPartialData: ISurvey = {
  id: 21497,
  surveyName: 'thyme palatable',
  surveyDescription: '../fake-data/blob/hipster.txt',
  logosURL: 'excepting reproachfully mmm',
  formURL: 'against',
  open: true,
  language: 'ES',
  uniqueChallengers: true,
  challengerPools: [
    {
      id: 2,
      poolOrder: 1,
      challengersURL: 'https://api.flickr.com/pool1',
      numberOfMatches: 15,
      matchesDescription: '../fake-data/blob/hipster.txt',
    },
    {
      id: 3,
      poolOrder: 2,
      challengersURL: 'https://api.flickr.com/pool2',
      numberOfMatches: 20,
      matchesDescription: '../fake-data/blob/hipster.txt',
    },
  ],
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
  open: true,
  language: 'FR',
  uniqueChallengers: false,
  challengerPools: [
    {
      id: 4,
      poolOrder: 1,
      challengersURL: 'https://api.flickr.com/pool1',
      numberOfMatches: 10,
      matchesDescription: '../fake-data/blob/hipster.txt',
    },
    {
      id: 5,
      poolOrder: 2,
      challengersURL: 'https://api.flickr.com/pool2',
      numberOfMatches: 15,
      matchesDescription: '../fake-data/blob/hipster.txt',
    },
    {
      id: 6,
      poolOrder: 3,
      challengersURL: 'https://api.flickr.com/pool3',
      numberOfMatches: 20,
      matchesDescription: '../fake-data/blob/hipster.txt',
    },
  ],
};

export const sampleWithNewData: NewSurvey = {
  surveyName: 'spiteful wisely',
  surveyDescription: '../fake-data/blob/hipster.txt',
  formURL: 'after',
  open: true,
  language: 'ES',
  uniqueChallengers: false,
  challengerPools: [
    {
      poolOrder: 1,
      challengersURL: 'https://api.flickr.com/pool1',
      numberOfMatches: 10,
      matchesDescription: '../fake-data/blob/hipster.txt',
    },
  ],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

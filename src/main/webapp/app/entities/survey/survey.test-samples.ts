import { ISurvey, NewSurvey } from './survey.model';

export const sampleWithRequiredData: ISurvey = {
  id: 31920,
  surveyName: 'de manière à ce que applaudir',
  surveyDescription: '../fake-data/blob/hipster.txt',
  formURL: 'ah de façon que',
  challengersPool1URL: 'tant entièrement',
  numberOfMatchesPerPool: 6745,
  matchesDescription: '../fake-data/blob/hipster.txt',
  open: true,
  language: 'FR',
  uniqueChallengers: true,
};

export const sampleWithPartialData: ISurvey = {
  id: 21497,
  surveyName: 'population du Québec mince',
  surveyDescription: '../fake-data/blob/hipster.txt',
  logosURL: 'à la merci sitôt plic',
  formURL: 'depuis',
  challengersPool1URL: 'spécialiste complètement',
  challengersPool2URL: 'grandement de façon que',
  numberOfMatchesPerPool: 21441,
  matchesDescription: '../fake-data/blob/hipster.txt',
  matchesDescriptionPool3: '../fake-data/blob/hipster.txt',
  open: true,
  language: 'ES',
  uniqueChallengers: true,
};

export const sampleWithFullData: ISurvey = {
  id: 15877,
  surveyName: 'adversaire',
  surveyDescription: '../fake-data/blob/hipster.txt',
  contactsDescription: '../fake-data/blob/hipster.txt',
  friendlyURL: 'après commander',
  photoURL: 'coupable',
  logosURL: 'mélanger auprès de alors que',
  formURL: 'cadre de crainte que renouveler',
  challengersPool1URL: 'inspecter debout',
  challengersPool2URL: 'minuscule dans la mesure où exploiter',
  challengersPool3URL: 'depuis plouf divinement',
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
  surveyName: 'serviable sans doute',
  surveyDescription: '../fake-data/blob/hipster.txt',
  formURL: 'que',
  challengersPool1URL: 'alors',
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

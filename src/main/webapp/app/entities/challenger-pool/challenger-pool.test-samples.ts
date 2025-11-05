import { IChallengerPool } from './challenger-pool.model';

export const sampleWithRequiredData: IChallengerPool = {
  id: 1,
  poolOrder: 1,
  challengersURL: 'https://test-bucket.s3.us-east-1.amazonaws.com/pool-1/',
  numberOfMatches: 10,
  matchesDescription: 'Test pool description',
};

export const sampleWithPartialData: IChallengerPool = {
  id: 2,
  poolOrder: 2,
  challengersURL: 'https://test-bucket.s3.us-east-1.amazonaws.com/pool-2/',
  numberOfMatches: 15,
  matchesDescription: 'Another pool description',
  surveyId: 100,
};

export const sampleWithFullData: IChallengerPool = {
  id: 3,
  poolOrder: 3,
  challengersURL: 'https://test-bucket.s3.us-east-1.amazonaws.com/pool-3/',
  numberOfMatches: 20,
  matchesDescription: 'Full pool description with all fields',
  surveyId: 200,
};

export const sampleWithNewData: IChallengerPool = {
  poolOrder: 1,
  challengersURL: 'https://test-bucket.s3.us-east-1.amazonaws.com/new-pool/',
  numberOfMatches: 5,
  matchesDescription: 'New pool description',
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);

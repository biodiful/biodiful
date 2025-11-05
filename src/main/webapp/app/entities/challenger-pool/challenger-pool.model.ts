export interface IChallengerPool {
  id?: number;
  poolOrder?: number;
  /** URL to S3 folder containing challenger images (e.g., https://bucket.s3.region.amazonaws.com/folder/) */
  challengersURL?: string;
  numberOfMatches?: number;
  matchesDescription?: string;
  surveyId?: number | null;
}

export type NewChallengerPool = Omit<IChallengerPool, 'id'> & { id: null };

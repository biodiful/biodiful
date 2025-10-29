export interface IChallengerPool {
  id?: number;
  poolOrder?: number;
  challengersURL?: string;
  numberOfMatches?: number;
  matchesDescription?: string;
  surveyId?: number | null;
}

export type NewChallengerPool = Omit<IChallengerPool, 'id'> & { id: null };

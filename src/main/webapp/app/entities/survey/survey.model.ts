import { Language } from 'app/entities/enumerations/language.model';
import { IChallengerPool } from 'app/entities/challenger-pool/challenger-pool.model';

export interface ISurvey {
  id: number;
  surveyName?: string | null;
  surveyDescription?: string | null;
  contactsDescription?: string | null;
  friendlyURL?: string | null;
  photoURL?: string | null;
  logosURL?: string | null;
  formURL?: string | null;
  challengerPools?: IChallengerPool[] | null;
  open?: boolean | null;
  language?: keyof typeof Language | null;
  uniqueChallengers?: boolean | null;
}

export type NewSurvey = Omit<ISurvey, 'id'> & { id: null };

import { Language } from 'app/entities/enumerations/language.model';

export interface ISurvey {
  id: number;
  surveyName?: string | null;
  surveyDescription?: string | null;
  contactsDescription?: string | null;
  friendlyURL?: string | null;
  photoURL?: string | null;
  logosURL?: string | null;
  formURL?: string | null;
  challengersPool1URL?: string | null;
  challengersPool2URL?: string | null;
  challengersPool3URL?: string | null;
  numberOfMatchesPerPool?: number | null;
  numberOfMatchesPerPool2?: number | null;
  numberOfMatchesPerPool3?: number | null;
  matchesDescription?: string | null;
  matchesDescriptionPool2?: string | null;
  matchesDescriptionPool3?: string | null;
  open?: boolean | null;
  language?: keyof typeof Language | null;
  uniqueChallengers?: boolean | null;
}

export type NewSurvey = Omit<ISurvey, 'id'> & { id: null };

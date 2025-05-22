import dayjs from 'dayjs/esm';
import { ISurvey } from 'app/entities/survey/survey.model';

export interface IAnswer {
  id: number;
  judgeID?: string | null;
  challenger1?: string | null;
  challenger2?: string | null;
  winner?: string | null;
  startTime?: dayjs.Dayjs | null;
  endTime?: dayjs.Dayjs | null;
  poolNumber?: number | null;
  survey?: Pick<ISurvey, 'id'> | null;
}

export type NewAnswer = Omit<IAnswer, 'id'> & { id: null };

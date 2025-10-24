import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAnswer, NewAnswer } from '../answer.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAnswer for edit and NewAnswerFormGroupInput for create.
 */
type AnswerFormGroupInput = IAnswer | PartialWithRequiredKeyOf<NewAnswer>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAnswer | NewAnswer> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type AnswerFormRawValue = FormValueOf<IAnswer>;

type NewAnswerFormRawValue = FormValueOf<NewAnswer>;

type AnswerFormDefaults = Pick<NewAnswer, 'id' | 'startTime' | 'endTime'>;

type AnswerFormGroupContent = {
  id: FormControl<AnswerFormRawValue['id'] | NewAnswer['id']>;
  judgeID: FormControl<AnswerFormRawValue['judgeID']>;
  challenger1: FormControl<AnswerFormRawValue['challenger1']>;
  challenger2: FormControl<AnswerFormRawValue['challenger2']>;
  winner: FormControl<AnswerFormRawValue['winner']>;
  startTime: FormControl<AnswerFormRawValue['startTime']>;
  endTime: FormControl<AnswerFormRawValue['endTime']>;
  poolNumber: FormControl<AnswerFormRawValue['poolNumber']>;
  survey: FormControl<AnswerFormRawValue['survey']>;
};

export type AnswerFormGroup = FormGroup<AnswerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AnswerFormService {
  createAnswerFormGroup(answer: AnswerFormGroupInput = { id: null }): AnswerFormGroup {
    const answerRawValue = this.convertAnswerToAnswerRawValue({
      ...this.getFormDefaults(),
      ...answer,
    });
    return new FormGroup<AnswerFormGroupContent>({
      id: new FormControl(
        { value: answerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      judgeID: new FormControl(answerRawValue.judgeID, {
        validators: [Validators.required],
      }),
      challenger1: new FormControl(answerRawValue.challenger1, {
        validators: [Validators.required],
      }),
      challenger2: new FormControl(answerRawValue.challenger2, {
        validators: [Validators.required],
      }),
      winner: new FormControl(answerRawValue.winner, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(answerRawValue.startTime, {
        validators: [Validators.required],
      }),
      endTime: new FormControl(answerRawValue.endTime, {
        validators: [Validators.required],
      }),
      poolNumber: new FormControl(answerRawValue.poolNumber, {
        validators: [Validators.required],
      }),
      survey: new FormControl(answerRawValue.survey),
    });
  }

  getAnswer(form: AnswerFormGroup): IAnswer | NewAnswer {
    return this.convertAnswerRawValueToAnswer(form.getRawValue() as AnswerFormRawValue | NewAnswerFormRawValue);
  }

  resetForm(form: AnswerFormGroup, answer: AnswerFormGroupInput): void {
    const answerRawValue = this.convertAnswerToAnswerRawValue({ ...this.getFormDefaults(), ...answer });
    form.reset(
      {
        ...answerRawValue,
        id: { value: answerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AnswerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertAnswerRawValueToAnswer(rawAnswer: AnswerFormRawValue | NewAnswerFormRawValue): IAnswer | NewAnswer {
    return {
      ...rawAnswer,
      startTime: dayjs(rawAnswer.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawAnswer.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertAnswerToAnswerRawValue(
    answer: IAnswer | (Partial<NewAnswer> & AnswerFormDefaults),
  ): AnswerFormRawValue | PartialWithRequiredKeyOf<NewAnswerFormRawValue> {
    return {
      ...answer,
      startTime: answer.startTime ? answer.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: answer.endTime ? answer.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}

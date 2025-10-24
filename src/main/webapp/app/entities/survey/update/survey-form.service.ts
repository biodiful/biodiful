import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISurvey, NewSurvey } from '../survey.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISurvey for edit and NewSurveyFormGroupInput for create.
 */
type SurveyFormGroupInput = ISurvey | PartialWithRequiredKeyOf<NewSurvey>;

type SurveyFormDefaults = Pick<NewSurvey, 'id' | 'open' | 'uniqueChallengers'>;

type SurveyFormGroupContent = {
  id: FormControl<ISurvey['id'] | NewSurvey['id']>;
  surveyName: FormControl<ISurvey['surveyName']>;
  surveyDescription: FormControl<ISurvey['surveyDescription']>;
  contactsDescription: FormControl<ISurvey['contactsDescription']>;
  friendlyURL: FormControl<ISurvey['friendlyURL']>;
  photoURL: FormControl<ISurvey['photoURL']>;
  logosURL: FormControl<ISurvey['logosURL']>;
  formURL: FormControl<ISurvey['formURL']>;
  challengersPool1URL: FormControl<ISurvey['challengersPool1URL']>;
  challengersPool2URL: FormControl<ISurvey['challengersPool2URL']>;
  challengersPool3URL: FormControl<ISurvey['challengersPool3URL']>;
  numberOfMatchesPerPool: FormControl<ISurvey['numberOfMatchesPerPool']>;
  numberOfMatchesPerPool2: FormControl<ISurvey['numberOfMatchesPerPool2']>;
  numberOfMatchesPerPool3: FormControl<ISurvey['numberOfMatchesPerPool3']>;
  matchesDescription: FormControl<ISurvey['matchesDescription']>;
  matchesDescriptionPool2: FormControl<ISurvey['matchesDescriptionPool2']>;
  matchesDescriptionPool3: FormControl<ISurvey['matchesDescriptionPool3']>;
  open: FormControl<ISurvey['open']>;
  language: FormControl<ISurvey['language']>;
  uniqueChallengers: FormControl<ISurvey['uniqueChallengers']>;
};

export type SurveyFormGroup = FormGroup<SurveyFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SurveyFormService {
  createSurveyFormGroup(survey: SurveyFormGroupInput = { id: null }): SurveyFormGroup {
    const surveyRawValue = {
      ...this.getFormDefaults(),
      ...survey,
    };
    return new FormGroup<SurveyFormGroupContent>({
      id: new FormControl(
        { value: surveyRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      surveyName: new FormControl(surveyRawValue.surveyName, {
        validators: [Validators.required],
      }),
      surveyDescription: new FormControl(surveyRawValue.surveyDescription, {
        validators: [Validators.required],
      }),
      contactsDescription: new FormControl(surveyRawValue.contactsDescription),
      friendlyURL: new FormControl(surveyRawValue.friendlyURL),
      photoURL: new FormControl(surveyRawValue.photoURL),
      logosURL: new FormControl(surveyRawValue.logosURL),
      formURL: new FormControl(surveyRawValue.formURL, {
        validators: [Validators.required],
      }),
      challengersPool1URL: new FormControl(surveyRawValue.challengersPool1URL, {
        validators: [Validators.required],
      }),
      challengersPool2URL: new FormControl(surveyRawValue.challengersPool2URL),
      challengersPool3URL: new FormControl(surveyRawValue.challengersPool3URL),
      numberOfMatchesPerPool: new FormControl(surveyRawValue.numberOfMatchesPerPool, {
        validators: [Validators.required],
      }),
      numberOfMatchesPerPool2: new FormControl(surveyRawValue.numberOfMatchesPerPool2),
      numberOfMatchesPerPool3: new FormControl(surveyRawValue.numberOfMatchesPerPool3),
      matchesDescription: new FormControl(surveyRawValue.matchesDescription, {
        validators: [Validators.required],
      }),
      matchesDescriptionPool2: new FormControl(surveyRawValue.matchesDescriptionPool2),
      matchesDescriptionPool3: new FormControl(surveyRawValue.matchesDescriptionPool3),
      open: new FormControl(surveyRawValue.open, {
        validators: [Validators.required],
      }),
      language: new FormControl(surveyRawValue.language, {
        validators: [Validators.required],
      }),
      uniqueChallengers: new FormControl(surveyRawValue.uniqueChallengers, {
        validators: [Validators.required],
      }),
    });
  }

  getSurvey(form: SurveyFormGroup): ISurvey | NewSurvey {
    return form.getRawValue() as ISurvey | NewSurvey;
  }

  resetForm(form: SurveyFormGroup, survey: SurveyFormGroupInput): void {
    const surveyRawValue = { ...this.getFormDefaults(), ...survey };
    form.reset(
      {
        ...surveyRawValue,
        id: { value: surveyRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SurveyFormDefaults {
    return {
      id: null,
      open: false,
      uniqueChallengers: false,
    };
  }
}

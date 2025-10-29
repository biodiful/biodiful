import { Injectable } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';

import { ISurvey, NewSurvey } from '../survey.model';
import { IChallengerPool } from 'app/entities/challenger-pool/challenger-pool.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISurvey for edit and NewSurveyFormGroupInput for create.
 */
type SurveyFormGroupInput = ISurvey | PartialWithRequiredKeyOf<NewSurvey>;

type SurveyFormDefaults = Pick<NewSurvey, 'id' | 'open' | 'uniqueChallengers' | 'challengerPools'>;

type ChallengerPoolFormGroupContent = {
  id: FormControl<number | null>;
  poolOrder: FormControl<number | null>;
  challengersURL: FormControl<string | null>;
  numberOfMatches: FormControl<number | null>;
  matchesDescription: FormControl<string | null>;
};

type SurveyFormGroupContent = {
  id: FormControl<ISurvey['id'] | NewSurvey['id']>;
  surveyName: FormControl<ISurvey['surveyName']>;
  surveyDescription: FormControl<ISurvey['surveyDescription']>;
  contactsDescription: FormControl<ISurvey['contactsDescription']>;
  friendlyURL: FormControl<ISurvey['friendlyURL']>;
  photoURL: FormControl<ISurvey['photoURL']>;
  logosURL: FormControl<ISurvey['logosURL']>;
  formURL: FormControl<ISurvey['formURL']>;
  challengerPools: FormArray<FormGroup<ChallengerPoolFormGroupContent>>;
  open: FormControl<ISurvey['open']>;
  language: FormControl<ISurvey['language']>;
  uniqueChallengers: FormControl<ISurvey['uniqueChallengers']>;
};

export type SurveyFormGroup = FormGroup<SurveyFormGroupContent>;
export type ChallengerPoolFormGroup = FormGroup<ChallengerPoolFormGroupContent>;

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
      challengerPools: new FormArray<FormGroup<ChallengerPoolFormGroupContent>>(
        (surveyRawValue.challengerPools ?? []).map(pool => this.createChallengerPoolFormGroup(pool)),
      ),
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

  createChallengerPoolFormGroup(pool: IChallengerPool | null = null): ChallengerPoolFormGroup {
    return new FormGroup<ChallengerPoolFormGroupContent>({
      id: new FormControl(pool?.id ?? null),
      poolOrder: new FormControl(pool?.poolOrder ?? null, {
        validators: [Validators.required],
      }),
      challengersURL: new FormControl(pool?.challengersURL ?? null, {
        validators: [Validators.required],
      }),
      numberOfMatches: new FormControl(pool?.numberOfMatches ?? null, {
        validators: [Validators.required, Validators.min(1)],
      }),
      matchesDescription: new FormControl(pool?.matchesDescription ?? null, {
        validators: [Validators.required],
      }),
    });
  }

  getSurvey(form: SurveyFormGroup): ISurvey | NewSurvey {
    const rawValue = form.getRawValue();
    return {
      ...rawValue,
      challengerPools: rawValue.challengerPools,
    } as ISurvey | NewSurvey;
  }

  resetForm(form: SurveyFormGroup, survey: SurveyFormGroupInput): void {
    const surveyRawValue = { ...this.getFormDefaults(), ...survey };

    // Clear existing challenger pools
    const challengerPoolsArray = form.controls.challengerPools;
    while (challengerPoolsArray.length) {
      challengerPoolsArray.removeAt(0);
    }

    // Add new challenger pools
    (surveyRawValue.challengerPools ?? []).forEach(pool => {
      challengerPoolsArray.push(this.createChallengerPoolFormGroup(pool));
    });

    form.reset(
      {
        ...surveyRawValue,
        id: { value: surveyRawValue.id, disabled: true },
        challengerPools: challengerPoolsArray.value,
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SurveyFormDefaults {
    return {
      id: null,
      open: false,
      uniqueChallengers: false,
      challengerPools: [],
    };
  }
}

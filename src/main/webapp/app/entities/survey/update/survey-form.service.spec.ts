import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../survey.test-samples';

import { SurveyFormService } from './survey-form.service';

describe('Survey Form Service', () => {
  let service: SurveyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SurveyFormService);
  });

  describe('Service methods', () => {
    describe('createSurveyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSurveyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            surveyName: expect.any(Object),
            surveyDescription: expect.any(Object),
            contactsDescription: expect.any(Object),
            friendlyURL: expect.any(Object),
            photoURL: expect.any(Object),
            logosURL: expect.any(Object),
            formURL: expect.any(Object),
            open: expect.any(Object),
            language: expect.any(Object),
            uniqueChallengers: expect.any(Object),
            challengerPools: expect.any(Object),
          }),
        );
      });

      it('passing ISurvey should create a new form with FormGroup', () => {
        const formGroup = service.createSurveyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            surveyName: expect.any(Object),
            surveyDescription: expect.any(Object),
            contactsDescription: expect.any(Object),
            friendlyURL: expect.any(Object),
            photoURL: expect.any(Object),
            logosURL: expect.any(Object),
            formURL: expect.any(Object),
            open: expect.any(Object),
            language: expect.any(Object),
            uniqueChallengers: expect.any(Object),
            challengerPools: expect.any(Object),
          }),
        );
      });
    });

    describe('getSurvey', () => {
      it('should return NewSurvey for default Survey initial value', () => {
        const formGroup = service.createSurveyFormGroup(sampleWithNewData);

        const survey = service.getSurvey(formGroup) as any;

        expect(survey).toMatchObject(sampleWithNewData);
      });

      it('should return NewSurvey for empty Survey initial value', () => {
        const formGroup = service.createSurveyFormGroup();

        const survey = service.getSurvey(formGroup) as any;

        expect(survey).toMatchObject({});
      });

      it('should return ISurvey', () => {
        const formGroup = service.createSurveyFormGroup(sampleWithRequiredData);

        const survey = service.getSurvey(formGroup) as any;

        expect(survey).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISurvey should not enable id FormControl', () => {
        const formGroup = service.createSurveyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSurvey should disable id FormControl', () => {
        const formGroup = service.createSurveyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});

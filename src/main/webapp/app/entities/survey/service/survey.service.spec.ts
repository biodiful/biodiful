import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISurvey } from '../survey.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../survey.test-samples';

import { SurveyService } from './survey.service';

const requireRestSample: ISurvey = {
  ...sampleWithRequiredData,
};

describe('Survey Service', () => {
  let service: SurveyService;
  let httpMock: HttpTestingController;
  let expectedResult: ISurvey | ISurvey[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SurveyService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Survey', () => {
      const survey = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(survey).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Survey', () => {
      const survey = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(survey).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Survey', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Survey', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Survey', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSurveyToCollectionIfMissing', () => {
      it('should add a Survey to an empty array', () => {
        const survey: ISurvey = sampleWithRequiredData;
        expectedResult = service.addSurveyToCollectionIfMissing([], survey);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(survey);
      });

      it('should not add a Survey to an array that contains it', () => {
        const survey: ISurvey = sampleWithRequiredData;
        const surveyCollection: ISurvey[] = [
          {
            ...survey,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSurveyToCollectionIfMissing(surveyCollection, survey);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Survey to an array that doesn't contain it", () => {
        const survey: ISurvey = sampleWithRequiredData;
        const surveyCollection: ISurvey[] = [sampleWithPartialData];
        expectedResult = service.addSurveyToCollectionIfMissing(surveyCollection, survey);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(survey);
      });

      it('should add only unique Survey to an array', () => {
        const surveyArray: ISurvey[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const surveyCollection: ISurvey[] = [sampleWithRequiredData];
        expectedResult = service.addSurveyToCollectionIfMissing(surveyCollection, ...surveyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const survey: ISurvey = sampleWithRequiredData;
        const survey2: ISurvey = sampleWithPartialData;
        expectedResult = service.addSurveyToCollectionIfMissing([], survey, survey2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(survey);
        expect(expectedResult).toContain(survey2);
      });

      it('should accept null and undefined values', () => {
        const survey: ISurvey = sampleWithRequiredData;
        expectedResult = service.addSurveyToCollectionIfMissing([], null, survey, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(survey);
      });

      it('should return initial array if no Survey is added', () => {
        const surveyCollection: ISurvey[] = [sampleWithRequiredData];
        expectedResult = service.addSurveyToCollectionIfMissing(surveyCollection, undefined, null);
        expect(expectedResult).toEqual(surveyCollection);
      });
    });

    describe('compareSurvey', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSurvey(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 18911 };
        const entity2 = null;

        const compareResult1 = service.compareSurvey(entity1, entity2);
        const compareResult2 = service.compareSurvey(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 18911 };
        const entity2 = { id: 18583 };

        const compareResult1 = service.compareSurvey(entity1, entity2);
        const compareResult2 = service.compareSurvey(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 18911 };
        const entity2 = { id: 18911 };

        const compareResult1 = service.compareSurvey(entity1, entity2);
        const compareResult2 = service.compareSurvey(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

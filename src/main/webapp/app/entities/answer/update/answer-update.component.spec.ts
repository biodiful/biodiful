import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISurvey } from 'app/entities/survey/survey.model';
import { SurveyService } from 'app/entities/survey/service/survey.service';
import { AnswerService } from '../service/answer.service';
import { IAnswer } from '../answer.model';
import { AnswerFormService } from './answer-form.service';

import { AnswerUpdateComponent } from './answer-update.component';

describe('Answer Management Update Component', () => {
  let comp: AnswerUpdateComponent;
  let fixture: ComponentFixture<AnswerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let answerFormService: AnswerFormService;
  let answerService: AnswerService;
  let surveyService: SurveyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AnswerUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AnswerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnswerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    answerFormService = TestBed.inject(AnswerFormService);
    answerService = TestBed.inject(AnswerService);
    surveyService = TestBed.inject(SurveyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Survey query and add missing value', () => {
      const answer: IAnswer = { id: 25690 };
      const survey: ISurvey = { id: 18911 };
      answer.survey = survey;

      const surveyCollection: ISurvey[] = [{ id: 18911 }];
      jest.spyOn(surveyService, 'query').mockReturnValue(of(new HttpResponse({ body: surveyCollection })));
      const additionalSurveys = [survey];
      const expectedCollection: ISurvey[] = [...additionalSurveys, ...surveyCollection];
      jest.spyOn(surveyService, 'addSurveyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(surveyService.query).toHaveBeenCalled();
      expect(surveyService.addSurveyToCollectionIfMissing).toHaveBeenCalledWith(
        surveyCollection,
        ...additionalSurveys.map(expect.objectContaining),
      );
      expect(comp.surveysSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const answer: IAnswer = { id: 25690 };
      const survey: ISurvey = { id: 18911 };
      answer.survey = survey;

      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      expect(comp.surveysSharedCollection).toContainEqual(survey);
      expect(comp.answer).toEqual(answer);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnswer>>();
      const answer = { id: 19540 };
      jest.spyOn(answerFormService, 'getAnswer').mockReturnValue(answer);
      jest.spyOn(answerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answer }));
      saveSubject.complete();

      // THEN
      expect(answerFormService.getAnswer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(answerService.update).toHaveBeenCalledWith(expect.objectContaining(answer));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnswer>>();
      const answer = { id: 19540 };
      jest.spyOn(answerFormService, 'getAnswer').mockReturnValue({ id: null });
      jest.spyOn(answerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ answer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: answer }));
      saveSubject.complete();

      // THEN
      expect(answerFormService.getAnswer).toHaveBeenCalled();
      expect(answerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnswer>>();
      const answer = { id: 19540 };
      jest.spyOn(answerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ answer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(answerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSurvey', () => {
      it('should forward to surveyService', () => {
        const entity = { id: 18911 };
        const entity2 = { id: 18583 };
        jest.spyOn(surveyService, 'compareSurvey');
        comp.compareSurvey(entity, entity2);
        expect(surveyService.compareSurvey).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});

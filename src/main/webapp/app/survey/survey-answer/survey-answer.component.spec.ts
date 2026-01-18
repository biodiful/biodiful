import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { TranslateModule } from '@ngx-translate/core';

import { SurveyAnswerComponent } from './survey-answer.component';
import { ISurvey } from 'app/entities/survey/survey.model';
import { AnswerService } from 'app/entities/answer/service/answer.service';

describe('SurveyAnswerComponent - Pool Introduction', () => {
  let component: SurveyAnswerComponent;
  let fixture: ComponentFixture<SurveyAnswerComponent>;
  let answerService: AnswerService;

  const mockSurveyWithIntroduction: ISurvey = {
    id: 1,
    surveyName: 'Test Survey',
    surveyDescription: 'Test Description',
    formURL: 'https://forms.google.com/test?entry=',
    open: true,
    language: 'EN',
    uniqueChallengers: false,
    challengerPools: [
      {
        id: 1,
        poolOrder: 1,
        challengersURL: 'https://test-bucket.s3.us-east-1.amazonaws.com/pool-1/',
        numberOfMatches: 2,
        matchesDescription: 'Pool 1 description',
        introductionMessage: '<p>Welcome to Pool 1</p>',
      },
      {
        id: 2,
        poolOrder: 2,
        challengersURL: 'https://test-bucket.s3.us-east-1.amazonaws.com/pool-2/',
        numberOfMatches: 2,
        matchesDescription: 'Pool 2 description',
        introductionMessage: '<p>Welcome to Pool 2</p>',
      },
    ],
  };

  const mockSurveyWithoutIntroduction: ISurvey = {
    id: 2,
    surveyName: 'Test Survey No Intro',
    surveyDescription: 'Test Description',
    formURL: 'https://forms.google.com/test?entry=',
    open: true,
    language: 'EN',
    uniqueChallengers: false,
    challengerPools: [
      {
        id: 3,
        poolOrder: 1,
        challengersURL: 'https://test-bucket.s3.us-east-1.amazonaws.com/pool-3/',
        numberOfMatches: 2,
        matchesDescription: 'Pool 3 description',
      },
    ],
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SurveyAnswerComponent, TranslateModule.forRoot()],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({ survey: mockSurveyWithIntroduction }),
          },
        },
        AnswerService,
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SurveyAnswerComponent);
    component = fixture.componentInstance;
    answerService = TestBed.inject(AnswerService);
  });

  describe('Pool Introduction Logic', () => {
    it('should create component', () => {
      expect(component).toBeTruthy();
    });

    it('should return introduction message for current pool', () => {
      component.survey = mockSurveyWithIntroduction;
      component.currentPoolIndex = 0;

      const introduction = component.getIntroductionForCurrentPool();

      expect(introduction).toBe('<p>Welcome to Pool 1</p>');
    });

    it('should return empty string when pool has no introduction message', () => {
      component.survey = mockSurveyWithoutIntroduction;
      component.currentPoolIndex = 0;

      const introduction = component.getIntroductionForCurrentPool();

      expect(introduction).toBe('');
    });

    it('should detect when to show pool introduction', () => {
      component.survey = mockSurveyWithIntroduction;
      component.currentPoolIndex = 0;
      component.poolIntroductionShown = new Set<number>();

      const shouldShow = component.shouldShowPoolIntroduction();

      expect(shouldShow).toBe(true);
    });

    it('should not show introduction if already shown for this pool', () => {
      component.survey = mockSurveyWithIntroduction;
      component.currentPoolIndex = 0;
      component.poolIntroductionShown = new Set<number>([0]);

      const shouldShow = component.shouldShowPoolIntroduction();

      expect(shouldShow).toBe(false);
    });

    it('should not show introduction if pool has no introduction message', () => {
      component.survey = mockSurveyWithoutIntroduction;
      component.currentPoolIndex = 0;
      component.poolIntroductionShown = new Set<number>();

      const shouldShow = component.shouldShowPoolIntroduction();

      expect(shouldShow).toBe(false);
    });

    it('should mark pool introduction as shown when started', () => {
      component.survey = mockSurveyWithIntroduction;
      component.currentPoolIndex = 0;
      component.poolIntroductionShown = new Set<number>();
      component.showPoolIntroduction.set(true);
      component.challengerPools.set([
        [
          { id: 'img1', url: 'url1' },
          { id: 'img2', url: 'url2' },
        ],
      ]);

      component.onPoolIntroductionStart();

      expect(component.poolIntroductionShown.has(0)).toBe(true);
      expect(component.showPoolIntroduction()).toBe(false);
    });

    it('should show introduction for second pool after first pool is complete', () => {
      component.survey = mockSurveyWithIntroduction;
      component.currentPoolIndex = 1;
      component.poolIntroductionShown = new Set<number>([0]); // First pool intro was shown

      const shouldShow = component.shouldShowPoolIntroduction();

      expect(shouldShow).toBe(true);
    });

    it('should get correct introduction message for second pool', () => {
      component.survey = mockSurveyWithIntroduction;
      component.currentPoolIndex = 1;

      const introduction = component.getIntroductionForCurrentPool();

      expect(introduction).toBe('<p>Welcome to Pool 2</p>');
    });

    it('should handle undefined introduction message gracefully', () => {
      component.survey = {
        ...mockSurveyWithIntroduction,
        challengerPools: [
          {
            id: 1,
            poolOrder: 1,
            challengersURL: 'https://test-bucket.s3.us-east-1.amazonaws.com/pool-1/',
            numberOfMatches: 2,
            matchesDescription: 'Pool 1 description',
            introductionMessage: undefined,
          },
        ],
      };
      component.currentPoolIndex = 0;

      const introduction = component.getIntroductionForCurrentPool();
      const shouldShow = component.shouldShowPoolIntroduction();

      expect(introduction).toBe('');
      expect(shouldShow).toBe(false);
    });
  });

  describe('Integration with Match Flow', () => {
    it('should initialize with showPoolIntroduction as false', () => {
      expect(component.showPoolIntroduction()).toBe(false);
    });

    it('should initialize with empty poolIntroductionShown set', () => {
      expect(component.poolIntroductionShown.size).toBe(0);
    });
  });
});

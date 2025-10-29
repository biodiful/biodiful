import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AnswerDetailComponent } from './answer-detail.component';

describe('Answer Management Detail Component', () => {
  let comp: AnswerDetailComponent;
  let fixture: ComponentFixture<AnswerDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnswerDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./answer-detail.component').then(m => m.AnswerDetailComponent),
              resolve: { answer: () => of({ id: 19540 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AnswerDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AnswerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load answer on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AnswerDetailComponent);

      // THEN
      expect(instance.answer()).toEqual(expect.objectContaining({ id: 19540 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { TranslateModule } from '@ngx-translate/core';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from '@fortawesome/free-solid-svg-icons';

import { SurveyAnswerPoolIntroComponent } from './survey-answer-pool-intro.component';

describe('SurveyAnswerPoolIntroComponent', () => {
  let component: SurveyAnswerPoolIntroComponent;
  let fixture: ComponentFixture<SurveyAnswerPoolIntroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SurveyAnswerPoolIntroComponent, TranslateModule.forRoot(), FontAwesomeModule],
      providers: [provideHttpClient()],
    }).compileComponents();

    // Add FontAwesome icons to library
    const library = TestBed.inject(FaIconLibrary);
    library.addIconPacks(fas);

    fixture = TestBed.createComponent(SurveyAnswerPoolIntroComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('poolNumber', 1);
    fixture.componentRef.setInput('introductionMessage', '<p>Test introduction message</p>');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display introduction message as HTML', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const introDiv = compiled.querySelector('.pool-introduction');
    expect(introDiv?.innerHTML).toBe('<p>Test introduction message</p>');
  });

  it('should emit startPool event when start button is clicked', () => {
    let emitted = false;
    fixture.componentRef.instance.startPool.subscribe(() => {
      emitted = true;
    });

    const compiled = fixture.nativeElement as HTMLElement;
    const startButton = compiled.querySelector('[data-cy="startPoolButton"]') as HTMLButtonElement;
    startButton.click();

    expect(emitted).toBe(true);
  });

  it('should update introduction message when input changes', () => {
    fixture.componentRef.setInput('introductionMessage', '<p>Updated message</p>');
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const introDiv = compiled.querySelector('.pool-introduction');
    expect(introDiv?.innerHTML).toBe('<p>Updated message</p>');
  });
});

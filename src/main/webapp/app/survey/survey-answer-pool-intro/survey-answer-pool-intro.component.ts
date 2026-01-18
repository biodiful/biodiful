import { Component, input, output } from '@angular/core';
import SharedModule from 'app/shared/shared.module';

@Component({
  selector: 'jhi-survey-answer-pool-intro',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './survey-answer-pool-intro.component.html',
  styleUrl: './survey-answer-pool-intro.component.scss',
})
export class SurveyAnswerPoolIntroComponent {
  poolNumber = input.required<number>();
  introductionMessage = input.required<string>();

  startPool = output();

  onStart(): void {
    this.startPool.emit();
  }
}

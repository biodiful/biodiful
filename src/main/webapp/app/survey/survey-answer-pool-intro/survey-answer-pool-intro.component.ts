import { Component, input, output } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@Component({
  selector: 'jhi-survey-answer-pool-intro',
  standalone: true,
  imports: [TranslateModule, FontAwesomeModule],
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

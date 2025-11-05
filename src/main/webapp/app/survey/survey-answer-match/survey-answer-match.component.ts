import { Component, input, output } from '@angular/core';
import { Challenger } from 'app/shared/model/challenger.model';

@Component({
  selector: 'jhi-survey-answer-match',
  templateUrl: './survey-answer-match.component.html',
  standalone: true,
  styleUrl: './survey-answer-match.scss',
})
export class SurveyAnswerMatchComponent {
  // Show loader while retrieving images from S3
  challengerOne = input<Challenger | undefined, Challenger | undefined>(undefined, {
    transform: value => value ?? new Challenger('UNDEFINED_1', '/content/images/loader.gif'),
  });

  challengerTwo = input<Challenger | undefined, Challenger | undefined>(undefined, {
    transform: value => value ?? new Challenger('UNDEFINED_2', '/content/images/loader.gif'),
  });

  winnerSelected = output<Challenger>();

  challengerClicked(challenger: Challenger): void {
    // TODO add start and end date to challenges
    this.winnerSelected.emit(challenger);
  }
}

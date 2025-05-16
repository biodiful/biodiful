import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IAnswer } from '../answer.model';

@Component({
  selector: 'jhi-answer-detail',
  templateUrl: './answer-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class AnswerDetailComponent {
  answer = input<IAnswer | null>(null);

  previousState(): void {
    window.history.back();
  }
}

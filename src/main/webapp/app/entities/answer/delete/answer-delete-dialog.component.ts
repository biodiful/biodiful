import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAnswer } from '../answer.model';
import { AnswerService } from '../service/answer.service';

@Component({
  templateUrl: './answer-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AnswerDeleteDialogComponent {
  answer?: IAnswer;

  protected answerService = inject(AnswerService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.answerService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

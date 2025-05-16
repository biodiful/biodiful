import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISurvey } from '../survey.model';
import { SurveyService } from '../service/survey.service';

@Component({
  templateUrl: './survey-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SurveyDeleteDialogComponent {
  survey?: ISurvey;

  protected surveyService = inject(SurveyService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.surveyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

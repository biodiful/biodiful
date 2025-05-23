import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISurvey } from 'app/entities/survey/survey.model';
import { SurveyService } from 'app/entities/survey/service/survey.service';
import { IAnswer } from '../answer.model';
import { AnswerService } from '../service/answer.service';
import { AnswerFormGroup, AnswerFormService } from './answer-form.service';

@Component({
  selector: 'jhi-answer-update',
  templateUrl: './answer-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AnswerUpdateComponent implements OnInit {
  isSaving = false;
  answer: IAnswer | null = null;

  surveysSharedCollection: ISurvey[] = [];

  protected answerService = inject(AnswerService);
  protected answerFormService = inject(AnswerFormService);
  protected surveyService = inject(SurveyService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AnswerFormGroup = this.answerFormService.createAnswerFormGroup();

  compareSurvey = (o1: ISurvey | null, o2: ISurvey | null): boolean => this.surveyService.compareSurvey(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ answer }) => {
      this.answer = answer;
      if (answer) {
        this.updateForm(answer);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const answer = this.answerFormService.getAnswer(this.editForm);
    if (answer.id !== null) {
      this.subscribeToSaveResponse(this.answerService.update(answer));
    } else {
      this.subscribeToSaveResponse(this.answerService.create(answer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(answer: IAnswer): void {
    this.answer = answer;
    this.answerFormService.resetForm(this.editForm, answer);

    this.surveysSharedCollection = this.surveyService.addSurveyToCollectionIfMissing<ISurvey>(this.surveysSharedCollection, answer.survey);
  }

  protected loadRelationshipsOptions(): void {
    this.surveyService
      .query()
      .pipe(map((res: HttpResponse<ISurvey[]>) => res.body ?? []))
      .pipe(map((surveys: ISurvey[]) => this.surveyService.addSurveyToCollectionIfMissing<ISurvey>(surveys, this.answer?.survey)))
      .subscribe((surveys: ISurvey[]) => (this.surveysSharedCollection = surveys));
  }
}

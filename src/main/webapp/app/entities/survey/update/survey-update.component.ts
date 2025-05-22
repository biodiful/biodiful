import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Language } from 'app/entities/enumerations/language.model';
import { SurveyService } from '../service/survey.service';
import { ISurvey } from '../survey.model';
import { SurveyFormGroup, SurveyFormService } from './survey-form.service';

@Component({
  selector: 'jhi-survey-update',
  templateUrl: './survey-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SurveyUpdateComponent implements OnInit {
  isSaving = false;
  survey: ISurvey | null = null;
  languageValues = Object.keys(Language);

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected surveyService = inject(SurveyService);
  protected surveyFormService = inject(SurveyFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SurveyFormGroup = this.surveyFormService.createSurveyFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ survey }) => {
      this.survey = survey;
      if (survey) {
        this.updateForm(survey);
      }
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('biodifulApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const survey = this.surveyFormService.getSurvey(this.editForm);
    if (survey.id !== null) {
      this.subscribeToSaveResponse(this.surveyService.update(survey));
    } else {
      this.subscribeToSaveResponse(this.surveyService.create(survey));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISurvey>>): void {
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

  protected updateForm(survey: ISurvey): void {
    this.survey = survey;
    this.surveyFormService.resetForm(this.editForm, survey);
  }
}

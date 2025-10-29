import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { AngularEditorModule } from '@kolkov/angular-editor';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule, FormArray } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { Language } from 'app/entities/enumerations/language.model';
import { SurveyService } from '../service/survey.service';
import { ISurvey } from '../survey.model';
import { SurveyFormGroup, SurveyFormService, ChallengerPoolFormGroup } from './survey-form.service';
import { AngularEditorConfig } from '@kolkov/angular-editor';

@Component({
  selector: 'jhi-survey-update',
  templateUrl: './survey-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, AngularEditorModule],
})
export class SurveyUpdateComponent implements OnInit {
  isSaving = false;
  survey: ISurvey | null = null;
  languageValues = Object.keys(Language);
  angularEditorConfig: AngularEditorConfig = {
    editable: true,
    spellcheck: true,
    height: '10rem',
    minHeight: '5rem',
    placeholder: 'Enter text here...',
    translate: 'no',
  };

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

  get challengerPoolsFormArray(): FormArray<ChallengerPoolFormGroup> {
    return this.editForm.controls.challengerPools;
  }

  addChallengerPool(): void {
    const nextOrder = this.challengerPoolsFormArray.length + 1;
    const newPool = this.surveyFormService.createChallengerPoolFormGroup(null);
    newPool.patchValue({ poolOrder: nextOrder });
    this.challengerPoolsFormArray.push(newPool);
  }

  removeChallengerPool(index: number): void {
    this.challengerPoolsFormArray.removeAt(index);
    // Update poolOrder for remaining pools
    this.challengerPoolsFormArray.controls.forEach((control, idx) => {
      control.patchValue({ poolOrder: idx + 1 });
    });
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

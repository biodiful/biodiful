import { Component, OnDestroy, OnInit, ViewEncapsulation, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ISurvey } from '../entities/survey/survey.model';
import { SurveyService } from '../entities/survey/service/survey.service';
import { AlertService } from '../core/util/alert.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule],
  encapsulation: ViewEncapsulation.None,
})
export default class HomeComponent implements OnInit, OnDestroy {
  account = signal<Account | null>(null);
  openSurveys = signal<ISurvey[]>([]);
  closedSurveys = signal<ISurvey[]>([]);

  private readonly destroy$ = new Subject<void>();

  private readonly accountService = inject(AccountService);
  private readonly surveyService = inject(SurveyService);
  private readonly alertService = inject(AlertService);
  private readonly router = inject(Router);

  ngOnInit(): void {
    this.loadOpenSurveys();
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.account.set(account));
  }

  loadOpenSurveys(): void {
    this.surveyService
      .query({
        page: 0,
        size: 30,
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: response => this.initOpenSurveys(response.body ?? []),
        error: err => this.onError(err.message),
      });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initOpenSurveys(surveys: ISurvey[]): void {
    // TODO create service method to retrieve list of open surveys
    surveys.forEach(survey => {
      if (survey.open) {
        this.openSurveys().push(survey);
      } else {
        this.closedSurveys().push(survey);
      }
    });
  }

  private onError(errorMessage: string): void {
    this.alertService.addAlert({
      type: 'danger',
      message: errorMessage,
    });
  }
}

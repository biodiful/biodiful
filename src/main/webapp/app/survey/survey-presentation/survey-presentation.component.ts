import { Component, OnInit, ViewEncapsulation, inject, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ISurvey } from 'app/entities/survey/survey.model';
import { Subject, catchError, tap } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { SurveyService } from 'app/entities/survey/service/survey.service';
import { CommonModule } from '@angular/common';
import SharedModule from 'app/shared/shared.module';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { TranslateService } from '@ngx-translate/core';
import { Meta } from '@angular/platform-browser';

@Component({
  selector: 'jhi-survey-presentation',
  templateUrl: './survey-presentation.component.html',
  styleUrl: 'survey-presentation.scss',
  standalone: true,
  imports: [CommonModule, RouterModule, SharedModule],
  encapsulation: ViewEncapsulation.None,
})
export class SurveyPresentationComponent implements OnInit {
  survey!: ISurvey;
  surveyJudgesCount!: number;
  surveyAbsoluteUrl!: string;
  surveyAbsoluteUrlEncoded!: string;
  account = signal<Account | null>(null);

  private readonly destroy$ = new Subject<void>();

  private readonly surveyService = inject(SurveyService);
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly accountService = inject(AccountService);
  private readonly languageService = inject(TranslateService);
  private readonly router = inject(Router);
  private readonly meta = inject(Meta);

  ngOnInit(): void {
    this.reloadParentIframe();

    this.activatedRoute.data.subscribe(({ survey }) => {
      this.accountService
        .getAuthenticationState()
        .pipe(takeUntil(this.destroy$))
        .subscribe(account => this.account.set(account));

      this.survey = survey;

      this.surveyService
        .getSurveyJudgesCount(survey.id)
        .pipe(
          tap(res => {
            this.onCountSuccess(res);
          }),
          catchError(error => {
            this.onError();
            throw error;
          }),
        )
        .subscribe();

      this.initSurveyAbsoluteUrl();

      this.updateMetaTags();

      this.initLocale();
    });
  }

  updateMetaTags(): void {
    // Issue with dynamically updating Meta-tags: Facebook's crawler doesnt interprets javascript
    // Work-around: Use angular universal's Server Side Rendering, but this technology is not ready for java yet
    // (see https://github.com/swaechter/angularj-universal )
    this.meta.updateTag({ name: 'og:url', content: this.surveyAbsoluteUrl });
    this.meta.updateTag({ name: 'og:title', content: this.survey.surveyName ?? '' });
    // this.meta.updateTag({ name: 'og:description', content: this.firstSentence(this.survey.surveyDescription ?? '') });
  }

  // firstSentence(text: string): string {
  //   return this.htmlToPlaintext(text).split('.')[0];
  // }

  htmlToPlaintext(text: string): string {
    return text ? String(text).replace(/<[^>]+>/gm, ' ') : '';
  }

  reloadParentIframe(): void {
    // Detect whether we're in an iframe (i.e. if we follow the link at the end of the google form)
    if (window.frameElement) {
      // Reload parent with curent path
      window.parent.location.href = '#/' + this.router.url;
    }
  }

  initSurveyAbsoluteUrl(): void {
    this.surveyAbsoluteUrl = 'https://www.biodiful.org/#';
    // eslint-disable-next-line no-console
    console.log(this.activatedRoute.snapshot.url);
    if (this.survey.friendlyURL) {
      this.surveyAbsoluteUrl += '/' + this.survey.friendlyURL;
    } else {
      this.surveyAbsoluteUrl += this.router.url;
    }

    this.surveyAbsoluteUrlEncoded = encodeURIComponent(this.surveyAbsoluteUrl);
  }

  initLocale(): void {
    const languageKey = this.survey.language?.toLowerCase() ?? 'en';
    sessionStorage.setItem('locale', languageKey);
    this.languageService.use(languageKey);
  }

  onError(): void {
    // eslint-disable-next-line no-console
    console.debug('Failed to retrieve the number of judges for this survey');
    // alert('An error occurred. Please try again later.');
  }

  onCountSuccess(response: number): void {
    // eslint-disable-next-line no-console
    console.debug('Number of distinct judges for this survey: ', response);
    this.surveyJudgesCount = response;
  }

  getFacebookUrl(): string {
    return `https://www.facebook.com/sharer/sharer.php?u=${this.surveyAbsoluteUrlEncoded}&amp;src=sdkpreparse`;
  }

  getTwitterUrl(): string {
    return `https://x.com/intent/post?text=${this.surveyAbsoluteUrlEncoded}`;
  }

  getMailToHref(): string {
    return `mailto:?Subject=${encodeURIComponent(this.survey.surveyName ?? '')}&body=${this.surveyAbsoluteUrlEncoded}`;
  }
}

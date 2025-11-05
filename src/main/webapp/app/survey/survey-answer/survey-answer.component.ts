import { Component, OnInit, inject, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Challenger } from 'app/shared/model/challenger.model';
import { catchError, tap } from 'rxjs';
import { NewAnswer } from 'app/entities/answer/answer.model';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { ISurvey } from 'app/entities/survey/survey.model';
import dayjs from 'dayjs/esm';
import { SurveyAnswerMatchComponent } from 'app/survey/survey-answer-match/survey-answer-match.component';
import { AnswerService } from 'app/entities/answer/service/answer.service';
import { v4 as uuidv4 } from 'uuid';
import { HttpClient } from '@angular/common/http';
import SharedModule from 'app/shared/shared.module';

interface S3ImageListResponse {
  imageUrls: string[];
}

@Component({
  selector: 'jhi-survey-answer',
  templateUrl: './survey-answer.component.html',
  styles: [],
  standalone: true,
  imports: [SharedModule, SurveyAnswerMatchComponent],
})
export class SurveyAnswerComponent implements OnInit {
  survey!: ISurvey;
  totalNbOfMatches = 0;
  judgeId = signal('');
  answers = signal<NewAnswer[]>([]);
  // Array containing the pools of challengers:
  challengerPools = signal<Challenger[][]>([]);
  currentPoolIndex = 0;
  challengerOne = signal<Challenger | undefined>(undefined);
  challengerTwo = signal<Challenger | undefined>(undefined);
  isAllMatchesCompleted = signal(false);
  matchStarts!: dayjs.Dayjs;
  socialFormUrl!: SafeResourceUrl;

  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly sanitizer = inject(DomSanitizer);
  private readonly answerService = inject(AnswerService);
  private readonly http = inject(HttpClient);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ survey }) => {
      this.survey = survey;
    });

    // TODO send GA event with survey ID?

    this.initChallengers();

    this.initJudgeId();

    this.socialFormUrl = this.sanitizer.bypassSecurityTrustResourceUrl((this.survey.formURL ?? '') + this.judgeId());
  }

  initChallengers(): void {
    // Sort pools by poolOrder and fetch challengers for each
    const sortedPools = [...(this.survey.challengerPools ?? [])].sort((a, b) => (a.poolOrder ?? 0) - (b.poolOrder ?? 0));

    sortedPools.forEach((pool, index) => {
      if (pool.challengersURL) {
        // Call backend endpoint to list images from S3
        this.http
          .get<S3ImageListResponse>('/api/s3/list-images', {
            params: { folderUrl: pool.challengersURL },
          })
          .pipe(
            tap(response => this.onGetChallengersSuccess(response, index)),
            catchError(error => {
              this.onError(`Failed to retrieve challengers for Pool ${index + 1} - ${error.message ?? 'Unknown error'}`);
              throw error;
            }),
          )
          .subscribe();
      }
    });
  }

  onGetChallengersSuccess(response: S3ImageListResponse, poolIndex: number): void {
    // Process the list of image URLs returned by the backend
    const challengersPool: Challenger[] = [];

    for (const imageUrl of response.imageUrls) {
      // Extract filename from URL for the challenger ID
      const urlParts = imageUrl.split('/');
      const filename = urlParts[urlParts.length - 1];
      // Remove file extension for cleaner ID
      const id = filename.replace(/\.[^/.]+$/, '');
      challengersPool.push(new Challenger(id, imageUrl));
    }

    if (challengersPool.length === 0) {
      this.onError(`No images found in Pool ${poolIndex + 1}`);
      return;
    }

    // Update the challengerPools array
    const currentPools = this.challengerPools();
    currentPools[poolIndex] = challengersPool;
    this.challengerPools.set(currentPools);

    this.initNbOfMatches();

    this.initNextMatch();
  }

  initNbOfMatches(): void {
    // Calculate total number of matches across all pools
    this.totalNbOfMatches = 0;
    const sortedPools = [...(this.survey.challengerPools ?? [])].sort((a, b) => (a.poolOrder ?? 0) - (b.poolOrder ?? 0));

    sortedPools.forEach(pool => {
      this.totalNbOfMatches += pool.numberOfMatches ?? 0;
    });

    if (this.totalNbOfMatches < 1) {
      this.totalNbOfMatches = 10;
    }
  }

  initJudgeId(): void {
    this.judgeId.set(`C_${this.survey.id}_judge_${uuidv4()}`);
  }

  initNextMatch(): void {
    // Determine the current pool index based on how many answers have been recorded
    this.updateCurrentPoolIndex();

    // Load 2 random challengers from the current pool
    const currentPool = this.challengerPools()[this.currentPoolIndex];
    this.initNextChallengers(currentPool);

    this.matchStarts = dayjs();
  }

  initNextChallengers(challengersPool: Challenger[]): void {
    const randomIndex1 = Math.floor(Math.random() * challengersPool.length);
    this.challengerOne.set(challengersPool[randomIndex1]);

    // Check whether challengers should only be presented once (tirage sans remise)
    if (this.survey.uniqueChallengers && challengersPool.length > 0) {
      challengersPool.splice(randomIndex1, 1);
    }

    const randomIndex2 = Math.floor(Math.random() * challengersPool.length);
    this.challengerTwo.set(challengersPool[randomIndex2]);

    // Check that the 2 challengers are different (in case 2 images with the same ID exist in the pool)
    while (this.challengerOne()?.id === this.challengerTwo()?.id) {
      const randomIndex = Math.floor(Math.random() * challengersPool.length);
      this.challengerTwo.set(challengersPool[randomIndex]);
    }

    // Check whether challengers should only be presented once (tirage sans remise)
    if (this.survey.uniqueChallengers && challengersPool.length > 0) {
      challengersPool.splice(randomIndex2, 1);
    }
  }

  addWinner(winner: Challenger): void {
    // console.debug('Winner added: ' + JSON.stringify(winner));
    // Add a new Answer from the winner
    const matchEnds = dayjs();
    const newAnswer: NewAnswer = {
      id: null,
      judgeID: this.judgeId(),
      challenger1: this.challengerOne()?.id ?? null,
      challenger2: this.challengerTwo()?.id ?? null,
      winner: winner.id,
      startTime: this.matchStarts,
      endTime: matchEnds,
      poolNumber: this.currentPoolIndex + 1, // Pool number is 1-indexed
      survey: { id: this.survey.id },
    };

    this.answers().push(newAnswer);

    // Determine whether to display next challengers
    if (this.answers().length < this.totalNbOfMatches) {
      this.initNextMatch();
    } else {
      // If we've got enough winners, save the answers and display socio-pro questions
      // console.debug('Answers: ' + JSON.stringify(this.answers()));

      this.isAllMatchesCompleted.set(true);
      this.answerService
        .createAll(this.answers())
        .pipe(
          tap(() => {
            this.onSaveResponseSuccess();
          }),
          catchError(error => {
            this.onError(`Failed to save the list of responses -  ${error.message ?? 'Unknown error'}`);
            throw error;
          }),
        )
        .subscribe();
    }
  }

  updateCurrentPoolIndex(): void {
    // Determine which pool we're on based on how many answers have been recorded
    const sortedPools = [...(this.survey.challengerPools ?? [])].sort((a, b) => (a.poolOrder ?? 0) - (b.poolOrder ?? 0));

    let cumulativeMatches = 0;
    for (let i = 0; i < sortedPools.length; i++) {
      cumulativeMatches += sortedPools[i].numberOfMatches ?? 0;
      if (this.answers().length < cumulativeMatches) {
        this.currentPoolIndex = i;
        return;
      }
    }

    // Default to the last pool if we're somehow beyond all pools
    this.currentPoolIndex = Math.max(0, sortedPools.length - 1);
  }

  getCurrentMatchNumber(): number {
    return this.answers().length + 1;
  }

  getDescriptionForCurrentPool(): string {
    const sortedPools = [...(this.survey.challengerPools ?? [])].sort((a, b) => (a.poolOrder ?? 0) - (b.poolOrder ?? 0));
    const currentPool = sortedPools[this.currentPoolIndex];
    return currentPool.matchesDescription ?? '';
  }

  getPercentAdvancement(): number {
    return (this.getCurrentMatchNumber() * 100) / this.totalNbOfMatches;
  }

  private onError(errorMessage: string): void {
    // eslint-disable-next-line no-console
    console.log(errorMessage);
    alert('An error occurred. Please try again later.');
  }

  private onSaveResponseSuccess(): void {
    // alert('Save success!');
    if (this.survey.formURL) {
      window.location.href = this.survey.formURL + this.judgeId();
    }
  }
}

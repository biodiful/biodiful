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

interface FlickrPhotoset {
  photo: {
    id: string;
    secret: string;
    server: string;
    farm: number;
    title: string;
  }[];
}

interface FlickrResponse {
  photoset: FlickrPhotoset;
  stat: string;
  message: string;
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
  totalNbOfMatches!: number;
  judgeId = signal('');
  answers = signal<NewAnswer[]>([]);
  // 2 dimensions array containing the pools of challengers:
  challengers = signal<Challenger[][]>([]);
  challengersPool1 = signal<Challenger[]>([]);
  challengersPool2 = signal<Challenger[]>([]);
  challengersPool3 = signal<Challenger[]>([]);
  currentPool = 1;
  totalNbOfPools = 1;
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
    if (this.survey.challengersPool1URL) {
      this.http
        .get<FlickrResponse>(this.survey.challengersPool1URL)
        .pipe(
          tap(response => this.onGetChallengersSuccess(response, this.challengersPool1(), 0)),
          catchError(error => {
            this.onError(`Failed to retrieve challengers for Pool 1 - ${error.message ?? 'Unknown error'}`);
            throw error;
          }),
        )
        .subscribe();
    }

    if (this.survey.challengersPool2URL) {
      this.http
        .get<FlickrResponse>(this.survey.challengersPool2URL)
        .pipe(
          tap(response => this.onGetChallengersSuccess(response, this.challengersPool2(), 1)),
          catchError(error => {
            this.onError(`Failed to retrieve challengers for Pool 2 -  ${error.message ?? 'Unknown error'}`);
            throw error;
          }),
        )
        .subscribe();
    }

    if (this.survey.challengersPool3URL) {
      this.http
        .get<FlickrResponse>(this.survey.challengersPool3URL)
        .pipe(
          tap(response => this.onGetChallengersSuccess(response, this.challengersPool3(), 2)),
          catchError(error => {
            this.onError(`Failed to retrieve challengers for Pool 3 -  ${error.message ?? 'Unknown error'}`);
            throw error;
          }),
        )
        .subscribe();
    }
  }

  onGetChallengersSuccess(response: FlickrResponse, challengersPool: Challenger[], poolIndex: number): void {
    // console.debug(JSON.stringify(response));

    if (response.stat === 'fail') {
      this.onError('Failed to retrieve challengers: ' + response.message);
      return;
    }

    const photoset = response.photoset;
    for (const photo of photoset.photo) {
      const photoUrl = `https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg`;
      challengersPool.push(new Challenger(photo.title, photoUrl));
    }

    // this.challengers().push(challengersPool);
    this.challengers()[poolIndex] = challengersPool;

    this.initNbOfMatches();

    this.initNextMatch();
  }

  initNbOfMatches(): void {
    this.totalNbOfMatches = this.survey.numberOfMatchesPerPool ?? 0;
    if (this.challengersPool2.length > 0 && this.survey.numberOfMatchesPerPool2 && this.survey.numberOfMatchesPerPool2 > 0) {
      this.totalNbOfMatches += this.survey.numberOfMatchesPerPool2 ?? 0;
      this.totalNbOfPools = 2;
    }
    if (this.challengersPool3.length > 0 && this.survey.numberOfMatchesPerPool3 && this.survey.numberOfMatchesPerPool3 > 0) {
      this.totalNbOfMatches += this.survey.numberOfMatchesPerPool3 ?? 0;
      this.totalNbOfPools = 3;
    }

    if (this.totalNbOfMatches < 1) {
      this.totalNbOfMatches = 10;
    }
  }

  initJudgeId(): void {
    this.judgeId.set(`C_${this.survey.id}_judge_${uuidv4()}`);
  }

  initNextMatch(): void {
    // Determine the current pool based on how many answers have been recorded
    // this.currentPool = Math.floor(this.answers().length / (this.survey.numberOfMatchesPerPool ?? 0)) + 1;

    // Load 2 random challengers from the right pool
    this.initNextChallengers(this.challengers()[this.currentPool - 1]);

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
      poolNumber: this.currentPool,
      survey: { id: this.survey.id },
    };

    this.answers().push(newAnswer);

    // Determine whether to display next challengers
    if (this.answers().length < this.totalNbOfMatches) {
      this.updateCurrentPool();
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

  updateCurrentPool(): void {
    // Determine which pool we're on
    if (this.currentPool === 1 && this.totalNbOfPools > 1 && this.answers().length >= (this.survey.numberOfMatchesPerPool ?? 0)) {
      this.currentPool = 2;
    } else if (
      this.currentPool === 2 &&
      this.totalNbOfPools > 2 &&
      this.answers().length >= (this.survey.numberOfMatchesPerPool ?? 0) + (this.survey.numberOfMatchesPerPool2 ?? 0)
    ) {
      this.currentPool = 3;
    }
  }

  getCurrentMatchNumber(): number {
    return this.answers().length + 1;
  }

  getDescriptionForCurrentPool(): any {
    if (this.currentPool === 2 && this.survey.matchesDescriptionPool2) {
      return this.survey.matchesDescriptionPool2;
    } else if (this.currentPool === 3 && this.survey.matchesDescriptionPool3) {
      return this.survey.matchesDescriptionPool3;
    }

    return this.survey.matchesDescription;
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

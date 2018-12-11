import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISurvey } from 'app/shared/model/survey.model';
import { Challenger } from 'app/shared/model/challenger.model';
import { Answer, IAnswer } from 'app/shared/model/answer.model';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import moment from 'moment/src/moment';
import { AnswerService } from 'app/entities/answer';
import { Observable } from 'rxjs';
import { HttpResponse, HttpErrorResponse, HttpClient } from '@angular/common/http';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-survey-answer',
    templateUrl: './survey-answer.component.html',
    styles: []
})
export class SurveyAnswerComponent implements OnInit {
    survey: ISurvey;
    nbOfMatches: number;
    judgeId: string;
    // winners: Challenger[] = [];
    answers: Answer[] = [];
    challengers: Challenger[][] = [];
    challengersPool1: Challenger[] = [];
    challengersPool2: Challenger[] = [];
    challengersPool3: Challenger[] = [];
    currentPool = 2;
    challengerOne: Challenger;
    challengerTwo: Challenger;
    isAllMatchesCompleted: boolean = false;
    matchStarts: moment.Moment;

    constructor(
        private activatedRoute: ActivatedRoute,
        public sanitizer: DomSanitizer,
        private answerService: AnswerService,
        private http: HttpClient,
        private jhiAlertService: JhiAlertService
    ) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ survey }) => {
            this.survey = survey;
        });

        this.initChallengers();

        // this.initNbOfMatches();

        // this.initNextMatch();

        this.initJudgeId();
    }

    initChallengers() {
        this.http
            .get(this.survey.challengersPool1URL)
            .subscribe(
                response => this.onGetChallengersSuccess(response, this.challengersPool1),
                (res: HttpErrorResponse) => this.onError('Failed to retrieve challengers for Pool 1- ' + res.message)
            );

        if (this.survey.challengersPool2URL) {
            this.http
                .get(this.survey.challengersPool2URL)
                .subscribe(
                    response => this.onGetChallengersSuccess(response, this.challengersPool2),
                    (res: HttpErrorResponse) => this.onError('Failed to retrieve challengers for Pool 2 - ' + res.message)
                );
        }

        if (this.survey.challengersPool3URL) {
            this.http
                .get(this.survey.challengersPool3URL)
                .subscribe(
                    response => this.onGetChallengersSuccess(response, this.challengersPool3),
                    (res: HttpErrorResponse) => this.onError('Failed to retrieve challengers for Pool 3 - ' + res.message)
                );
        }
    }

    onGetChallengersSuccess(response, challengersPool) {
        // console.debug(JSON.stringify(response));

        if (response['stat'] == 'fail') {
            this.onError('Failed to retrieve challengers: ' + response['message']);
            return;
        }

        const photoset = response['photoset'];
        for (const photo of photoset.photo) {
            const photoUrl = `https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}.jpg`;
            challengersPool.push(new Challenger(photo.title, photoUrl));
        }

        this.challengers.push(challengersPool);

        this.initNbOfMatches();

        this.initNextMatch();
    }

    initNbOfMatches() {
        this.nbOfMatches = this.survey.numberOfMatches;
        if (this.challengersPool2.length > 0) {
            this.nbOfMatches += this.survey.numberOfMatches;
        }
        if (this.challengersPool3.length > 0) {
            this.nbOfMatches += this.survey.numberOfMatches;
        }

        if (this.nbOfMatches < 1) {
            this.nbOfMatches = 10;
        }
    }

    initJudgeId() {
        const uniqueString = require('unique-string');
        this.judgeId = 'judge_' + uniqueString();
    }

    initNextMatch() {
        //if (this.answers.length >)
        //TODO - make this.survey.numberOfMatches required??
        this.currentPool = Math.floor(this.answers.length / this.survey.numberOfMatches) + 1;
        // Load 2 random challengers from the right pool
        this.initNextChallengers(this.challengers[this.currentPool - 1]);

        this.matchStarts = moment();
    }

    initNextChallengers(challengersPool: Challenger[]) {
        this.challengerOne = challengersPool[Math.floor(Math.random() * challengersPool.length)];
        this.challengerTwo = challengersPool[Math.floor(Math.random() * challengersPool.length)];
        this.checkChallengersAreDifferent(challengersPool);
    }

    checkChallengersAreDifferent(challengersPool) {
        while (this.challengerOne.id == this.challengerTwo.id) {
            this.challengerTwo = challengersPool[Math.floor(Math.random() * challengersPool.length)];
        }
    }

    addWinner(winner) {
        console.debug('Winner added: ' + JSON.stringify(winner));
        // Add a new Answer from the winner
        const matchEnds = moment();
        this.answers.push(
            new Answer(
                undefined,
                this.judgeId,
                this.challengerOne.id,
                this.challengerTwo.id,
                winner.id,
                this.matchStarts,
                matchEnds,
                this.currentPool, //TODO retrieve the right Pool Number - same in Challenger?
                this.survey.id
            )
        );

        // Determine whether to display next challengers
        if (this.answers.length < this.nbOfMatches) {
            this.initNextMatch();
        } else {
            // If we've got enough winners, save the answers and display socio-pro questions
            console.debug('Answers: ' + JSON.stringify(this.answers));
            this.isAllMatchesCompleted = true;

            this.subscribeToSaveAllResponse(this.answerService.createAll(this.answers));
        }
    }

    getCurrentMatchNumber(): number {
        return this.answers.length + 1;
    }

    getPercentAdvancement(): number {
        return (this.getCurrentMatchNumber() * 100) / this.nbOfMatches;
    }

    getSocialFormUrl(): SafeResourceUrl {
        return this.sanitizer.bypassSecurityTrustResourceUrl(this.survey.formURL + this.judgeId);
    }

    private subscribeToSaveAllResponse(result: Observable<HttpResponse<IAnswer[]>>) {
        result.subscribe(
            (res: HttpResponse<IAnswer[]>) => this.onSaveResponseSuccess(),
            (res: HttpErrorResponse) => this.onError('Failed to save the list of responses - ' + res.message)
        );
    }

    private onError(errorMessage: string) {
        console.log(errorMessage);
        alert('An error occurred. Please try again later.');
        //this.jhiAlertService.addAlert({type: 'danger', msg: 'error.generic', timeout: 2000}, []);
    }

    onSaveResponseSuccess(): void {
        // alert('Save success!');
    }
}
